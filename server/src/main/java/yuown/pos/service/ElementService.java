package yuown.pos.service;

import yuown.pos.service.dto.ElementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Element.
 */
public interface ElementService {

    /**
     * Save a element.
     *
     * @param elementDTO the entity to save
     * @return the persisted entity
     */
    ElementDTO save(ElementDTO elementDTO);

    /**
     *  Get all the elements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ElementDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" element.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ElementDTO findOne(Long id);

    /**
     *  Delete the "id" element.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
