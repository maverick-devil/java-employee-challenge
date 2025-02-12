package com.reliaquest.api.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class EmployeeInputDto {
    @NotBlank
    private String name;

    @Positive(message = "must be greater than 0") @NotNull private Integer salary;

    @Min(value = 16, message = "must be greater than or equal to 16")
    @Max(value = 75, message = "must be less than or equal to 75")
    @NotNull private Integer age;

    @NotBlank
    private String title;
}
