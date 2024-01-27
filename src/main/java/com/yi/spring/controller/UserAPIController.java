package com.yi.spring.controller;

import com.yi.spring.entity.User;
import com.yi.spring.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;


@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserAPIController {

    @Autowired
     UserService userservice;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){

        List<User> users = userservice.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User saveduser = userservice.createUser(user);

        return new ResponseEntity<>(saveduser, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id,
                                           @RequestBody User user){
        user.setId(id);
        User updatedUser = userservice.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<User> deleteUser(@RequestBody User user){

     userservice.deleteUser(user);
     return new ResponseEntity<>(HttpStatus.OK);
    }
}
