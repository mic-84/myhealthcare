package it.unipi.lsmsd.myhealthcare.startingdata;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.CityDao;
import it.unipi.lsmsd.myhealthcare.dao.StructureDao;
import it.unipi.lsmsd.myhealthcare.dao.UserDao;
import it.unipi.lsmsd.myhealthcare.dto.*;
import it.unipi.lsmsd.myhealthcare.repository.CityRepository;
import it.unipi.lsmsd.myhealthcare.repository.StructureRepository;
import it.unipi.lsmsd.myhealthcare.repository.UserRepository;
import it.unipi.lsmsd.myhealthcare.service.UserUtility;
import it.unipi.lsmsd.myhealthcare.service.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserReader {
	private static String line = "";
	private static String splitBy = ",";

	private static List<UserDTO> users;

	public static void read(CityRepository cityRepository){
		users = new ArrayList<UserDTO>();
		readUsers(cityRepository);
	}
	public static void readUsers(CityRepository cityRepository) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					new File(MyHealthCareApplication.properties.users)));
			br.readLine();

			List<CityDTO> storedCities = CityDao.readAllDTO(cityRepository);

			while ((line = br.readLine()) != null){
				String[] lineSplit = line.split(splitBy);
				String username = (lineSplit[0] + "." + lineSplit[1]).toLowerCase();
				UserDTO user = new UserDTO(username, Utility.getHash(username),lineSplit[0],lineSplit[1],
						username+"@email.com",lineSplit[3],randomCity(storedCities),
						lineSplit[4],lineSplit[2],randomRegistrationDate());
				users.add(user);
			}
			for(int i=0; i<users.size(); i++){
				UserDTO usr = users.get(i);
				for(int j=0; j<i; j++){
					if(usr.getUsername().equals(users.get(j).getUsername())){
						usr.setUsername(usr.getUsername()+"1");
						usr.setPassword(Utility.getHash(usr.getUsername()));
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static CityNoDetailsDTO randomCity(List<CityDTO> storedCities){
		int size = storedCities.size()-1;
		return new CityNoDetailsDTO(storedCities.get(Utility.getRandomInt(0,size)));
	}

	public static Date randomRegistrationDate(){
		return Date.from(LocalDateTime.ofInstant(
				Utility.getToday().toInstant(),
				ZoneId.systemDefault()).minusDays(Utility.getRandomInt(501,1000))
				.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static void populateUser(UserRepository userRepository, CityRepository cityRepository,
                                    StructureRepository structureRepository){
		userRepository.deleteAll();
		read(cityRepository);
		System.out.println("read users from starting data: " + users.size());
		List<UserDTO> usersToSave = new ArrayList<UserDTO>();
		int count = 0;
		for(UserDTO user:users) {
			usersToSave.add(user);
			if(count % 5000 == 0){
				UserDao.createMany(usersToSave, userRepository);
				usersToSave = new ArrayList<UserDTO>();
				System.out.println(count + " users created");
			}
			count++;
		}
		if(usersToSave.size() > 0)
			UserDao.createMany(usersToSave, userRepository);
	}

	public static void populateUserRole(StructureRepository structureRepository,
										UserRepository userRepository){
		List<UserDTO> storedUsers = UserDao.readAllDTO(userRepository);
		String admin = UserUtility.getAdminRole();
		String employee = UserUtility.getEmployeeRole();
		int size = storedUsers.size()-1;

		for(int i=0; i<10; i++) {
			UserDTO user = storedUsers.get(Utility.getRandomInt(0, size));
			user.addRole(admin,null);
			UserDao.update(user,userRepository);
		}
		System.out.println("admin ok");

		List<StructureDTO> storedStructures = StructureDao.readAllDTO(structureRepository);
		int count = 0;
		for(StructureDTO structure : storedStructures){
			StructureNoDetailsDTO structureNoDetailsDTO = new StructureNoDetailsDTO();
			structureNoDetailsDTO.setId(structure.getId());
			structureNoDetailsDTO.setAslCode(structure.getAslCode());
			structureNoDetailsDTO.setStructureCode(structure.getStructureCode());
			structureNoDetailsDTO.setName(structure.getName());
			structureNoDetailsDTO.setCity(structure.getCity());
			structureNoDetailsDTO.setAddress(structure.getAddress());
			structureNoDetailsDTO.setRegion(structure.getRegion());
			for(int i=0; i<2; i++) {
				UserDTO user = storedUsers.get(Utility.getRandomInt(0, size));
				user.addRole(employee,structureNoDetailsDTO);
				UserDao.update(user,userRepository);
			}
			count++;
			System.out.println("employee structure " + count + " ok");
		}
	}
}
