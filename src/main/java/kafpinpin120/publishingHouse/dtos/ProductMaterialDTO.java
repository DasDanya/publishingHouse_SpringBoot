package kafpinpin120.publishingHouse.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMaterialDTO {
    private long materialId;
    private int countMaterials;
}
