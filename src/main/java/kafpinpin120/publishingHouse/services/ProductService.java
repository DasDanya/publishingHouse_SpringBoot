package kafpinpin120.publishingHouse.services;

import kafpinpin120.publishingHouse.dtos.ProductDTO;
import kafpinpin120.publishingHouse.dtos.ProductMaterialDTO;
import kafpinpin120.publishingHouse.models.PhotoProduct;
import kafpinpin120.publishingHouse.models.Product;
import kafpinpin120.publishingHouse.models.ProductMaterial;
import kafpinpin120.publishingHouse.repositories.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final FilesService filesService;

    private final int countItemsInPage = 7;

    public ProductService(ProductRepository productRepository, FilesService filesService) {
        this.productRepository = productRepository;
        this.filesService = filesService;
    }

    private List<ProductDTO> getListDTOS(List<Product> products) throws IOException {
        List<ProductDTO> productDTOS = new ArrayList<>();

        for(Product product: products){

            List<byte[]> productPhotos = new ArrayList<>();
            for(PhotoProduct photoProduct: product.getPhotos()){
                productPhotos.add(filesService.getFile(photoProduct.getPath()));
            }

            List<ProductMaterialDTO> productMaterialDTOS = new ArrayList<>();
            for(ProductMaterial productMaterial: product.getMaterialsWithCount()){
                productMaterialDTOS.add(new ProductMaterialDTO(productMaterial.getMaterial().getId(), productMaterial.getCountMaterials()));
            }

            ProductDTO productDTO = new ProductDTO(product.getId(), product.getUser().getId(), product.getTypeProduct().getId(), product.getName(), product.getCost(),productMaterialDTOS, productPhotos);
            productDTOS.add(productDTO);
        }

        return productDTOS;
    }


    public List<ProductDTO> findAll() throws IOException {
        List<Product> products = (List<Product>) productRepository.findAll(Sort.by("name"));

        return getListDTOS(products);
    }

    public List<ProductDTO> findByName(String name) throws IOException {
        List<Product> products = productRepository.findByNameContainsIgnoreCase(name);

        return getListDTOS(products);
    }

    public List<ProductDTO> findByPage(int page) throws IOException {
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("name"));
        List<Product> products = productRepository.findAll(pageable).getContent();

        return getListDTOS(products);
    }

    public List<ProductDTO> findByPage(int page, String name) throws IOException {
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("name"));
        List<Product> products = productRepository.findByNameContainsIgnoreCase(pageable, name).getContent();

        return getListDTOS(products);
    }

    public List<ProductDTO> findByPage(int page, int userId) throws IOException {
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("name"));
        List<Product> products = productRepository.findByUserId(pageable, userId).getContent();

        return getListDTOS(products);
    }

    public List<ProductDTO> findByPage(int page, int userId, String name) throws IOException {
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("name"));
        List<Product> products = productRepository.findByNameContainsIgnoreCaseAndUserId(pageable, name,userId).getContent();

        return getListDTOS(products);
    }
}
