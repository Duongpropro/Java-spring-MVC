package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getHomePage(){
        return "Get HomePage";
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    public User saveUser(User user){
        return this.userRepository.save(user);
    }
    public List<User> getAllUsersByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
}

