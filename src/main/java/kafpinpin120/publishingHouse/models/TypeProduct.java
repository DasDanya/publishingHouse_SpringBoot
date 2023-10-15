package kafpinpin120.publishingHouse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Data
@NoArgsConstructor
@Table(name = "typeProducts", uniqueConstraints = @UniqueConstraint(columnNames = "type"))
public class TypeProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 5, max = 20, message = "Длина типа продукции не входит в диапазон от 5 до 20 символов")
    @Pattern(regexp = "^[А-Яа-я ]+$", message = "Тип продукции должен состоять из русских букв")
    private String type;

    @Column(nullable = false)
    @DecimalMin(value = "1.00", message = "Минимальный процент = 1")
    @DecimalMax(value = "1000.00", message = "Максимальный процент = 1000")
    private double margin;

}
