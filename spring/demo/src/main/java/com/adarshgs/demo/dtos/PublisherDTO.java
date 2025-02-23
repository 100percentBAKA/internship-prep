package com.adarshgs.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherDTO {
    @NotBlank(message = "Publisher name cannot be blank.")
    private String name;
}
