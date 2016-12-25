package yuown.pos.service;

import yuown.pos.service.dto.ListLevelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing ListLevel.
 */
public interface ListLevelService {

    /**
     * Save a listLevel.
     *
     * @param listLevelDTO the entity to save
     * @return the persisted entity
     */
    ListLevelDTO save(ListLevelDTO listLevelDTO);

    /**
     *  Get all the listLevels.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ListLevelDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" listLevel.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ListLevelDTO findOne(Long id);

    /**
     *  Delete the "id" listLevel.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
