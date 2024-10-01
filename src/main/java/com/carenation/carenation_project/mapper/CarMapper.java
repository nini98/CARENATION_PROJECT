package com.carenation.carenation_project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.carenation.carenation_project.config.MapStructMapperConfig;
import com.carenation.carenation_project.dto.response.CarResponseDTO;
import com.carenation.carenation_project.entity.Car;
import com.carenation.carenation_project.entity.Category;

@Mapper(config = MapStructMapperConfig.class, imports = {Category.class})
public interface CarMapper {

	@Mapping(expression = "java(car.getCategories().stream().map(Category::getName).collect(java.util.stream.Collectors.joining(\", \")))", target = "categoryName")
	CarResponseDTO toCarResponseDTO(Car car);
}