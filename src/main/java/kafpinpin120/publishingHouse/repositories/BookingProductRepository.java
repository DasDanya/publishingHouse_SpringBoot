package kafpinpin120.publishingHouse.repositories;


import kafpinpin120.publishingHouse.models.BookingProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingProductRepository extends PagingAndSortingRepository<BookingProduct, Long> {

    Page<BookingProduct> findByProductUserIdAndBookingStatusContainsIgnoreCase(Pageable pageable, long userId, String status);

    Page<BookingProduct> findByProductUserId(Pageable pageable, long userId);

}
