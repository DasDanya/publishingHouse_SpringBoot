package kafpinpin120.publishingHouse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "productsMaterials", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "material_id"}))
public class ProductMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Range(min=1, max= 1000, message = "Количество материала не входит в диапазон от 1 до 1000")
    private int countMaterials;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @ManyToOne
    @NotNull
    @JoinColumn(name="material_id",nullable = false)
    private Material material;

    public ProductMaterial(int countMaterials, Product product, Material material) {
        this.countMaterials = countMaterials;
        this.product = product;
        this.material = material;
    }
}
