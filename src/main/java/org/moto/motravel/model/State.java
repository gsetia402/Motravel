package org.moto.motravel.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "states")
public class State {
    @Id
    private String id;

    @NotBlank(message = "State name is required")
    @Size(max = 100, message = "State name must not exceed 100 characters")
    @Indexed(unique = true)
    private String name;

    public State() {}

    public State(String name) {
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
