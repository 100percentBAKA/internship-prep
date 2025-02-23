package com.adarshgs.demo.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class BookWithPublisherDTO {
    @NotBlank(message = "Book name cannot be blank")
    private String name;

    @NotNull(message = "Publisher cannot be null")
    @Valid
    private PublisherDTO publisher;
}

