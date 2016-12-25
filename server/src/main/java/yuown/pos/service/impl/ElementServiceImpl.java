package yuown.pos.service.impl;

import yuown.pos.service.ElementService;
import yuown.pos.domain.Element;
import yuown.pos.repository.ElementRepository;
import yuown.pos.service.dto.ElementDTO;
import yuown.pos.service.mapper.ElementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Element.
 */
@Service
@Transactional
public class ElementServiceImpl implements ElementService{

    private final Logger log = LoggerFactory.getLogger(ElementServiceImpl.class);
    
    @Inject
    private ElementRepository elementRepository;

    @Inject
    private ElementMapper elementMapper;

    /**
     * Save a element.
     *
     * @param elementDTO the entity to save
     * @return the persisted entity
     */
    public ElementDTO save(ElementDTO elementDTO) {
        log.debug("Request to save Element : {}", elementDTO);
        Element element = elementMapper.elementDTOToElement(elementDTO);
        element = elementRepository.save(element);
        ElementDTO result = elementMapper.elementToElementDTO(element);
        return result;
    }

    /**
     *  Get all the elements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ElementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Elements");
        Page<Element> result = elementRepository.findAll(pageable);
        return result.map(element -> elementMapper.elementToElementDTO(element));
    }

    /**
     *  Get one element by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ElementDTO findOne(Long id) {
        log.debug("Request to get Element : {}", id);
        Element element = elementRepository.findOne(id);
        ElementDTO elementDTO = elementMapper.elementToElementDTO(element);
        return elementDTO;
    }

    /**
     *  Delete the  element by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Element : {}", id);
        elementRepository.delete(id);
    }
}
