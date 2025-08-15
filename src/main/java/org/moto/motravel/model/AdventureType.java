package org.moto.motravel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "adventure_types")
public class AdventureType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Adventure type name is required")
    @Size(max = 100, message = "Adventure type name must not exceed 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "adventureTypes", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<HiddenGem> hiddenGems = new HashSet<>();

    // Constructors
    public AdventureType() {}

    public AdventureType(String name) {
        this.name = name;
    }

    // Getters and Setters
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

    public Set<HiddenGem> getHiddenGems() {
        return hiddenGems;
    }

    public void setHiddenGems(Set<HiddenGem> hiddenGems) {
        this.hiddenGems = hiddenGems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdventureType)) return false;
        AdventureType that = (AdventureType) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AdventureType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
