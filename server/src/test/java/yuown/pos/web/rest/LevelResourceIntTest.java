package yuown.pos.web.rest;

import yuown.pos.YuPosApp;

import yuown.pos.domain.Level;
import yuown.pos.repository.LevelRepository;
import yuown.pos.service.LevelService;
import yuown.pos.service.dto.LevelDTO;
import yuown.pos.service.mapper.LevelMapper;

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

/**
 * Test class for the LevelResource REST controller.
 *
 * @see LevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuPosApp.class)
public class LevelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Inject
    private LevelRepository levelRepository;

    @Inject
    private LevelMapper levelMapper;

    @Inject
    private LevelService levelService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLevelMockMvc;

    private Level level;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LevelResource levelResource = new LevelResource();
        ReflectionTestUtils.setField(levelResource, "levelService", levelService);
        this.restLevelMockMvc = MockMvcBuilders.standaloneSetup(levelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Level createEntity(EntityManager em) {
        Level level = new Level()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .enabled(DEFAULT_ENABLED);
        return level;
    }

    @Before
    public void initTest() {
        level = createEntity(em);
    }

    @Test
    @Transactional
    public void createLevel() throws Exception {
        int databaseSizeBeforeCreate = levelRepository.findAll().size();

        // Create the Level
        LevelDTO levelDTO = levelMapper.levelToLevelDTO(level);

        restLevelMockMvc.perform(post("/api/levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(levelDTO)))
            .andExpect(status().isCreated());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeCreate + 1);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLevel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLevel.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = levelRepository.findAll().size();

        // Create the Level with an existing ID
        Level existingLevel = new Level();
        existingLevel.setId(1L);
        LevelDTO existingLevelDTO = levelMapper.levelToLevelDTO(existingLevel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLevelMockMvc.perform(post("/api/levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLevelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLevels() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        // Get all the levelList
        restLevelMockMvc.perform(get("/api/levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(level.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        // Get the level
        restLevelMockMvc.perform(get("/api/levels/{id}", level.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(level.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLevel() throws Exception {
        // Get the level
        restLevelMockMvc.perform(get("/api/levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();

        // Update the level
        Level updatedLevel = levelRepository.findOne(level.getId());
        updatedLevel
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .enabled(UPDATED_ENABLED);
        LevelDTO levelDTO = levelMapper.levelToLevelDTO(updatedLevel);

        restLevelMockMvc.perform(put("/api/levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(levelDTO)))
            .andExpect(status().isOk());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate);
        Level testLevel = levelList.get(levelList.size() - 1);
        assertThat(testLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLevel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLevel.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingLevel() throws Exception {
        int databaseSizeBeforeUpdate = levelRepository.findAll().size();

        // Create the Level
        LevelDTO levelDTO = levelMapper.levelToLevelDTO(level);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLevelMockMvc.perform(put("/api/levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(levelDTO)))
            .andExpect(status().isCreated());

        // Validate the Level in the database
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        int databaseSizeBeforeDelete = levelRepository.findAll().size();

        // Get the level
        restLevelMockMvc.perform(delete("/api/levels/{id}", level.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Level> levelList = levelRepository.findAll();
        assertThat(levelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
