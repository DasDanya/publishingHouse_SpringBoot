package kafpinpin120.publishingHouse.repositories;

import kafpinpin120.publishingHouse.models.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends PagingAndSortingRepository<Material,Long> {

    Page<Material> findByTypeContainsIgnoreCase(Pageable pageable, String type);

    Optional<Material> findById(long id);

    void save(Material material);

    void deleteById(long id);
}
