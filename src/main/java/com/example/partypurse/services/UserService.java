package com.example.partypurse.services;

import com.example.partypurse.dto.request.UserUpdateRequest;
import com.example.partypurse.dto.response.RoleDto;
import com.example.partypurse.dto.response.UserDto;
import com.example.partypurse.models.Privilege;
import com.example.partypurse.models.Role;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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


//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(), user.getPassword(), true, true, true,
//                true, getAuthorities(user.getRoles()));

        return new CustomUserDetails(user, getAuthorities(user.getRoles()));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователя с именем '%s' не существует", username)
        ));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Пользователя с таким id не существует"));
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public UserDto getInfo(User user) {
        Set<RoleDto> roles = user.getRoles().stream().map(role -> {

            return new RoleDto(role.getId(), role.getName(), role.getPrivileges());
        }).collect(Collectors.toSet());

        return new UserDto(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), getAuthorities(user.getRoles()));
    }

    public List<UserDto> getAllUsers() {

        List<User> allUsers = findAllUsers();

        return allUsers.stream().map(this::getInfo).collect(Collectors.toList());
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresent(user -> userRepository.deleteById(id));
    }


    public Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    public UserDto update(UserUpdateRequest updateRequest, CustomUserDetails userDetails) {

        User user = findByUsername(userDetails.getUsername());


        if (updateRequest.getUsername() != null)
            user.setUsername(updateRequest.getUsername());
        if (updateRequest.getFirstName() != null)
            user.setFirstName(updateRequest.getFirstName());
        if (updateRequest.getLastName() != null)
            user.setLastName(updateRequest.getLastName());
        userRepository.save(user);
        return getInfo(user);
    }
}
