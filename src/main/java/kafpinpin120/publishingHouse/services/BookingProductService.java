package kafpinpin120.publishingHouse.services;


import jakarta.transaction.Transactional;
import kafpinpin120.publishingHouse.models.Booking;
import kafpinpin120.publishingHouse.models.BookingProduct;
import kafpinpin120.publishingHouse.models.Product;
import kafpinpin120.publishingHouse.repositories.BookingProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
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

//    public List<BookingProduct> getUserBookings(int page, long userId){
//        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by(Sort.Order.desc("id")));
//        List<BookingProduct> bookingProducts = bookingProductRepository.findByProductUserId(pageable,userId).getContent();
//
//        return bookingProducts.stream()
//                .filter(distinctByKey(bp -> bp.getBooking().getId()))
//                .toList();
//    }

//    public List<BookingProduct> getUserBookings(int page, long userId, String status){
//        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by(Sort.Order.desc("id")));
//        List<BookingProduct> bookingProducts = bookingProductRepository.findByProductUserIdAndBookingStatusContainsIgnoreCase(pageable,userId,status).getContent();
//
//        return bookingProducts.stream()
//                .filter(distinctByKey(bp -> bp.getBooking().getId()))
//                .toList();
//    }

        public List<BookingProduct> getUserBookings(int page, long userId){
        List<BookingProduct> bookingProducts = bookingProductRepository.findByProductUserId(userId);

        return getPagedBookingProducts(bookingProducts, page);
    }

    public List<BookingProduct> getUserBookings(int page, long userId, String status){
        List<BookingProduct> bookingProducts = bookingProductRepository.findByProductUserIdAndBookingStatusContainsIgnoreCase(userId,status);

        return getPagedBookingProducts(bookingProducts, page);
    }

    private List<BookingProduct> getPagedBookingProducts(List<BookingProduct> bookingProducts, int page){
        List<BookingProduct> uniqueByBookingId = bookingProducts.stream()
                .filter(distinctByKey(bp -> bp.getBooking().getId()))
                .sorted((bp1, bp2) -> Long.compare(bp2.getBooking().getId(), bp1.getBooking().getId()))
                .toList();

        int startIndex = page * countItemsInPage;
        int endIndex = startIndex + countItemsInPage;

        List<BookingProduct> pagedBookingProducts = new ArrayList<>();
        for(int i = startIndex; i < endIndex; i++){
            if(uniqueByBookingId.size() - i  > 0){
                pagedBookingProducts.add(uniqueByBookingId.get(i));
            }else{
                break;
            }
        }

        return pagedBookingProducts;
    }


    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Transactional
    public void deleteByBookingId(long bookingId) {
        bookingProductRepository.deleteByBookingId(bookingId);
    }
}
