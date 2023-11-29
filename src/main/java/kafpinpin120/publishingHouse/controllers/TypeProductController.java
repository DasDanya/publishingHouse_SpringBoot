package kafpinpin120.publishingHouse.controllers;

import jakarta.validation.Valid;
import kafpinpin120.publishingHouse.models.TypeProduct;
import kafpinpin120.publishingHouse.services.TypeProductService;
import kafpinpin120.publishingHouse.services.ValidateInputService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/typeProducts")
public class TypeProductController {

    private final TypeProductService typeProductService;
    private final ValidateInputService validateInputService;

    public TypeProductController(TypeProductService typeProductService, ValidateInputService validateInputService) {
        this.typeProductService = typeProductService;
        this.validateInputService = validateInputService;
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch() {
        return new ResponseEntity<>("Некорректный параметр в запросе", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<?> getByNumberPage(@RequestParam(name = "page") int page){

        List<TypeProduct> typeProducts;
        try{
            typeProducts = typeProductService.findByPage(page);
        } catch (Exception e){
            return new ResponseEntity<>("Ошибка получения списка типов продукции", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(typeProducts, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid TypeProduct typeProduct, BindingResult bindingResult){
        try {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
            }

            typeProductService.save(typeProduct);

        } catch (Exception e){
            if(e instanceof DataIntegrityViolationException){
                return new ResponseEntity<>("В базе данных уже существует тип продукции с наименованием " + typeProduct.getType(), HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>("Ошибка добавления типа продукции", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(typeProduct, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid TypeProduct typeProduct, BindingResult bindingResult, @PathVariable("id") long id){
        try{
            Optional<TypeProduct> typeProductInDb = typeProductService.findById(id);
            if(typeProductInDb.isEmpty()){
                return new ResponseEntity<>("Тип продукции не найден", HttpStatus.BAD_REQUEST);
            }else{

                if(typeProductInDb.get().getId() != typeProduct.getId()){
                    return new ResponseEntity<>("Параметр id не совпадает с id типа продукции", HttpStatus.BAD_REQUEST);
                }

                if (bindingResult.hasErrors()) {
                    return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
                }

                typeProductService.save(typeProduct);
            }
        }catch (Exception e){
            if(e instanceof DataIntegrityViolationException){
                return new ResponseEntity<>("В базе данных уже существует тип продукции с наименованием " + typeProduct.getType(), HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>("Ошибка изменения данных о типе продукции", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(typeProduct, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id){
        try {
            Optional<TypeProduct> typeProductInDb = typeProductService.findById(id);
            if (typeProductInDb.isEmpty()) {
                return new ResponseEntity<>("Тип продукции не найден", HttpStatus.BAD_REQUEST);
            } else {
                if(!typeProductInDb.get().getProducts().isEmpty()){
                    return new ResponseEntity<>("Невозможно удалить тип продукции, так как он указан в продукции", HttpStatus.CONFLICT);
                }

                typeProductService.delete(id);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>("Ошибка удаления типа продукции", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Тип продукции успешно удалён!", HttpStatus.OK);
    }

}