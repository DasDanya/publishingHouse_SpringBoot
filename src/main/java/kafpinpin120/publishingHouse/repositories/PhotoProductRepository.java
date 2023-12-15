package kafpinpin120.publishingHouse.repositories;

import kafpinpin120.publishingHouse.models.PhotoProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoProductRepository extends CrudRepository<PhotoProduct, Long> {

    void deleteByProductId(long productId);
}
