package com.dxc.carrental.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReturnRentalRequest {
    @NotNull(message = "actualReturnDate is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate actualReturnDate;
}
