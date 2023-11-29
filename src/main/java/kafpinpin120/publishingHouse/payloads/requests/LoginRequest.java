package kafpinpin120.publishingHouse.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Getter
public class LoginRequest {

    @NotBlank(message = "Необходимо ввести электронную почту")
    @Email(message = "Неверный адрес электронной почты")
    private String email;


    @NotBlank(message = "Необходимо ввести пароль")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "Пароль должен состоять минимум из 8 символов. В нем должна быть, как минимум, 1 заглавная буква, 1 строчная буква и 1 цифра. Пробелы недопустимы")
    private String password;
}
