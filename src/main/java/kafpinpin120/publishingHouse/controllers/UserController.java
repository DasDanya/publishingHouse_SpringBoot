package kafpinpin120.publishingHouse.controllers;

import jakarta.persistence.EntityNotFoundException;
import kafpinpin120.publishingHouse.dtos.UserSendDTO;
import kafpinpin120.publishingHouse.models.User;
import kafpinpin120.publishingHouse.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "page") int page, @RequestParam(name = "role") String role, @RequestParam(name = "name",required = false) String name){
        List<User> users;
        try{
            users = userService.get(page, role, name);
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка получения списка пользователей", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable long id){
        UserSendDTO userSendDTO;
        try{
            userSendDTO = userService.getSendDTO(id);
        }catch (Exception e){
            if(e instanceof EntityNotFoundException){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Ошибка получения пользователя с id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(userSendDTO, HttpStatus.OK);
    }
}
