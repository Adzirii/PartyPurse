package com.example.partypurse.services;

import com.example.partypurse.dto.request.PasswordUpdateForm;
import com.example.partypurse.dto.request.SignUpRequest;
import com.example.partypurse.dto.request.UserUpdateDto;
import com.example.partypurse.dto.response.UserInfoDto;
import com.example.partypurse.models.Privilege;
import com.example.partypurse.models.Role;
import com.example.partypurse.models.Room;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.RoleRepository;
import com.example.partypurse.repositories.UserRepository;
import com.example.partypurse.util.errors.PasswordComplexityException;
import com.example.partypurse.util.errors.PasswordNotEqualsException;
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
    private final PasswordUtil passwordUtil;
    private final RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);

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

    public User save(SignUpRequest signUpRequest) throws PasswordComplexityException, PasswordNotEqualsException {


//        passwordService.validate(signUpRequest.password(),
//                signUpRequest.confirmPassword());

        User user = new User();
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setUsername(signUpRequest.username());
        user.setVisitedRooms(new ArrayList<>());
        user.setCreatedRooms(new ArrayList<>());
        user.setAddedProducts(new ArrayList<>());
        user.setPassword(passwordUtil.encode(signUpRequest.password()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));
        return userRepository.save(user);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public UserInfoDto getInfo(User user) {
        return new UserInfoDto(user.getId(), user.getUsername(), user.getFirstName(),
                user.getLastName(), getAuthorities(user.getRoles()));
    }

    public List<UserInfoDto> getAllUsers() {

        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(this::getInfo).collect(Collectors.toList());
    }

    public void delete(CustomUserDetails userDetails) {
        User user = findByUsername(userDetails.getUsername());
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

    public UserInfoDto update(UserUpdateDto updateRequest, CustomUserDetails userDetails) {

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

    public List<Room> getAllRooms(CustomUserDetails userDetails) {
        User user = findByUsername(userDetails.getUsername());
        return user.getCreatedRooms();
    }

    public void passwordUpdate2(UserDetails userDetails, PasswordUpdateForm form) {
//        var user = findByUsername(userDetails.getUsername());
//        passwordUtil.verify(form.oldPassword(), userDetails.getPassword());
////        passwordService.validate(form.oldPassword(),form.newPassword(), form.newPasswordConfirm());
//        user.setPassword(passwordUtil.encode(form.newPassword()));
//        userRepository.save(user);
    }
    public void passwordUpdate(UserDetails userDetails, PasswordUpdateForm form) {
        var user = findByUsername(userDetails.getUsername());
        passwordUtil.verify(form.oldPassword(), userDetails.getPassword());
//        passwordService.validate(form.oldPassword(),form.newPassword(), form.newPasswordConfirm());
        user.setPassword(passwordUtil.encode(form.newPassword()));
        userRepository.save(user);
    }
}

 /// jopa
