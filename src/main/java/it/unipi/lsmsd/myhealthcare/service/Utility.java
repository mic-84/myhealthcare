package it.unipi.lsmsd.myhealthcare.service;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.model.*;

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
    private static final SimpleDateFormat dateHourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateHourNoSecondsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
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

    public static String getHash(String string) {
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

    public static Integer[] getLastMonth(){
        Integer month[] = new Integer[2];
        Date today = getToday();
        Date previous = Date.from(LocalDateTime.ofInstant(
                        today.toInstant(),
                        ZoneId.systemDefault()).minusMonths(1)
                .atZone(ZoneId.systemDefault()).toInstant());
        month[0] = Integer.valueOf(new SimpleDateFormat("yyyy").format(previous));
        month[1] = Integer.valueOf(new SimpleDateFormat("MM").format(previous));
        return month;
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

    public static String getNowNoSymbols(){
        return getNow().replace("-","").replace(":","").replace(".","").replace(" ","");
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

    public static Date stringToDateNoSeconds(String date) {
        try {
            return dateHourNoSecondsFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateHourToString(Date date) {
        return dateHourFormat.format(date);
    }

    public static String dateHourToStringNoSeconds(Date date) {
        return dateHourNoSecondsFormat.format(date);
    }
    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public static String hourToString(Date date) {
        return hourFormat.format(date);
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

    public static List<StructureService> sortStructureServices(List<StructureService> services){
        Collections.sort(services, new Comparator<StructureService>() {
            @Override
            public int compare(StructureService o1, StructureService o2) {
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

    public static void setRegionsAndAreas(){
        MyHealthCareApplication.regions = new HashMap<Integer, String>();
        MyHealthCareApplication.regions.put(3,"Lombardia");
        MyHealthCareApplication.regions.put(16,"Puglia");
        MyHealthCareApplication.regions.put(10,"Umbria");

        MyHealthCareApplication.areas = new ArrayList<String>();
        MyHealthCareApplication.areas.add("North");
        MyHealthCareApplication.areas.add("Centre");
        MyHealthCareApplication.areas.add("South");
    }

    public static String getRegion(Integer key){
        return MyHealthCareApplication.regions.get(key);
    }

    public static boolean regionExists(String key){
        return MyHealthCareApplication.regions.containsKey(Integer.valueOf(key));
    }
    public static void setMonths(){
        MyHealthCareApplication.months = new HashMap<Integer, String>();
        MyHealthCareApplication.months.put(1, "January");
        MyHealthCareApplication.months.put(2, "February");
        MyHealthCareApplication.months.put(3, "March");
        MyHealthCareApplication.months.put(4, "April");
        MyHealthCareApplication.months.put(5, "May");
        MyHealthCareApplication.months.put(6, "June");
        MyHealthCareApplication.months.put(7, "July");
        MyHealthCareApplication.months.put(8, "August");
        MyHealthCareApplication.months.put(9, "September");
        MyHealthCareApplication.months.put(10, "October");
        MyHealthCareApplication.months.put(11, "November");
        MyHealthCareApplication.months.put(12, "December");
    }

    public static String getMonth(String numberOfMonth){
        return MyHealthCareApplication.months.get(Integer.valueOf(numberOfMonth));
    }

    public static void setHours(){
        MyHealthCareApplication.hours = new ArrayList<String>();
        MyHealthCareApplication.hours.add("09:00");
        MyHealthCareApplication.hours.add("10:00");
        MyHealthCareApplication.hours.add("11:00");
        MyHealthCareApplication.hours.add("12:00");
        MyHealthCareApplication.hours.add("13:00");
        MyHealthCareApplication.hours.add("14:00");
        MyHealthCareApplication.hours.add("15:00");
        MyHealthCareApplication.hours.add("16:00");
        MyHealthCareApplication.hours.add("17:00");
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
            if(city.getCode().contains(code))
                return city;
        return null;
    }

    public static int getYear(Date date){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.MONTH)+1;
    }
}
