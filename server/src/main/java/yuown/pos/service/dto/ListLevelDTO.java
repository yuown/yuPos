package yuown.pos.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ListLevel entity.
 */
public class ListLevelDTO implements Serializable {

    private Long id;

    private Integer rank;

    private Boolean active;


    private Long listId;
    
    private Long levelId;
    
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

    public Long getListId() {
        return listId;
    }

    public void setListId(Long multiListId) {
        this.listId = multiListId;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ListLevelDTO listLevelDTO = (ListLevelDTO) o;

        if ( ! Objects.equals(id, listLevelDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ListLevelDTO{" +
            "id=" + id +
            ", rank='" + rank + "'" +
            ", active='" + active + "'" +
            '}';
    }
}
