package kafpinpin120.publishingHouse.services;

import kafpinpin120.publishingHouse.dtos.BookingAcceptDTO;
import kafpinpin120.publishingHouse.dtos.BookingSimpleSendDTO;
import kafpinpin120.publishingHouse.dtos.ProductWithEditionDTO;
import kafpinpin120.publishingHouse.models.Booking;
import kafpinpin120.publishingHouse.models.BookingProduct;
import kafpinpin120.publishingHouse.models.Product;
import kafpinpin120.publishingHouse.repositories.BookingRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final ProductService productService;

    private final BookingProductService bookingProductService;

    public BookingService(BookingRepository bookingRepository, @Lazy ProductService productService, BookingProductService bookingProductService) {
        this.bookingRepository = bookingRepository;
        this.productService = productService;
        this.bookingProductService = bookingProductService;
    }

//    private List<BookingSimpleSendDTO> getBookingSendDTOS(List<Booking> bookings){
//
//        List<BookingSimpleSendDTO> bookingSimpleSendDTOS = new ArrayList<>();
//        for(Booking booking: bookings){
//            bookingSimpleSendDTOS.add(getBookingSimpleSendDTO(booking));
//        }
//
//        return bookingSimpleSendDTOS;
//    }

    public BookingSimpleSendDTO getBookingSimpleSendDTO(Booking booking){
        return new BookingSimpleSendDTO(booking.getId(),booking.getCost());
    }

    public void add(BookingAcceptDTO bookingAcceptDTO) {

        try {
            Booking booking = new Booking();
            booking.setStatus(bookingAcceptDTO.getStatus());
            booking.setStartExecution(bookingAcceptDTO.getStartExecution());
            booking.setCost(bookingAcceptDTO.getCost());

            booking.setProducts(new ArrayList<>());
            for (ProductWithEditionDTO productWithEditionDTO : bookingAcceptDTO.getProductsWithMargin()) {
                Optional<Product> product = productService.findById(productWithEditionDTO.getId());
                product.ifPresent(value -> booking.getProducts().add(new BookingProduct(productWithEditionDTO.getEdition(), booking, value)));
            }

            bookingRepository.save(booking);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public List<BookingSimpleSendDTO> get(Integer page, Long bookingId, Long userId, String status) {
        List<BookingSimpleSendDTO> bookingSimpleSendDTOS = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();

        if(bookingId != null){
            Optional<Booking> booking = bookingRepository.findById(bookingId);
            booking.ifPresent(value->bookingSimpleSendDTOS.add(getBookingSimpleSendDTO(value)));
        }else {
            if (userId == null) {
                Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Order.desc("id")));
                if(status != null) {
                    bookings = bookingRepository.findByStatusContainsIgnoreCase(pageable, status).getContent();
                } else{
                    try {
                        bookings = bookingRepository.findAll(pageable).getContent();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } else {
                List<BookingProduct> bookingProducts;
                if(status != null){
                    bookingProducts = bookingProductService.getUserBookings(page, userId, status);
                }else{
                    bookingProducts = bookingProductService.getUserBookings(page,userId);
                }

                for (BookingProduct bookingProduct : bookingProducts) {
                    bookings.add(bookingProduct.getBooking());
                }
            }

            for (Booking booking : bookings) {
                bookingSimpleSendDTOS.add(getBookingSimpleSendDTO(booking));
            }
        }

        return bookingSimpleSendDTOS;
    }
}
