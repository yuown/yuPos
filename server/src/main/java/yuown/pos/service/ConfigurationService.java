package yuown.pos.service;

import yuown.pos.service.dto.ConfigurationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Configuration.
 */
public interface ConfigurationService {

    /**
     * Save a configuration.
     *
     * @param configurationDTO the entity to save
     * @return the persisted entity
     */
    ConfigurationDTO save(ConfigurationDTO configurationDTO);

    /**
     *  Get all the configurations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ConfigurationDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" configuration.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ConfigurationDTO findOne(Long id);

    /**
     *  Delete the "id" configuration.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
