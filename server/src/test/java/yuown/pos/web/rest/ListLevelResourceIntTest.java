package yuown.pos.web.rest;

import yuown.pos.YuPosApp;

import yuown.pos.domain.ListLevel;
import yuown.pos.repository.ListLevelRepository;
import yuown.pos.service.ListLevelService;
import yuown.pos.service.dto.ListLevelDTO;
import yuown.pos.service.mapper.ListLevelMapper;

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
 * Test class for the ListLevelResource REST controller.
 *
 * @see ListLevelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuPosApp.class)
public class ListLevelResourceIntTest {

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private ListLevelRepository listLevelRepository;

    @Inject
    private ListLevelMapper listLevelMapper;

    @Inject
    private ListLevelService listLevelService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restListLevelMockMvc;

    private ListLevel listLevel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ListLevelResource listLevelResource = new ListLevelResource();
        ReflectionTestUtils.setField(listLevelResource, "listLevelService", listLevelService);
        this.restListLevelMockMvc = MockMvcBuilders.standaloneSetup(listLevelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ListLevel createEntity(EntityManager em) {
        ListLevel listLevel = new ListLevel()
                .rank(DEFAULT_RANK)
                .active(DEFAULT_ACTIVE);
        return listLevel;
    }

    @Before
    public void initTest() {
        listLevel = createEntity(em);
    }

    @Test
    @Transactional
    public void createListLevel() throws Exception {
        int databaseSizeBeforeCreate = listLevelRepository.findAll().size();

        // Create the ListLevel
        ListLevelDTO listLevelDTO = listLevelMapper.listLevelToListLevelDTO(listLevel);

        restListLevelMockMvc.perform(post("/api/list-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listLevelDTO)))
            .andExpect(status().isCreated());

        // Validate the ListLevel in the database
        List<ListLevel> listLevelList = listLevelRepository.findAll();
        assertThat(listLevelList).hasSize(databaseSizeBeforeCreate + 1);
        ListLevel testListLevel = listLevelList.get(listLevelList.size() - 1);
        assertThat(testListLevel.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testListLevel.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createListLevelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = listLevelRepository.findAll().size();

        // Create the ListLevel with an existing ID
        ListLevel existingListLevel = new ListLevel();
        existingListLevel.setId(1L);
        ListLevelDTO existingListLevelDTO = listLevelMapper.listLevelToListLevelDTO(existingListLevel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restListLevelMockMvc.perform(post("/api/list-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingListLevelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ListLevel> listLevelList = listLevelRepository.findAll();
        assertThat(listLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllListLevels() throws Exception {
        // Initialize the database
        listLevelRepository.saveAndFlush(listLevel);

        // Get all the listLevelList
        restListLevelMockMvc.perform(get("/api/list-levels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(listLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getListLevel() throws Exception {
        // Initialize the database
        listLevelRepository.saveAndFlush(listLevel);

        // Get the listLevel
        restListLevelMockMvc.perform(get("/api/list-levels/{id}", listLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(listLevel.getId().intValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingListLevel() throws Exception {
        // Get the listLevel
        restListLevelMockMvc.perform(get("/api/list-levels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateListLevel() throws Exception {
        // Initialize the database
        listLevelRepository.saveAndFlush(listLevel);
        int databaseSizeBeforeUpdate = listLevelRepository.findAll().size();

        // Update the listLevel
        ListLevel updatedListLevel = listLevelRepository.findOne(listLevel.getId());
        updatedListLevel
                .rank(UPDATED_RANK)
                .active(UPDATED_ACTIVE);
        ListLevelDTO listLevelDTO = listLevelMapper.listLevelToListLevelDTO(updatedListLevel);

        restListLevelMockMvc.perform(put("/api/list-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listLevelDTO)))
            .andExpect(status().isOk());

        // Validate the ListLevel in the database
        List<ListLevel> listLevelList = listLevelRepository.findAll();
        assertThat(listLevelList).hasSize(databaseSizeBeforeUpdate);
        ListLevel testListLevel = listLevelList.get(listLevelList.size() - 1);
        assertThat(testListLevel.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testListLevel.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingListLevel() throws Exception {
        int databaseSizeBeforeUpdate = listLevelRepository.findAll().size();

        // Create the ListLevel
        ListLevelDTO listLevelDTO = listLevelMapper.listLevelToListLevelDTO(listLevel);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restListLevelMockMvc.perform(put("/api/list-levels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listLevelDTO)))
            .andExpect(status().isCreated());

        // Validate the ListLevel in the database
        List<ListLevel> listLevelList = listLevelRepository.findAll();
        assertThat(listLevelList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteListLevel() throws Exception {
        // Initialize the database
        listLevelRepository.saveAndFlush(listLevel);
        int databaseSizeBeforeDelete = listLevelRepository.findAll().size();

        // Get the listLevel
        restListLevelMockMvc.perform(delete("/api/list-levels/{id}", listLevel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ListLevel> listLevelList = listLevelRepository.findAll();
        assertThat(listLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
