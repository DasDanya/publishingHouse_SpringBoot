package kafpinpin120.publishingHouse.controllers;


import jakarta.validation.Valid;
import kafpinpin120.publishingHouse.dtos.EmployeeDTO;
import kafpinpin120.publishingHouse.exceptions.FileIsNotImageException;
import kafpinpin120.publishingHouse.models.Employee;
import kafpinpin120.publishingHouse.services.EmployeeService;
import kafpinpin120.publishingHouse.services.FilesService;
import kafpinpin120.publishingHouse.services.ValidateInputService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ValidateInputService validateInputService;

    public EmployeeController(EmployeeService employeeService, ValidateInputService validateInputService, FilesService filesService) {
        this.employeeService = employeeService;
        this.validateInputService = validateInputService;

    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "page") int page, @RequestParam(name = "surname", required = false) String surname){
        List<EmployeeDTO> employees;

        try{
            if(surname == null){
                employees = employeeService.findByPage(page);
            }else{
                employees = employeeService.findByPage(page,surname);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Ошибка получения списка сотрудников", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestPart @Valid Employee employee, BindingResult bindingResult, @RequestPart MultipartFile photo){
        try{
            if(bindingResult.hasErrors()){
                return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
            }

            employeeService.save(employee, photo,false);

        }catch (Exception e){
            if(e instanceof DataIntegrityViolationException){
                if(e.getMessage().contains("(phone)")){
                    return new ResponseEntity<>("Номер телефона уже существует в базе данных",HttpStatus.CONFLICT);
                }else {
                    return new ResponseEntity<>("Электронная почта уже существует в базе данных", HttpStatus.CONFLICT);
                }
            }else if(e instanceof FileIsNotImageException){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("Ошибка добавления сотрудника", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id){
        try{
            Optional<Employee> employeeInDb = employeeService.findById(id);
            if(employeeInDb.isEmpty()){
                return new ResponseEntity<>("Сотрудник не найден", HttpStatus.BAD_REQUEST);
            }else{
                Employee employee = employeeInDb.get();
                if(!employee.getBookings().isEmpty()){
                    return new ResponseEntity<>("Невозможно удалить сотрудника, так как он связан с заказом", HttpStatus.CONFLICT);
                }

                employeeService.delete(employee);
            }
        } catch (Exception e){
            return new ResponseEntity<>("Ошибка удаления сотрудника", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Сотрудник успешно удалён!", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update (@RequestPart @Valid Employee employee, BindingResult bindingResult, @RequestPart MultipartFile photo, @PathVariable("id") long id){
        try{
            Optional<Employee> employeeInDb = employeeService.findById(id);
            if(employeeInDb.isEmpty()){
                return new ResponseEntity<>("Сотрудник не найден", HttpStatus.BAD_REQUEST);
            }else{
                Employee existEmployee = employeeInDb.get();
                if(existEmployee.getId() != employee.getId()){
                    return new ResponseEntity<>("Параметр id не совпадает с id сотрудника", HttpStatus.BAD_REQUEST);
                }

                if(bindingResult.hasErrors()){
                    return new ResponseEntity<>(validateInputService.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
                }

                employeeService.save(employee, photo, true);
            }
        }catch (Exception e){
            if(e instanceof DataIntegrityViolationException){
                if(e.getMessage().contains("(phone)")){
                    return new ResponseEntity<>("Номер телефона уже существует в базе данных",HttpStatus.CONFLICT);
                }else {
                    return new ResponseEntity<>("Электронная почта уже существует в базе данных", HttpStatus.CONFLICT);
                }
            }else if(e instanceof FileIsNotImageException){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            System.out.println(e.getMessage());
            return new ResponseEntity<>("Ошибка изменения данных о сотруднике", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
