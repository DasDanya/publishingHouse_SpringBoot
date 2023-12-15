package kafpinpin120.publishingHouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class CountProductsDTO {

    private BookingSimpleSendDTO booking;
    private int margin;
}
