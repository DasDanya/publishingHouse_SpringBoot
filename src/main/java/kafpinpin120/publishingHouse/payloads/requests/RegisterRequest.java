package kafpinpin120.publishingHouse.payloads.requests;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import kafpinpin120.publishingHouse.models.UserRole;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotBlank(message = "Необходимо ввести имя")
    @Size(max = 50, message = "Длина наименования пользователя не входит в диапазон от 1 до 50 символов")
    private String name;

    @NotBlank(message = "Необходимо ввести номер телефона")
    @Pattern(regexp = "\\+7-\\d{3}-\\d{3}-\\d{2}-\\d{2}", message = "Неверный номер телефона.Паттерн: +7-###-###-##-##")
    private String phone;

    @NotBlank(message = "Необходимо ввести электронную почту")
    @Email(message = "Неверный адрес электронной почты")
    private String email;

    @NotBlank(message = "Необходимо ввести пароль")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "Пароль должен состоять минимум из 8 символов. В нем должна быть, как минимум, 1 заглавная буква, 1 строчная буква и 1 цифра. Пробелы недопустимы")
    private String password;

    @NotNull(message = "Необходимо указать роль пользователя")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
