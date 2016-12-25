package yuown.pos.service.mapper;

import yuown.pos.domain.*;
import yuown.pos.service.dto.SkuDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Sku and its DTO SkuDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SkuMapper {

    @Mapping(source = "type.id", target = "typeId")
    SkuDTO skuToSkuDTO(Sku sku);

    List<SkuDTO> skusToSkuDTOs(List<Sku> skus);

    @Mapping(source = "typeId", target = "type")
    Sku skuDTOToSku(SkuDTO skuDTO);

    List<Sku> skuDTOsToSkus(List<SkuDTO> skuDTOs);

    default Element elementFromId(Long id) {
        if (id == null) {
            return null;
        }
        Element element = new Element();
        element.setId(id);
        return element;
    }
}
