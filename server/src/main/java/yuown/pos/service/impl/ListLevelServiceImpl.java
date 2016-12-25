package yuown.pos.service.impl;

import yuown.pos.service.ListLevelService;
import yuown.pos.domain.ListLevel;
import yuown.pos.repository.ListLevelRepository;
import yuown.pos.service.dto.ListLevelDTO;
import yuown.pos.service.mapper.ListLevelMapper;
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
 * Service Implementation for managing ListLevel.
 */
@Service
@Transactional
public class ListLevelServiceImpl implements ListLevelService{

    private final Logger log = LoggerFactory.getLogger(ListLevelServiceImpl.class);
    
    @Inject
    private ListLevelRepository listLevelRepository;

    @Inject
    private ListLevelMapper listLevelMapper;

    /**
     * Save a listLevel.
     *
     * @param listLevelDTO the entity to save
     * @return the persisted entity
     */
    public ListLevelDTO save(ListLevelDTO listLevelDTO) {
        log.debug("Request to save ListLevel : {}", listLevelDTO);
        ListLevel listLevel = listLevelMapper.listLevelDTOToListLevel(listLevelDTO);
        listLevel = listLevelRepository.save(listLevel);
        ListLevelDTO result = listLevelMapper.listLevelToListLevelDTO(listLevel);
        return result;
    }

    /**
     *  Get all the listLevels.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ListLevelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ListLevels");
        Page<ListLevel> result = listLevelRepository.findAll(pageable);
        return result.map(listLevel -> listLevelMapper.listLevelToListLevelDTO(listLevel));
    }

    /**
     *  Get one listLevel by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ListLevelDTO findOne(Long id) {
        log.debug("Request to get ListLevel : {}", id);
        ListLevel listLevel = listLevelRepository.findOne(id);
        ListLevelDTO listLevelDTO = listLevelMapper.listLevelToListLevelDTO(listLevel);
        return listLevelDTO;
    }

    /**
     *  Delete the  listLevel by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ListLevel : {}", id);
        listLevelRepository.delete(id);
    }
}
