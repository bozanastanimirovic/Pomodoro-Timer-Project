package com.example.backend.DTO;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListDTO {

    private List<Long> items;

}
