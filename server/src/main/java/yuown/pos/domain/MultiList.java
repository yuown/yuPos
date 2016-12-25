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
 * A MultiList.
 */
@Entity
@Table(name = "multi_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MultiList implements Serializable {

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

    @OneToMany(mappedBy = "list")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ListLevel> lists = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MultiList name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public MultiList description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public MultiList enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<ListLevel> getLists() {
        return lists;
    }

    public MultiList lists(Set<ListLevel> listLevels) {
        this.lists = listLevels;
        return this;
    }

    public MultiList addLists(ListLevel listLevel) {
        lists.add(listLevel);
        listLevel.setList(this);
        return this;
    }

    public MultiList removeLists(ListLevel listLevel) {
        lists.remove(listLevel);
        listLevel.setList(null);
        return this;
    }

    public void setLists(Set<ListLevel> listLevels) {
        this.lists = listLevels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MultiList multiList = (MultiList) o;
        if (multiList.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, multiList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MultiList{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", enabled='" + enabled + "'" +
            '}';
    }
}
