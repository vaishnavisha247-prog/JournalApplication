package com.example.journalInformation.journalInfomation.Controller;

import com.example.journalInformation.journalInfomation.Entity.User;
import com.example.journalInformation.journalInfomation.Repository.UserRepository;
import com.example.journalInformation.journalInfomation.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    //  Get User by userName
    @GetMapping("/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName) {
        User user = userService.findBYUserName(userName);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userName}/journalInfo")
    public ResponseEntity<?> getUserJournals(@PathVariable String userName) {
        User user = userService.findBYUserName(userName);
        if (user != null) {
            return new ResponseEntity<>(user.getJournalEntries(), HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }



    @GetMapping()
    public ResponseEntity<?> getAllUsers(){
        List<User> user =  userService.getAll();
        if (user == null || user.isEmpty()) {

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody User user){
        userService.saveEntry(user);
        return new ResponseEntity<>("User Created successfully",HttpStatus.CREATED);
    }
    @PutMapping()
    public ResponseEntity<?> updateEntity(@RequestBody User user ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User userInDb = userService.findBYUserName(userName);
        if(userInDb != null ){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userService.saveEntry(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUserById(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
