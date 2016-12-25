package yuown.pos.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the LevelElement entity.
 */
public class LevelElementDTO implements Serializable {

    private Long id;

    private Integer rank;

    private Boolean active;


    private Long levelId;
    
    private Long elementId;
    
    private Long parentElementId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getElementId() {
        return elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    public Long getParentElementId() {
        return parentElementId;
    }

    public void setParentElementId(Long elementId) {
        this.parentElementId = elementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LevelElementDTO levelElementDTO = (LevelElementDTO) o;

        if ( ! Objects.equals(id, levelElementDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LevelElementDTO{" +
            "id=" + id +
            ", rank='" + rank + "'" +
            ", active='" + active + "'" +
            '}';
    }
}
