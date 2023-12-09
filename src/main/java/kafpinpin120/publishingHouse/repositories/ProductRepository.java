package kafpinpin120.publishingHouse.repositories;


import kafpinpin120.publishingHouse.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    Page<Product> findByNameContainsIgnoreCase(Pageable pageable, String name);

    Page<Product> findByNameContainsIgnoreCaseAndUserId(Pageable pageable, String name, int userId);

    Page<Product> findByUserId(Pageable pageable, int userId);

    List<Product> findByNameContainsIgnoreCase(String name);


}
