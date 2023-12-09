package kafpinpin120.publishingHouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private long id, userId, typeProductId;
    private String name;
    private BigDecimal cost;

    List<ProductMaterialDTO> productMaterialDTOS;
    List<byte[]> photos;
}
