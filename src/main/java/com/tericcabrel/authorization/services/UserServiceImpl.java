package com.tericcabrel.authorization.services;

import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.repositories.UserRepository;
import com.tericcabrel.authorization.services.interfaces.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    // private BCryptPasswordEncoder bcryptEncoder;

    public UserServiceImpl(UserRepository userRepository/*, BCryptPasswordEncoder bcryptEncoder*/) {
        this.userRepository = userRepository;
        // this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public User save(UserDto userDto) {
        User newUser = new User();
        Date dateNow = new Date();

        newUser.setEmail(userDto.getEmail())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setPassword(userDto.getPassword())
                // .setPassword(bcryptEncoder.encode(userDto.getPassword()))
                .setGender(userDto.getGender())
                .setConfirmed(false)
                .setEnabled(true)
                .setAvatar(null)
                .setTimezone(userDto.getTimezone())
                .setCoordinates(userDto.getCoordinates())
                .setCreatedAt(dateNow)
                .setUpdatedAt(dateNow);

        return userRepository.save(newUser);
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(new ObjectId(id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(String id) {
        Optional<User> optionalUser = userRepository.findById(new ObjectId(id));

        return optionalUser.orElse(null);
    }

    @Override
    public User update(String id, UserDto userDto) {
        User user = findById(id);

        if(user != null) {
            // All properties must exists in the DTO even if you don't intend to update it
            // Otherwise, it will set to null
            BeanUtils.copyProperties(userDto, user, "password");

            userRepository.save(user);

            return user;
        }

        return null;
    }
}
