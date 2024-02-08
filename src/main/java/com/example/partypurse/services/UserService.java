package com.example.partypurse.services;

import com.example.partypurse.dto.response.RoleDto;
import com.example.partypurse.dto.response.UserDto;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return new CustomUserDetails(user, user.getRoles());
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователя с именем '%s' не существует", username)
        ));

    }
    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public List<User> findAllUsers(){
        return new ArrayList<>(userRepository.findAll());
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public UserDto getInfo(User user){
        Set<RoleDto> roles = user.getRoles().stream().map(role -> new RoleDto(role.getId(), role.getName())).collect(Collectors.toSet());
        return new UserDto(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), roles);
    }

    public List<UserDto> getAllUsers(){

        List<User> allUsers = findAllUsers();

        return allUsers.stream().map(this::getInfo).collect(Collectors.toList());
    }

    public void delete(User user){
        userRepository.delete(user);
    }
    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> userRepository.deleteById(id));
    }


}
