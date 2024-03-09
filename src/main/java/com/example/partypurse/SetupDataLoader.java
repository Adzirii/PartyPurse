package com.example.partypurse;

import com.example.partypurse.models.*;
import com.example.partypurse.repositories.PrivilegeRepository;
import com.example.partypurse.repositories.RoleRepository;
import com.example.partypurse.repositories.RoomRepository;
import com.example.partypurse.repositories.UserRepository;
import com.example.partypurse.services.RoomService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SetupDataLoader {


    private final PrivilegeRepository privilegeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;


    @PostConstruct
    public void dataSetUp() {

        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        Set<Privilege> adminPrivileges = new HashSet<>();
        Set<Privilege> userPrivileges = new HashSet<>();
        adminPrivileges.add(deletePrivilege);
        adminPrivileges.add(writePrivilege);
        adminPrivileges.add(readPrivilege);

        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", userPrivileges);

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");

        User admin  = new User();
        admin.setUsername("admin");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword(passwordEncoder.encode("123"));
        admin.setRoles(Collections.singletonList(adminRole));
        admin.setVisitedRooms(new ArrayList<>());
        admin.setCreatedRooms(new ArrayList<>());
        admin.setAddedProducts(new ArrayList<>());
        userRepository.save(admin);

        User user  = new User();
        user.setUsername("user");
        user.setFirstName("User");
        user.setLastName("User");
        user.setPassword(passwordEncoder.encode("123"));
        user.setRoles(Collections.singletonList(userRole));
        user.setVisitedRooms(new ArrayList<>());
        user.setCreatedRooms(new ArrayList<>());
        user.setAddedProducts(new ArrayList<>());
        userRepository.save(user);



        Room room = new Room();
        room.setName("Room 1");
        room.setRoomCategory(ERoomCategory.CASH);
        room.setInvitationLink(UUID.randomUUID().toString());
        room.setProducts(new ArrayList<>());
        room.setUsers(new ArrayList<>());
        room.setCreatedAt(new Timestamp(new Date().getTime()));
        room.setCreator(user);
        roomRepository.save(room);
        user.getCreatedRooms().add(room);


    }


    protected void createRoleIfNotFound(String name, Set<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }


    protected Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }


}
