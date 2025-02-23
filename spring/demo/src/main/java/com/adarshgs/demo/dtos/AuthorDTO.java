package com.adarshgs.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {
    @NotBlank(message = "Author name cannot be blank")
    private String name;

    @NotNull(message = "Book UUID cannot be blank")
    private UUID bookId;
}
