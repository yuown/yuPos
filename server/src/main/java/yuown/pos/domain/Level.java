package yuown.pos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Level.
 */
@Entity
@Table(name = "level")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(mappedBy = "level")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ListLevel> levelLists = new HashSet<>();

    @OneToMany(mappedBy = "level")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LevelElement> elementLevels = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Level name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Level description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Level enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<ListLevel> getLevelLists() {
        return levelLists;
    }

    public Level levelLists(Set<ListLevel> listLevels) {
        this.levelLists = listLevels;
        return this;
    }

    public Level addLevelLists(ListLevel listLevel) {
        levelLists.add(listLevel);
        listLevel.setLevel(this);
        return this;
    }

    public Level removeLevelLists(ListLevel listLevel) {
        levelLists.remove(listLevel);
        listLevel.setLevel(null);
        return this;
    }

    public void setLevelLists(Set<ListLevel> listLevels) {
        this.levelLists = listLevels;
    }

    public Set<LevelElement> getElementLevels() {
        return elementLevels;
    }

    public Level elementLevels(Set<LevelElement> levelElements) {
        this.elementLevels = levelElements;
        return this;
    }

    public Level addElementLevels(LevelElement levelElement) {
        elementLevels.add(levelElement);
        levelElement.setLevel(this);
        return this;
    }

    public Level removeElementLevels(LevelElement levelElement) {
        elementLevels.remove(levelElement);
        levelElement.setLevel(null);
        return this;
    }

    public void setElementLevels(Set<LevelElement> levelElements) {
        this.elementLevels = levelElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Level level = (Level) o;
        if (level.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, level.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Level{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", enabled='" + enabled + "'" +
            '}';
    }
}
