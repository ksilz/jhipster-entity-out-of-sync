package com.betterprojectsfaster.opensource.jhipster.entityoutofsync.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.IntegrationTest;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.domain.Thing;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.repository.ThingRepository;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.dto.ThingDTO;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.mapper.ThingMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ThingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ThingResourceIT {

    private static final Boolean DEFAULT_ENABLE_SOMETHING = false;
    private static final Boolean UPDATED_ENABLE_SOMETHING = true;

    private static final String ENTITY_API_URL = "/api/things";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ThingRepository thingRepository;

    @Autowired
    private ThingMapper thingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restThingMockMvc;

    private Thing thing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thing createEntity(EntityManager em) {
        Thing thing = new Thing().enableSomething(DEFAULT_ENABLE_SOMETHING);
        return thing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thing createUpdatedEntity(EntityManager em) {
        Thing thing = new Thing().enableSomething(UPDATED_ENABLE_SOMETHING);
        return thing;
    }

    @BeforeEach
    public void initTest() {
        thing = createEntity(em);
    }

    @Test
    @Transactional
    void createThing() throws Exception {
        int databaseSizeBeforeCreate = thingRepository.findAll().size();
        // Create the Thing
        ThingDTO thingDTO = thingMapper.toDto(thing);
        restThingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(thingDTO)))
            .andExpect(status().isCreated());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeCreate + 1);
        Thing testThing = thingList.get(thingList.size() - 1);
        assertThat(testThing.getEnableSomething()).isEqualTo(DEFAULT_ENABLE_SOMETHING);
    }

    @Test
    @Transactional
    void createThingWithExistingId() throws Exception {
        // Create the Thing with an existing ID
        thing.setId(1L);
        ThingDTO thingDTO = thingMapper.toDto(thing);

        int databaseSizeBeforeCreate = thingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restThingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(thingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnableSomethingIsRequired() throws Exception {
        int databaseSizeBeforeTest = thingRepository.findAll().size();
        // set the field null
        thing.setEnableSomething(null);

        // Create the Thing, which fails.
        ThingDTO thingDTO = thingMapper.toDto(thing);

        restThingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(thingDTO)))
            .andExpect(status().isBadRequest());

        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllThings() throws Exception {
        // Initialize the database
        thingRepository.saveAndFlush(thing);

        // Get all the thingList
        restThingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thing.getId().intValue())))
            .andExpect(jsonPath("$.[*].enableSomething").value(hasItem(DEFAULT_ENABLE_SOMETHING.booleanValue())));
    }

    @Test
    @Transactional
    void getThing() throws Exception {
        // Initialize the database
        thingRepository.saveAndFlush(thing);

        // Get the thing
        restThingMockMvc
            .perform(get(ENTITY_API_URL_ID, thing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(thing.getId().intValue()))
            .andExpect(jsonPath("$.enableSomething").value(DEFAULT_ENABLE_SOMETHING.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingThing() throws Exception {
        // Get the thing
        restThingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewThing() throws Exception {
        // Initialize the database
        thingRepository.saveAndFlush(thing);

        int databaseSizeBeforeUpdate = thingRepository.findAll().size();

        // Update the thing
        Thing updatedThing = thingRepository.findById(thing.getId()).get();
        // Disconnect from session so that the updates on updatedThing are not directly saved in db
        em.detach(updatedThing);
        updatedThing.enableSomething(UPDATED_ENABLE_SOMETHING);
        ThingDTO thingDTO = thingMapper.toDto(updatedThing);

        restThingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, thingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(thingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeUpdate);
        Thing testThing = thingList.get(thingList.size() - 1);
        assertThat(testThing.getEnableSomething()).isEqualTo(UPDATED_ENABLE_SOMETHING);
    }

    @Test
    @Transactional
    void putNonExistingThing() throws Exception {
        int databaseSizeBeforeUpdate = thingRepository.findAll().size();
        thing.setId(count.incrementAndGet());

        // Create the Thing
        ThingDTO thingDTO = thingMapper.toDto(thing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, thingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(thingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchThing() throws Exception {
        int databaseSizeBeforeUpdate = thingRepository.findAll().size();
        thing.setId(count.incrementAndGet());

        // Create the Thing
        ThingDTO thingDTO = thingMapper.toDto(thing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(thingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamThing() throws Exception {
        int databaseSizeBeforeUpdate = thingRepository.findAll().size();
        thing.setId(count.incrementAndGet());

        // Create the Thing
        ThingDTO thingDTO = thingMapper.toDto(thing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(thingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateThingWithPatch() throws Exception {
        // Initialize the database
        thingRepository.saveAndFlush(thing);

        int databaseSizeBeforeUpdate = thingRepository.findAll().size();

        // Update the thing using partial update
        Thing partialUpdatedThing = new Thing();
        partialUpdatedThing.setId(thing.getId());

        restThingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThing))
            )
            .andExpect(status().isOk());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeUpdate);
        Thing testThing = thingList.get(thingList.size() - 1);
        assertThat(testThing.getEnableSomething()).isEqualTo(DEFAULT_ENABLE_SOMETHING);
    }

    @Test
    @Transactional
    void fullUpdateThingWithPatch() throws Exception {
        // Initialize the database
        thingRepository.saveAndFlush(thing);

        int databaseSizeBeforeUpdate = thingRepository.findAll().size();

        // Update the thing using partial update
        Thing partialUpdatedThing = new Thing();
        partialUpdatedThing.setId(thing.getId());

        partialUpdatedThing.enableSomething(UPDATED_ENABLE_SOMETHING);

        restThingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedThing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedThing))
            )
            .andExpect(status().isOk());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeUpdate);
        Thing testThing = thingList.get(thingList.size() - 1);
        assertThat(testThing.getEnableSomething()).isEqualTo(UPDATED_ENABLE_SOMETHING);
    }

    @Test
    @Transactional
    void patchNonExistingThing() throws Exception {
        int databaseSizeBeforeUpdate = thingRepository.findAll().size();
        thing.setId(count.incrementAndGet());

        // Create the Thing
        ThingDTO thingDTO = thingMapper.toDto(thing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, thingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(thingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchThing() throws Exception {
        int databaseSizeBeforeUpdate = thingRepository.findAll().size();
        thing.setId(count.incrementAndGet());

        // Create the Thing
        ThingDTO thingDTO = thingMapper.toDto(thing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(thingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamThing() throws Exception {
        int databaseSizeBeforeUpdate = thingRepository.findAll().size();
        thing.setId(count.incrementAndGet());

        // Create the Thing
        ThingDTO thingDTO = thingMapper.toDto(thing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restThingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(thingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Thing in the database
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteThing() throws Exception {
        // Initialize the database
        thingRepository.saveAndFlush(thing);

        int databaseSizeBeforeDelete = thingRepository.findAll().size();

        // Delete the thing
        restThingMockMvc
            .perform(delete(ENTITY_API_URL_ID, thing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Thing> thingList = thingRepository.findAll();
        assertThat(thingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
