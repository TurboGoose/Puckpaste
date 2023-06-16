package ru.turbogoose.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RenewedPostDto {
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")

    private LocalDateTime expiresAt;
}
