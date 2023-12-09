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

    private final int countItemsInPage = 7;

    public TypeProductService(TypeProductRepository typeProductRepository) {
        this.typeProductRepository = typeProductRepository;
    }

    public List<TypeProduct> findAll(){
        return (List<TypeProduct>) typeProductRepository.findAll(Sort.by("type"));
    }

    public List<TypeProduct> findByPage(int page){
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("type"));

        return typeProductRepository.findAll(pageable).getContent();
    }

    public List<TypeProduct> findByPage(int page, String type){
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("type"));

        return typeProductRepository.findByTypeContainsIgnoreCase(pageable,type).getContent();
    }

    public List<TypeProduct> findByType(String type) {
        return typeProductRepository.findByTypeContainsIgnoreCase(type);
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

