package yuown.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import yuown.pos.service.MultiListService;
import yuown.pos.web.rest.util.HeaderUtil;
import yuown.pos.web.rest.util.PaginationUtil;
import yuown.pos.service.dto.MultiListDTO;

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
 * REST controller for managing MultiList.
 */
@RestController
@RequestMapping("/api")
public class MultiListResource {

    private final Logger log = LoggerFactory.getLogger(MultiListResource.class);
        
    @Inject
    private MultiListService multiListService;

    /**
     * POST  /multi-lists : Create a new multiList.
     *
     * @param multiListDTO the multiListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new multiListDTO, or with status 400 (Bad Request) if the multiList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/multi-lists")
    @Timed
    public ResponseEntity<MultiListDTO> createMultiList(@RequestBody MultiListDTO multiListDTO) throws URISyntaxException {
        log.debug("REST request to save MultiList : {}", multiListDTO);
        if (multiListDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("multiList", "idexists", "A new multiList cannot already have an ID")).body(null);
        }
        MultiListDTO result = multiListService.save(multiListDTO);
        return ResponseEntity.created(new URI("/api/multi-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("multiList", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /multi-lists : Updates an existing multiList.
     *
     * @param multiListDTO the multiListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated multiListDTO,
     * or with status 400 (Bad Request) if the multiListDTO is not valid,
     * or with status 500 (Internal Server Error) if the multiListDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/multi-lists")
    @Timed
    public ResponseEntity<MultiListDTO> updateMultiList(@RequestBody MultiListDTO multiListDTO) throws URISyntaxException {
        log.debug("REST request to update MultiList : {}", multiListDTO);
        if (multiListDTO.getId() == null) {
            return createMultiList(multiListDTO);
        }
        MultiListDTO result = multiListService.save(multiListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("multiList", multiListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /multi-lists : get all the multiLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of multiLists in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/multi-lists")
    @Timed
    public ResponseEntity<List<MultiListDTO>> getAllMultiLists(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MultiLists");
        Page<MultiListDTO> page = multiListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/multi-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /multi-lists/:id : get the "id" multiList.
     *
     * @param id the id of the multiListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the multiListDTO, or with status 404 (Not Found)
     */
    @GetMapping("/multi-lists/{id}")
    @Timed
    public ResponseEntity<MultiListDTO> getMultiList(@PathVariable Long id) {
        log.debug("REST request to get MultiList : {}", id);
        MultiListDTO multiListDTO = multiListService.findOne(id);
        return Optional.ofNullable(multiListDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /multi-lists/:id : delete the "id" multiList.
     *
     * @param id the id of the multiListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/multi-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteMultiList(@PathVariable Long id) {
        log.debug("REST request to delete MultiList : {}", id);
        multiListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("multiList", id.toString())).build();
    }

}
