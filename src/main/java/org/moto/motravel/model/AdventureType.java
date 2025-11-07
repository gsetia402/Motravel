package org.moto.motravel.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "adventure_types")
public class AdventureType {
    @Id
    private String id;

    @NotBlank(message = "Adventure type name is required")
    @Size(max = 100, message = "Adventure type name must not exceed 100 characters")
    @Indexed(unique = true)
    private String name;

    public AdventureType() {}

    public AdventureType(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
