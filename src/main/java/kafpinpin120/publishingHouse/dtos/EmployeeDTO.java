package kafpinpin120.publishingHouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private long id;
    private String name, surname, patronymic, phone,email,post,pathToImage;
    private LocalDate birthday;
    private byte[] photo;
}
