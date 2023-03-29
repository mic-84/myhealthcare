package it.unipi.lsmsd.myhealthcare.startingdata;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.dao.UserDao;
import it.unipi.lsmsd.myhealthcare.model.*;
import it.unipi.lsmsd.myhealthcare.repository.StructureRepository;
import it.unipi.lsmsd.myhealthcare.repository.UserRepository;
import it.unipi.lsmsd.myhealthcare.service.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.io.*;

public class BookingAndReviewCreator {
    private static String[] statuses;
    private static String line = "";
    private static String splitBy = ",";
    private static List<Review> reviewsRead;

    public static void populateBookingAndReview(StructureRepository structureRepository,
                                                UserRepository userRepository){
        List<User> storedUsers = UserDao.readAll(userRepository);
        List<Structure> storedStructures = StructureDao.readAll(structureRepository);
        for(User user : storedUsers){
            user.setBookings(new ArrayList<UserBooking>());
            user.setReviews(new ArrayList<UserReview>());
        }
        readReviews();
        statuses = new String[3];
        statuses[0] = BookingUtility.getCreatedStatus();
        statuses[1] = BookingUtility.getConfirmedStatus();
        statuses[2] = BookingUtility.getRenderedStatus();
        int countStructure = 0;
        int countBookings = 0;
        int countReviews = 0;
        for(Structure structure : storedStructures){
            structure.setBookings(new ArrayList<StructureBooking>());
            structure.setReviews(new ArrayList<StructureReview>());
            countStructure++;
            int quantity = Utility.getRandomInt(500, 1000);
            countBookings += quantity;
            int userSize = storedUsers.size() - 1;
            /* create N-quantity bookings for the structure */
            for (int i = 0; i < quantity; i++) {
                Booking booking = randomBooking(structure);
                User user = storedUsers.get(Utility.getRandomInt(0, userSize));
                booking.computeCode(structure, user);
                UserBooking userBooking = BookingUtility.toUserBooking(booking, structure);
                StructureBooking structureBooking = BookingUtility.toStructureBooking(booking, user);
                Review review = null;
                structure.addBooking(structureBooking);
                if (booking.getStatus().equals(statuses[2])) {
                    review = randomReview(booking);
                    StructureReview structureReview = ReviewUtility.toStructureReview(review, user);
                    structure.addReview(structureReview);
                    countReviews++;
                }
                for (int j = 0; j < storedUsers.size(); j++)
                    if (storedUsers.get(j).getId().equals(structureBooking.getUser().getId())) {
                        storedUsers.get(j).addBooking(userBooking);
                        if (review != null) {
                            UserReview userReview = ReviewUtility.toUserReview(review, structure);
                            storedUsers.get(j).addReview(userReview);
                        }
                        break;
                    }
            }
            System.out.println(countStructure + ": creating " + quantity + " bookings for structure "
                    + structure.getId());
            StructureDao.update(structure, structureRepository);
        }
        System.out.println("saving bookings and reviews for users");

        int countUsers = 0;
        for(User user : storedUsers) {
            countUsers++;
            System.out.println(countUsers + ": user " + user.getId() + " - "
                    + user.getNumberOfRenderedBookings() + " bookings and "
                    + user.getReviews().size() + " reviews");
            if(user.getBookings().size() > 0 || user.getReviews().size() > 0)
                UserDao.update(user, userRepository);
        }
        System.out.println(countBookings + " bookings and " + countReviews + " reviews created");
    }

    public static Date randomCreationDate(Date maxDate){
        return Date.from(LocalDateTime.ofInstant(
                        maxDate.toInstant(),
                        ZoneId.systemDefault()).minusDays(Utility.getRandomInt(50,1000))
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date nextDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 10);
        return c.getTime();
    }

    public static void readReviews() {
        try {
            reviewsRead = new ArrayList<Review>();
            BufferedReader br = new BufferedReader(new FileReader(
                    new File(MyHealthCareApplication.properties.doctorReviews)));
            br.readLine();

            while ((line = br.readLine()) != null){
                String[] lineSplit = line.split(splitBy);
                Review review = new Review();
                review.setText(lineSplit[1]);
                review.setRating(Integer.valueOf(lineSplit[2]));
                reviewsRead.add(review);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static Booking randomBooking(Structure structure){
        Booking booking = new Booking();
        booking.setCreationDate(randomCreationDate(Utility.getToday()));
        booking.setConfirmationDate(nextDate(booking.getCreationDate()));
        booking.setBookingDate(nextDate(booking.getConfirmationDate()));
        booking.setStatus(statuses[Utility.getRandomInt(0,2)]);
        int servicesQuantity = 3;
        if(structure.getServices().size() < 3)
            servicesQuantity = Utility.getRandomInt(1,structure.getServices().size());
        for(int j=0; j<servicesQuantity; j++)
            booking.addService(structure.getServices().get(
                    Utility.getRandomInt(0,structure.getServices().size()-1)));
        booking.computeTotal();

        return booking;
    }

    public static Review randomReview(Booking booking){
        int readReviewsSize = reviewsRead.size() - 1;
        Review reviewRead = reviewsRead.get(Utility.getRandomInt(0, readReviewsSize));
        Review review = new Review();
        if (reviewRead.getRating() == 1)
            review.setRating(Utility.getRandomInt(3, 5));
        else
            review.setRating(Utility.getRandomInt(1, 2));
        review.setText(reviewRead.getText());
        review.setDate(nextDate(booking.getBookingDate()));

        return review;
    }
}
