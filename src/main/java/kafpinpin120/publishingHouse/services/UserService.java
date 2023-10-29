package kafpinpin120.publishingHouse.services;

import jakarta.transaction.Transactional;
import kafpinpin120.publishingHouse.models.User;
import kafpinpin120.publishingHouse.repositories.UserRepository;
import kafpinpin120.publishingHouse.security.details.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone){
        return userRepository.existsByPhone(phone);
    }

    public void add(User user){
        userRepository.save(user);
    }

}
