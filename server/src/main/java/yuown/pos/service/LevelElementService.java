package yuown.pos.service;

import yuown.pos.service.dto.LevelElementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing LevelElement.
 */
public interface LevelElementService {

    /**
     * Save a levelElement.
     *
     * @param levelElementDTO the entity to save
     * @return the persisted entity
     */
    LevelElementDTO save(LevelElementDTO levelElementDTO);

    /**
     *  Get all the levelElements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LevelElementDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" levelElement.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    LevelElementDTO findOne(Long id);

    /**
     *  Delete the "id" levelElement.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
