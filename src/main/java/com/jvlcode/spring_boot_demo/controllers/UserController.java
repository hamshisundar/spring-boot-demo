package com.jvlcode.spring_boot_demo.controllers;

import com.jvlcode.spring_boot_demo.entity.UserEntity;
import com.jvlcode.spring_boot_demo.exceptions.ResourceNotFoundException;
import com.jvlcode.spring_boot_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
//    @GetMapping
//    public String getUsers(){
//        return "Helo JVL Code";
//    }
    @GetMapping //getting Users
    public List<UserEntity> getUsers() {
//        return Arrays.asList(
//                new User(1L,"jhon", "John@gmail.com"), new User(2L,"Joe", "joe@gmail.com"), new User(3L,"Com", "com@gmail.com"));
           return userRepository.findAll();
    }
    @PostMapping //Creating Users
    public UserEntity createUser(@RequestBody UserEntity user ){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
// getting user by id
//    @GetMapping("/{id}") // getting user by id
//    public Optional<UserEntity> getUserByID(@PathVariable Long id){
//        return userRepository.findById(id);
//    }

    // Exception handling When User Not Found
    @GetMapping("/{id}")
    public UserEntity getUserByID(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found : "+id));
    }

   //Updating User Data
   @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
    UserEntity userData = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User NOT FOUND"));
    userData.setName(userData.getName());
    userData.setEmail(userData.getEmail());
    return userRepository.save(userData);
    }

    //Delete User Data
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        UserEntity userData = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User NOT FOUND"));
        userRepository.delete(userData);
        return ResponseEntity.ok().build();

    }
}

