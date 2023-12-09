package kafpinpin120.publishingHouse.services;

import kafpinpin120.publishingHouse.dtos.EmployeeDTO;
import kafpinpin120.publishingHouse.exceptions.FileIsNotImageException;
import kafpinpin120.publishingHouse.models.Employee;
import kafpinpin120.publishingHouse.repositories.EmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final FilesService filesService;
    private final int countItemsInPage = 7;

    public EmployeeService(EmployeeRepository employeeRepository, FilesService filesService) {
        this.employeeRepository = employeeRepository;
        this.filesService = filesService;
    }


    public List<EmployeeDTO> findByPage(int page) throws IOException {
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("surname"));
        List<Employee> employees = employeeRepository.findAll(pageable).getContent();

        return getListDTOS(employees);
    }

    public List<EmployeeDTO> findByPage(int page, String surname) throws IOException{
        Pageable pageable = PageRequest.of(page, countItemsInPage, Sort.by("surname"));
        List<Employee> employees = employeeRepository.findBySurnameContainsIgnoreCase(pageable, surname).getContent();

        return getListDTOS(employees);
    }

    private List<EmployeeDTO> getListDTOS(List<Employee> employees) throws IOException {
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();

        for(Employee employee: employees){
            byte[] photo = filesService.getFile(employee.getPathToImage());
            EmployeeDTO employeeDTO = new EmployeeDTO(employee.getId(), employee.getName(), employee.getSurname(), employee.getPatronymic(),
                    employee.getPhone(), employee.getEmail(), employee.getPost(), employee.getPathToImage(), employee.getBirthday(), photo);

            employeeDTOS.add(employeeDTO);
        }

        return employeeDTOS;
    }

    public Optional<Employee> findById(long id){
        return employeeRepository.findById(id);
    }

    public void save(Employee employee, MultipartFile photo, boolean updated) throws IOException {
        if(!filesService.isImage(photo)){
            throw new FileIsNotImageException("Файл не является изображением");
        }

        Random random = new Random();
        String newPhotoName = employee.getSurname() + employee.getName() +random.nextInt(1000000) + photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf('.'));
        String destinationPath = "src/main/resources/static/images/employees/" + newPhotoName;

        String pastImagePath = employee.getPathToImage();
        employee.setPathToImage(destinationPath);

        employeeRepository.save(employee);
        filesService.saveImage(photo, destinationPath);

        if(updated){
            filesService.deleteFile(pastImagePath);
        }

    }

    public void delete(Employee employee) throws IOException {
        String pathToImage = employee.getPathToImage();

        employeeRepository.deleteById(employee.getId());
        filesService.deleteFile(pathToImage);
    }
}
