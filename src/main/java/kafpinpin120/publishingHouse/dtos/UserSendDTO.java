package kafpinpin120.publishingHouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSendDTO {

    private long id;
    private String name,email, phone;

    private List<ProductSimpleSendDTO> products;

    private List<BookingSimpleSendDTO> bookings;
}
