package yuown.pos.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LevelElement.
 */
@Entity
@Table(name = "level_element")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LevelElement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    private Level level;

    @ManyToOne
    private Element element;

    @ManyToOne
    private Element parentElement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public LevelElement rank(Integer rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Boolean isActive() {
        return active;
    }

    public LevelElement active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Level getLevel() {
        return level;
    }

    public LevelElement level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Element getElement() {
        return element;
    }

    public LevelElement element(Element element) {
        this.element = element;
        return this;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Element getParentElement() {
        return parentElement;
    }

    public LevelElement parentElement(Element element) {
        this.parentElement = element;
        return this;
    }

    public void setParentElement(Element element) {
        this.parentElement = element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LevelElement levelElement = (LevelElement) o;
        if (levelElement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, levelElement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LevelElement{" +
            "id=" + id +
            ", rank='" + rank + "'" +
            ", active='" + active + "'" +
            '}';
    }
}
