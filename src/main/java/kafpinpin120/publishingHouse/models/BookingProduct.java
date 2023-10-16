package kafpinpin120.publishingHouse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Data
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
    @NotEmpty
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @NotEmpty
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
