package com.app.katchup.Users.repository.node1;

import com.app.katchup.Users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserNode1Repository extends CrudRepository<User, String> {
    User findByUserName(String userName);
}
