package yuown.pos.service.dto;

import java.io.Serializable;
import java.util.Objects;

import yuown.pos.domain.enumeration.DataType;

/**
 * A DTO for the Configuration entity.
 */
public class ConfigurationDTO implements Serializable {

    private Long id;

    private String name;

    private String value;

    private Boolean cached;

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

    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public Boolean getCached() {
        return cached;
    }

    public void setCached(Boolean cached) {
        this.cached = cached;
    }
    public DataType getType() {
        return type;
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

        ConfigurationDTO configurationDTO = (ConfigurationDTO) o;

        if ( ! Objects.equals(id, configurationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ConfigurationDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", value='" + value + "'" +
            ", cached='" + cached + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
