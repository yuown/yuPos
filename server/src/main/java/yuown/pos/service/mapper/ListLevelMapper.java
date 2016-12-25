package yuown.pos.service.mapper;

import yuown.pos.domain.*;
import yuown.pos.service.dto.ListLevelDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ListLevel and its DTO ListLevelDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ListLevelMapper {

    @Mapping(source = "list.id", target = "listId")
    @Mapping(source = "level.id", target = "levelId")
    ListLevelDTO listLevelToListLevelDTO(ListLevel listLevel);

    List<ListLevelDTO> listLevelsToListLevelDTOs(List<ListLevel> listLevels);

    @Mapping(source = "listId", target = "list")
    @Mapping(source = "levelId", target = "level")
    ListLevel listLevelDTOToListLevel(ListLevelDTO listLevelDTO);

    List<ListLevel> listLevelDTOsToListLevels(List<ListLevelDTO> listLevelDTOs);

    default MultiList multiListFromId(Long id) {
        if (id == null) {
            return null;
        }
        MultiList multiList = new MultiList();
        multiList.setId(id);
        return multiList;
    }

    default Level levelFromId(Long id) {
        if (id == null) {
            return null;
        }
        Level level = new Level();
        level.setId(id);
        return level;
    }
}
