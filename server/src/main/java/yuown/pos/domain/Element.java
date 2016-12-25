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
 * A Element.
 */
@Entity
@Table(name = "element")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Element implements Serializable {

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

    @OneToMany(mappedBy = "element")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LevelElement> elements = new HashSet<>();

    @OneToMany(mappedBy = "parentElement")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LevelElement> parents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Element name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Element description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Element enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<LevelElement> getElements() {
        return elements;
    }

    public Element elements(Set<LevelElement> levelElements) {
        this.elements = levelElements;
        return this;
    }

    public Element addElements(LevelElement levelElement) {
        elements.add(levelElement);
        levelElement.setElement(this);
        return this;
    }

    public Element removeElements(LevelElement levelElement) {
        elements.remove(levelElement);
        levelElement.setElement(null);
        return this;
    }

    public void setElements(Set<LevelElement> levelElements) {
        this.elements = levelElements;
    }

    public Set<LevelElement> getParents() {
        return parents;
    }

    public Element parents(Set<LevelElement> levelElements) {
        this.parents = levelElements;
        return this;
    }

    public Element addParents(LevelElement levelElement) {
        parents.add(levelElement);
        levelElement.setParentElement(this);
        return this;
    }

    public Element removeParents(LevelElement levelElement) {
        parents.remove(levelElement);
        levelElement.setParentElement(null);
        return this;
    }

    public void setParents(Set<LevelElement> levelElements) {
        this.parents = levelElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Element element = (Element) o;
        if (element.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, element.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Element{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", enabled='" + enabled + "'" +
            '}';
    }
}
