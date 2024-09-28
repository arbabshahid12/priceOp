package com.priceComparison.controller;

import com.priceComparison.model.Users;
import com.priceComparison.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<Users>> getAllUsers(){
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/createUser")
    public ResponseEntity<Users> createUser(@RequestBody Users user){
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/editUser/{id}")
    public ResponseEntity<Users> editUser(@PathVariable(value = "id") Long userId, @RequestBody Users newUser){
        return new ResponseEntity<>(userService.updateUser(userId, newUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") Long userId){
        return new ResponseEntity<>(userService.delete(userId), HttpStatus.NO_CONTENT);
    }

}
