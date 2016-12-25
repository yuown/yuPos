package yuown.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import yuown.pos.service.LevelElementService;
import yuown.pos.web.rest.util.HeaderUtil;
import yuown.pos.web.rest.util.PaginationUtil;
import yuown.pos.service.dto.LevelElementDTO;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing LevelElement.
 */
@RestController
@RequestMapping("/api")
public class LevelElementResource {

    private final Logger log = LoggerFactory.getLogger(LevelElementResource.class);
        
    @Inject
    private LevelElementService levelElementService;

    /**
     * POST  /level-elements : Create a new levelElement.
     *
     * @param levelElementDTO the levelElementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new levelElementDTO, or with status 400 (Bad Request) if the levelElement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/level-elements")
    @Timed
    public ResponseEntity<LevelElementDTO> createLevelElement(@RequestBody LevelElementDTO levelElementDTO) throws URISyntaxException {
        log.debug("REST request to save LevelElement : {}", levelElementDTO);
        if (levelElementDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("levelElement", "idexists", "A new levelElement cannot already have an ID")).body(null);
        }
        LevelElementDTO result = levelElementService.save(levelElementDTO);
        return ResponseEntity.created(new URI("/api/level-elements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("levelElement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /level-elements : Updates an existing levelElement.
     *
     * @param levelElementDTO the levelElementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated levelElementDTO,
     * or with status 400 (Bad Request) if the levelElementDTO is not valid,
     * or with status 500 (Internal Server Error) if the levelElementDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/level-elements")
    @Timed
    public ResponseEntity<LevelElementDTO> updateLevelElement(@RequestBody LevelElementDTO levelElementDTO) throws URISyntaxException {
        log.debug("REST request to update LevelElement : {}", levelElementDTO);
        if (levelElementDTO.getId() == null) {
            return createLevelElement(levelElementDTO);
        }
        LevelElementDTO result = levelElementService.save(levelElementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("levelElement", levelElementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /level-elements : get all the levelElements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of levelElements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/level-elements")
    @Timed
    public ResponseEntity<List<LevelElementDTO>> getAllLevelElements(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LevelElements");
        Page<LevelElementDTO> page = levelElementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/level-elements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /level-elements/:id : get the "id" levelElement.
     *
     * @param id the id of the levelElementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the levelElementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/level-elements/{id}")
    @Timed
    public ResponseEntity<LevelElementDTO> getLevelElement(@PathVariable Long id) {
        log.debug("REST request to get LevelElement : {}", id);
        LevelElementDTO levelElementDTO = levelElementService.findOne(id);
        return Optional.ofNullable(levelElementDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /level-elements/:id : delete the "id" levelElement.
     *
     * @param id the id of the levelElementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/level-elements/{id}")
    @Timed
    public ResponseEntity<Void> deleteLevelElement(@PathVariable Long id) {
        log.debug("REST request to delete LevelElement : {}", id);
        levelElementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("levelElement", id.toString())).build();
    }

}
