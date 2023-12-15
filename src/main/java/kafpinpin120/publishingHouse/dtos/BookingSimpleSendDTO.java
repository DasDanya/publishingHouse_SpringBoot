package kafpinpin120.publishingHouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class BookingSimpleSendDTO {

    private long id;
    private BigDecimal cost;

}
