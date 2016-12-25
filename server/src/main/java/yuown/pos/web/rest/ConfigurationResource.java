package yuown.pos.web.rest;

import com.codahale.metrics.annotation.Timed;
import yuown.pos.service.ConfigurationService;
import yuown.pos.web.rest.util.HeaderUtil;
import yuown.pos.web.rest.util.PaginationUtil;
import yuown.pos.service.dto.ConfigurationDTO;

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
 * REST controller for managing Configuration.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationResource.class);
        
    @Inject
    private ConfigurationService configurationService;

    /**
     * POST  /configurations : Create a new configuration.
     *
     * @param configurationDTO the configurationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configurationDTO, or with status 400 (Bad Request) if the configuration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/configurations")
    @Timed
    public ResponseEntity<ConfigurationDTO> createConfiguration(@RequestBody ConfigurationDTO configurationDTO) throws URISyntaxException {
        log.debug("REST request to save Configuration : {}", configurationDTO);
        if (configurationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("configuration", "idexists", "A new configuration cannot already have an ID")).body(null);
        }
        ConfigurationDTO result = configurationService.save(configurationDTO);
        return ResponseEntity.created(new URI("/api/configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("configuration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /configurations : Updates an existing configuration.
     *
     * @param configurationDTO the configurationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configurationDTO,
     * or with status 400 (Bad Request) if the configurationDTO is not valid,
     * or with status 500 (Internal Server Error) if the configurationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/configurations")
    @Timed
    public ResponseEntity<ConfigurationDTO> updateConfiguration(@RequestBody ConfigurationDTO configurationDTO) throws URISyntaxException {
        log.debug("REST request to update Configuration : {}", configurationDTO);
        if (configurationDTO.getId() == null) {
            return createConfiguration(configurationDTO);
        }
        ConfigurationDTO result = configurationService.save(configurationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("configuration", configurationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /configurations : get all the configurations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of configurations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/configurations")
    @Timed
    public ResponseEntity<List<ConfigurationDTO>> getAllConfigurations(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Configurations");
        Page<ConfigurationDTO> page = configurationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/configurations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /configurations/:id : get the "id" configuration.
     *
     * @param id the id of the configurationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configurationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/configurations/{id}")
    @Timed
    public ResponseEntity<ConfigurationDTO> getConfiguration(@PathVariable Long id) {
        log.debug("REST request to get Configuration : {}", id);
        ConfigurationDTO configurationDTO = configurationService.findOne(id);
        return Optional.ofNullable(configurationDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /configurations/:id : delete the "id" configuration.
     *
     * @param id the id of the configurationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/configurations/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete Configuration : {}", id);
        configurationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("configuration", id.toString())).build();
    }

}
