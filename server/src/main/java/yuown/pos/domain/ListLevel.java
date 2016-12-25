package yuown.pos.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ListLevel.
 */
@Entity
@Table(name = "list_level")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ListLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    private MultiList list;

    @ManyToOne
    private Level level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public ListLevel rank(Integer rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Boolean isActive() {
        return active;
    }

    public ListLevel active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public MultiList getList() {
        return list;
    }

    public ListLevel list(MultiList multiList) {
        this.list = multiList;
        return this;
    }

    public void setList(MultiList multiList) {
        this.list = multiList;
    }

    public Level getLevel() {
        return level;
    }

    public ListLevel level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListLevel listLevel = (ListLevel) o;
        if (listLevel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, listLevel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ListLevel{" +
            "id=" + id +
            ", rank='" + rank + "'" +
            ", active='" + active + "'" +
            '}';
    }
}
