package com.adarshgs.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookDTO {
    @NotBlank(message = "Book name cannot be blank")
    private String name;

    @NotNull(message = "Publisher ID cannot be null")
    private UUID publisherId;
}
