package com.example.partypurse.services;

import com.example.partypurse.dto.request.ProductCreationForm;
import com.example.partypurse.dto.request.ProductUpdateForm;
import com.example.partypurse.dto.request.RoomCreationForm;
import com.example.partypurse.dto.request.RoomUpdateForm;
import com.example.partypurse.dto.response.ProductInfoDto;
import com.example.partypurse.dto.response.RoomInfoDto;
import com.example.partypurse.dto.response.UserInfoDto;
import com.example.partypurse.models.Product;
import com.example.partypurse.models.Room;
import com.example.partypurse.models.User;
import com.example.partypurse.repositories.RoomRepository;
import com.example.partypurse.repositories.UserRepository;
import com.example.partypurse.util.errors.ProductModifyException;
import com.example.partypurse.util.errors.RoomAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;
    private final ProductService productService;
    private final UserRepository userRepository;


    public Room findRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Такой комнаты не существует"));
    }

    public Room findByLink(String link) {
        return roomRepository.findByInvitationLink(link).orElseThrow(() -> new IllegalArgumentException("Комнаты с такой ссылкой не существует"));
    }

    public List<RoomInfoDto> getAll(){
        var rooms = roomRepository.findAll();
        List<RoomInfoDto> res = new ArrayList<>();
        for(var room : rooms)
            res.add(info(room));
        return res;
    }

    public RoomInfoDto getInfoById(Long id) {
        Room room = findRoomById(id);

        return info(room);
    }

    public RoomInfoDto getInfoByLink(String link) {
        Room room = findByLink(link);
        return info(room);
    }

    public String getInviteLink(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        checkRoomCreator(room, userDetails);
        return room.getInvitationLink();
    }


    public RoomInfoDto save(RoomCreationForm form, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        Room room = new Room();
        room.setName(form.name());
        room.setDescription(form.description());
        room.setRoomCategory(form.category());
        room.setInvitationLink(UUID.randomUUID().toString());
        room.setProducts(new ArrayList<>());
        room.setUsers(new ArrayList<>());
        room.setCreatedAt(new Timestamp(new Date().getTime()));
        room.setCreator(user);
        room.getUsers().add(user);
        user.getCreatedRooms().add(room);
        roomRepository.save(room);
        userRepository.save(user);
        return info(room);
    }

    public String update(Long id, RoomUpdateForm form, UserDetails userDetails) {
        Room room = findRoomById(id);
        checkRoomCreator(room, userDetails);

        if (form.getName() != null)
            room.setName(form.getName());

        if (form.getDescription() != null)
            room.setDescription(form.getDescription());

        if (form.getCategory() != null)
            room.setRoomCategory(form.getCategory());
        roomRepository.save(room);
        return "Комната изменена";
    }

    public String delete(Long id, UserDetails userDetails) {
        Room room = findRoomById(id);
        checkRoomCreator(room, userDetails);
        roomRepository.delete(room);
        return "Комната удалена";
    }


    public String join(String link, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Room room = findByLink(link);
        if (room.getUsers().contains(user))
            return "Вы уже являетесь участником";
        room.getUsers().add(user);
        user.getVisitedRooms().add(room);
        roomRepository.save(room);
        userRepository.save(user);
        return "Вы успешно вошли";
    }

    public String join(Long roomId, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Room room = findRoomById(roomId);
        if (room.getUsers().contains(user))
            return "Вы уже являетесь участником";
        room.getUsers().add(user);
        user.getVisitedRooms().add(room);
        roomRepository.save(room);
        userRepository.save(user);
        return "Вы успешно вошли";
    }

    public String inviteUser(Long userId, Long roomId, CustomUserDetails userDetails) {
        User invitedUser = userService.findById(userId);
        Room room = findRoomById(roomId);
        checkRoomCreator(room, userDetails);
        if (!room.getUsers().contains(userService.findByUsername(userDetails.getUsername())))
            throw new RoomAccessException();
        if (room.getUsers().contains(invitedUser) || room.getCreator().equals(invitedUser))
            return "Пользователь уже является участником";

        room.getUsers().add(invitedUser);
        invitedUser.getVisitedRooms().add(room);
        roomRepository.save(room);
        userRepository.save(invitedUser);
        return "Вы добавили пользователя в комнату";
    }

    public String leave(Long id, UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Room room = findRoomById(id);

        if (room.getUsers().contains(user)) {
            extractUser(room, user);
            return "Вы успешно вышли из комнаты (как участник)";
        }
//         else if (room.getCreator().equals(user)) {
//            room.setCreator(null);
//            user.getCreatedRooms().remove(room);
//            roomRepository.save(room);
//            userRepository.save(user);
//            return "Вы успешно вышли из комнаты (как создатель)";
//        }
        return "Вы уже не являетесь участником";
    }

    public String kick(Long roomId, Long userId, CustomUserDetails userDetails) {
        Room room = findRoomById(roomId);
        checkRoomCreator(room, userDetails);
        User user = userService.findById(userId);
        extractUser(room, user);
        return "Пользователь исключен";
    }

    public String changeCreator(Long roomId, Long userId, CustomUserDetails userDetails) {
        Room room = findRoomById(roomId);
        checkRoomCreator(room, userDetails);
        User newCreator = userService.findById(userId);
        User oldCreator = userService.findByUsername(userDetails.getUsername());
        room.setCreator(newCreator);
        oldCreator.getCreatedRooms().remove(room);
        newCreator.getCreatedRooms().add(room);
        userRepository.save(newCreator);
        userRepository.save(oldCreator);
        roomRepository.save(room);

        return "Создатель комнаты переопределен";

    }




    public ProductInfoDto addProduct(Long roomId, ProductCreationForm productPayload, CustomUserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Room room = findRoomById(roomId);
        checkRoomCreator(room, userDetails);

        var product = productService.save(productPayload);
        product.setAdder(user);
        user.getAddedProducts().add(product);
        room.getProducts().add(product);
        productService.save(product);
        roomRepository.save(room);
        userService.save(user);
        return productService.getInfo(product);
    }

    public ProductInfoDto updateProduct(Long roomId, Long productId, CustomUserDetails userDetails, ProductUpdateForm productUpdateForm){
        Room room = findRoomById(roomId);
        checkRoomCreator(room, userDetails);
        return productService.update(productId, productUpdateForm);
    }

    public void deleteProduct(Long roomId, Long productId, CustomUserDetails userDetails) {
        Product product = productService.getById(productId);
        Room room = findRoomById(roomId);
        checkRoomCreator(room, userDetails);
        room.getProducts().remove(product);
        productService.delete(productId);

    }

    public List<ProductInfoDto> allProducts(Long roomId, CustomUserDetails userDetails){
        Room room = findRoomById(roomId);
        checkRoomCreator(room, userDetails);
        return productService.getAll(room.getProducts());
    }








    public ResponseEntity<?> getAllUsers(Long roomId) {
        Room room = findRoomById(roomId);
        List<UserInfoDto> users = room.getUsers().stream().map(userService::getInfo).toList();
        return ResponseEntity.ok(users);
    }
    public ResponseEntity<?> getAllProducts(Long roomId) {
        Room room = findRoomById(roomId);
        List<ProductInfoDto> productsDto = room.getProducts().stream().map(productService::getInfo).toList();
        return ResponseEntity.ok(productsDto);
    }
    public ResponseEntity<?> getTotalPrice(Long roomId) {
        return ResponseEntity.ok(getAllProductsCost(roomId));
    }
    public ResponseEntity<?> getCreator(Long roomId) {
        return ResponseEntity.ok(userService.getInfo(findRoomById(roomId).getCreator()));
    }
    public ResponseEntity<?> getName(Long roomId) {
        return ResponseEntity.ok(findRoomById(roomId).getName());
    }
    public ResponseEntity<?> getDescription(Long roomId) {
        return ResponseEntity.ok(findRoomById(roomId).getDescription());
    }
    public ResponseEntity<?> getCreatedAt(Long roomId) {
        return ResponseEntity.ok(findRoomById(roomId).getCreatedAt());
    }
    public ResponseEntity<?> getCategory(Long roomId) {
        return ResponseEntity.ok(findRoomById(roomId).getRoomCategory());
    }









    private void checkRoomCreator(Room room, UserDetails userDetails) throws RoomAccessException {
        User user = userService.findByUsername(userDetails.getUsername());

        boolean isAdmin = !userDetails.getAuthorities().stream()
                .filter(r -> r.getAuthority().equals("ROLE_ADMIN")).toList().isEmpty();

        if (!isAdmin && !room.getUsers().contains(user))
            throw new RoomAccessException();
    }

    private RoomInfoDto info(Room room) {
        List<UserInfoDto> users = room.getUsers().stream().map(userService::getInfo).toList();
        List<ProductInfoDto> products = room.getProducts().stream().map(productService::getInfo).toList();
        return new RoomInfoDto(room.getId(), room.getName(), userService.getInfo(room.getCreator()), room.getInvitationLink(), room.getDescription(),
                room.getCreatedAt(), room.getRoomCategory(), users, products);
    }

    private void extractUser(Room room, User user) {
        room.getUsers().remove(user);
        user.getVisitedRooms().remove(room);
        roomRepository.save(room);
        userRepository.save(user);
    }

    private Double getAllProductsCost(Long roomId) {
        Room room = findRoomById(roomId);
        var sum = 0d;
        for (var product : room.getProducts())
            sum += product.getPrice();
        return sum;
    }
}
