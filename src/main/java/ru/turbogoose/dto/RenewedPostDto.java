package ru.turbogoose.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RenewedPostDto {
    private LocalDateTime expires;
}
