package kafpinpin120.publishingHouse.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
public class BookingAcceptDTO {

    private long id;

    @NotBlank
    private String status;

    private LocalDate startExecution;

    @Future
    private LocalDate endExecution;

    @DecimalMin(value = "1", message = "1₽")
    @DecimalMax(value = "100000000", message = "Максимальная стоимость материала = 100000000₽")
    @Digits(integer = 9, fraction = 2, message = "Доступно 2 цифры после запятой. Длина целой части стоимости должна быть не более чем из 7 цифр")
    private BigDecimal cost;

    @Valid
    private PrintingHouse printingHouse;

    @NotNull
    private List<ProductWithEditionDTO> productsWithMargin;

    private List<Long> idsOfEmployees;

}
