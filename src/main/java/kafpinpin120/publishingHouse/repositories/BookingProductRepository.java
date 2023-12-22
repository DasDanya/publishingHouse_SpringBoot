package kafpinpin120.publishingHouse.repositories;


import kafpinpin120.publishingHouse.models.BookingProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingProductRepository extends PagingAndSortingRepository<BookingProduct, Long> {

    //Page<BookingProduct> findByProductUserIdAndBookingStatusContainsIgnoreCase(Pageable pageable, long userId, String status);

    //Page<BookingProduct> findByProductUserId(Pageable pageable, long userId);

    //List<BookingProduct> findByBookingId(long bookingId);

    List<BookingProduct> findByProductUserId(long userId);
    List<BookingProduct> findByProductUserIdAndBookingStatusContainsIgnoreCase(long userId, String status);

    List<BookingProduct> findByProductUserIdAndBookingStatusContainsIgnoreCaseAndBookingEndExecutionBetween(long userId, String status, LocalDate startDate, LocalDate endDate);
    void deleteByBookingId(long bookingId);
}
