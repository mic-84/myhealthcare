package it.unipi.lsmsd.myhealthcare.session;

import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.dao.UserDao;
import it.unipi.lsmsd.myhealthcare.databaseConnection.RedisConnectionManager;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.model.Booking;
import it.unipi.lsmsd.myhealthcare.service.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BookingCart {
    private RedisConnectionManager redis;
    private final String SERVICE_TYPE = "service";
    private final String BOOKING_TYPE = "booking";
    private final Integer expirationTime = PropertiesManager.redisBookingExpirationTime;

    public BookingCart(User user, Structure structure){
        redis = new RedisConnectionManager(user.getId(), BOOKING_TYPE, expirationTime);
        String key = user.getId() + ":structure";
        if(!redis.existsKey(key)) {
            redis.setItemByKey(key, structure.getId());
            redis.setExpirationTime(key, expirationTime, BOOKING_TYPE);
        }
        else
            System.out.println("key " + user.getId() + ":" + BOOKING_TYPE + " already exists");
        redis.closeConnection();
    }

    public String addService(Service service, String userId, String structureId){
        redis = new RedisConnectionManager(userId, BOOKING_TYPE, expirationTime);
        String ret = "OK";
        String key = userId + ":structure";
        if(redis.getItemByKey(key).equals(structureId)) {
            System.out.println("\nredis.getValues: " + redis.getValues(userId, SERVICE_TYPE).entrySet().size());
            for (Map.Entry<String, String> map : redis.getValues(userId, SERVICE_TYPE).entrySet()) {
                System.out.println("  - " + map.getKey() + " - " + map.getValue());
                if (map.getValue().equals(service.getId())) {
                    ret = "service already selected";
                    break;
                }
            }
            if (ret.equals("OK")) {
                redis.addItem(userId, service.getId(), SERVICE_TYPE + ":" + Utility.getNowNoSymbols(), key, expirationTime);
                System.out.println("\nadded - redis.getValues: " + redis.getValues(userId, SERVICE_TYPE).entrySet().size());
                for (Map.Entry<String, String> map : redis.getValues(userId, SERVICE_TYPE).entrySet())
                    System.out.println("  - " + map.getKey() + " - " + map.getValue());
                ret = "service successfully added";
            }
        } else
            ret = "you can't select services of different structures in the same booking";

        redis.closeConnection();

        return ret;
    }

    public void removeService(String userId, String serviceId){
        redis = new RedisConnectionManager(userId, BOOKING_TYPE, expirationTime);
        for(Map.Entry<String, String> map: redis.getValues(userId, SERVICE_TYPE).entrySet())
            if(map.getKey().contains(userId) && map.getValue().equals(serviceId)) {
                redis.removeItemByKey(map.getKey());
                break;
            }
        redis.closeConnection();
    }

    public Booking saveBooking(User user, Structure structure, Date bookingDate){
        redis = new RedisConnectionManager(user.getId(), BOOKING_TYPE, expirationTime);
        Booking booking = new Booking();
        booking.computeCode(structure, user);
        booking.setBookingDate(bookingDate);
        booking.setCreationDate(Utility.getToday());
        booking.setStatus(BookingUtility.getCreatedStatus());

        for(Map.Entry<String, String> map: redis.getValues(user.getId(), SERVICE_TYPE).entrySet()) {
            for(StructureService service : structure.getActiveServices())
                if(map.getKey().contains(user.getId()) && map.getValue().equals(service.getId()))
                    booking.addService(service);
        }
        booking.computeTotal();

        UserBooking userBooking = BookingUtility.toUserBooking(booking, structure);
        user.addBooking(userBooking);

        StructureBooking structureBooking = BookingUtility.toStructureBooking(booking, user);
        structure.addBooking(structureBooking);

        StructureDao.saveNewBooking(structureBooking, structure);
        UserDao.saveNewBooking(userBooking, user);

        for(Map.Entry<String, String> map: redis.getValues(user.getId(), SERVICE_TYPE).entrySet())
            redis.removeItemByKey(map.getKey());

        redis.closeConnection();

        return booking;
    }

    public List<StructureService> getServices(User user, Structure structure){
        redis = new RedisConnectionManager(user.getId(), BOOKING_TYPE, expirationTime);
        List<StructureService> services = new ArrayList<StructureService>();
        for(Map.Entry<String, String> map: redis.getValues(user.getId(), SERVICE_TYPE).entrySet()) {
            if(map.getKey().contains(user.getId())) {
                StructureService service = structure.getServiceById(map.getValue());
                service.setRate(structure.getServiceById(service.getId()).getRate());
                services.add(service);
            }
        }
        redis.closeConnection();

        return services;
    }

    public Float getTotal(User user, Structure structure){
        redis = new RedisConnectionManager(user.getId(), BOOKING_TYPE, expirationTime);
        Float total = 0f;
        for(StructureService service : getServices(user, structure))
            total += service.getRate();
        redis.closeConnection();

        return total;
    }

    public boolean isExpired(User user) {
        redis = new RedisConnectionManager(user.getId(), BOOKING_TYPE, null);
        return redis.isExpired();
    }
}
