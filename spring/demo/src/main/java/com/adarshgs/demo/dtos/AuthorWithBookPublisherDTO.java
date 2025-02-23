package com.adarshgs.demo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorWithBookPublisherDTO {
    @NotBlank(message = "Author name cannot be blank")
    private String name;

    @Valid
    private BookWithPublisherDTO book;
}
