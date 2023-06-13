package ru.turbogoose.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RenewDto {
    private int renewalTimeInDays;
}
