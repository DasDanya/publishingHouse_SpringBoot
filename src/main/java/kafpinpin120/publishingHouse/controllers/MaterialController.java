package kafpinpin120.publishingHouse.controllers;

import jakarta.validation.Valid;
import kafpinpin120.publishingHouse.models.Material;
import kafpinpin120.publishingHouse.services.MaterialService;
import kafpinpin120.publishingHouse.services.ValidateInputService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    private final ValidateInputService validateInputService;

    public MaterialController(MaterialService materialService, ValidateInputService validateInputService) {
        this.materialService = materialService;
        this.validateInputService = validateInputService;
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "page",required = false) Integer page, @RequestParam(name = "type",required = false) String type){
        List<Material> materials;

        try{
            if(page != null) {
                if (type == null) {
                    materials = materialService.findByPage(page);
                } else {
                    materials = materialService.findByPage(page, type);
                }
            }else{
                materials = materialService.findAll();
            }
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка получения списка материалов",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id){
        Optional<Material> material;
        try{
            material = materialService.findById(id);
            if(material.isEmpty()){
                return new ResponseEntity<>("Материал не найден", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка получения материала", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(material.get(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Material material, BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
            }

            materialService.save(material);

        }catch (Exception e){
            if(e instanceof DataIntegrityViolationException){
                return new ResponseEntity<>("В базе данных есть материал с введенными типом, цветом и размером", HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>("Ошибка добавления материала", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(material, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id){
        try{
            Optional<Material> materialInDb = materialService.findById(id);
            if(materialInDb.isEmpty()){
                return new ResponseEntity<>("Материал не найден", HttpStatus.BAD_REQUEST);
            }else{
                if(!materialInDb.get().getProductMaterials().isEmpty()){
                    return new ResponseEntity<>("Невозможно удалить материал, так как он используется в продукции", HttpStatus.CONFLICT);
                }

                materialService.delete(id);
            }

        }catch (Exception e){
            return new ResponseEntity<>("Ошибка удаления материала", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Материал успешно удалён!", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid Material material, BindingResult bindingResult, @PathVariable("id") long id){
        try{
            Optional<Material> materialInDb = materialService.findById(id);
            if(materialInDb.isEmpty()){
                return new ResponseEntity<>("Материал не найден", HttpStatus.BAD_REQUEST);
            }else{

                if(materialInDb.get().getId() != material.getId()){
                    return new ResponseEntity<>("Параметр id не совпадает с id материала", HttpStatus.BAD_REQUEST);
                }

                if(bindingResult.hasErrors()){
                    return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
                }

                materialService.save(material);
            }
        }catch (Exception e){
            if(e instanceof DataIntegrityViolationException){
                return new ResponseEntity<>("В базе данных есть материал с введенными типом, цветом и размером", HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>("Ошибка изменения данных о материале", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(material, HttpStatus.OK);
    }

}
