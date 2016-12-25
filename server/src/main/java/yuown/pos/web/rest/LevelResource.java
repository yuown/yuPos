package yuown.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import yuown.pos.service.LevelService;
import yuown.pos.web.rest.util.HeaderUtil;
import yuown.pos.web.rest.util.PaginationUtil;
import yuown.pos.service.dto.LevelDTO;

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
 * REST controller for managing Level.
 */
@RestController
@RequestMapping("/api")
public class LevelResource {

    private final Logger log = LoggerFactory.getLogger(LevelResource.class);
        
    @Inject
    private LevelService levelService;

    /**
     * POST  /levels : Create a new level.
     *
     * @param levelDTO the levelDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new levelDTO, or with status 400 (Bad Request) if the level has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/levels")
    @Timed
    public ResponseEntity<LevelDTO> createLevel(@RequestBody LevelDTO levelDTO) throws URISyntaxException {
        log.debug("REST request to save Level : {}", levelDTO);
        if (levelDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("level", "idexists", "A new level cannot already have an ID")).body(null);
        }
        LevelDTO result = levelService.save(levelDTO);
        return ResponseEntity.created(new URI("/api/levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("level", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /levels : Updates an existing level.
     *
     * @param levelDTO the levelDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated levelDTO,
     * or with status 400 (Bad Request) if the levelDTO is not valid,
     * or with status 500 (Internal Server Error) if the levelDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/levels")
    @Timed
    public ResponseEntity<LevelDTO> updateLevel(@RequestBody LevelDTO levelDTO) throws URISyntaxException {
        log.debug("REST request to update Level : {}", levelDTO);
        if (levelDTO.getId() == null) {
            return createLevel(levelDTO);
        }
        LevelDTO result = levelService.save(levelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("level", levelDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /levels : get all the levels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of levels in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/levels")
    @Timed
    public ResponseEntity<List<LevelDTO>> getAllLevels(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Levels");
        Page<LevelDTO> page = levelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/levels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /levels/:id : get the "id" level.
     *
     * @param id the id of the levelDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the levelDTO, or with status 404 (Not Found)
     */
    @GetMapping("/levels/{id}")
    @Timed
    public ResponseEntity<LevelDTO> getLevel(@PathVariable Long id) {
        log.debug("REST request to get Level : {}", id);
        LevelDTO levelDTO = levelService.findOne(id);
        return Optional.ofNullable(levelDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /levels/:id : delete the "id" level.
     *
     * @param id the id of the levelDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/levels/{id}")
    @Timed
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        log.debug("REST request to delete Level : {}", id);
        levelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("level", id.toString())).build();
    }

}
