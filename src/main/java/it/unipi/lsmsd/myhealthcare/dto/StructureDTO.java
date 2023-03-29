package it.unipi.lsmsd.myhealthcare.dto;

import it.unipi.lsmsd.myhealthcare.service.BookingUtility;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection="Structure")
public class StructureDTO extends AbstractStructureDTO {
	@Id
	private String id;
	private Set<StructureServiceDTO> services;
	private List<StructureBookingDTO> bookings;
	private List<StructureReviewDTO> reviews;

	protected Integer numberOfRenderedBookings;
	protected Float costOfRenderedBookings;

	public StructureDTO(){
		services = new HashSet<StructureServiceDTO>();
		bookings = new ArrayList<StructureBookingDTO>();
		reviews = new ArrayList<StructureReviewDTO>();
		numberOfRenderedBookings = 0;
		costOfRenderedBookings = 0f;
	}

	public StructureDTO(String aslCode, String structureCode, String name, String address,
                        CityNoDetailsDTO city, String region, String area) {
		super(aslCode, structureCode, name, address, city, region, area);
		services = new HashSet<StructureServiceDTO>();
		bookings = new ArrayList<StructureBookingDTO>();
		reviews = new ArrayList<StructureReviewDTO>();
		numberOfRenderedBookings = 0;
		costOfRenderedBookings = 0f;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<StructureServiceDTO> getServices() {
		return services;
	}

	public void setServices(Set<StructureServiceDTO> services) {
		this.services = services;
	}
	public void addService(StructureServiceDTO service) {
		services.add(service);
	}

	public List<StructureBookingDTO> getBookings() {
		return bookings;
	}

	public void setBookings(List<StructureBookingDTO> bookings) {
		this.bookings = bookings;
		for(StructureBookingDTO booking : bookings)
			if(booking.getStatus().equals(BookingUtility.getRenderedStatus())) {
				numberOfRenderedBookings++;
				costOfRenderedBookings += booking.getTotal();
			}
	}

	public void addBooking(StructureBookingDTO booking){
		bookings.add(booking);
		if(booking.getStatus().equals(BookingUtility.getRenderedStatus())) {
			numberOfRenderedBookings++;
			costOfRenderedBookings += booking.getTotal();
		}
	}

	public List<StructureReviewDTO> getReviews() {
		return reviews;
	}

	public void setReviews(List<StructureReviewDTO> reviews) {
		this.reviews = reviews;
	}

	public void addReview(StructureReviewDTO review){
		reviews.add(review);
	}

	public Integer getNumberOfRenderedBookings() {
		return numberOfRenderedBookings;
	}

	public Float getCostOfRenderedBookings() {
		return costOfRenderedBookings;
	}

	@Override
	public String toString() {
		return "Structure{" +
				"id='" + id + '\'' +
				", aslCode='" + aslCode + '\'' +
				", structureCode='" + structureCode + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", city='" + city + '\'' +
				", region='" + region + '\'' +
				", services=" + services.size() +
				", numberOfRenderedBookings=" + numberOfRenderedBookings +
				", costOfRenderedBookings=" + costOfRenderedBookings +
				", reviews=" + reviews.size() +
				'}';
	}
}
