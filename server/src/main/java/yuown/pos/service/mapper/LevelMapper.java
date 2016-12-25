package yuown.pos.service.mapper;

import yuown.pos.domain.*;
import yuown.pos.service.dto.LevelDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Level and its DTO LevelDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LevelMapper {

    LevelDTO levelToLevelDTO(Level level);

    List<LevelDTO> levelsToLevelDTOs(List<Level> levels);

    @Mapping(target = "levelLists", ignore = true)
    @Mapping(target = "elementLevels", ignore = true)
    Level levelDTOToLevel(LevelDTO levelDTO);

    List<Level> levelDTOsToLevels(List<LevelDTO> levelDTOs);
}
