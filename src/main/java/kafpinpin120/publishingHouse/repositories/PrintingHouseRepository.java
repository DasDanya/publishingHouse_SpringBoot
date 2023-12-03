package kafpinpin120.publishingHouse.repositories;

import kafpinpin120.publishingHouse.models.PrintingHouse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Repository
public interface PrintingHouseRepository extends PagingAndSortingRepository<PrintingHouse, Long> {

    Optional<PrintingHouse> findById(long id);
    Page<PrintingHouse> findByNameContainsIgnoreCase(Pageable pageable, String name);
    void save(PrintingHouse printingHouse);

    void deleteById(long id);
}
