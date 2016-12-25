package yuown.pos.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import yuown.pos.domain.enumeration.DataType;

/**
 * A Configuration.
 */
@Entity
@Table(name = "configuration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "cached")
    private Boolean cached;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DataType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Configuration name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public Configuration value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean isCached() {
        return cached;
    }

    public Configuration cached(Boolean cached) {
        this.cached = cached;
        return this;
    }

    public void setCached(Boolean cached) {
        this.cached = cached;
    }

    public DataType getType() {
        return type;
    }

    public Configuration type(DataType type) {
        this.type = type;
        return this;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Configuration configuration = (Configuration) o;
        if (configuration.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, configuration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Configuration{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", value='" + value + "'" +
            ", cached='" + cached + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
