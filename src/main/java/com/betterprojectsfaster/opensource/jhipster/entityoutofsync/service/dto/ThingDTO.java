package com.betterprojectsfaster.opensource.jhipster.entityoutofsync.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.betterprojectsfaster.opensource.jhipster.entityoutofsync.domain.Thing} entity.
 */
public class ThingDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean enableSomething;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnableSomething() {
        return enableSomething;
    }

    public void setEnableSomething(Boolean enableSomething) {
        this.enableSomething = enableSomething;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThingDTO)) {
            return false;
        }

        ThingDTO thingDTO = (ThingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, thingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThingDTO{" +
            "id=" + getId() +
            ", enableSomething='" + getEnableSomething() + "'" +
            "}";
    }
}
