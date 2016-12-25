package yuown.pos.service;

import yuown.pos.service.dto.MultiListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing MultiList.
 */
public interface MultiListService {

    /**
     * Save a multiList.
     *
     * @param multiListDTO the entity to save
     * @return the persisted entity
     */
    MultiListDTO save(MultiListDTO multiListDTO);

    /**
     *  Get all the multiLists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MultiListDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" multiList.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MultiListDTO findOne(Long id);

    /**
     *  Delete the "id" multiList.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
