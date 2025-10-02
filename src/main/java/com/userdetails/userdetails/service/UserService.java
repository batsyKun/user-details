package com.userdetails.userdetails.service;

import com.userdetails.userdetails.entity.User;
import com.userdetails.userdetails.entity.Address;
import com.userdetails.userdetails.repository.UserRepository;
import com.userdetails.userdetails.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        user.setAge(User.calculateAge(user.getDob()));
        if (user.getAddresses() != null) {
            for (Address addr : user.getAddresses()) {
                addr.setUser(user);
            }
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDetails.getUsername());
        user.setDob(userDetails.getDob());
        user.setAge(User.calculateAge(userDetails.getDob()));
        user.setGender(userDetails.getGender());
        user.setCommunicationLanguages(userDetails.getCommunicationLanguages());
        user.setDocumentPaths(userDetails.getDocumentPaths());
        user.setPhotoPath(userDetails.getPhotoPath());

        // Handle addresses
        user.getAddresses().clear();
        if (userDetails.getAddresses() != null) {
            for (Address addr : userDetails.getAddresses()) {
                addr.setUser(user);
                user.getAddresses().add(addr);
            }
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}