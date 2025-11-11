package com.meucurriculo.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProjectOutputDTO(Long id, String description, LocalDate joinDate, LocalDate exitDate) { }
