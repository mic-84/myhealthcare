package it.unipi.lsmsd.myhealthcare.mongo.startingdata;

import it.unipi.lsmsd.myhealthcare.dao.BookingDao;
import it.unipi.lsmsd.myhealthcare.dao.BookingStatusDao;
import it.unipi.lsmsd.myhealthcare.mongo.dto.*;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.utility.BookingUtility;
import it.unipi.lsmsd.myhealthcare.utility.UserUtility;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class BookingCreator {
    public static void populateBooking(BookingRepository bookingRepository,
                                       StructureRepository structureRepository,
                                       UserRepository userRepository){
        bookingRepository.deleteAll();
        List<UserDTO> storedUsers = userRepository.findAll();
        List<StructureDTO> storedStructures = structureRepository.findAll();
        int userSize = storedUsers.size()-1;
        String status = BookingUtility.getRenderedStatus().getId();
        int countStructure = 0;
        for(StructureDTO structure : storedStructures){
            countStructure++;
            List<BookingDTO> bookingsToSave = new ArrayList<BookingDTO>();
            int quantity = Utility.getRandomInt(500,1000);
            List<ServiceDTO> services = new ArrayList<>(structure.getServices());
            System.out.println(countStructure + ": creating " + quantity + " random bookings for structure "
                    + structure.getId() + " (" + services.size() + " services)");
            //System.out.println(structure.completeToString());
            /* create N-quantity bookings for the structure */
            for(int i=0; i<quantity; i++){
                String creationDate = randomCreationDate(Utility.getToday());
                String confirmationDate = nextDate(creationDate);
                String bookingDate = nextDate(confirmationDate);
                BookingDTO booking = new BookingDTO(
                        storedUsers.get(Utility.getRandomInt(0,userSize)).getId(),
                        structure.getId(), status,
                        creationDate,confirmationDate,bookingDate
                );
                int servicesQuantity = 3;
                if(services.size() < 3)
                    servicesQuantity = Utility.getRandomInt(1,services.size());
                for(int j=0; j<servicesQuantity; j++)
                    booking.addService(services.get(Utility.getRandomInt(0,services.size()-1)));
                booking.computeTotal();
                //System.out.println(booking.completeToString());
                bookingsToSave.add(booking);
            }
            BookingDao.createMany(bookingsToSave,bookingRepository);
        }
    }

    public static void populateStatus(BookingStatusRepository bookingStatusRepository){
        bookingStatusRepository.deleteAll();
        BookingStatusDao.create(new BookingStatusDTO("created",
                "confirmed by the user"),bookingStatusRepository);
        BookingStatusDao.create(new BookingStatusDTO("confirmed",
                "confirmed by the structure"),bookingStatusRepository);
        BookingStatusDao.create(new BookingStatusDTO("rendered",
                "services received by the user"),bookingStatusRepository);
        BookingStatusDao.create(new BookingStatusDTO("cancelled",
                "cancelled"),bookingStatusRepository);
    }

    public static UserDTO randomEmployee(List<UserDTO> storedUsers, StructureDTO structure){
        for(UserDTO user:storedUsers)
            for(UserDTO.UserRole userRole:user.getRoles())
                if(userRole.getRole().equals(UserUtility.getEmployeeRole())
                        && userRole.getStructure().equals(structure.getId()) ) {
                    return user;
                }
        return null;
    }

    public static String randomCreationDate(Date maxDate){
        return Utility.dateToString(Date.from(LocalDateTime.ofInstant(
                        maxDate.toInstant(),
                        ZoneId.systemDefault()).minusDays(Utility.getRandomInt(50,400))
                .atZone(ZoneId.systemDefault()).toInstant()));
    }

    public static String nextDate(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(date));
            c.add(Calendar.DATE, 10);
            return sdf.format(c.getTime());
        }
        catch (ParseException e){}
        return null;
    }
}
