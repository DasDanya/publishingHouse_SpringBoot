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
@Table(name = "bookingsProducts", uniqueConstraints = @UniqueConstraint(columnNames = {"booking_id", "product_id"}))
public class BookingProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Range(min=1, max= 1000, message = "Количество продукции не входит в диапазон от 1 до 1000")
    private int edition;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    public BookingProduct(int edition, Booking booking, Product product) {
        this.edition = edition;
        this.booking = booking;
        this.product = product;
    }
}
