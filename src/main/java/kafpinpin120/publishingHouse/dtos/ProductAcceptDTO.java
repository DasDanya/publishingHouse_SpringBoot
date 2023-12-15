package kafpinpin120.publishingHouse.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import kafpinpin120.publishingHouse.models.TypeProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAcceptDTO {

    private long id,userId;

    @NotBlank
    @Size(max = 30, message = "Длина названия продукции не входит в диапазон от 1 до 30 символов")
    private String name;

    @DecimalMin(value = "1", message = "1₽")
    @DecimalMax(value = "100000", message = "Максимальная стоимость материала = 100000₽")
    @Digits(integer = 6, fraction = 2, message = "Доступно 2 цифры после запятой. Длина целой части стоимости должна быть не более чем из 6 цифр")
    private BigDecimal cost;

    @Valid
    private TypeProduct typeProduct;

    List<ProductMaterialDTO> productMaterialDTOS;
}
