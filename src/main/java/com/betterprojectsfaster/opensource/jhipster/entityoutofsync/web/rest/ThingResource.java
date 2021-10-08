package com.betterprojectsfaster.opensource.jhipster.entityoutofsync.web.rest;

import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.repository.ThingRepository;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.ThingService;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.dto.ThingDTO;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.betterprojectsfaster.opensource.jhipster.entityoutofsync.domain.Thing}.
 */
@RestController
@RequestMapping("/api")
public class ThingResource {

    private final Logger log = LoggerFactory.getLogger(ThingResource.class);

    private static final String ENTITY_NAME = "thing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ThingService thingService;

    private final ThingRepository thingRepository;

    public ThingResource(ThingService thingService, ThingRepository thingRepository) {
        this.thingService = thingService;
        this.thingRepository = thingRepository;
    }

    /**
     * {@code POST  /things} : Create a new thing.
     *
     * @param thingDTO the thingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new thingDTO, or with status {@code 400 (Bad Request)} if the thing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/things")
    public ResponseEntity<ThingDTO> createThing(@Valid @RequestBody ThingDTO thingDTO) throws URISyntaxException {
        log.debug("REST request to save Thing : {}", thingDTO);
        if (thingDTO.getId() != null) {
            throw new BadRequestAlertException("A new thing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ThingDTO result = thingService.save(thingDTO);
        return ResponseEntity
            .created(new URI("/api/things/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /things/:id} : Updates an existing thing.
     *
     * @param id the id of the thingDTO to save.
     * @param thingDTO the thingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated thingDTO,
     * or with status {@code 400 (Bad Request)} if the thingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the thingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/things/{id}")
    public ResponseEntity<ThingDTO> updateThing(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ThingDTO thingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Thing : {}, {}", id, thingDTO);
        if (thingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, thingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!thingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ThingDTO result = thingService.save(thingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, thingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /things/:id} : Partial updates given fields of an existing thing, field will ignore if it is null
     *
     * @param id the id of the thingDTO to save.
     * @param thingDTO the thingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated thingDTO,
     * or with status {@code 400 (Bad Request)} if the thingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the thingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the thingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/things/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ThingDTO> partialUpdateThing(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ThingDTO thingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Thing partially : {}, {}", id, thingDTO);
        if (thingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, thingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!thingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ThingDTO> result = thingService.partialUpdate(thingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, thingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /things} : get all the things.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of things in body.
     */
    @GetMapping("/things")
    public ResponseEntity<List<ThingDTO>> getAllThings(Pageable pageable) {
        log.debug("REST request to get a page of Things");
        Page<ThingDTO> page = thingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /things/:id} : get the "id" thing.
     *
     * @param id the id of the thingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the thingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/things/{id}")
    public ResponseEntity<ThingDTO> getThing(@PathVariable Long id) {
        log.debug("REST request to get Thing : {}", id);
        Optional<ThingDTO> thingDTO = thingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(thingDTO);
    }

    /**
     * {@code DELETE  /things/:id} : delete the "id" thing.
     *
     * @param id the id of the thingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/things/{id}")
    public ResponseEntity<Void> deleteThing(@PathVariable Long id) {
        log.debug("REST request to delete Thing : {}", id);
        thingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
