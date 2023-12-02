package kafpinpin120.publishingHouse.repositories;

import kafpinpin120.publishingHouse.models.TypeProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.Optional;

@Repository
public interface TypeProductRepository extends PagingAndSortingRepository<TypeProduct, Long> {

    Page<TypeProduct> findByTypeContainsIgnoreCase(Pageable pageable, String type);

    Optional<TypeProduct> findById(long id);

    void save(TypeProduct typeProduct);

    void deleteById(long id);
}
