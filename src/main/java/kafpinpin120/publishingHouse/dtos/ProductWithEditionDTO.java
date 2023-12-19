package kafpinpin120.publishingHouse.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductWithEditionDTO {

    private long id;

    @NotBlank
    @Size(max = 30, message = "Длина названия продукции не входит в диапазон от 1 до 30 символов")
    private String name;

    @DecimalMin(value = "1", message = "1₽")
    @DecimalMax(value = "100000", message = "Максимальная стоимость продукции = 100000₽")
    @Digits(integer = 6, fraction = 2, message = "Доступно 2 цифры после запятой. Длина целой части стоимости должна быть не более чем из 6 цифр")
    private BigDecimal cost;

    @Range(min=1, max= 1000, message = "Количество продукции не входит в диапазон от 1 до 1000")
    private int edition;
}
