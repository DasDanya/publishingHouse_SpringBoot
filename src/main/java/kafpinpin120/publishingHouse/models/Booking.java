package kafpinpin120.publishingHouse.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd.MM.yyyy")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startExecution;

    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd.MM.yyyy")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endExecution;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusOfBooking status;


    @Column(nullable = false)
    @DecimalMin(value = "1", message = "1₽")
    @DecimalMax(value = "1000000", message = "Максимальная стоимость материала = 1000000₽")
    @Digits(integer = 7, fraction = 2, message = "Доступно 2 цифры после запятой. Длина целой части стоимости должна быть не более чем из 7 цифр")
    private BigDecimal cost;

    @ManyToOne
    @NotEmpty
    @JoinColumn(name = "printing_house_id")
    private PrintingHouse printingHouse;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingProduct> products;

    @ManyToMany(mappedBy = "bookings")
    private List<Employee> employees;
}
