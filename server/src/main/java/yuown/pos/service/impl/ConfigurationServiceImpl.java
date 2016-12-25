package yuown.pos.service.impl;

import yuown.pos.service.ConfigurationService;
import yuown.pos.domain.Configuration;
import yuown.pos.repository.ConfigurationRepository;
import yuown.pos.service.dto.ConfigurationDTO;
import yuown.pos.service.mapper.ConfigurationMapper;
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
 * Service Implementation for managing Configuration.
 */
@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService{

    private final Logger log = LoggerFactory.getLogger(ConfigurationServiceImpl.class);
    
    @Inject
    private ConfigurationRepository configurationRepository;

    @Inject
    private ConfigurationMapper configurationMapper;

    /**
     * Save a configuration.
     *
     * @param configurationDTO the entity to save
     * @return the persisted entity
     */
    public ConfigurationDTO save(ConfigurationDTO configurationDTO) {
        log.debug("Request to save Configuration : {}", configurationDTO);
        Configuration configuration = configurationMapper.configurationDTOToConfiguration(configurationDTO);
        configuration = configurationRepository.save(configuration);
        ConfigurationDTO result = configurationMapper.configurationToConfigurationDTO(configuration);
        return result;
    }

    /**
     *  Get all the configurations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ConfigurationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Configurations");
        Page<Configuration> result = configurationRepository.findAll(pageable);
        return result.map(configuration -> configurationMapper.configurationToConfigurationDTO(configuration));
    }

    /**
     *  Get one configuration by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ConfigurationDTO findOne(Long id) {
        log.debug("Request to get Configuration : {}", id);
        Configuration configuration = configurationRepository.findOne(id);
        ConfigurationDTO configurationDTO = configurationMapper.configurationToConfigurationDTO(configuration);
        return configurationDTO;
    }

    /**
     *  Delete the  configuration by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Configuration : {}", id);
        configurationRepository.delete(id);
    }
}
