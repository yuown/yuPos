package yuown.pos.service.mapper;

import yuown.pos.domain.*;
import yuown.pos.service.dto.MultiListDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity MultiList and its DTO MultiListDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MultiListMapper {

    MultiListDTO multiListToMultiListDTO(MultiList multiList);

    List<MultiListDTO> multiListsToMultiListDTOs(List<MultiList> multiLists);

    @Mapping(target = "lists", ignore = true)
    MultiList multiListDTOToMultiList(MultiListDTO multiListDTO);

    List<MultiList> multiListDTOsToMultiLists(List<MultiListDTO> multiListDTOs);
}
