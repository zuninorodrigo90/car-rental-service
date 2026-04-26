package com.dxc.carrental.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RentCarsRequest {
    @NotBlank(message = "DNI is requited")
    private String customerDni;
    @NotEmpty(message = "Patents are required")
    private List<String> patents;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull(message = "expectedReturnDate is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedReturnDate;
}
