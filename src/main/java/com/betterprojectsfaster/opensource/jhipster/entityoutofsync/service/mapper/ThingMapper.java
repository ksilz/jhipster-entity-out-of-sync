package com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.mapper;

import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.domain.*;
import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.dto.ThingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Thing} and its DTO {@link ThingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ThingMapper extends EntityMapper<ThingDTO, Thing> {}
