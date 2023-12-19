package kafpinpin120.publishingHouse.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 30, message = "Длина названия продукции не входит в диапазон от 1 до 30 символов")
    private String name;

    @Column(nullable = false)
    @DecimalMin(value = "1", message = "1₽")
    @DecimalMax(value = "100000", message = "Максимальная стоимость продукции = 100000₽")
    @Digits(integer = 6, fraction = 2, message = "Доступно 2 цифры после запятой. Длина целой части стоимости должна быть не более чем из 6 цифр")
    private BigDecimal cost;

    @ManyToOne
    @NotNull
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name="type_product_id", nullable = false)
    private TypeProduct typeProduct;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @NotNull
    private List<ProductMaterial> materialsWithCount;

    @OneToMany(mappedBy = "product")
    private List<BookingProduct> bookings;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @NotNull
    private List<PhotoProduct> photos;
}
