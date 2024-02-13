package com.example.partypurse;

import com.example.partypurse.models.Privilege;
import com.example.partypurse.models.Role;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.PrivilegeRepository;
import com.example.partypurse.repositories.RoleRepository;
import com.example.partypurse.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (true)
            return;
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        Set<Privilege> adminPrivileges = new HashSet<>();
        Set<Privilege> userPrivileges = new HashSet<>();
        adminPrivileges.add(readPrivilege);
        userPrivileges.add(readPrivilege);
        adminPrivileges.add(writePrivilege);

        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", userPrivileges);

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        User user  = new User();
        user.setUsername("admin");
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setPassword(passwordEncoder.encode("123"));
        user.setRoles(Collections.singletonList(adminRole));
        user.setVisitedRooms(new ArrayList<>());
        user.setCreatedRooms(new ArrayList<>());
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    protected void createRoleIfNotFound(String name, Set<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }

    @Transactional
    protected Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }


}
