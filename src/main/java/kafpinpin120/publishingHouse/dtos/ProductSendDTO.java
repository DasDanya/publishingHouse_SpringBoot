package kafpinpin120.publishingHouse.dtos;

import kafpinpin120.publishingHouse.models.TypeProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductSendDTO {

    private long id;

    private String name,username, userEmail;

    private BigDecimal cost;

    private TypeProduct typeProduct;

    List<ProductMaterialDTO> productMaterialDTOS;

    List<CountProductsInBookingDTO> countProductsInBookingDTOS;

    List<byte[]> photos;
}
