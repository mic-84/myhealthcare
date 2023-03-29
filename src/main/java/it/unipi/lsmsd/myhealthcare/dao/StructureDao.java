package it.unipi.lsmsd.myhealthcare.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.aggregation.*;
import it.unipi.lsmsd.myhealthcare.databaseConnection.MongoConnectionManager;
import it.unipi.lsmsd.myhealthcare.dto.*;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.repository.StructureRepository;
import it.unipi.lsmsd.myhealthcare.service.Utility;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class StructureDao {
    public static Structure fromDTO(AbstractStructureDTO abstractStructureDTO) {
        Structure structure = new Structure();
        structure.setAslCode(abstractStructureDTO.getAslCode());
        structure.setStructureCode(abstractStructureDTO.getStructureCode());
        structure.setName(abstractStructureDTO.getName());
        structure.setAddress(abstractStructureDTO.getAddress());
        structure.setCity(CityDao.fromDTO(abstractStructureDTO.getCity()));
        structure.setRegion(abstractStructureDTO.getRegion());
        structure.setArea(abstractStructureDTO.getArea());

        if(abstractStructureDTO instanceof StructureDTO) {
            StructureDTO structureDTO = (StructureDTO)abstractStructureDTO;
            structure.setId(structureDTO.getId());
            for (StructureServiceDTO structureServiceDTO : structureDTO.getServices())
                structure.addService(ServiceDao.fromDTOtoStructureService(structureServiceDTO));
            structure.setServices(Utility.sortStructureServices(structure.getServices()));
            for (StructureBookingDTO bookingDTO : structureDTO.getBookings())
                structure.addBooking(fromStructureBookingDTO(bookingDTO));
            for(StructureReviewDTO reviewDTO : structureDTO.getReviews())
                structure.addReview(fromStructureReviewDTO(reviewDTO));
        } else if(abstractStructureDTO instanceof StructureNoDetailsDTO)
            structure.setId(((StructureNoDetailsDTO)abstractStructureDTO).getId());

        return structure;
    }

    public static StructureDTO toDTO(Structure structure){
        StructureDTO structureDTO = new StructureDTO();
        structureDTO.setId(structure.getId());
        structureDTO.setAslCode(structure.getAslCode());
        structureDTO.setStructureCode(structure.getStructureCode());
        structureDTO.setName(structure.getName());
        structureDTO.setAddress(structure.getAddress());
        structureDTO.setCity(CityDao.toDTONoDetails(structure.getCity()));
        structureDTO.setRegion(structure.getRegion());
        structureDTO.setArea(structure.getArea());

        if(structure.getServices().size() > 0)
            for(StructureService service:structure.getServices())
                structureDTO.addService(ServiceDao.toStructureDTO(service));

        if(structure.getBookings().size() > 0)
            for (StructureBooking booking : structure.getBookings())
                structureDTO.addBooking(toStructureBookingDTO(booking));

        if(structure.getReviews().size() > 0)
            for(StructureReview review : structure.getReviews())
                structureDTO.addReview(toStructureReviewDTO(review));

        return structureDTO;
    }

    public static StructureNoDetailsDTO toDTONoDetails(Structure structure){
        StructureNoDetailsDTO structureDTO = new StructureNoDetailsDTO();
        structureDTO.setId(structure.getId());
        structureDTO.setAslCode(structure.getAslCode());
        structureDTO.setStructureCode(structure.getStructureCode());
        structureDTO.setName(structure.getName());
        structureDTO.setAddress(structure.getAddress());
        structureDTO.setCity(CityDao.toDTONoDetails(structure.getCity()));
        structureDTO.setRegion(structure.getRegion());
        structureDTO.setArea(structure.getArea());
        return structureDTO;
    }

    public static void create(StructureDTO object, StructureRepository structureRepository){
        structureRepository.save(object);
    }

    public static void createMany(List<StructureDTO> objects, StructureRepository structureRepository){
        structureRepository.saveAll(objects);
    }

    public static void create(Structure object, StructureRepository structureRepository){
        structureRepository.save(toDTO(object));
    }

    public static StructureDTO readById(String id, StructureRepository structureRepository){
        return structureRepository.findById(id).get();
    }

    public static StructureDTO readByStructureCode(String code, StructureRepository structureRepository){
        return structureRepository.findByStructureCode(code);
    }


    public static List<Structure> readByCityId(
            String cityId, StructureRepository structureRepository){
        List<StructureDTO> structuresDTO = structureRepository.findByCityId(cityId);
        List<Structure> structures = new ArrayList<Structure>();
        for(StructureDTO structureDTO : structuresDTO)
            structures.add(fromDTO(structureDTO));
        return structures;
    }

    public static List<Structure> readByRegion(
            String region, StructureRepository structureRepository){
        List<StructureDTO> structuresDTO = structureRepository.findByRegion(region);
        List<Structure> structures = new ArrayList<Structure>();
        for(StructureDTO structureDTO : structuresDTO)
            structures.add(fromDTO(structureDTO));
        return structures;
    }

    public static List<Structure> readByArea(
            String area, StructureRepository structureRepository){
        List<StructureDTO> structuresDTO = structureRepository.findByArea(area);
        List<Structure> structures = new ArrayList<Structure>();
        for(StructureDTO structureDTO : structuresDTO)
            structures.add(fromDTO(structureDTO));
        return structures;
    }

    public static List<StructureDTO> readAllDTO(StructureRepository structureRepository){
        return structureRepository.findAll();
    }

    public static List<Structure> readAll(StructureRepository structureRepository){
        List<Structure> objects = new ArrayList<Structure>();
        for(StructureDTO object: readAllDTO(structureRepository))
            objects.add(fromDTO(object));
        return objects;
    }

    public static void update(StructureDTO object, StructureRepository structureRepository){
        structureRepository.save(object);
    }

    public static void update(Structure object, StructureRepository structureRepository){
        structureRepository.save(toDTO(object));
    }

    public static void delete(StructureDTO object, StructureRepository structureRepository){
        structureRepository.delete(object);
    }

    public static void delete(Structure object, StructureRepository structureRepository){
        structureRepository.delete(toDTO(object));
    }

    public static Long size(StructureRepository structureRepository){
        return structureRepository.count();
    }

    public static void addService(StructureService service, Structure structure) {
        System.out.println(MyHealthCareApplication.mongo.getStructureCollection().updateOne(
                Filters.eq("_id", new ObjectId(structure.getId())),
                Updates.push("services", ServiceDao.toStructureDTO(service).toBsonDocument())));
    }

    public static void updateServiceActivation(StructureService service) {
        System.out.println(MyHealthCareApplication.mongo.getStructureCollection().updateOne(
                new BasicDBObject("services._id", new ObjectId(service.getId())),
                new BasicDBObject("$set", new BasicDBObject("services.$.active", service.isActive()))));
    }

    public static void updateServiceRate(StructureService service) {
        System.out.println(MyHealthCareApplication.mongo.getStructureCollection().updateOne(
                new BasicDBObject("services._id", new ObjectId(service.getId())),
                new BasicDBObject("$set", new BasicDBObject("services.$.rate", service.getRate()))));
    }

    public static void saveNewBooking(StructureBooking booking, Structure structure) {
        System.out.println(MyHealthCareApplication.mongo.getStructureCollection().updateOne(
                Filters.eq("_id", new ObjectId(structure.getId())),
                Updates.push("bookings", toStructureBookingDTO(booking).toBsonDocument())));
    }

    public static void updateBookingStatus(StructureBooking booking) {
        System.out.println(MyHealthCareApplication.mongo.getStructureCollection().updateOne(
                new BasicDBObject("bookings.code", booking.getCode()),
                new BasicDBObject("$set", new BasicDBObject("bookings.$.status", booking.getStatus()))));
    }

    public static void updateBookingDate(StructureBooking booking) {
        System.out.println(MyHealthCareApplication.mongo.getStructureCollection().updateOne(
                new BasicDBObject("bookings.code", booking.getCode()),
                new BasicDBObject("$set", new BasicDBObject("bookings.$.bookingDate", booking.getBookingDate()))));
    }

    public static void updateBookingConfirmationDate(StructureBooking booking) {
        System.out.println(MyHealthCareApplication.mongo.getStructureCollection().updateOne(
                new BasicDBObject("bookings.code", booking.getCode()),
                new BasicDBObject("$set", new BasicDBObject("bookings.$.confirmationDate", booking.getConfirmationDate()))));
    }

    public static StructureBookingDTO toStructureBookingDTO(StructureBooking booking){
        StructureBookingDTO bookingDTO = new StructureBookingDTO();
        bookingDTO.setCode(booking.getCode());
        bookingDTO.setUser(UserDao.toDTONoDetails(booking.getUser()));
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setCreationDate(booking.getCreationDate());
        bookingDTO.setConfirmationDate(booking.getConfirmationDate());
        bookingDTO.setBookingDate(booking.getBookingDate());
        bookingDTO.setYear(booking.getYear());
        bookingDTO.setMonth(booking.getMonth());
        for(StructureService service : booking.getServices()){
            bookingDTO.addService(ServiceDao.toStructureDTO(service));
        }
        bookingDTO.setTotal(booking.getTotal());

        return bookingDTO;
    }

    public static StructureBooking fromStructureBookingDTO(StructureBookingDTO bookingDTO){
        StructureBooking booking = new StructureBooking();
        booking.setCode(bookingDTO.getCode());
        booking.setUser(UserDao.fromDTO(bookingDTO.getUser()));
        booking.setStatus(bookingDTO.getStatus());
        booking.setCreationDate(bookingDTO.getCreationDate());
        booking.setConfirmationDate(bookingDTO.getConfirmationDate());
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setYear(bookingDTO.getYear());
        booking.setMonth(bookingDTO.getMonth());
        for(StructureServiceDTO structureServiceDTO : bookingDTO.getServices()){
            booking.addService(ServiceDao.fromDTOtoStructureService(structureServiceDTO));
        }
        booking.setTotal(bookingDTO.getTotal());

        return booking;
    }

    public static void saveNewReview(StructureReview review, Structure structure){
        MongoConnectionManager mongo = MyHealthCareApplication.mongo;
        System.out.println(mongo.getStructureCollection().updateOne(
                Filters.eq("_id", new ObjectId(structure.getId())),
                Updates.push("reviews", toStructureReviewDTO(review).toBsonDocument())));
    }

    public static StructureReviewDTO toStructureReviewDTO(StructureReview review){
        StructureReviewDTO reviewDTO = new StructureReviewDTO();
        reviewDTO.setUser(UserDao.toDTONoDetails(review.getUser()));
        reviewDTO.setDate(review.getDate());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setText(review.getText());

        return reviewDTO;
    }

    public static StructureReview fromStructureReviewDTO(StructureReviewDTO reviewDTO){
        StructureReview review = new StructureReview();
        review.setUser(UserDao.fromDTO(reviewDTO.getUser()));
        review.setDate(reviewDTO.getDate());
        review.setRating(reviewDTO.getRating());
        review.setText(reviewDTO.getText());

        return review;
    }

    public static List<BasicAggregation> getBookingsPerMonthByYearAndStructure(
            String structureId, Integer year, StructureRepository structureRepository){
        return AggregationUtility.getBookingsPerMonth(
                structureRepository.getBookingsPerMonthByYearAndStructure(structureId, year));
    }

    public static List<BasicAggregation> getReviewsPerRatingByStructure(
            String structureId, StructureRepository structureRepository){
        return AggregationUtility.getReviewsPerRating(
                structureRepository.getReviewsPerRatingByStructure(structureId));
    }

    public static Float getAverageOfReviewsByStructure(String structureId, StructureRepository structureRepository){
        return structureRepository.getAverageOfReviewsByStructure(structureId);
    }

    public static List<BasicAggregation> getUsersWithExpenditureGreaterThanThreshold(
            String structureId, Float threshold, StructureRepository structureRepository){
        return structureRepository.getUsersWithExpenditureGreaterThanThreshold(structureId, threshold);
    }

    public static List<BasicAggregation> getServicesUsageByMonth(
            String structureId, Integer year, Integer month, StructureRepository structureRepository){
        return structureRepository.getServicesUsageByMonth(structureId, year, month);
    }

    public static List<BasicAggregation> getFirstNStructureCostByRegionAndMonthAndYear(
            String region, Integer month, Integer year, Integer limit, StructureRepository structureRepository){
        return structureRepository.getFirstNStructureCostByRegionAndMonthAndYear(region, month, year, limit);
    }

    public static List<BasicAggregation> getBookingsPerStructureByRegionAndYear(
            String region, Integer year, StructureRepository structureRepository){
        return structureRepository.getBookingsPerStructureByRegionAndYear(region, year);
    }
}
