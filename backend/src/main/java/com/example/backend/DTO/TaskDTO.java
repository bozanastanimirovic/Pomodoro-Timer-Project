package com.example.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskDTO {

    private String name;

    private String teamName;

    public TaskDTO(String name, String teamName) {
        this.name = name;
        this.teamName = teamName;
    }

}
