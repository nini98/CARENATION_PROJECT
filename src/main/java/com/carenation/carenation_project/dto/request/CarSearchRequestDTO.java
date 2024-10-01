package com.carenation.carenation_project.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CarSearchRequestDTO {
	private final Integer carId;
	private final Integer categoryId;
	private final String manufacturer;
	private final String modelName;
	private final Integer manufactureYear;
	private final Boolean rentalStatus;
}
