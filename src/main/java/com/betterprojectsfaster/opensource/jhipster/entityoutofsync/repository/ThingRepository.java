package com.betterprojectsfaster.opensource.jhipster.entityoutofsync.repository;

import com.betterprojectsfaster.opensource.jhipster.entityoutofsync.domain.Thing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Thing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ThingRepository extends JpaRepository<Thing, Long> {}
