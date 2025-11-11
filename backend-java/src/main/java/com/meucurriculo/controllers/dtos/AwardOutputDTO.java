package com.meucurriculo.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AwardOutputDTO(Long id, String title, String description, int year) {
}