package yuown.pos.web.rest;

import yuown.pos.YuPosApp;

import yuown.pos.domain.MultiList;
import yuown.pos.repository.MultiListRepository;
import yuown.pos.service.MultiListService;
import yuown.pos.service.dto.MultiListDTO;
import yuown.pos.service.mapper.MultiListMapper;

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
 * Test class for the MultiListResource REST controller.
 *
 * @see MultiListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuPosApp.class)
public class MultiListResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Inject
    private MultiListRepository multiListRepository;

    @Inject
    private MultiListMapper multiListMapper;

    @Inject
    private MultiListService multiListService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMultiListMockMvc;

    private MultiList multiList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MultiListResource multiListResource = new MultiListResource();
        ReflectionTestUtils.setField(multiListResource, "multiListService", multiListService);
        this.restMultiListMockMvc = MockMvcBuilders.standaloneSetup(multiListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MultiList createEntity(EntityManager em) {
        MultiList multiList = new MultiList()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .enabled(DEFAULT_ENABLED);
        return multiList;
    }

    @Before
    public void initTest() {
        multiList = createEntity(em);
    }

    @Test
    @Transactional
    public void createMultiList() throws Exception {
        int databaseSizeBeforeCreate = multiListRepository.findAll().size();

        // Create the MultiList
        MultiListDTO multiListDTO = multiListMapper.multiListToMultiListDTO(multiList);

        restMultiListMockMvc.perform(post("/api/multi-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(multiListDTO)))
            .andExpect(status().isCreated());

        // Validate the MultiList in the database
        List<MultiList> multiListList = multiListRepository.findAll();
        assertThat(multiListList).hasSize(databaseSizeBeforeCreate + 1);
        MultiList testMultiList = multiListList.get(multiListList.size() - 1);
        assertThat(testMultiList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMultiList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMultiList.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createMultiListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = multiListRepository.findAll().size();

        // Create the MultiList with an existing ID
        MultiList existingMultiList = new MultiList();
        existingMultiList.setId(1L);
        MultiListDTO existingMultiListDTO = multiListMapper.multiListToMultiListDTO(existingMultiList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMultiListMockMvc.perform(post("/api/multi-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMultiListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MultiList> multiListList = multiListRepository.findAll();
        assertThat(multiListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMultiLists() throws Exception {
        // Initialize the database
        multiListRepository.saveAndFlush(multiList);

        // Get all the multiListList
        restMultiListMockMvc.perform(get("/api/multi-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(multiList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getMultiList() throws Exception {
        // Initialize the database
        multiListRepository.saveAndFlush(multiList);

        // Get the multiList
        restMultiListMockMvc.perform(get("/api/multi-lists/{id}", multiList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(multiList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMultiList() throws Exception {
        // Get the multiList
        restMultiListMockMvc.perform(get("/api/multi-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMultiList() throws Exception {
        // Initialize the database
        multiListRepository.saveAndFlush(multiList);
        int databaseSizeBeforeUpdate = multiListRepository.findAll().size();

        // Update the multiList
        MultiList updatedMultiList = multiListRepository.findOne(multiList.getId());
        updatedMultiList
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .enabled(UPDATED_ENABLED);
        MultiListDTO multiListDTO = multiListMapper.multiListToMultiListDTO(updatedMultiList);

        restMultiListMockMvc.perform(put("/api/multi-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(multiListDTO)))
            .andExpect(status().isOk());

        // Validate the MultiList in the database
        List<MultiList> multiListList = multiListRepository.findAll();
        assertThat(multiListList).hasSize(databaseSizeBeforeUpdate);
        MultiList testMultiList = multiListList.get(multiListList.size() - 1);
        assertThat(testMultiList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMultiList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMultiList.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingMultiList() throws Exception {
        int databaseSizeBeforeUpdate = multiListRepository.findAll().size();

        // Create the MultiList
        MultiListDTO multiListDTO = multiListMapper.multiListToMultiListDTO(multiList);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMultiListMockMvc.perform(put("/api/multi-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(multiListDTO)))
            .andExpect(status().isCreated());

        // Validate the MultiList in the database
        List<MultiList> multiListList = multiListRepository.findAll();
        assertThat(multiListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMultiList() throws Exception {
        // Initialize the database
        multiListRepository.saveAndFlush(multiList);
        int databaseSizeBeforeDelete = multiListRepository.findAll().size();

        // Get the multiList
        restMultiListMockMvc.perform(delete("/api/multi-lists/{id}", multiList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MultiList> multiListList = multiListRepository.findAll();
        assertThat(multiListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
