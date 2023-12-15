package kafpinpin120.publishingHouse.repositories;

import kafpinpin120.publishingHouse.models.ProductMaterial;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMaterialRepository extends CrudRepository<ProductMaterial, Long> {

    void deleteByProductId(long productId);
}
