package yuown.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import yuown.pos.service.ListLevelService;
import yuown.pos.web.rest.util.HeaderUtil;
import yuown.pos.web.rest.util.PaginationUtil;
import yuown.pos.service.dto.ListLevelDTO;

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
 * REST controller for managing ListLevel.
 */
@RestController
@RequestMapping("/api")
public class ListLevelResource {

    private final Logger log = LoggerFactory.getLogger(ListLevelResource.class);
        
    @Inject
    private ListLevelService listLevelService;

    /**
     * POST  /list-levels : Create a new listLevel.
     *
     * @param listLevelDTO the listLevelDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new listLevelDTO, or with status 400 (Bad Request) if the listLevel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/list-levels")
    @Timed
    public ResponseEntity<ListLevelDTO> createListLevel(@RequestBody ListLevelDTO listLevelDTO) throws URISyntaxException {
        log.debug("REST request to save ListLevel : {}", listLevelDTO);
        if (listLevelDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("listLevel", "idexists", "A new listLevel cannot already have an ID")).body(null);
        }
        ListLevelDTO result = listLevelService.save(listLevelDTO);
        return ResponseEntity.created(new URI("/api/list-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("listLevel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /list-levels : Updates an existing listLevel.
     *
     * @param listLevelDTO the listLevelDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated listLevelDTO,
     * or with status 400 (Bad Request) if the listLevelDTO is not valid,
     * or with status 500 (Internal Server Error) if the listLevelDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/list-levels")
    @Timed
    public ResponseEntity<ListLevelDTO> updateListLevel(@RequestBody ListLevelDTO listLevelDTO) throws URISyntaxException {
        log.debug("REST request to update ListLevel : {}", listLevelDTO);
        if (listLevelDTO.getId() == null) {
            return createListLevel(listLevelDTO);
        }
        ListLevelDTO result = listLevelService.save(listLevelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("listLevel", listLevelDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /list-levels : get all the listLevels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of listLevels in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/list-levels")
    @Timed
    public ResponseEntity<List<ListLevelDTO>> getAllListLevels(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ListLevels");
        Page<ListLevelDTO> page = listLevelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/list-levels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /list-levels/:id : get the "id" listLevel.
     *
     * @param id the id of the listLevelDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the listLevelDTO, or with status 404 (Not Found)
     */
    @GetMapping("/list-levels/{id}")
    @Timed
    public ResponseEntity<ListLevelDTO> getListLevel(@PathVariable Long id) {
        log.debug("REST request to get ListLevel : {}", id);
        ListLevelDTO listLevelDTO = listLevelService.findOne(id);
        return Optional.ofNullable(listLevelDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /list-levels/:id : delete the "id" listLevel.
     *
     * @param id the id of the listLevelDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/list-levels/{id}")
    @Timed
    public ResponseEntity<Void> deleteListLevel(@PathVariable Long id) {
        log.debug("REST request to delete ListLevel : {}", id);
        listLevelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("listLevel", id.toString())).build();
    }

}
