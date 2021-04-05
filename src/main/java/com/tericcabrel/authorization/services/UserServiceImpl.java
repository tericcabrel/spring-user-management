package com.tericcabrel.authorization.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import com.tericcabrel.authorization.models.dtos.CreateUserDto;
import com.tericcabrel.authorization.models.dtos.UpdatePasswordDto;
import com.tericcabrel.authorization.models.dtos.UpdateUserDto;
import com.tericcabrel.authorization.models.entities.User;
import com.tericcabrel.authorization.repositories.UserRepository;


@Service
public class UserServiceImpl implements com.tericcabrel.authorization.services.interfaces.UserService {
    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptEncoder; // Fails when injected by the constructor

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(CreateUserDto createUserDto) {
        User newUser = new User();

        newUser.setEmail(createUserDto.getEmail())
                .setFirstName(createUserDto.getFirstName())
                .setLastName(createUserDto.getLastName())
                .setPassword(bCryptEncoder.encode(createUserDto.getPassword()))
                .setGender(createUserDto.getGender())
                .setConfirmed(createUserDto.isConfirmed())
                .setEnabled(createUserDto.isEnabled())
                .setAvatar(null)
                .setTimezone(createUserDto.getTimezone())
                .setCoordinates(createUserDto.getCoordinates())
                .setRole(createUserDto.getRole());

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
    public User update(String id, UpdateUserDto updateUserDto) {
        User user = findById(id);

        if(user != null) {
            // All properties must exists in the DTO even if you don't intend to update it
            // Otherwise, it will set to null
            // BeanUtils.copyProperties(userDto, user, "password");

            if(updateUserDto.getFirstName() != null) {
                user.setFirstName(updateUserDto.getFirstName());
            }
            if(updateUserDto.getLastName() != null) {
                user.setLastName(updateUserDto.getLastName());
            }
            if(updateUserDto.getTimezone() != null) {
                user.setTimezone(updateUserDto.getTimezone());
            }
            if(updateUserDto.getGender() != null) {
                user.setGender(updateUserDto.getGender());
            }
            if(updateUserDto.getAvatar() != null) {
                user.setAvatar(updateUserDto.getAvatar());
            }
            if(updateUserDto.getCoordinates() != null) {
                user.setCoordinates(updateUserDto.getCoordinates());
            }

            userRepository.save(user);

            return user;
        }

        return null;
    }

    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public User updatePassword(String id, UpdatePasswordDto updatePasswordDto) {
        User user = findById(id);

        if(user != null) {
            if (bCryptEncoder.matches(updatePasswordDto.getCurrentPassword(), user.getPassword())) {
                user.setPassword(bCryptEncoder.encode(updatePasswordDto.getNewPassword()));

                userRepository.save(user);
            } else {
                return null;
            }
        }

        return user;
    }

    @Override
    public User updatePassword(String id, String newPassword) {
        User user = findById(id);

        if(user != null) {
            user.setPassword(bCryptEncoder.encode(newPassword));
            userRepository.save(user);
        }

        return user;
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(), user.getPassword(), true, true, true, true, getAuthority(user)
        );
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        // user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));

        return authorities;
    }
}
