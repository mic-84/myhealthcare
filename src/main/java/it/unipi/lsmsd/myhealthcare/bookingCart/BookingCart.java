package it.unipi.lsmsd.myhealthcare.bookingCart;

import it.unipi.lsmsd.myhealthcare.dao.ServiceDao;
import it.unipi.lsmsd.myhealthcare.databaseConnection.RedisConnectionManager;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.model.Booking;
import it.unipi.lsmsd.myhealthcare.mongo.repository.ServiceRepository;
import it.unipi.lsmsd.myhealthcare.utility.BookingUtility;
import it.unipi.lsmsd.myhealthcare.utility.PropertiesManager;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BookingCart {
    private RedisConnectionManager redis;
    private User user;
    private Structure structure;
    private final String TYPE = "service";
    private static String host = PropertiesManager.redisHost;

    public BookingCart(User user, Structure structure){
        redis = new RedisConnectionManager(user.getId(), "booking", false);
        this.user = user;
        this.structure = structure;
        for(Map.Entry<String, String> map: redis.getValues(TYPE).entrySet())
            redis.removeItemByKey(map.getKey());
    }

    public void addService(Service service){
        boolean check = true;
            for(Map.Entry<String, String> map: redis.getValues(TYPE).entrySet())
                if(map.getValue().equals(service.getId())) {
                    check = false;
                    break;
                }
        if(check)
            redis.addItem(service.getId(), TYPE);
    }

    public void removeService(String serviceId){
        for(Map.Entry<String, String> map: redis.getValues(TYPE).entrySet())
            if(map.getValue().equals(serviceId))
                redis.removeItemByKey(map.getKey());
    }

    public Booking getBooking(Date bookingDate, ServiceRepository serviceRepository){
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setStructure(structure);
        for(Map.Entry<String, String> map: redis.getValues(TYPE).entrySet()) {
            for(Service structureService : structure.getActiveServices())
                if(map.getValue().equals(structureService.getId())) {
                    Service service = ServiceDao.fromMongo(
                            ServiceDao.readById(map.getValue(), serviceRepository));
                    service.setRate(structure.getServiceById(service.getId()).getRate());
                    booking.addService(service);
                }
        }
        booking.computeTotal();
        booking.setBookingDate(bookingDate);
        booking.setCreationDate(Utility.getToday());
        booking.setStatus(BookingUtility.getCreatedStatus());
        try {
            closeCart();
        } catch(Exception e){}
        return booking;
    }

    public List<Service> getServices(ServiceRepository serviceRepository){
        List<Service> services = new ArrayList<Service>();
        for(Map.Entry<String, String> map: redis.getValues(TYPE).entrySet()) {
            Service service = ServiceDao.fromMongo(
                    ServiceDao.readById(map.getValue(), serviceRepository));
            service.setRate(structure.getServiceById(service.getId()).getRate());
            services.add(service);
        }
        return services;
    }

    public Float getTotal(ServiceRepository serviceRepository){
        Float total = 0f;
        for(Service service : getServices(serviceRepository))
            total += service.getRate();
        return total;
    }

    public boolean isExpired() {
        return redis.isExpired();
    }

    public void closeCart(){
        redis.closeConnection();
    }
}
