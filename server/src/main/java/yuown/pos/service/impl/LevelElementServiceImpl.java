package yuown.pos.service.impl;

import yuown.pos.service.LevelElementService;
import yuown.pos.domain.LevelElement;
import yuown.pos.repository.LevelElementRepository;
import yuown.pos.service.dto.LevelElementDTO;
import yuown.pos.service.mapper.LevelElementMapper;
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
 * Service Implementation for managing LevelElement.
 */
@Service
@Transactional
public class LevelElementServiceImpl implements LevelElementService{

    private final Logger log = LoggerFactory.getLogger(LevelElementServiceImpl.class);
    
    @Inject
    private LevelElementRepository levelElementRepository;

    @Inject
    private LevelElementMapper levelElementMapper;

    /**
     * Save a levelElement.
     *
     * @param levelElementDTO the entity to save
     * @return the persisted entity
     */
    public LevelElementDTO save(LevelElementDTO levelElementDTO) {
        log.debug("Request to save LevelElement : {}", levelElementDTO);
        LevelElement levelElement = levelElementMapper.levelElementDTOToLevelElement(levelElementDTO);
        levelElement = levelElementRepository.save(levelElement);
        LevelElementDTO result = levelElementMapper.levelElementToLevelElementDTO(levelElement);
        return result;
    }

    /**
     *  Get all the levelElements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<LevelElementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LevelElements");
        Page<LevelElement> result = levelElementRepository.findAll(pageable);
        return result.map(levelElement -> levelElementMapper.levelElementToLevelElementDTO(levelElement));
    }

    /**
     *  Get one levelElement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public LevelElementDTO findOne(Long id) {
        log.debug("Request to get LevelElement : {}", id);
        LevelElement levelElement = levelElementRepository.findOne(id);
        LevelElementDTO levelElementDTO = levelElementMapper.levelElementToLevelElementDTO(levelElement);
        return levelElementDTO;
    }

    /**
     *  Delete the  levelElement by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LevelElement : {}", id);
        levelElementRepository.delete(id);
    }
}
