package com.carenation.carenation_project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.carenation.carenation_project.config.MapStructMapperConfig;
import com.carenation.carenation_project.dto.response.CarResponseDTO;
import com.carenation.carenation_project.entity.Car;

@Mapper(config = MapStructMapperConfig.class)
public interface CarMapper {

	@Mapping(source = "category.name", target = "categoryName")
	CarResponseDTO toCarResponseDTO(Car car);
}