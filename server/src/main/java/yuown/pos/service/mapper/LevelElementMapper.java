package yuown.pos.service.mapper;

import yuown.pos.domain.*;
import yuown.pos.service.dto.LevelElementDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity LevelElement and its DTO LevelElementDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LevelElementMapper {

    @Mapping(source = "level.id", target = "levelId")
    @Mapping(source = "element.id", target = "elementId")
    @Mapping(source = "parentElement.id", target = "parentElementId")
    LevelElementDTO levelElementToLevelElementDTO(LevelElement levelElement);

    List<LevelElementDTO> levelElementsToLevelElementDTOs(List<LevelElement> levelElements);

    @Mapping(source = "levelId", target = "level")
    @Mapping(source = "elementId", target = "element")
    @Mapping(source = "parentElementId", target = "parentElement")
    LevelElement levelElementDTOToLevelElement(LevelElementDTO levelElementDTO);

    List<LevelElement> levelElementDTOsToLevelElements(List<LevelElementDTO> levelElementDTOs);

    default Level levelFromId(Long id) {
        if (id == null) {
            return null;
        }
        Level level = new Level();
        level.setId(id);
        return level;
    }

    default Element elementFromId(Long id) {
        if (id == null) {
            return null;
        }
        Element element = new Element();
        element.setId(id);
        return element;
    }
}
