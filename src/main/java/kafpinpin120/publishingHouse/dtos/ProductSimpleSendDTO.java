package kafpinpin120.publishingHouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductSimpleSendDTO {

    private long id;
    private String name,username;
    private int edition;
    private BigDecimal cost;
    private byte[] photo;
}
