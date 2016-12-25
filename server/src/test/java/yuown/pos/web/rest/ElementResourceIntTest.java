package yuown.pos.web.rest;

import yuown.pos.YuPosApp;

import yuown.pos.domain.Element;
import yuown.pos.repository.ElementRepository;
import yuown.pos.service.ElementService;
import yuown.pos.service.dto.ElementDTO;
import yuown.pos.service.mapper.ElementMapper;

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
 * Test class for the ElementResource REST controller.
 *
 * @see ElementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YuPosApp.class)
public class ElementResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Inject
    private ElementRepository elementRepository;

    @Inject
    private ElementMapper elementMapper;

    @Inject
    private ElementService elementService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restElementMockMvc;

    private Element element;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ElementResource elementResource = new ElementResource();
        ReflectionTestUtils.setField(elementResource, "elementService", elementService);
        this.restElementMockMvc = MockMvcBuilders.standaloneSetup(elementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Element createEntity(EntityManager em) {
        Element element = new Element()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .enabled(DEFAULT_ENABLED);
        return element;
    }

    @Before
    public void initTest() {
        element = createEntity(em);
    }

    @Test
    @Transactional
    public void createElement() throws Exception {
        int databaseSizeBeforeCreate = elementRepository.findAll().size();

        // Create the Element
        ElementDTO elementDTO = elementMapper.elementToElementDTO(element);

        restElementMockMvc.perform(post("/api/elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(elementDTO)))
            .andExpect(status().isCreated());

        // Validate the Element in the database
        List<Element> elementList = elementRepository.findAll();
        assertThat(elementList).hasSize(databaseSizeBeforeCreate + 1);
        Element testElement = elementList.get(elementList.size() - 1);
        assertThat(testElement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testElement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testElement.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createElementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = elementRepository.findAll().size();

        // Create the Element with an existing ID
        Element existingElement = new Element();
        existingElement.setId(1L);
        ElementDTO existingElementDTO = elementMapper.elementToElementDTO(existingElement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restElementMockMvc.perform(post("/api/elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingElementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Element> elementList = elementRepository.findAll();
        assertThat(elementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllElements() throws Exception {
        // Initialize the database
        elementRepository.saveAndFlush(element);

        // Get all the elementList
        restElementMockMvc.perform(get("/api/elements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(element.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getElement() throws Exception {
        // Initialize the database
        elementRepository.saveAndFlush(element);

        // Get the element
        restElementMockMvc.perform(get("/api/elements/{id}", element.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(element.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingElement() throws Exception {
        // Get the element
        restElementMockMvc.perform(get("/api/elements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElement() throws Exception {
        // Initialize the database
        elementRepository.saveAndFlush(element);
        int databaseSizeBeforeUpdate = elementRepository.findAll().size();

        // Update the element
        Element updatedElement = elementRepository.findOne(element.getId());
        updatedElement
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .enabled(UPDATED_ENABLED);
        ElementDTO elementDTO = elementMapper.elementToElementDTO(updatedElement);

        restElementMockMvc.perform(put("/api/elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(elementDTO)))
            .andExpect(status().isOk());

        // Validate the Element in the database
        List<Element> elementList = elementRepository.findAll();
        assertThat(elementList).hasSize(databaseSizeBeforeUpdate);
        Element testElement = elementList.get(elementList.size() - 1);
        assertThat(testElement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testElement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testElement.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingElement() throws Exception {
        int databaseSizeBeforeUpdate = elementRepository.findAll().size();

        // Create the Element
        ElementDTO elementDTO = elementMapper.elementToElementDTO(element);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restElementMockMvc.perform(put("/api/elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(elementDTO)))
            .andExpect(status().isCreated());

        // Validate the Element in the database
        List<Element> elementList = elementRepository.findAll();
        assertThat(elementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteElement() throws Exception {
        // Initialize the database
        elementRepository.saveAndFlush(element);
        int databaseSizeBeforeDelete = elementRepository.findAll().size();

        // Get the element
        restElementMockMvc.perform(delete("/api/elements/{id}", element.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Element> elementList = elementRepository.findAll();
        assertThat(elementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
