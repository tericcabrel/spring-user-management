package com.tericcabrel.authorization.services;

import org.bson.types.ObjectId;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import com.tericcabrel.authorization.dtos.UserDto;
import com.tericcabrel.authorization.models.User;
import com.tericcabrel.authorization.repositories.UserRepository;
import com.tericcabrel.authorization.services.interfaces.IUserService;
import com.tericcabrel.authorization.dtos.UpdatePasswordDto;
import com.tericcabrel.authorization.dtos.UpdateUserDto;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptEncoder) {
        this.userRepository = userRepository;
        this.bCryptEncoder = bCryptEncoder;
    }

    @Override
    public User save(UserDto userDto) {
        User newUser = new User();

        newUser.setEmail(userDto.getEmail())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setPassword(bCryptEncoder.encode(userDto.getPassword()))
                .setGender(userDto.getGender())
                .setConfirmed(userDto.isConfirmed())
                .setEnabled(userDto.isEnabled())
                .setAvatar(null)
                .setTimezone(userDto.getTimezone())
                .setCoordinates(userDto.getCoordinates())
                .setRoles(userDto.getRoles());

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
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return authorities;
    }
}
