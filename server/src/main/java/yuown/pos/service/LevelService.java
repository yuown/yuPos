package yuown.pos.service;

import yuown.pos.service.dto.LevelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Level.
 */
public interface LevelService {

    /**
     * Save a level.
     *
     * @param levelDTO the entity to save
     * @return the persisted entity
     */
    LevelDTO save(LevelDTO levelDTO);

    /**
     *  Get all the levels.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LevelDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" level.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    LevelDTO findOne(Long id);

    /**
     *  Delete the "id" level.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
