package yuown.pos.service.impl;

import yuown.pos.service.LevelService;
import yuown.pos.domain.Level;
import yuown.pos.repository.LevelRepository;
import yuown.pos.service.dto.LevelDTO;
import yuown.pos.service.mapper.LevelMapper;
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
 * Service Implementation for managing Level.
 */
@Service
@Transactional
public class LevelServiceImpl implements LevelService{

    private final Logger log = LoggerFactory.getLogger(LevelServiceImpl.class);
    
    @Inject
    private LevelRepository levelRepository;

    @Inject
    private LevelMapper levelMapper;

    /**
     * Save a level.
     *
     * @param levelDTO the entity to save
     * @return the persisted entity
     */
    public LevelDTO save(LevelDTO levelDTO) {
        log.debug("Request to save Level : {}", levelDTO);
        Level level = levelMapper.levelDTOToLevel(levelDTO);
        level = levelRepository.save(level);
        LevelDTO result = levelMapper.levelToLevelDTO(level);
        return result;
    }

    /**
     *  Get all the levels.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<LevelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Levels");
        Page<Level> result = levelRepository.findAll(pageable);
        return result.map(level -> levelMapper.levelToLevelDTO(level));
    }

    /**
     *  Get one level by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public LevelDTO findOne(Long id) {
        log.debug("Request to get Level : {}", id);
        Level level = levelRepository.findOne(id);
        LevelDTO levelDTO = levelMapper.levelToLevelDTO(level);
        return levelDTO;
    }

    /**
     *  Delete the  level by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Level : {}", id);
        levelRepository.delete(id);
    }
}
