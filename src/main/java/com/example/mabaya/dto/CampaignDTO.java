package com.example.mabaya.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CampaignDTO {

    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull
    private LocalDate startDate;

    @DecimalMin(value = "0.0", inclusive = true)
    private double bid;

    private boolean active = true;

    private Set<String> productSerialNumbers = new HashSet<>();

}
