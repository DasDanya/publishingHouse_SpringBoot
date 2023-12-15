package kafpinpin120.publishingHouse.dtos;

import kafpinpin120.publishingHouse.models.Material;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMaterialDTO {
    private Material material;
    private int countMaterials;
}
