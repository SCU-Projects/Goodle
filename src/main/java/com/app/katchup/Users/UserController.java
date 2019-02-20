package com.app.katchup.Users;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	@GetMapping()
	public ResponseEntity<List<User>> getAllUsers(){
	    List<User> users = userService.getAllUsers();
	    return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping("/{userName}")
	public ResponseEntity<User> getUserByUserName(@PathVariable String userName){
	    User user=  userService.getUserByUserName(userName);
	    if(user == null)
	    	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<User> addUser(@RequestBody User user){
		user = userService.addUser(user);
		if(user == null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{userName}")
	public ResponseEntity<User> deleteUser(@PathVariable String userName){
		 User user = userService.deleteUserByUsername(userName);
		 if(user == null)
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}