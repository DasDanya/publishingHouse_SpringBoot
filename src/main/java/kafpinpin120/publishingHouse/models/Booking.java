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
    private LocalDate startExecution;


    private LocalDate endExecution;

    @Column(nullable = false)
    @Size(max = 20)
    private String status;


    @Column(nullable = false)
    @DecimalMin(value = "1", message = "1₽")
    @DecimalMax(value = "100000000", message = "Максимальная стоимость материала = 100000000₽")
    @Digits(integer = 9, fraction = 2, message = "Доступно 2 цифры после запятой. Длина целой части стоимости должна быть не более чем из 7 цифр")
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "printing_house_id")
    private PrintingHouse printingHouse;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingProduct> products;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "bookings_employees",
            joinColumns = @JoinColumn(name = "booking_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "employee_id",nullable = false),
            uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "booking_id"}))
    private List<Employee> employees;
}
