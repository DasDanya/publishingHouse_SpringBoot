package kafpinpin120.publishingHouse.payloads.responses;

import kafpinpin120.publishingHouse.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class JwtResponse {

    private final String type = "Bearer";

    private String token;

    private User user;
}
