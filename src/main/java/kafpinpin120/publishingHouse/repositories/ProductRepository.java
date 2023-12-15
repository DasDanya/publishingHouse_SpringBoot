package kafpinpin120.publishingHouse.repositories;


import kafpinpin120.publishingHouse.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    Page<Product> findByNameContainsIgnoreCase(Pageable pageable, String name);

    Page<Product> findByNameContainsIgnoreCaseAndUserId(Pageable pageable, String name, int userId);

    Page<Product> findByUserId(Pageable pageable, int userId);

    Optional<Product> findById(long id);

    List<Product> findByNameContainsIgnoreCase(String name);

    void save(Product product);

    void deleteById(long id);


}
