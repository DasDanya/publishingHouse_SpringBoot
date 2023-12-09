package kafpinpin120.publishingHouse.services;

import kafpinpin120.publishingHouse.models.Material;
import kafpinpin120.publishingHouse.repositories.MaterialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final int countItemsInPage = 7;
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public List<Material> findByPage(int page){
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("type"));

        return materialRepository.findAll(pageable).getContent();
    }

    public List<Material> findByPage(int page, String type){
        Pageable pageable = PageRequest.of(page,countItemsInPage, Sort.by("type"));

        return materialRepository.findByTypeContainsIgnoreCase(pageable,type).getContent();
    }


    public void save(Material material) {
        materialRepository.save(material);
    }

    public Optional<Material> findById(long id) {
        return materialRepository.findById(id);
    }

    public void delete(long id) {
        materialRepository.deleteById(id);
    }
}
