package com.app.katchup.Users.repository.node0;

import com.app.katchup.Users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserNode0Repository extends CrudRepository<User, String> {
    User findByUserName(String userName);
}