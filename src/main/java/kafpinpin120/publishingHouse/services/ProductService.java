package kafpinpin120.publishingHouse.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kafpinpin120.publishingHouse.dtos.*;
import kafpinpin120.publishingHouse.exceptions.FileIsNotImageException;
import kafpinpin120.publishingHouse.models.*;
import kafpinpin120.publishingHouse.repositories.PhotoProductRepository;
import kafpinpin120.publishingHouse.repositories.ProductMaterialRepository;
import kafpinpin120.publishingHouse.repositories.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMaterialRepository productMaterialRepository;

    private final PhotoProductRepository photoProductRepository;

    private final FilesService filesService;

    private final UserService userService;

    private final BookingService bookingService;

    private final int countItemsInPage = 7;

    public ProductService(ProductRepository productRepository, ProductMaterialRepository productMaterialRepository, PhotoProductRepository photoProductRepository, FilesService filesService, UserService userService, BookingService bookingService) {
        this.productRepository = productRepository;
        this.productMaterialRepository = productMaterialRepository;
        this.photoProductRepository = photoProductRepository;
        this.filesService = filesService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    public List<ProductSimpleSendDTO> getListSimpleSendDTOSForUser(List<Product> products) throws IOException {
        List<ProductSimpleSendDTO> productSimpleSendDTOS = new ArrayList<>();

        for(Product product: products){
            ProductSimpleSendDTO productSimpleSendDTO = new ProductSimpleSendDTO(product.getId(), product.getName(), product.getUser().getName(), 0, product.getCost(), filesService.getFile(product.getPhotos().get(0).getPath()));
            productSimpleSendDTOS.add(productSimpleSendDTO);
        }

        return productSimpleSendDTOS.stream()
                .sorted(Comparator.comparing(ProductSimpleSendDTO::getName))
                .collect(Collectors.toList());
    }

    public List<ProductSimpleSendDTO> getListSimpleSendDTOS(List<BookingProduct> bookingProducts) throws IOException {
        List<ProductSimpleSendDTO> productSendDTOS = new ArrayList<>();

        for(BookingProduct bookingProduct: bookingProducts){
            Product product = bookingProduct.getProduct();

            ProductSimpleSendDTO productSimpleSendDTO = new ProductSimpleSendDTO(product.getId(),product.getName(),product.getUser().getName(), bookingProduct.getEdition(),product.getCost(), filesService.getFile(product.getPhotos().get(0).getPath()));
            productSendDTOS.add(productSimpleSendDTO);
        }

        return productSendDTOS.stream()
                .sorted(Comparator.comparing(ProductSimpleSendDTO::getName))
                .collect(Collectors.toList());
    }

    private List<ProductSendDTO> getListSendDTOS(List<Product> products) throws IOException {
        List<ProductSendDTO> productSendDTOS = new ArrayList<>();

        for(Product product: products){
            productSendDTOS.add(getSendDTO(product,true));
        }

        return productSendDTOS;
    }

    private ProductSendDTO getSendDTO(Product product, boolean shortVersion) throws IOException {

        List<byte[]> productPhotos = new ArrayList<>();
        for(PhotoProduct photoProduct: product.getPhotos()){
            productPhotos.add(filesService.getFile(photoProduct.getPath()));
        }

        if(shortVersion){
            return new ProductSendDTO(product.getId(), product.getName(), product.getUser().getName(), product.getUser().getEmail(), product.getCost(), product.getTypeProduct(), null, null, productPhotos);
        }



        List<ProductMaterialDTO> productMaterialDTOS = new ArrayList<>();
        for(ProductMaterial productMaterial: product.getMaterialsWithCount()){
            productMaterialDTOS.add(new ProductMaterialDTO(productMaterial.getMaterial(), productMaterial.getCountMaterials()));
        }

        List<CountProductsInBookingDTO> countProductsInBookingDTOS = new ArrayList<>();
        for(BookingProduct bookingProduct: product.getBookings()){
            CountProductsInBookingDTO countProductsInBookingDTO = new CountProductsInBookingDTO(bookingService.getBookingSimpleSendDTO(bookingProduct.getBooking()), bookingProduct.getEdition());
            countProductsInBookingDTOS.add(countProductsInBookingDTO);
        }

        countProductsInBookingDTOS = countProductsInBookingDTOS.stream()
                .sorted(Comparator.comparing((CountProductsInBookingDTO dto) -> dto.getBooking().getId()).reversed())
                .collect(Collectors.toList());


        return new ProductSendDTO(product.getId(), product.getName(), product.getUser().getName(), product.getUser().getEmail(), product.getCost(), product.getTypeProduct(), productMaterialDTOS, countProductsInBookingDTOS, productPhotos);
    }


    public List<ProductSendDTO> findAll() throws IOException {
        List<Product> products = (List<Product>) productRepository.findAll(Sort.by("name"));

        return getListSendDTOS(products);
    }


    public List<ProductSendDTO> findByUserId(long userId) throws IOException {
        List<Product> products = productRepository.findByUserIdOrderByNameAsc(userId);
        return getListSendDTOS(products);
    }

    public List<ProductSendDTO> findByName(String name) throws IOException {
        List<Product> products = productRepository.findByNameContainsIgnoreCase(name);

        return getListSendDTOS(products);
    }

    public List<ProductSendDTO> findByPage(int page) throws IOException {
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("name"));
        List<Product> products = productRepository.findAll(pageable).getContent();

        return getListSendDTOS(products);
    }

    public List<ProductSendDTO> findByPage(int page, String name) throws IOException {
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("name"));
        List<Product> products = productRepository.findByNameContainsIgnoreCase(pageable, name).getContent();

        return getListSendDTOS(products);
    }

    public List<ProductSendDTO> findByPage(int page, long userId) throws IOException {
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("name"));
        List<Product> products = productRepository.findByUserId(pageable, userId).getContent();

        return getListSendDTOS(products);
    }

    public List<ProductSendDTO> findByPage(int page, long userId, String name) throws IOException {
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("name"));
        List<Product> products = productRepository.findByNameContainsIgnoreCaseAndUserId(pageable, name,userId).getContent();

        return getListSendDTOS(products);
    }

    public ProductSendDTO findByIdSendDTO(long id) throws IOException {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new EntityNotFoundException("Продукция не найдена");
        }else{
            return getSendDTO(product.get(), false);
        }
    }

    public Optional<Product> findById(long id){
        return productRepository.findById(id);
    }



    public void add(ProductAcceptDTO productAcceptDTO, List<MultipartFile> photos) throws IOException {
        Product product = new Product();
        product.setId(productAcceptDTO.getId());
        product.setName(productAcceptDTO.getName());
        product.setCost(productAcceptDTO.getCost());
        product.setTypeProduct(productAcceptDTO.getTypeProduct());

        //Optional<User> user = userService.findById(productAcceptDTO.getUserId());
        //user.ifPresent(product::setUser);
        setUser(product, productAcceptDTO.getUserId());
        setMaterialsWithCount(product, productAcceptDTO.getProductMaterialDTOS());
        setPhotos(product, photos);

//        product.setMaterialsWithCount(new ArrayList<>());
//        for(ProductMaterialDTO productMaterialDTO: productAcceptDTO.getProductMaterialDTOS()){
//            product.getMaterialsWithCount().add(new ProductMaterial(productMaterialDTO.getCountMaterials(), product, productMaterialDTO.getMaterial()));
//        }

       // try {
            productRepository.save(product);
        //} catch (Exception e){
            //e.printStackTrace();
        //}
    }

    private void setUser(Product product, long userId){
        Optional<User> user = userService.findById(userId);
        user.ifPresent(product::setUser);
    }

    private void setMaterialsWithCount(Product product, List<ProductMaterialDTO> productMaterialDTOS){
        product.setMaterialsWithCount(new ArrayList<>());
        for(ProductMaterialDTO productMaterialDTO: productMaterialDTOS){
            product.getMaterialsWithCount().add(new ProductMaterial(productMaterialDTO.getCountMaterials(), product, productMaterialDTO.getMaterial()));
        }
    }

    private void setPhotos(Product product, List<MultipartFile> photos) throws IOException {
        Random random = new Random();
        product.setPhotos(new ArrayList<>());
        for(MultipartFile photo: photos){
            if(!filesService.isImage(photo)){
                throw new FileIsNotImageException("Файл не является изображением");
            }else{
                String photoNewName = product.getName() + random.nextInt(10000000) + photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf('.'));
                String destinationPath = "src/main/resources/static/images/products/" + photoNewName;
                filesService.saveImage(photo, destinationPath);

                PhotoProduct photoProduct = new PhotoProduct(destinationPath, product);
                product.getPhotos().add(photoProduct);
            }
        }
    }



    @Transactional
    public void update(Product product,ProductAcceptDTO productAcceptDTO, List<MultipartFile> photos) throws IOException {
        product.setName(productAcceptDTO.getName());
        product.setCost(productAcceptDTO.getCost());
        product.setTypeProduct(productAcceptDTO.getTypeProduct());

        try {

        for(PhotoProduct photoProduct: product.getPhotos()){
            filesService.deleteFile(photoProduct.getPath());
        }

        product.getPhotos().clear();
        product.getBookings().clear();
        productMaterialRepository.deleteByProductId(product.getId());
        photoProductRepository.deleteByProductId(product.getId());

        setUser(product, productAcceptDTO.getUserId());
        setMaterialsWithCount(product, productAcceptDTO.getProductMaterialDTOS());
        setPhotos(product, photos);


        productRepository.save(product);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void delete(Product product) throws IOException {
        List<String> pathsToFiles = new ArrayList<>();
        for(PhotoProduct photoProduct: product.getPhotos()){
            pathsToFiles.add(photoProduct.getPath());
        }

        productRepository.deleteById(product.getId());

        for(String path: pathsToFiles){
            filesService.deleteFile(path);
        }
    }
}
