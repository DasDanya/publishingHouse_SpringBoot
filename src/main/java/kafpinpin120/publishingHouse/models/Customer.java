package kafpinpin120.publishingHouse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 50, message = "Длина наименования заказчика не входит в диапазон от 1 до 50 символов")
    private String name;

    @Column(nullable = false,unique = true, length = 17)
    @NotBlank
    @Pattern(regexp = "\\+7-\\d{3}-\\d{3}-\\d{2}-\\d{2}", message = "Неверный номер телефона.Паттерн: +7-###-###-##-##")
    private String phone;

    @Column(nullable = false, unique = true, length = 60)
    @NotBlank
    @Email(message = "Неверный адрес электронной почты")
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Product> products;

}
