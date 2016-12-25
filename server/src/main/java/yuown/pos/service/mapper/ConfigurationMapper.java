package yuown.pos.service.mapper;

import yuown.pos.domain.*;
import yuown.pos.service.dto.ConfigurationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Configuration and its DTO ConfigurationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfigurationMapper {

    ConfigurationDTO configurationToConfigurationDTO(Configuration configuration);

    List<ConfigurationDTO> configurationsToConfigurationDTOs(List<Configuration> configurations);

    Configuration configurationDTOToConfiguration(ConfigurationDTO configurationDTO);

    List<Configuration> configurationDTOsToConfigurations(List<ConfigurationDTO> configurationDTOs);
}
