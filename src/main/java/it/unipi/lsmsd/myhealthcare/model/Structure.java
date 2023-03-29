package it.unipi.lsmsd.myhealthcare.model;

import com.google.gson.Gson;
import it.unipi.lsmsd.myhealthcare.repository.CityRepository;
import it.unipi.lsmsd.myhealthcare.repository.ServiceRepository;
import it.unipi.lsmsd.myhealthcare.service.BookingUtility;

import java.util.ArrayList;
import java.util.List;

public class Structure {
	private String id;
	private String aslCode, structureCode, name, address, region, area;
	private City city;
	private List<StructureService> services;
	private List<StructureBooking> bookings;
	private List<StructureReview> reviews;
	protected Integer numberOfRenderedBookings;
	protected Float costOfRenderedBookings;

	public Structure(){
		services = new ArrayList<StructureService>();
		bookings = new ArrayList<StructureBooking>();
		reviews = new ArrayList<StructureReview>();
		numberOfRenderedBookings = 0;
		costOfRenderedBookings = 0f;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAslCode() {
		return aslCode;
	}

	public void setAslCode(String aslCode) {
		this.aslCode = aslCode;
	}

	public String getStructureCode() {
		return structureCode;
	}

	public void setStructureCode(String structureCode) {
		this.structureCode = structureCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<StructureService> getServices() {
		return services;
	}

	public List<StructureService> getActiveServices() {
		List<StructureService> activeServices = new ArrayList<StructureService>();
		for(StructureService service : services)
			if(service.isActive())
				activeServices.add(service);
		return activeServices;
	}

	public Integer getNumberOfActiveServices(){
		return getActiveServices().size();
	}

	public StructureService getServiceById(String serviceId){
		for(StructureService service : getServices())
			if(service.getId().equals(serviceId))
				return service;
		return null;
	}

	public void setServices(List<StructureService> services) {
		this.services = services;
	}
	public void addService(StructureService service) {
		services.add(service);
	}

	public List<StructureBooking> getBookings() {
		return bookings;
	}

	public void setBookings(List<StructureBooking> bookings) {
		this.bookings = bookings;
		for(Booking booking : bookings)
			if(booking.getStatus().equals(BookingUtility.getRenderedStatus())) {
				numberOfRenderedBookings++;
				costOfRenderedBookings += booking.getTotal();
			}
	}

	public void addBooking(StructureBooking booking){
		bookings.add(booking);
		if(booking.getStatus().equals(BookingUtility.getRenderedStatus())) {
			numberOfRenderedBookings++;
			costOfRenderedBookings += booking.getTotal();
		}
	}

	public StructureBooking getBookingByCode(String bookingCode){
		for(StructureBooking booking : bookings)
			if(booking.getCode().equals(bookingCode))
				return booking;
		return null;
	}

	public List<StructureReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<StructureReview> reviews) {
		this.reviews = reviews;
	}

	public void addReview(StructureReview review){
		reviews.add(review);
	}

	public Integer getNumberOfRenderedBookings() {
		return numberOfRenderedBookings;
	}

	public Float getCostOfRenderedBookings() {
		return costOfRenderedBookings;
	}

	public String getShortDescription() {
		return name + ", " + city.getName() + " (" + city.getProvince() + ")";
	}

	public String completeToString(CityRepository cityRepository, ServiceRepository serviceRepository){
		String ret = toString() + "\n    id: " + id
				+ "\n    city: " + city.getName() + "\n    services: ";
		for(StructureService service:services) {
			ret += "\n        - " + service;
			if(service.isActive())
				ret += " - active";
			else
				ret += " - not active";
		}

		return ret;
	}

	public String toJSONString(){
		Structure structure = new Structure();
		structure.setId(id);
		structure.setName(name);
		structure.setAslCode(aslCode);
		structure.setStructureCode(structureCode);
		structure.setRegion(region);
		structure.setArea(area);
		structure.setCity(city);
		structure.setAddress(address);
		structure.setServices(services);
		return new Gson().toJson(structure);
	}
}
