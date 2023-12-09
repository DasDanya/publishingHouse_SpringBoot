package kafpinpin120.publishingHouse.controllers;


import kafpinpin120.publishingHouse.dtos.ProductDTO;
import kafpinpin120.publishingHouse.services.FilesService;
import kafpinpin120.publishingHouse.services.ProductService;
import kafpinpin120.publishingHouse.services.ValidateInputService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> get(@RequestParam(name = "userId", required = false) Integer userId, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "name", required = false) String name){

        List<ProductDTO> productDTOS;
        try{
            if(userId == null) {
                if (page == null) {
                    if (name == null) {
                        productDTOS = productService.findAll();
                    } else {
                        productDTOS = productService.findByName(name);
                    }
                } else {
                    if (name == null) {
                        productDTOS = productService.findByPage(page);
                    } else {
                        productDTOS = productService.findByPage(page, name);
                    }
                }
            }else{
                if(name == null){
                    productDTOS = productService.findByPage(page, userId);
                }else{
                    productDTOS = productService.findByPage(page, userId,name);
                }
            }
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка получения списка продукций", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }
}
