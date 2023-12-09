package kafpinpin120.publishingHouse.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import kafpinpin120.publishingHouse.controllers.EmployeeController;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 20, message = "Длина имени сотрудника не входит в диапазон от 1 до 20 символов")
    @Pattern(regexp = "^[А-Яа-я]+$", message = "Имя сотрудника должно состоять из русских букв")
    private String name;

    @Column(nullable = false)
    @NotBlank
    @Size(max = 20, message = "Длина фамилии сотрудника не входит в диапазон от 1 до 20 символов")
    @Pattern(regexp = "^[А-Яа-я]+$", message = "Фамилия сотрудника должна состоять из русских букв")
    private String surname;

    @Size(max = 20, message = "Максимальная длина отчества: 20 символов")
    @Pattern(regexp = "^(|[А-Яа-я]+)$", message = "Отчество сотрудника должно состоять из русских букв")
    private String patronymic;

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
    @Size(min = 3,max = 25, message = "Длина должности не входит в диапазон от 3 до 25 символов")
    @Pattern(regexp = "^[А-Яа-я ]+$", message = "Должность должна состоять из русских букв")
    private String post;

    @Column(nullable = false)
    //@NotBlank(groups = EmployeeController.class)
    private String pathToImage;

    @Column(nullable = false)
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ManyToMany(mappedBy = "employees")
    @JsonIgnore
    private List<Booking> bookings;

}
