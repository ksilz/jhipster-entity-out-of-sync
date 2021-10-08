package com.betterprojectsfaster.opensource.jhipster.entityoutofsync.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Thing.
 */
@Entity
@Table(name = "thing")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Thing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "enable_something", nullable = false)
    private Boolean enableSomething;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Thing id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnableSomething() {
        return this.enableSomething;
    }

    public Thing enableSomething(Boolean enableSomething) {
        this.setEnableSomething(enableSomething);
        return this;
    }

    public void setEnableSomething(Boolean enableSomething) {
        this.enableSomething = enableSomething;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Thing)) {
            return false;
        }
        return id != null && id.equals(((Thing) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Thing{" +
            "id=" + getId() +
            ", enableSomething='" + getEnableSomething() + "'" +
            "}";
    }
}
