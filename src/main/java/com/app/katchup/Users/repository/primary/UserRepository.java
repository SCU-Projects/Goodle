package com.app.katchup.Users.repository.primary;

import com.app.katchup.Users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, String> {
	User findByUserName(String userName);
}