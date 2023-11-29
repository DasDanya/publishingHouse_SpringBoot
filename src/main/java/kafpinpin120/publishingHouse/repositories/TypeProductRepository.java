package kafpinpin120.publishingHouse.repositories;

import kafpinpin120.publishingHouse.models.TypeProduct;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.lang.reflect.Type;
import java.util.Optional;

public interface TypeProductRepository extends PagingAndSortingRepository<TypeProduct, Long> {

    void save(TypeProduct typeProduct);
    Optional<TypeProduct> findById(long id);

    void deleteById(long id);
}
