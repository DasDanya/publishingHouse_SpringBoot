package kafpinpin120.publishingHouse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="printingHouses")
public class PrintingHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 20, message = "Длина названия типографии не входит в диапазон от 1 до 20 символов")
    private String name;

    @Column(nullable = false, unique = true, length = 17)
    @NotBlank
    @Pattern(regexp = "\\+7-\\d{3}-\\d{3}-\\d{2}-\\d{2}", message = "Неверный номер телефона.Паттерн: +7-###-###-##-##")
    private String phone;

    @Column(nullable = false, unique = true, length = 60)
    @NotBlank
    @Email(message = "Неверный адрес электронной почты")
    private String email;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 10, max = 50, message = "Длина наименования субъекта РФ не входит в диапазон от 10 до 50 символов")
    @Pattern(regexp = "^[А-Яа-я ]+$", message = "Субъект РФ должен состоять из русских букв")
    private String state;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 50, message = "Длина наименования города не входит в диапазон от 3 до 50 символов")
    @Pattern(regexp = "^[А-Яа-я]*(?:[\s-][А-Яа-я]*)*$", message = "Некорректный ввод названия города")
    private String city;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 50, message = "Длина наименования улицы не входит в диапазон от 3 до 50 символов")
    @Pattern(regexp = "[а-яА-Я\\d\\-\\s]+", message = "Некорректный ввод названия улицы")
    private String street;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 7, message = "Длина номера дома не входит в диапазон от 1 до 7 символов")
    @Pattern(regexp = "^[0-9]+[А-Е]?(/[0-9]+)?$", message = "Некорректный ввод номера дома")
    private String house;


    @OneToMany(mappedBy = "printingHouse")
    @JsonIgnore
    private List<Booking> bookings;
}
