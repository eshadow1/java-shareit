package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SentItemDto {
    @Positive
    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Boolean available;

    @Positive
    private Integer owner;

    @Positive
    private Integer request;
}
