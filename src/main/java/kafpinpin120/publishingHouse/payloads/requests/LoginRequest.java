package kafpinpin120.publishingHouse.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Необходимо ввести электронную почту")
    @Email
    private String email;


    @NotBlank(message = "Необходимо ввести пароль")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,}(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])$", message = "Пароль должен состоять минимум из 8 символов. В нем должна быть, как минимум, 1 заглавная буква, 1 строчная буква и 1 цифра")
    private String password;
}
