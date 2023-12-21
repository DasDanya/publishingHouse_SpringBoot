package kafpinpin120.publishingHouse.controllers;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import kafpinpin120.publishingHouse.dtos.ProductAcceptDTO;
import kafpinpin120.publishingHouse.dtos.ProductSendDTO;
import kafpinpin120.publishingHouse.exceptions.FileIsNotImageException;
import kafpinpin120.publishingHouse.models.Product;
import kafpinpin120.publishingHouse.services.FilesService;
import kafpinpin120.publishingHouse.services.ProductService;
import kafpinpin120.publishingHouse.services.ValidateInputService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ValidateInputService validateInputService;
    private final FilesService filesService;

    public ProductController(ProductService productService, ValidateInputService validateInputService, FilesService filesService) {
        this.productService = productService;
        this.validateInputService = validateInputService;
        this.filesService = filesService;
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "userId", required = false) Long userId, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "name", required = false) String name){

        List<ProductSendDTO> productSendDTOS;
        try{
            if(userId == null) {
                if (page == null) {
                    if (name == null) {
                        productSendDTOS = productService.findAll();
                    } else {
                        productSendDTOS = productService.findByName(name);
                    }
                } else {
                    if (name == null) {
                        productSendDTOS = productService.findByPage(page);
                    } else {
                        productSendDTOS = productService.findByPage(page, name);
                    }
                }
            }else{
                if(page != null) {
                    if (name == null) {
                        productSendDTOS = productService.findByPage(page, userId);
                    } else {
                        productSendDTOS = productService.findByPage(page, userId, name);
                    }
                }else{
                    productSendDTOS = productService.findByUserId(userId);
                }
            }
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка получения списка продукций", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(productSendDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id){
        ProductSendDTO productAcceptDTO;
        try{
            productAcceptDTO = productService.findByIdSendDTO(id);
        }catch (Exception e){
            if(e instanceof EntityNotFoundException){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Ошибка получения продукции с id = '"+id+"'", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(productAcceptDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestPart @Valid ProductAcceptDTO product, BindingResult bindingResult, @RequestPart List<MultipartFile> photos){
        try{
            if(bindingResult.hasErrors()){
                return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
            }

            productService.add(product, photos);
        }catch (Exception e){
            if(e instanceof FileIsNotImageException){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("Ошибка добавления продукции", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id){
        try{
            Optional<Product> productInDb = productService.findById(id);
            if(productInDb.isEmpty()){
                return new ResponseEntity<>("Продукция не найдена", HttpStatus.BAD_REQUEST);
            }else{
                Product product = productInDb.get();
                if(!product.getBookings().isEmpty()){
                    return new ResponseEntity<>("Невозможно удалить продукцию, так как она указана в заказе", HttpStatus.CONFLICT);
                }

                productService.delete(product);
            }
        } catch (Exception e){
            return new ResponseEntity<>("Ошибка удаления продукции", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Продукция успешно удалена!", HttpStatus.NO_CONTENT);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@RequestPart @Valid ProductAcceptDTO product, BindingResult bindingResult, @RequestPart List<MultipartFile> photos, @PathVariable("id") long id){
        try{
            Optional<Product> productInDb = productService.findById(id);
            if(productInDb.isEmpty()){
                return new ResponseEntity<>("Продукция не найдена", HttpStatus.BAD_REQUEST);
            }else{
                Product existProduct = productInDb.get();
                if(existProduct.getId() != product.getId()){
                    return new ResponseEntity<>("Параметр id не совпадает с id продукции", HttpStatus.BAD_REQUEST);
                }

                if(!existProduct.getBookings().isEmpty()){
                    return new ResponseEntity<>("Невозможно изменить данные о продукции, так как она указана в заказе", HttpStatus.CONFLICT);
                }

                if(bindingResult.hasErrors()){
                    return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
                }

                productService.update(existProduct, product, photos);

            }
        }catch (Exception e){
            if(e instanceof FileIsNotImageException){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("Ошибка изменения данных о продукции", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Данные о продукции успешно изменены", HttpStatus.OK);
    }
}
