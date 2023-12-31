package kafpinpin120.publishingHouse.repositories;

import kafpinpin120.publishingHouse.models.Booking;
import kafpinpin120.publishingHouse.models.StatusOfBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends PagingAndSortingRepository<Booking, Long> {
    void save(Booking booking);

    Optional<Booking> findById(long id);

    Page<Booking> findByStatusContainsIgnoreCase(Pageable pageable, String status);

    List<Booking> findByStatusContainsIgnoreCaseAndEndExecutionBetween(String status, LocalDate startDate, LocalDate endDate);

    void delete(Booking booking);
}
