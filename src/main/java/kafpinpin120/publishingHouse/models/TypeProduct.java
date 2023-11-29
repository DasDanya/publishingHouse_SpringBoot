package kafpinpin120.publishingHouse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "typeProducts")
@Data
@NoArgsConstructor
public class TypeProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(min = 5, max = 20, message = "Длина наименования типа продукции не входит в диапазон от 5 до 20 символов")
    @Pattern(regexp = "^[А-Яа-я ]+$", message = "Тип продукции должен состоять из русских букв")
    private String type;

    @Column(nullable = false)
    @DecimalMin(value = "1.00", message = "Минимальный процент наценки = 1")
    @DecimalMax(value = "1000.00", message = "Максимальный процент наценки = 1000")
    @Digits(integer = 4, fraction = 2, message = "Максимальная длина целой части наценки = 4, после запятой = 2")
    private double margin;

    @OneToMany(mappedBy = "typeProduct")
    @JsonIgnore
    private List<Product> products;
}
