package kafpinpin120.publishingHouse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
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
    @DecimalMax(value = "100000", message = "Максимальная стоимость материала = 100000₽")
    @Digits(integer = 6, fraction = 2, message = "Доступно 2 цифры после запятой. Длина целой части стоимости должна быть не более чем из 6 цифр")
    private BigDecimal cost;

    @ManyToOne
    @NotEmpty
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @NotEmpty
    @JoinColumn(name="type_product_id", nullable = false)
    private TypeProduct typeProduct;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductMaterial> materialsWithCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<BookingProduct> bookings;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PhotoProduct> photos;
}
