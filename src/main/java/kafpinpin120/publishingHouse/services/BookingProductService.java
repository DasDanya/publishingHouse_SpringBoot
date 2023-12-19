package kafpinpin120.publishingHouse.services;


import kafpinpin120.publishingHouse.models.BookingProduct;
import kafpinpin120.publishingHouse.repositories.BookingProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


@Service
public class BookingProductService {

    private final BookingProductRepository bookingProductRepository;
    private final int countItemsInPage = 7;

    public BookingProductService(BookingProductRepository bookingProductRepository) {
        this.bookingProductRepository = bookingProductRepository;
    }

    public List<BookingProduct> getUserBookings(int page, long userId){
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by(Sort.Order.desc("id")));
        List<BookingProduct> bookingProducts = bookingProductRepository.findByProductUserId(pageable,userId).getContent();

        return bookingProducts.stream()
                .filter(distinctByKey(bp -> bp.getBooking().getId()))
                .toList();
    }

    public List<BookingProduct> getUserBookings(int page, long userId, String status){
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by(Sort.Order.desc("id")));
        List<BookingProduct> bookingProducts = bookingProductRepository.findByProductUserIdAndBookingStatusContainsIgnoreCase(pageable,userId,status).getContent();

        return bookingProducts.stream()
                .filter(distinctByKey(bp -> bp.getBooking().getId()))
                .toList();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
