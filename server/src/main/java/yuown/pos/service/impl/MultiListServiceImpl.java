package yuown.pos.service.impl;

import yuown.pos.service.MultiListService;
import yuown.pos.domain.MultiList;
import yuown.pos.repository.MultiListRepository;
import yuown.pos.service.dto.MultiListDTO;
import yuown.pos.service.mapper.MultiListMapper;
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
 * Service Implementation for managing MultiList.
 */
@Service
@Transactional
public class MultiListServiceImpl implements MultiListService{

    private final Logger log = LoggerFactory.getLogger(MultiListServiceImpl.class);
    
    @Inject
    private MultiListRepository multiListRepository;

    @Inject
    private MultiListMapper multiListMapper;

    /**
     * Save a multiList.
     *
     * @param multiListDTO the entity to save
     * @return the persisted entity
     */
    public MultiListDTO save(MultiListDTO multiListDTO) {
        log.debug("Request to save MultiList : {}", multiListDTO);
        MultiList multiList = multiListMapper.multiListDTOToMultiList(multiListDTO);
        multiList = multiListRepository.save(multiList);
        MultiListDTO result = multiListMapper.multiListToMultiListDTO(multiList);
        return result;
    }

    /**
     *  Get all the multiLists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<MultiListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MultiLists");
        Page<MultiList> result = multiListRepository.findAll(pageable);
        return result.map(multiList -> multiListMapper.multiListToMultiListDTO(multiList));
    }

    /**
     *  Get one multiList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MultiListDTO findOne(Long id) {
        log.debug("Request to get MultiList : {}", id);
        MultiList multiList = multiListRepository.findOne(id);
        MultiListDTO multiListDTO = multiListMapper.multiListToMultiListDTO(multiList);
        return multiListDTO;
    }

    /**
     *  Delete the  multiList by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MultiList : {}", id);
        multiListRepository.delete(id);
    }
}
