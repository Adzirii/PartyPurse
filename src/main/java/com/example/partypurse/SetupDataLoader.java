package com.example.partypurse;

import com.example.partypurse.models.*;
import com.example.partypurse.repositories.*;
import com.example.partypurse.services.PasswordUtil;
import com.example.partypurse.services.ProductService;
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


    private final PasswordUtil passwordUtil;
    private final ProductRepository productRepository;
    private final PrivilegeRepository privilegeRepository;
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
        admin.setPassword(passwordUtil.encode("123"));
        admin.setRoles(Collections.singletonList(adminRole));
        admin.setVisitedRooms(new ArrayList<>());
        admin.setCreatedRooms(new ArrayList<>());
        admin.setAddedProducts(new ArrayList<>());
        userRepository.save(admin);

        User user  = new User();
        user.setUsername("user");
        user.setFirstName("User");
        user.setLastName("User");
        user.setPassword(passwordUtil.encode("123"));
        user.setRoles(Collections.singletonList(userRole));
        user.setVisitedRooms(new ArrayList<>());
        user.setCreatedRooms(new ArrayList<>());
        user.setAddedProducts(new ArrayList<>());
        userRepository.save(user);

        User user2  = new User();
        user2.setUsername("adziri");
        user2.setFirstName("ЖЕСТКИЙ");
        user2.setLastName("ЧЕЛ");
        user2.setPassword(passwordUtil.encode("123"));
        user2.setRoles(Collections.singletonList(userRole));
        user2.setVisitedRooms(new ArrayList<>());
        user2.setCreatedRooms(new ArrayList<>());
        user2.setAddedProducts(new ArrayList<>());
        userRepository.save(user2);

        User user3  = new User();
        user3.setUsername("ni3omi");
        user3.setFirstName("МОЩНЫЙ");
        user3.setLastName("ЧЕЛ");
        user3.setPassword(passwordUtil.encode("123"));
        user3.setRoles(Collections.singletonList(userRole));
        user3.setVisitedRooms(new ArrayList<>());
        user3.setCreatedRooms(new ArrayList<>());
        user3.setAddedProducts(new ArrayList<>());
        userRepository.save(user3);

        User user4  = new User();
        user4.setUsername("zetzet");
        user4.setFirstName("ИЛЮМБА");
        user4.setLastName("ШЛЮМБА");
        user4.setPassword(passwordUtil.encode("123"));
        user4.setRoles(Collections.singletonList(userRole));
        user4.setVisitedRooms(new ArrayList<>());
        user4.setCreatedRooms(new ArrayList<>());
        user4.setAddedProducts(new ArrayList<>());
        userRepository.save(user4);



        Room room = new Room();
        room.setName("Комната 1");
        room.setDescription("Описание комнаты 1");
        room.setRoomCategory(ERoomCategory.CASH);
        room.setInvitationLink(UUID.randomUUID().toString());
        room.setProducts(new ArrayList<>());
        room.setUsers(new ArrayList<>());
        room.setCreatedAt(new Timestamp(new Date().getTime()));
        room.setCreator(user);
        room.getUsers().add(user);
        roomRepository.save(room);
        user.getCreatedRooms().add(room);

        Room room2 = new Room();
        room2.setName("Комната 2");
        room2.setDescription("Описание комнаты 2");
        room2.setRoomCategory(ERoomCategory.CASH);
        room2.setInvitationLink(UUID.randomUUID().toString());
        room2.setProducts(new ArrayList<>());
        room2.setUsers(new ArrayList<>());
        room2.setCreatedAt(new Timestamp(new Date().getTime()));
        room2.setCreator(user2);
        room2.getUsers().add(user2);
        roomRepository.save(room2);
        user2.getCreatedRooms().add(room2);


        Product product = new Product();
        product.setProductName("Продукт 1");
        product.setCategory("Еда");
        product.setPrice(50d);
        product.setAdder(user);
        productRepository.save(product);

        room.getProducts().add(product);
        roomRepository.save(room);

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
