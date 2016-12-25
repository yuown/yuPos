package yuown.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import yuown.pos.service.ElementService;
import yuown.pos.web.rest.util.HeaderUtil;
import yuown.pos.web.rest.util.PaginationUtil;
import yuown.pos.service.dto.ElementDTO;

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
 * REST controller for managing Element.
 */
@RestController
@RequestMapping("/api")
public class ElementResource {

    private final Logger log = LoggerFactory.getLogger(ElementResource.class);
        
    @Inject
    private ElementService elementService;

    /**
     * POST  /elements : Create a new element.
     *
     * @param elementDTO the elementDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new elementDTO, or with status 400 (Bad Request) if the element has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/elements")
    @Timed
    public ResponseEntity<ElementDTO> createElement(@RequestBody ElementDTO elementDTO) throws URISyntaxException {
        log.debug("REST request to save Element : {}", elementDTO);
        if (elementDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("element", "idexists", "A new element cannot already have an ID")).body(null);
        }
        ElementDTO result = elementService.save(elementDTO);
        return ResponseEntity.created(new URI("/api/elements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("element", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /elements : Updates an existing element.
     *
     * @param elementDTO the elementDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated elementDTO,
     * or with status 400 (Bad Request) if the elementDTO is not valid,
     * or with status 500 (Internal Server Error) if the elementDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/elements")
    @Timed
    public ResponseEntity<ElementDTO> updateElement(@RequestBody ElementDTO elementDTO) throws URISyntaxException {
        log.debug("REST request to update Element : {}", elementDTO);
        if (elementDTO.getId() == null) {
            return createElement(elementDTO);
        }
        ElementDTO result = elementService.save(elementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("element", elementDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /elements : get all the elements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of elements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/elements")
    @Timed
    public ResponseEntity<List<ElementDTO>> getAllElements(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Elements");
        Page<ElementDTO> page = elementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/elements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /elements/:id : get the "id" element.
     *
     * @param id the id of the elementDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the elementDTO, or with status 404 (Not Found)
     */
    @GetMapping("/elements/{id}")
    @Timed
    public ResponseEntity<ElementDTO> getElement(@PathVariable Long id) {
        log.debug("REST request to get Element : {}", id);
        ElementDTO elementDTO = elementService.findOne(id);
        return Optional.ofNullable(elementDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /elements/:id : delete the "id" element.
     *
     * @param id the id of the elementDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/elements/{id}")
    @Timed
    public ResponseEntity<Void> deleteElement(@PathVariable Long id) {
        log.debug("REST request to delete Element : {}", id);
        elementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("element", id.toString())).build();
    }

}
