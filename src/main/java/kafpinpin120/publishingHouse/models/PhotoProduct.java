package kafpinpin120.publishingHouse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "photoProducts")
public class PhotoProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank
    private String path;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public PhotoProduct(String path, Product product) {
        this.path = path;
        this.product = product;
    }
}
