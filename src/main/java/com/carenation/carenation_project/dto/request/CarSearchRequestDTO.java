package com.carenation.carenation_project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CarSearchRequestDTO {
	private final String manufacturer;
	private final String modelName;
	private final Integer manufactureYear;
	@NotNull
	private final Integer page;
	@NotNull
	private final Integer size;
}
