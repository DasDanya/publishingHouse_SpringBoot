package kafpinpin120.publishingHouse.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Необходимо ввести имя")
    @Size(max = 50, message = "Длина наименования пользователя не входит в диапазон от 1 до 50 символов")
    private String name;

    @Column(nullable = false,unique = true, length = 17)
    @NotBlank(message = "Необходимо ввести номер телефона")
    @Pattern(regexp = "\\+7-\\d{3}-\\d{3}-\\d{2}-\\d{2}", message = "Неверный номер телефона.Паттерн: +7-###-###-##-##")
    private String phone;

    @Column(nullable = false, unique = true, length = 60)
    @NotBlank(message = "Необходимо ввести электронную почту")
    @Email(message = "Неверный адрес электронной почты")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Необходимо ввести пароль")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "Пароль должен состоять минимум из 8 символов. В нем должна быть, как минимум, 1 заглавная буква, 1 строчная буква и 1 цифра. Пробелы недопустимы")
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @NotNull(message = "Необходимо указать роль пользователя")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;

    public User(String name, String phone, String email, String password, UserRole role) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
