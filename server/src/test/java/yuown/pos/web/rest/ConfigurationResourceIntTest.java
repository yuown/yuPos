package yuown.pos.web.rest;

import yuown.pos.YuPosApp;

import yuown.pos.domain.Configuration;
import yuown.pos.repository.ConfigurationRepository;
import yuown.pos.service.ConfigurationService;
import yuown.pos.service.dto.ConfigurationDTO;
import yuown.pos.service.mapper.ConfigurationMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import yuown.pos.domain.enumeration.DataType;
/**
 * Test class for the ConfigurationResource REST controller.
 *
 * @see ConfigurationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuPosApp.class)
public class ConfigurationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CACHED = false;
    private static final Boolean UPDATED_CACHED = true;

    private static final DataType DEFAULT_TYPE = DataType.STRING;
    private static final DataType UPDATED_TYPE = DataType.DOUBLE;

    @Inject
    private ConfigurationRepository configurationRepository;

    @Inject
    private ConfigurationMapper configurationMapper;

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restConfigurationMockMvc;

    private Configuration configuration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConfigurationResource configurationResource = new ConfigurationResource();
        ReflectionTestUtils.setField(configurationResource, "configurationService", configurationService);
        this.restConfigurationMockMvc = MockMvcBuilders.standaloneSetup(configurationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Configuration createEntity(EntityManager em) {
        Configuration configuration = new Configuration()
                .name(DEFAULT_NAME)
                .value(DEFAULT_VALUE)
                .cached(DEFAULT_CACHED)
                .type(DEFAULT_TYPE);
        return configuration;
    }

    @Before
    public void initTest() {
        configuration = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfiguration() throws Exception {
        int databaseSizeBeforeCreate = configurationRepository.findAll().size();

        // Create the Configuration
        ConfigurationDTO configurationDTO = configurationMapper.configurationToConfigurationDTO(configuration);

        restConfigurationMockMvc.perform(post("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationDTO)))
            .andExpect(status().isCreated());

        // Validate the Configuration in the database
        List<Configuration> configurationList = configurationRepository.findAll();
        assertThat(configurationList).hasSize(databaseSizeBeforeCreate + 1);
        Configuration testConfiguration = configurationList.get(configurationList.size() - 1);
        assertThat(testConfiguration.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConfiguration.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testConfiguration.isCached()).isEqualTo(DEFAULT_CACHED);
        assertThat(testConfiguration.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configurationRepository.findAll().size();

        // Create the Configuration with an existing ID
        Configuration existingConfiguration = new Configuration();
        existingConfiguration.setId(1L);
        ConfigurationDTO existingConfigurationDTO = configurationMapper.configurationToConfigurationDTO(existingConfiguration);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigurationMockMvc.perform(post("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Configuration> configurationList = configurationRepository.findAll();
        assertThat(configurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllConfigurations() throws Exception {
        // Initialize the database
        configurationRepository.saveAndFlush(configuration);

        // Get all the configurationList
        restConfigurationMockMvc.perform(get("/api/configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configuration.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].cached").value(hasItem(DEFAULT_CACHED.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getConfiguration() throws Exception {
        // Initialize the database
        configurationRepository.saveAndFlush(configuration);

        // Get the configuration
        restConfigurationMockMvc.perform(get("/api/configurations/{id}", configuration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configuration.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.cached").value(DEFAULT_CACHED.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfiguration() throws Exception {
        // Get the configuration
        restConfigurationMockMvc.perform(get("/api/configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfiguration() throws Exception {
        // Initialize the database
        configurationRepository.saveAndFlush(configuration);
        int databaseSizeBeforeUpdate = configurationRepository.findAll().size();

        // Update the configuration
        Configuration updatedConfiguration = configurationRepository.findOne(configuration.getId());
        updatedConfiguration
                .name(UPDATED_NAME)
                .value(UPDATED_VALUE)
                .cached(UPDATED_CACHED)
                .type(UPDATED_TYPE);
        ConfigurationDTO configurationDTO = configurationMapper.configurationToConfigurationDTO(updatedConfiguration);

        restConfigurationMockMvc.perform(put("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationDTO)))
            .andExpect(status().isOk());

        // Validate the Configuration in the database
        List<Configuration> configurationList = configurationRepository.findAll();
        assertThat(configurationList).hasSize(databaseSizeBeforeUpdate);
        Configuration testConfiguration = configurationList.get(configurationList.size() - 1);
        assertThat(testConfiguration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConfiguration.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testConfiguration.isCached()).isEqualTo(UPDATED_CACHED);
        assertThat(testConfiguration.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = configurationRepository.findAll().size();

        // Create the Configuration
        ConfigurationDTO configurationDTO = configurationMapper.configurationToConfigurationDTO(configuration);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConfigurationMockMvc.perform(put("/api/configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configurationDTO)))
            .andExpect(status().isCreated());

        // Validate the Configuration in the database
        List<Configuration> configurationList = configurationRepository.findAll();
        assertThat(configurationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConfiguration() throws Exception {
        // Initialize the database
        configurationRepository.saveAndFlush(configuration);
        int databaseSizeBeforeDelete = configurationRepository.findAll().size();

        // Get the configuration
        restConfigurationMockMvc.perform(delete("/api/configurations/{id}", configuration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Configuration> configurationList = configurationRepository.findAll();
        assertThat(configurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
