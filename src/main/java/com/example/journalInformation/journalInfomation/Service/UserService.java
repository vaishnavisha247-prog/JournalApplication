package com.example.journalInformation.journalInfomation.Service;

import com.example.journalInformation.journalInfomation.Entity.User;
import com.example.journalInformation.journalInfomation.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

   private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void saveEntry(User user){

       user.setPassword(passwordEncoder.encode(user.getPassword()));
       //user.setRole(List.of("USER"));
       //user.setUserName(user.getUserName());
       //user.setPassword(user.getPassword());
       // user.setRole(user.getRole());
        if(user.getRole() == null || user.getRole().isEmpty()){
            user.setRole(List.of("USER"));
        }
        userRepository.save(user);
    }

    public void saveNewUser(User user){
       user.setPassword(passwordEncoder.encode(user.getPassword()));
      if(user.getRole() == null || user.getRole().isEmpty()){
           user.setRole(List.of("USER","ADMIN"));
       }

        userRepository.save(user);
    }
    public List<User> getAll(){

        return userRepository.findAll();
    }
    public Optional<User> findById(ObjectId id){

        return userRepository.findById(id);
    }
    public String deleteById(ObjectId id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return "User deleted successfully";
        }
        return "User not found";
    }
    public User findBYUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public User updateUser(String userName,User updatedUser){
        User existingUser = findBYUserName(userName);
        if(existingUser == null){
            return null;
        }
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setPassword(updatedUser.getPassword());
        return userRepository.save(existingUser);
    }
}
