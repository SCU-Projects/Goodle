package com.app.feign.client.feignclient.Users;

import katchup.Users.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "katchup-service")
public interface UserFeignClientProxy {

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getAllUsers();

    @GetMapping("/users/{userName}")
    public ResponseEntity<User> getUserByUserName(@PathVariable String userName);

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody @Valid User user);

    @DeleteMapping("/{userName}")
    public ResponseEntity<User> deleteUser(@PathVariable String userName);


}
