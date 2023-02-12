package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.aggregation.BookingAggregation;
import it.unipi.lsmsd.myhealthcare.mongo.dto.BookingDTO;
import it.unipi.lsmsd.myhealthcare.mongo.dto.ServiceDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.utility.BookingUtility;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.util.*;

public class BookingDao {
    public static Booking fromMongo(BookingDTO bookingDTO, UserRepository userRepository, CityRepository cityRepository,
                                    ServiceRepository serviceRepository, StructureRepository structureRepository,
                                    RoleRepository roleRepository, BookingStatusRepository bookingStatusRepository) {
        Booking booking = new Booking();
        booking.setId(bookingDTO.getId());
        booking.setUser(UserDao.fromMongo(UserDao.readById(bookingDTO.getUser(), userRepository),
                cityRepository, roleRepository, structureRepository,serviceRepository));
        booking.setStructure(StructureDao.fromMongo(StructureDao.readById(bookingDTO.getStructure(),
                structureRepository), cityRepository,serviceRepository));
        booking.setStatus(BookingStatusDao.fromMongo(BookingStatusDao.readById(
                bookingDTO.getStatus(), bookingStatusRepository)));
        booking.setCreationDate(Utility.stringToDate(bookingDTO.getCreationDate()));
        if(bookingDTO.getConfirmationDate() != null)
            booking.setConfirmationDate(Utility.stringToDate(bookingDTO.getConfirmationDate()));
        booking.setBookingDate(Utility.stringToDate(bookingDTO.getBookingDate()));
        booking.setTotal(bookingDTO.getTotal());

        for (BookingDTO.BookingService bookingService: bookingDTO.getServices()) {
            ServiceDTO serviceDTO = ServiceDao.readById(bookingService.getId(), serviceRepository);
            serviceDTO.setRate(bookingService.getRate());
            booking.addService(ServiceDao.fromMongo(serviceDTO));
        }
        return booking;
    }

    public static BookingDTO toMongo(Booking booking){
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setUser(booking.getUser().getId());
        bookingDTO.setStructure(booking.getStructure().getId());
        bookingDTO.setStatus(booking.getStatus().getId());
        bookingDTO.setCreationDate(Utility.dateToString(booking.getCreationDate()));
        if(booking.getConfirmationDate() != null)
            bookingDTO.setConfirmationDate(Utility.dateToString(booking.getConfirmationDate()));
        bookingDTO.setBookingDate(Utility.dateToString(booking.getBookingDate()));
        bookingDTO.setTotal(booking.getTotal());
        for (Service service:booking.getServices()) {
            ServiceDTO serviceDTO = new ServiceDTO();
            serviceDTO.setId(service.getId());
            serviceDTO.setRate(service.getRate());
            bookingDTO.addService(serviceDTO);
        }
        return bookingDTO;
    }

    public static void create(BookingDTO object, BookingRepository bookingRepository){
        bookingRepository.save(object);
    }

    public static void createMany(List<BookingDTO> objects, BookingRepository bookingRepository){
        bookingRepository.saveAll(objects);
    }

    public static void create(Booking object, BookingRepository bookingRepository){
        bookingRepository.save(toMongo(object));
    }

    public static BookingDTO readById(String id, BookingRepository bookingRepository){
        return bookingRepository.findById(id).get();
    }

    public static List<BookingDTO> readByUserMongo(String user, BookingRepository bookingRepository){
        return bookingRepository.findByUser(user);
    }

    public static List<Booking> readByUser(
            String user, BookingRepository bookingRepository,
            StructureRepository structureRepository, ServiceRepository serviceRepository,
            CityRepository cityRepository, RoleRepository roleRepository,
            UserRepository userRepository, BookingStatusRepository bookingStatusRepository){
        List<Booking> list = new ArrayList<Booking>();
        List<BookingDTO> mongoList = bookingRepository.findByUser(user);
        for(BookingDTO bookingDTO : mongoList)
            list.add(BookingDao.fromMongo(bookingDTO, userRepository, cityRepository,
                    serviceRepository, structureRepository, roleRepository,
                    bookingStatusRepository));
        return list;
    }

    public static List<Booking> readByStructure(
            String structure, BookingRepository bookingRepository,
            ServiceRepository serviceRepository, CityRepository cityRepository,
            UserRepository userRepository){
        List<Booking> list = new ArrayList<Booking>();
        List<BookingDTO> mongoList = bookingRepository.findByStructure(structure);
        for(BookingDTO bookingDTO : mongoList) {
            Booking booking = new Booking();
            booking.setId(bookingDTO.getId());
            booking.setUser(UserDao.fromMongoNoRoles(
                    UserDao.readById(bookingDTO.getUser(), userRepository),
                    cityRepository));
            booking.setStatus(BookingUtility.getStatusById(bookingDTO.getStatus()));
            booking.setCreationDate(Utility.stringToDate(bookingDTO.getCreationDate()));
            if(bookingDTO.getConfirmationDate() != null)
                booking.setConfirmationDate(Utility.stringToDate(bookingDTO.getConfirmationDate()));
            booking.setBookingDate(Utility.stringToDate(bookingDTO.getBookingDate()));
            booking.setTotal(bookingDTO.getTotal());

            for (BookingDTO.BookingService bookingService: bookingDTO.getServices()) {
                ServiceDTO serviceDTO = ServiceDao.readById(bookingService.getId(), serviceRepository);
                serviceDTO.setRate(bookingService.getRate());
                booking.addService(ServiceDao.fromMongo(serviceDTO));
            }
            list.add(booking);
        }
        return list;
    }

    public static List<BookingDTO> readByStructureMongo(String structure, BookingRepository bookingRepository){
        return bookingRepository.findByStructure(structure);
    }

    public static List<BookingDTO> readAllMongo(BookingRepository bookingRepository){
        return bookingRepository.findAll();
    }

    public static List<Booking> readAll(BookingRepository bookingRepository,
                                        UserRepository userRepository, CityRepository cityRepository,
                                        ServiceRepository serviceRepository,
                                        StructureRepository structureRepository,
                                        RoleRepository roleRepository,
                                        BookingStatusRepository bookingStatusRepository){
        List<Booking> objects = new ArrayList<Booking>();
        for(BookingDTO object:readAllMongo(bookingRepository))
            objects.add(fromMongo(object,userRepository,cityRepository,serviceRepository,
                    structureRepository,roleRepository,bookingStatusRepository));
        return objects;
    }

    public static List<BookingAggregation> getBookingsPerStructure(
            String statusId, BookingRepository bookingRepository,
            StructureRepository structureRepository, CityRepository cityRepository){
        List<BookingAggregation> list = bookingRepository.getBookingsPerStructure(statusId);
        for(BookingAggregation elem : list){
            Structure structure = StructureDao.fromMongoNoServices(
                    StructureDao.readById(elem.getId(), structureRepository),cityRepository);
            elem.setStructure(structure);
            elem.setCost(Utility.roundFloat(elem.getCost()));
        }
        return list;
    }

    public static List<BookingAggregation> getBookingsPerServiceByStructure(
            Structure structure, String statusId, BookingRepository bookingRepository){
        List<BookingAggregation> list = new ArrayList<BookingAggregation>();
        List<BookingDTO> bookings = bookingRepository.findByStructureAndStatus(
                structure.getId(), statusId);
        for(Service service : structure.getServices()){
            BookingAggregation aggregation = new BookingAggregation();
            aggregation.setService(service);
            Long count = Long.valueOf(0);
            Float cost = 0f;
            for(BookingDTO booking : bookings)
                for(BookingDTO.BookingService bookingService : booking.getServices())
                    if(bookingService.getId().equals(service.getId())) {
                        count++;
                        cost += bookingService.getRate();
                    }
            aggregation.setCount(count);
            aggregation.setCost(cost);
            list.add(aggregation);
        }
        Collections.sort(list, new Comparator<BookingAggregation>() {
            @Override
            public int compare(BookingAggregation o1, BookingAggregation o2) {
                return o2.getCost().compareTo(o1.getCost());
            }
        });
        return list;
    }

    public static List<BookingAggregation> getBookingsPerUserByStructure(
            String statusId, String structureId, BookingRepository bookingRepository,
            UserRepository userRepository, CityRepository cityRepository){
        List<BookingAggregation> list = bookingRepository.getBookingsPerUserByStructure(
                statusId, structureId);
        for(BookingAggregation elem : list)
            elem.setUser(UserDao.fromMongoNoRoles(UserDao.readById(elem.getId(),
                            userRepository),cityRepository));
        return list;
    }

    public static List<BookingAggregation> getBookingsPerMonthByYearAndStructure(
            Structure structure, String statusId, String year,
            BookingRepository bookingRepository){
        List<BookingAggregation> list = bookingRepository.getBookingsPerMonthByYearAndStructure(
                statusId, year, structure.getId());
        for(BookingAggregation elem : list)
            elem.setMonth(Utility.getMonth(elem.getId()));
        return list;
    }

    public static void update(BookingDTO object, BookingRepository bookingRepository){
        bookingRepository.save(object);
    }

    public static void update(Booking object, BookingRepository bookingRepository){
        bookingRepository.save(toMongo(object));
    }

    public static void delete(BookingDTO object, BookingRepository bookingRepository){
        bookingRepository.delete(object);
    }

    public static void delete(Booking object, BookingRepository bookingRepository){
        bookingRepository.delete(toMongo(object));
    }

    public static Long size(BookingRepository bookingRepository){
        return bookingRepository.count();
    }
}
