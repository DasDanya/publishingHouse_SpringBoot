package kafpinpin120.publishingHouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class CountProductsInBookingDTO {

    private BookingSimpleSendDTO booking;
    private int edition;
}
