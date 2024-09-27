package com.carenation.carenation_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CarModifyRequestDTO {
	@NotNull
	private final Integer carId;
	@NotNull
	private final Integer categoryId;
	@NotBlank
	private final String manufacturer;
	@NotBlank
	private final String modelName;
	@NotNull
	private final Integer manufactureYear;
	@NotNull
	private final Boolean rentalStatus;
}
