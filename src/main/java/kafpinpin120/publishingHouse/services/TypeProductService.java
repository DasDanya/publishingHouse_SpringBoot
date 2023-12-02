package kafpinpin120.publishingHouse.services;

import kafpinpin120.publishingHouse.models.TypeProduct;
import kafpinpin120.publishingHouse.repositories.TypeProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;


@Service
public class TypeProductService {

    private final TypeProductRepository typeProductRepository;

    public TypeProductService(TypeProductRepository typeProductRepository) {
        this.typeProductRepository = typeProductRepository;
    }

    public List<TypeProduct> findByPage(int page){
        int size = 7;
        Pageable pageable = PageRequest.of(page, size, Sort.by("type"));

        return typeProductRepository.findAll(pageable).getContent();
    }

    public List<TypeProduct> findByPage(int page, String type){
        int size = 7;
        Pageable pageable = PageRequest.of(page, size, Sort.by("type"));

        return typeProductRepository.findByTypeContainsIgnoreCase(pageable,type).getContent();
    }

    public Optional<TypeProduct> findById(long id){
        return typeProductRepository.findById(id);
    }

    public void save(TypeProduct typeProduct){
       typeProductRepository.save(typeProduct);
    }

    public void delete(long id){
        typeProductRepository.deleteById(id);
    }
}

