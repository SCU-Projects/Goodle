package katchup.Users;

import katchup.Exception.NotFoundException;
import katchup.Sharding.Utilities;
import katchup.Users.repository.node0.UserNode0Repository;
import katchup.Users.repository.node1.UserNode1Repository;
import katchup.Users.repository.node2.UserNode2Repository;
import katchup.Users.repository.primary.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	private static final Logger logger = LogManager.getLogger(katchup.Users.UserService.class);

//	@Autowired
//	private UserRepository userRepository;

	@Autowired
	private UserNode0Repository userNode0Repository;

	@Autowired
	private UserNode1Repository userNode1Repository;

	@Autowired
	private UserNode2Repository userNode2Repository;



	public User getUserByUserName(String userName) {
		User user = retrieveFromTable(userName);
		if(user == null)
			logger.info("GET User {} not found.",userName);
		else
			logger.info("GET User {} returned.",userName);
		return user;
	}

	public User addUser(User user) throws Exception {
	    user.setUserName(user.getUserName().replaceAll("\\s+",""));
		if(retrieveFromTable(user.getUserName()) != null) {
			logger.error("POST User {} already exists.",user.getUserName());
			return null;
		}
		user = saveUserToTable(user);
		logger.info("POST User {} created.", user.getUserName());
		return user;
	}

	public void deleteUserByUsername(String userName) throws Exception {
		User user = retrieveFromTable(userName);
		if(user == null) {
			logger.error("DELETE User {} not found.", userName);
			throw new NotFoundException("User not found");
		}
		deleteUserFromTable(user);
		logger.info("DELETE User {} deleted.", userName);
	}

	public boolean isCredentialsMatched(String userName, String password){
		User user =  this.getUserByUserName(userName);
		if(user != null && user.getPassword().equals(password))
			return true;
		return false;
	}

	private User saveUserToTable(User user) throws Exception {
		Integer databaseId = Utilities.getShardedDBLocation(user.getUserName()).ordinal();
		switch (databaseId){
			case 0:
				return userNode0Repository.save(user);
			case 1:
				return userNode1Repository.save(user);
			case 2:
				return userNode2Repository.save(user);
			default:
				throw new Exception("Error saving user to the table");
		}
	}

	private void deleteUserFromTable(User user) throws Exception {
		Integer databaseId = Utilities.getShardedDBLocation(user.getUserName()).ordinal();
		switch (databaseId){
			case 0:
				 userNode0Repository.delete(user);
				 break;
			case 1:
				 userNode1Repository.delete(user);
				 break;
			case 2:
				 userNode2Repository.delete(user);
				 break;
			default:
				throw new Exception("Error deleting user from the table");
		}
	}


	private User retrieveFromTable(String userName){
		Integer databaseId = Utilities.getShardedDBLocation(userName).ordinal();
		switch (databaseId){
			case 0:
				return userNode0Repository.findByUserName(userName);
			case 1:
				return userNode1Repository.findByUserName(userName);
			case 2:
				return userNode2Repository.findByUserName(userName);
		}
		return null;
	}

}