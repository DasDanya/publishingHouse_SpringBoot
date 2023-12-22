package kafpinpin120.publishingHouse.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kafpinpin120.publishingHouse.dtos.BookingSimpleSendDTO;
import kafpinpin120.publishingHouse.dtos.ProductSimpleSendDTO;
import kafpinpin120.publishingHouse.dtos.UserSendDTO;
import kafpinpin120.publishingHouse.models.BookingProduct;
import kafpinpin120.publishingHouse.models.User;
import kafpinpin120.publishingHouse.models.UserRole;
import kafpinpin120.publishingHouse.repositories.UserRepository;
import kafpinpin120.publishingHouse.security.details.UserDetailsImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ProductService productService;

    private final BookingProductService bookingProductService;

    private final BookingService bookingService;


    public UserService(UserRepository userRepository, @Lazy ProductService productService, BookingProductService bookingProductService, @Lazy BookingService bookingService) {
        this.userRepository = userRepository;
        this.productService = productService;
        this.bookingProductService = bookingProductService;
        this.bookingService = bookingService;
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

    public List<User> get(int page, String role, String name) {
        List<User> users;

        UserRole userRole = UserRole.valueOf(role);
        int countItemsInPage = 7;
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("name"));

        if(name == null){
            users = userRepository.findByRole(pageable, userRole).getContent();
        }else{
            users = userRepository.findByRoleAndNameContainsIgnoreCase(pageable, userRole, name).getContent();
        }

        return users;
    }

    public UserSendDTO getSendDTO(long id) throws IOException {
        Optional<User> userInDb = userRepository.findById(id);
        if(userInDb.isEmpty()){
            throw new EntityNotFoundException("Пользователь не найден");
        }else{
            User user = userInDb.get();

            if(user.getRole() == UserRole.CUSTOMER) {
                List<ProductSimpleSendDTO> productSimpleSendDTOS = productService.getListSimpleSendDTOSForUser(user.getProducts());
                List<BookingProduct> bookingProducts = bookingProductService.getUserBookingsProducts(user.getId());

                List<BookingSimpleSendDTO> bookingSimpleSendDTOS = new ArrayList<>();
                for (BookingProduct bookingProduct : bookingProducts) {
                    bookingSimpleSendDTOS.add(bookingService.getBookingSimpleSendDTO(bookingProduct.getBooking()));
                }

                return new UserSendDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), productSimpleSendDTOS, bookingSimpleSendDTOS);
            } else{
                return new UserSendDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), null, null);
            }
        }
    }
}
