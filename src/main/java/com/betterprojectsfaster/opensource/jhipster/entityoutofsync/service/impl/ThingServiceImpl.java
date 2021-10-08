package com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.impl;

import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.domain.Thing;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.repository.ThingRepository;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.ThingService;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.dto.ThingDTO;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.mapper.ThingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Thing}.
 */
@Service
@Transactional
public class ThingServiceImpl implements ThingService {

    private final Logger log = LoggerFactory.getLogger(ThingServiceImpl.class);

    private final ThingRepository thingRepository;

    private final ThingMapper thingMapper;

    public ThingServiceImpl(ThingRepository thingRepository, ThingMapper thingMapper) {
        this.thingRepository = thingRepository;
        this.thingMapper = thingMapper;
    }

    @Override
    public ThingDTO save(ThingDTO thingDTO) {
        log.debug("Request to save Thing : {}", thingDTO);
        Thing thing = thingMapper.toEntity(thingDTO);
        thing = thingRepository.save(thing);
        return thingMapper.toDto(thing);
    }

    @Override
    public Optional<ThingDTO> partialUpdate(ThingDTO thingDTO) {
        log.debug("Request to partially update Thing : {}", thingDTO);

        return thingRepository
            .findById(thingDTO.getId())
            .map(existingThing -> {
                thingMapper.partialUpdate(existingThing, thingDTO);

                return existingThing;
            })
            .map(thingRepository::save)
            .map(thingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Things");
        return thingRepository.findAll(pageable).map(thingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThingDTO> findOne(Long id) {
        log.debug("Request to get Thing : {}", id);
        return thingRepository.findById(id).map(thingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Thing : {}", id);
        thingRepository.deleteById(id);
    }
}
