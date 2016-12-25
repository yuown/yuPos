package yuown.pos.web.rest;

import yuown.pos.YuPosApp;

import yuown.pos.domain.LevelElement;
import yuown.pos.repository.LevelElementRepository;
import yuown.pos.service.LevelElementService;
import yuown.pos.service.dto.LevelElementDTO;
import yuown.pos.service.mapper.LevelElementMapper;

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
 * Test class for the LevelElementResource REST controller.
 *
 * @see LevelElementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuPosApp.class)
public class LevelElementResourceIntTest {

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private LevelElementRepository levelElementRepository;

    @Inject
    private LevelElementMapper levelElementMapper;

    @Inject
    private LevelElementService levelElementService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLevelElementMockMvc;

    private LevelElement levelElement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LevelElementResource levelElementResource = new LevelElementResource();
        ReflectionTestUtils.setField(levelElementResource, "levelElementService", levelElementService);
        this.restLevelElementMockMvc = MockMvcBuilders.standaloneSetup(levelElementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LevelElement createEntity(EntityManager em) {
        LevelElement levelElement = new LevelElement()
                .rank(DEFAULT_RANK)
                .active(DEFAULT_ACTIVE);
        return levelElement;
    }

    @Before
    public void initTest() {
        levelElement = createEntity(em);
    }

    @Test
    @Transactional
    public void createLevelElement() throws Exception {
        int databaseSizeBeforeCreate = levelElementRepository.findAll().size();

        // Create the LevelElement
        LevelElementDTO levelElementDTO = levelElementMapper.levelElementToLevelElementDTO(levelElement);

        restLevelElementMockMvc.perform(post("/api/level-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(levelElementDTO)))
            .andExpect(status().isCreated());

        // Validate the LevelElement in the database
        List<LevelElement> levelElementList = levelElementRepository.findAll();
        assertThat(levelElementList).hasSize(databaseSizeBeforeCreate + 1);
        LevelElement testLevelElement = levelElementList.get(levelElementList.size() - 1);
        assertThat(testLevelElement.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testLevelElement.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createLevelElementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = levelElementRepository.findAll().size();

        // Create the LevelElement with an existing ID
        LevelElement existingLevelElement = new LevelElement();
        existingLevelElement.setId(1L);
        LevelElementDTO existingLevelElementDTO = levelElementMapper.levelElementToLevelElementDTO(existingLevelElement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLevelElementMockMvc.perform(post("/api/level-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLevelElementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LevelElement> levelElementList = levelElementRepository.findAll();
        assertThat(levelElementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLevelElements() throws Exception {
        // Initialize the database
        levelElementRepository.saveAndFlush(levelElement);

        // Get all the levelElementList
        restLevelElementMockMvc.perform(get("/api/level-elements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(levelElement.getId().intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getLevelElement() throws Exception {
        // Initialize the database
        levelElementRepository.saveAndFlush(levelElement);

        // Get the levelElement
        restLevelElementMockMvc.perform(get("/api/level-elements/{id}", levelElement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(levelElement.getId().intValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLevelElement() throws Exception {
        // Get the levelElement
        restLevelElementMockMvc.perform(get("/api/level-elements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLevelElement() throws Exception {
        // Initialize the database
        levelElementRepository.saveAndFlush(levelElement);
        int databaseSizeBeforeUpdate = levelElementRepository.findAll().size();

        // Update the levelElement
        LevelElement updatedLevelElement = levelElementRepository.findOne(levelElement.getId());
        updatedLevelElement
                .rank(UPDATED_RANK)
                .active(UPDATED_ACTIVE);
        LevelElementDTO levelElementDTO = levelElementMapper.levelElementToLevelElementDTO(updatedLevelElement);

        restLevelElementMockMvc.perform(put("/api/level-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(levelElementDTO)))
            .andExpect(status().isOk());

        // Validate the LevelElement in the database
        List<LevelElement> levelElementList = levelElementRepository.findAll();
        assertThat(levelElementList).hasSize(databaseSizeBeforeUpdate);
        LevelElement testLevelElement = levelElementList.get(levelElementList.size() - 1);
        assertThat(testLevelElement.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testLevelElement.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingLevelElement() throws Exception {
        int databaseSizeBeforeUpdate = levelElementRepository.findAll().size();

        // Create the LevelElement
        LevelElementDTO levelElementDTO = levelElementMapper.levelElementToLevelElementDTO(levelElement);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLevelElementMockMvc.perform(put("/api/level-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(levelElementDTO)))
            .andExpect(status().isCreated());

        // Validate the LevelElement in the database
        List<LevelElement> levelElementList = levelElementRepository.findAll();
        assertThat(levelElementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLevelElement() throws Exception {
        // Initialize the database
        levelElementRepository.saveAndFlush(levelElement);
        int databaseSizeBeforeDelete = levelElementRepository.findAll().size();

        // Get the levelElement
        restLevelElementMockMvc.perform(delete("/api/level-elements/{id}", levelElement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LevelElement> levelElementList = levelElementRepository.findAll();
        assertThat(levelElementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
