package it.unipi.lsmsd.myhealthcare.dao;

import it.unipi.lsmsd.myhealthcare.model.BookingStatus;
import it.unipi.lsmsd.myhealthcare.mongo.dto.BookingStatusDTO;
import it.unipi.lsmsd.myhealthcare.mongo.repository.BookingStatusRepository;

import java.util.ArrayList;
import java.util.List;

public class BookingStatusDao {
    public static BookingStatus fromMongo(BookingStatusDTO bookingStatusDTO) {
        BookingStatus bookingStatus = new BookingStatus();
        bookingStatus.setId(bookingStatusDTO.getId());
        bookingStatus.setDescription(bookingStatusDTO.getDescription());
        bookingStatus.setMeaning(bookingStatusDTO.getMeaning());
        return bookingStatus;
    }

    public static BookingStatusDTO toMongo(BookingStatus bookingStatus){
        BookingStatusDTO bookingStatusDTO = new BookingStatusDTO();
        bookingStatusDTO.setId(bookingStatus.getId());
        bookingStatusDTO.setDescription(bookingStatus.getDescription());
        return bookingStatusDTO;
    }

    public static void create(BookingStatusDTO object, BookingStatusRepository bookingStatusRepository){
        bookingStatusRepository.save(object);
    }

    public static void create(BookingStatus object, BookingStatusRepository bookingStatusRepository){
        bookingStatusRepository.save(toMongo(object));
    }

    public static BookingStatusDTO readById(String id, BookingStatusRepository bookingStatusRepository){
        return bookingStatusRepository.findById(id).get();
    }

    public static List<BookingStatusDTO> readAllMongo(BookingStatusRepository bookingStatusRepository){
        return bookingStatusRepository.findAll();
    }

    public static List<BookingStatus> readAll(BookingStatusRepository bookingStatusRepository){
        List<BookingStatus> objects = new ArrayList<BookingStatus>();
        for(BookingStatusDTO object:readAllMongo(bookingStatusRepository))
            objects.add(fromMongo(object));
        return objects;
    }

    public static void update(BookingStatusDTO object, BookingStatusRepository bookingStatusRepository){
        bookingStatusRepository.save(object);
    }

    public static void update(BookingStatus object, BookingStatusRepository bookingStatusRepository){
        bookingStatusRepository.save(toMongo(object));
    }

    public static void delete(BookingStatusDTO object, BookingStatusRepository bookingStatusRepository){
        bookingStatusRepository.delete(object);
    }

    public static void delete(BookingStatus object, BookingStatusRepository bookingStatusRepository){
        bookingStatusRepository.delete(toMongo(object));
    }

    public static Long size(BookingStatusRepository bookingStatusRepository){
        return bookingStatusRepository.count();
    }
}
