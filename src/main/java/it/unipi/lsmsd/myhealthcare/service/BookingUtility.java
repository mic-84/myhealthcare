package it.unipi.lsmsd.myhealthcare.service;

import it.unipi.lsmsd.myhealthcare.model.*;

import java.util.*;

public class BookingUtility {
    public static String getCreatedStatus(){
        return "created";
    }

    public static String getConfirmedStatus(){
        return "confirmed";
    }

    public static String getRenderedStatus(){
        return "rendered";
    }

    public static String getCancelledStatus() {
        return "cancelled";
    }

    public static UserBooking toUserBooking(Booking booking, Structure structure){
        UserBooking userBooking = new UserBooking();
        userBooking.setCode(booking.getCode());
        userBooking.setStructure(structure);
        userBooking.setStatus(booking.getStatus());
        userBooking.setCreationDate(booking.getCreationDate());
        userBooking.setConfirmationDate(booking.getConfirmationDate());
        userBooking.setBookingDate(booking.getBookingDate());
        userBooking.setServices(booking.getServices());
        userBooking.setTotal(booking.getTotal());

        return userBooking;
    }

    public static StructureBooking toStructureBooking(Booking booking, User user){
        StructureBooking structureBooking = new StructureBooking();
        structureBooking.setCode(booking.getCode());
        structureBooking.setUser(user);
        structureBooking.setStatus(booking.getStatus());
        structureBooking.setCreationDate(booking.getCreationDate());
        structureBooking.setConfirmationDate(booking.getConfirmationDate());
        structureBooking.setBookingDate(booking.getBookingDate());
        structureBooking.setServices(booking.getServices());
        structureBooking.setTotal(booking.getTotal());

        return structureBooking;
    }

    public static List<StructureBooking> sortStructureBookings(List<StructureBooking> bookings, String criteria, boolean ascending){
        Collections.sort(bookings, new Comparator<StructureBooking>() {
            @Override
            public int compare(StructureBooking o1, StructureBooking o2) {
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

    public static List<StructureBooking> getStructureBookings(List<StructureBooking> bookings, String criteria,
            boolean order, String sortCriteria, boolean ascending) {
        if(criteria.equals("all"))
            return sortStructureBookings(bookings, sortCriteria, ascending);

        List<StructureBooking> list = new ArrayList<StructureBooking>();
        for(StructureBooking booking : bookings)
            if(booking.getStatus().equals(getCreatedStatus()) ||
                    booking.getStatus().equals(getConfirmedStatus()))
                list.add(booking);
        return sortStructureBookings(list, sortCriteria, ascending);
    }

    public static List<UserBooking> sortUserBookings(List<UserBooking> bookings, String criteria, boolean ascending){
        Collections.sort(bookings, new Comparator<UserBooking>() {
            @Override
            public int compare(UserBooking o1, UserBooking o2) {
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
}
