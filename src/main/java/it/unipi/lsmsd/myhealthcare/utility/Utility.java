package it.unipi.lsmsd.myhealthcare.utility;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.mongo.dto.CityDTO;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Utility {
    private static SimpleDateFormat dateHourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max+1);
    }

    public static float getRandomFloat(float min, float max) {
        return roundFloat(Math.round((ThreadLocalRandom.current().nextInt(Math.round(min), Math.round(max+1))
                + ThreadLocalRandom.current().nextFloat())*100.0f)/100.0f);
    }

    public static float roundFloat(float value) {
        long factor = (long) Math.pow(10, 2);
        return (float) Math.round(value * factor) / factor;
    }

    public static String crypt(String string) {
        MessageDigest digest;
        byte[] buffer;
        String hexStr = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            buffer = string.getBytes("UTF-8");
            digest.update(buffer);
            byte[] hash = digest.digest();

            for (int i = 0; i < hash.length; i++)
                hexStr +=  Integer.toString( ( hash[i] & 0xff ) + 0x100, 16).substring( 1 );

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {}

        return hexStr;
    }

    public static Date getToday() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getNow(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND);

        String ret = String.valueOf(year);
        if(month < 10)
            ret += "-0" + month;
        else
            ret += "-" + month;
        if(day < 10)
            ret += "-0" + day;
        else
            ret += "-" + day;
        if(hour < 10)
            ret += " 0" + hour;
        else
            ret += " " + hour;
        if(minute < 10)
            ret += ":0" + minute;
        else
            ret += ":" + minute;
        if(second < 10)
            ret += ":0" + second;
        else
            ret += ":" + second;
        if(millis < 10)
            ret += ".00" + millis;
        else
            if(millis < 100)
                ret += ".0" + millis;
            else
                ret += "." + millis;
        return ret;
    }

    public static Date stringToDateHour(String date) {
        try {
            return dateHourFormat.parse(date);
        } catch (ParseException e) {
            return stringToDate(date);
        }
    }

    public static Date stringToDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToHourString(Date date) {
        return dateHourFormat.format(date);
    }
    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public static List<Integer> yearsList(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        calendar.setTime(getToday());
        List<Integer> list = new ArrayList<Integer>();
        for(int year=2021; year <= calendar.get(Calendar.YEAR); year++)
            list.add(year);
        return list;
    }

    public static List<Service> sortServices(List<Service> services){
        Collections.sort(services, new Comparator<Service>() {
            @Override
            public int compare(Service o1, Service o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return services;
    }

    public static List<City> sortCities(List<City> cities){
        Collections.sort(cities, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return cities;
    }

    public static List<Booking> sortBookings(List<Booking> bookings, String criteria, boolean ascending){
        Collections.sort(bookings, new Comparator<Booking>() {
            @Override
            public int compare(Booking o1, Booking o2) {
                if(criteria.equals("creationDate")) {
                    if (ascending)
                        return o1.getCreationDate().compareTo(o2.getCreationDate());
                    else
                        return o2.getCreationDate().compareTo(o1.getCreationDate());
                } else if(criteria.equals("bookingDate")) {
                    if (ascending)
                        return o1.getBookingDate().compareTo(o2.getBookingDate());
                    else
                        return o2.getBookingDate().compareTo(o1.getBookingDate());
                }
                return 0;
            }
        });
        return bookings;
    }

    public static List<Review> sortReviews(List<Review> reviews, boolean ascending){
        Collections.sort(reviews, new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                if (ascending)
                    return o1.getDate().compareTo(o2.getDate());
                else
                    return o2.getDate().compareTo(o1.getDate());
            }
        });
        return reviews;
    }

    public static void setMonths(){
        MyHealthCareApplication.months = new HashMap<String, String>();
        MyHealthCareApplication.months.put("01", "January");
        MyHealthCareApplication.months.put("02", "February");
        MyHealthCareApplication.months.put("03", "March");
        MyHealthCareApplication.months.put("04", "April");
        MyHealthCareApplication.months.put("05", "May");
        MyHealthCareApplication.months.put("06", "June");
        MyHealthCareApplication.months.put("07", "July");
        MyHealthCareApplication.months.put("08", "August");
        MyHealthCareApplication.months.put("09", "September");
        MyHealthCareApplication.months.put("10", "October");
        MyHealthCareApplication.months.put("11", "November");
        MyHealthCareApplication.months.put("12", "December");
    }

    public static String getMonth(String numberOfMonth){
        return MyHealthCareApplication.months.get(numberOfMonth);
    }

    public static City getCityById(String id){
        for(City city : MyHealthCareApplication.cities)
            if(city.getId().equals(id))
                return city;
        return null;
    }

    public static City getCityByName(String name){
        for(City city : MyHealthCareApplication.cities)
            if(city.getName().equals(name.toUpperCase()))
                return city;
        return null;
    }

    public static City getCityByCode(String code){
        for(City city : MyHealthCareApplication.cities)
            if(city.getCode().equals(code))
                return city;
        return null;
    }
}
