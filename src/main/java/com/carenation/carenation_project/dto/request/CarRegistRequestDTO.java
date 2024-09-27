package com.carenation.carenation_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CarRegistRequestDTO {
	@NotNull
	private final Integer categoryId;
	@NotBlank
	private final String manufacturer;
	@NotBlank
	private final String modelName;
	@NotNull
	private final Integer manufactureYear;
}
