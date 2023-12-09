package kafpinpin120.publishingHouse.controllers;

import jakarta.validation.Valid;
import kafpinpin120.publishingHouse.models.PrintingHouse;
import kafpinpin120.publishingHouse.services.PrintingHouseService;
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
@RequestMapping("/api/printingHouses")
public class PrintingHouseController {

    private final PrintingHouseService printingHouseService;
    private final ValidateInputService validateInputService;

    public PrintingHouseController(PrintingHouseService printingHouseService, ValidateInputService validateInputService) {
        this.printingHouseService = printingHouseService;
        this.validateInputService = validateInputService;
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "page") int page, @RequestParam(name = "name", required = false) String name){
        List<PrintingHouse> printingHouses;

        try{
            if(name == null){
                printingHouses = printingHouseService.findByPage(page);
            }else{
                printingHouses = printingHouseService.findByPage(page, name);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка получения списка типографий", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(printingHouses, HttpStatus.OK);
    }




    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid PrintingHouse printingHouse, BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
            }

            printingHouseService.save(printingHouse);

        } catch (Exception e){
            if(e instanceof DataIntegrityViolationException){
                if(e.getMessage().contains("(phone)")){
                    return new ResponseEntity<>("Номер телефона уже существует в базе данных",HttpStatus.CONFLICT);
                }else {
                    return new ResponseEntity<>("Электронная почта уже существует в базе данных", HttpStatus.CONFLICT);
                }
            }

            return new ResponseEntity<>("Ошибка добавления типографии", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(printingHouse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id){
        try{
            Optional<PrintingHouse> printingHouseInDb = printingHouseService.findById(id);
            if(printingHouseInDb.isEmpty()){
                return new ResponseEntity<>("Типография не найдена", HttpStatus.BAD_REQUEST);
            }else{
                if(!printingHouseInDb.get().getBookings().isEmpty()){
                    return new ResponseEntity<>("Невозможно удалить типографию, так как она связана с заказом",HttpStatus.CONFLICT);
                }

                printingHouseService.delete(id);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка удаления типографии", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Типография успешно удалена!", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid PrintingHouse printingHouse, BindingResult bindingResult, @PathVariable("id") long id){
        try{
            Optional<PrintingHouse> printingHouseInDb = printingHouseService.findById(id);
            if(printingHouseInDb.isEmpty()){
                return new ResponseEntity<>("Типография не найдена", HttpStatus.BAD_REQUEST);
            }else{

                if(printingHouseInDb.get().getId() != printingHouse.getId()){
                    return new ResponseEntity<>("Параметр id не совпадает с id типографии",HttpStatus.BAD_REQUEST);
                }

                if(bindingResult.hasErrors()){
                    return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
                }

                printingHouseService.save(printingHouse);
            }
        }catch (Exception e){
            if(e instanceof DataIntegrityViolationException){
                if(e.getMessage().contains("(phone)")){
                    return new ResponseEntity<>("Номер телефона уже существует в базе данных",HttpStatus.CONFLICT);
                }else {
                    return new ResponseEntity<>("Электронная почта уже существует в базе данных", HttpStatus.CONFLICT);
                }
            }

            return new ResponseEntity<>("Ошибка изменения данных о типографии", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(printingHouse, HttpStatus.OK);
    }

}
