package com.carenation.carenation_project.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CarResponseDTO {
	private final Integer carId;
	private final String categoryName;
	private final String manufacturer;
	private final String modelName;
	private final Integer manufactureYear;
	private final Boolean rentalStatus;
}
