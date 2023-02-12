package it.unipi.lsmsd.myhealthcare.mongo.startingdata;

import it.unipi.lsmsd.myhealthcare.MyHealthCareApplication;
import it.unipi.lsmsd.myhealthcare.dao.RoleDao;
import it.unipi.lsmsd.myhealthcare.dao.UserDao;
import it.unipi.lsmsd.myhealthcare.mongo.dto.*;
import it.unipi.lsmsd.myhealthcare.mongo.repository.*;
import it.unipi.lsmsd.myhealthcare.utility.Utility;

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

			List<CityDTO> storedCities = cityRepository.findAll();

			while ((line = br.readLine()) != null){
				String[] lineSplit = line.split(splitBy);
				String username = (lineSplit[0] + "." + lineSplit[1]).toLowerCase();
				UserDTO user = new UserDTO(username, Utility.crypt(username),lineSplit[0],lineSplit[1],
						username+"@email.com",lineSplit[3],randomCity(storedCities),
						lineSplit[4],lineSplit[2],randomRegistrationDate());
				users.add(user);
			}
			for(int i=0; i<users.size(); i++){
				UserDTO usr = users.get(i);
				for(int j=0; j<i; j++){
					if(usr.getUsername().equals(users.get(j).getUsername())){
						usr.setUsername(usr.getUsername()+"1");
						usr.setPassword(Utility.crypt(usr.getUsername()));
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static String randomCity(List<CityDTO> storedCities){
		int size = storedCities.size()-1;
		return storedCities.get(Utility.getRandomInt(0,size)).getId();
	}

	public static String randomRegistrationDate(){
		return Utility.dateToHourString(Date.from(LocalDateTime.ofInstant(
				Utility.getToday().toInstant(),
				ZoneId.systemDefault()).minusDays(Utility.getRandomInt(501,1000))
				.atZone(ZoneId.systemDefault()).toInstant()));
	}

	public static void populateUser(UserRepository userRepository, CityRepository cityRepository,
									StructureRepository structureRepository, RoleRepository roleRepository){
		userRepository.deleteAll();
		read(cityRepository);
		System.out.println("read users from starting data: " + users.size());
		List<UserDTO> usersToSave = new ArrayList<UserDTO>();
		int count = 0;
		for(UserDTO user:users) {
			usersToSave.add(user);
			if(count % 10000 == 0){
				UserDao.createMany(usersToSave, userRepository);
				usersToSave = new ArrayList<UserDTO>();
			}
			count++;
		}
		if(usersToSave.size() > 0)
			UserDao.createMany(usersToSave, userRepository);
		populateUserRole(structureRepository, roleRepository, userRepository);
	}

	public static void populateRole(RoleRepository roleRepository){
		RoleDao.create(new RoleDTO("admin"),roleRepository);
		RoleDao.create(new RoleDTO("employee"),roleRepository);
	}

	public static void populateUserRole(StructureRepository structureRepository,
										RoleRepository roleRepository, UserRepository userRepository){
		List<UserDTO> storedUsers = UserDao.readAllMongo(userRepository);
		String admin = RoleDao.readByDescription("admin",roleRepository).getId();
		String employee = RoleDao.readByDescription("employee",roleRepository).getId();
		int size = storedUsers.size()-1;

		for(int i=0; i<10; i++) {
			UserDTO user = storedUsers.get(Utility.getRandomInt(0, size));
			user.addRole(admin,null);
			UserDao.update(user,userRepository);
		}
		System.out.println("admin ok");

		int count = 0;
		for(StructureDTO structure:structureRepository.findAll()){
			for(int i=0; i<4; i++) {
				UserDTO user = storedUsers.get(Utility.getRandomInt(0, size));
				user.addRole(employee,structure.getId());
				UserDao.update(user,userRepository);
			}
			count++;
			System.out.println("employee structure " + count + " ok");
		}
	}
}
