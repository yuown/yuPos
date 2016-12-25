package yuown.pos.service.mapper;

import yuown.pos.domain.*;
import yuown.pos.service.dto.ElementDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Element and its DTO ElementDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ElementMapper {

    ElementDTO elementToElementDTO(Element element);

    List<ElementDTO> elementsToElementDTOs(List<Element> elements);

    @Mapping(target = "elements", ignore = true)
    @Mapping(target = "parents", ignore = true)
    Element elementDTOToElement(ElementDTO elementDTO);

    List<Element> elementDTOsToElements(List<ElementDTO> elementDTOs);
}
