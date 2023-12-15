package kafpinpin120.publishingHouse.services;

import jakarta.transaction.Transactional;
import kafpinpin120.publishingHouse.models.User;
import kafpinpin120.publishingHouse.repositories.UserRepository;
import kafpinpin120.publishingHouse.security.details.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь с электронной почтой %s не найден", email)));

        return UserDetailsImpl.build(user);
    }

    public String getErrorMessageAboutExistPersonalData(String email, String phone){
        if(userRepository.existsByEmail(email)){
            return "Электронная почта уже используется!";
        } else if(userRepository.existsByPhone(phone)){
            return "Номер телефона уже используется!";
        }else{
            return null;
        }
    }

    public void add(User user){
        userRepository.save(user);
    }

    public Optional<User> findById(long id){
        return userRepository.findById(id);
    }
}
