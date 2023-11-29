package kafpinpin120.publishingHouse.controllers;

import jakarta.validation.Valid;
import kafpinpin120.publishingHouse.models.User;
import kafpinpin120.publishingHouse.payloads.requests.LoginRequest;
import kafpinpin120.publishingHouse.payloads.requests.RegisterRequest;
import kafpinpin120.publishingHouse.payloads.responses.JwtResponse;
import kafpinpin120.publishingHouse.security.details.UserDetailsImpl;
import kafpinpin120.publishingHouse.security.jwt.JwtUtils;
import kafpinpin120.publishingHouse.services.UserService;
import kafpinpin120.publishingHouse.services.ValidateInputService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult){
        try {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
            }

            String errorMessageAboutPersonalData = userService.getErrorMessageAboutExistPersonalData(registerRequest.getEmail(), registerRequest.getPhone());
            if(errorMessageAboutPersonalData != null){
                return new ResponseEntity<>(errorMessageAboutPersonalData, HttpStatus.BAD_REQUEST);
            }

            User user = new User(registerRequest.getName(), registerRequest.getPhone(), registerRequest.getEmail(), encoder.encode(registerRequest.getPassword()), registerRequest.getRole());
            userService.add(user);
        } catch (Exception e){
            return new ResponseEntity<>("Ошибка регистрации пользователя", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Вы успешно зарегистрировались!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult){
        String jwtToken;
        UserDetailsImpl userDetails;
        try {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            jwtToken = jwtUtils.generateJwtToken(authentication);
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
            //String userRole = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();

        } catch (Exception e){
            return new ResponseEntity<>("Ошибка авторизации пользователя", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new JwtResponse(jwtToken, userDetails.getUser()), HttpStatus.OK);
    }


}
