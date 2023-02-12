package it.unipi.lsmsd.myhealthcare.mongo.startingdata;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.ReviewDao;
import it.unipi.lsmsd.myhealthcare.dao.BookingDao;
import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.mongo.dto.*;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReviewReader {
    private static String line = "";
    private static String splitBy = ",";
    private static List<ReviewDTO> readReviews;

    public static void create(ReviewRepository reviewRepository,
                              StructureRepository structureRepository,
                              BookingRepository bookingRepository){
        readReviews();
        populateReview(reviewRepository, structureRepository, bookingRepository);
    }
    public static void populateReview(ReviewRepository reviewRepository,
                                      StructureRepository structureRepository,
                                      BookingRepository bookingRepository){
        //reviewRepository.deleteAll();
        List<StructureDTO> storedStructures = StructureDao.readAllMongo(structureRepository);
        int countStructure = 0;
        for(StructureDTO structure : storedStructures){
            countStructure++;
            List<BookingDTO> storedBookings = BookingDao.readByStructureMongo(
                    structure.getId(), bookingRepository);
            int bookingsSize = storedBookings.size() - 1;
            int readReviewsSize = readReviews.size() - 1;
            int quantity = Utility.getRandomInt(50, 300);
            List<ReviewDTO> reviewsToSave = new ArrayList<ReviewDTO>();
            for (int i = 0; i < quantity; i++) {
                BookingDTO booking = storedBookings.get(Utility.getRandomInt(0, bookingsSize));
                ReviewDTO readReview = readReviews.get(Utility.getRandomInt(0, readReviewsSize));
                Integer rating;
                if (readReview.getRating() == 1)
                    rating = Utility.getRandomInt(3, 5);
                else
                    rating = Utility.getRandomInt(1, 2);
                ReviewDTO review = new ReviewDTO(
                        booking.getUser(), booking.getStructure(), rating,
                        BookingCreator.nextDate(booking.getBookingDate()), readReview.getText());
                reviewsToSave.add(review);
            }
            System.out.println(countStructure + " - " + structure.getId()
                    + ": " + quantity + " reviews");
            ReviewDao.createMany(reviewsToSave, reviewRepository);
        }
    }

    public static void readReviews() {
        try {
            readReviews = new ArrayList<ReviewDTO>();
            BufferedReader br = new BufferedReader(new FileReader(
                    new File(MyHealthCareApplication.properties.doctorReviews)));
            br.readLine();

            while ((line = br.readLine()) != null){
                String[] lineSplit = line.split(splitBy);
                ReviewDTO readReview = new ReviewDTO();
                readReview.setText(lineSplit[1]);
                readReview.setRating(Integer.valueOf(lineSplit[2]));
                readReviews.add(readReview);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
