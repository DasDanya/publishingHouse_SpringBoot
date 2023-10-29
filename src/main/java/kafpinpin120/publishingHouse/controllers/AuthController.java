package kafpinpin120.publishingHouse.controllers;

import jakarta.validation.Valid;
import kafpinpin120.publishingHouse.models.User;
import kafpinpin120.publishingHouse.security.jwt.JwtUtils;
import kafpinpin120.publishingHouse.services.UserService;
import kafpinpin120.publishingHouse.services.ValidateInputService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

//@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final ValidateInputService validateInputService;

    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtUtils jwtUtils, UserService userService, ValidateInputService validateInputService) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.validateInputService = validateInputService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody @Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validateInputService.getErrors(bindingResult));
        }

        if(userService.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest().body("Электронная почта уже используется!");
        }

        if(userService.existsByPhone(user.getPhone())){
            return ResponseEntity.badRequest().body("Номер телефона уже используется!");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userService.add(user);

        return ResponseEntity.ok("Вы успешно зарегистрировались!");
    }


}
