package kafpinpin120.publishingHouse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name = "materials", uniqueConstraints = @UniqueConstraint(columnNames = {"type", "color", "size"}))
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Тип материала не должен быть пустым")
    @Size(min = 5, max = 20, message = "Длина типа материала не входит в диапазон от 5 до 20 символов")
    @Pattern(regexp = "^[А-Яа-я ]+$", message = "Тип материала должен состоять из русских букв")
    private String type;


    @Column(nullable = false)
    @NotBlank(message = "Цвет материала не должен быть пустым")
    @Size(min = 6, max = 15, message = "Длина цвета материала не входит в диапазон от 6 до 15 символов")
    @Pattern(regexp = "\\s*(0|[1-9]\\d?|1\\d\\d?|2[0-4]\\d|25[0-5])%?\\s*,\\s*(0|[1-9]\\d?|1\\d\\d?|2[0-4]\\d|25[0-5])%?\\s*,\\s*(0|[1-9]\\d?|1\\d\\d?|2[0-4]\\d|25[0-5])%?\\s*$", message = "Цвет материала должен быть записан следующим образом: R;G;B")
    private String color;


    @Column(nullable = false)
    @NotBlank(message = "Размер материала не должен быть пустым")
    @Size(max = 2, message = "Длина размера материала должна состоять максимум из 2 симвлолов")
    @Pattern(regexp = "[A-C]{1}[0-9]{1}", message = "Размер материала должен быть записан следующим образом: формат бумаги + размер. Пример: А4")
    private String size;

    @Column(nullable = false)
    @DecimalMin(value = "0.1", message = "Минимальная стоимость материала = 0.1₽")
    @DecimalMax(value = "100", message = "Максимальная стоимость материала = 100₽")
    @Digits(integer = 3, fraction = 2, message = "Доступно 2 цифры после запятой. Длина целой части стоимости должна быть не более чем из 3 цифр")
    private BigDecimal cost;
}


