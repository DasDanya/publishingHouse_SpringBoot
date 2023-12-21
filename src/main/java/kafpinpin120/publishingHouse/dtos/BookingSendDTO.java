package kafpinpin120.publishingHouse.dtos;

import kafpinpin120.publishingHouse.models.PrintingHouse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingSendDTO {

    private long id;
    private String status;
    private LocalDate startExecution, endExecution;
    private BigDecimal cost;
    private PrintingHouse printingHouse;
    List<ProductSimpleSendDTO> products;
    List<EmployeeDTO> employees;

}
