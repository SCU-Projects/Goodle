package com.app.katchup.Users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
	private static final Logger logger = LogManager.getLogger(com.app.katchup.Users.UserService.class);

	@Autowired
	private UserRepository userRepository;
	
	public User getUserByUserName(String userName) {
		User user = userRepository.findByUserName(userName);
		if(user == null) {
			logger.info("GET User {} not found.",userName);
			return user;
		}
		logger.info("GET User {} returned.",userName);
		return user;
	}
	
	public List<User> getAllUsers(){
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users::add);
		logger.info("GET User {} returned.", users);
		return users;
	}
	
	public User addUser(User user) {
	    user.setUserName(user.getUserName().replaceAll("\\s+",""));
		if(userRepository.findByUserName(user.getUserName()) != null) {
			logger.error("POST User {} already exists.",user.getUserName());
			return null;
		}
		userRepository.save(user);
		if(userRepository.findByUserName(user.getUserName()) == null) {
			logger.error("POST User {} not saved.", user.getUserName());
			return null;
		}
		logger.info("POST User {} created.", user.getUserName());
		return user;
	}	
	
	public User deleteUserByUsername(String username) {
		User user = userRepository.findByUserName(username);
		if(user == null) {
			logger.error("DELETE User {} not found.", username);
			return user;
		}
		userRepository.delete(user);
		logger.info("DELETE User {} deleted.", username);
		return user;
	}

	public boolean isCredentialsMatched(String userName, String password){
		User user =  this.getUserByUserName(userName);
		if(user.getPassword().equals(password))
			return true;
		return false;
	}

}