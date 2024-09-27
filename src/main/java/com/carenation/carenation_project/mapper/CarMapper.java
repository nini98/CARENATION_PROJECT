package com.carenation.carenation_project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.carenation.carenation_project.dto.request.CarModifyRequestDTO;
import com.carenation.carenation_project.dto.request.CarRegistRequestDTO;
import com.carenation.carenation_project.dto.request.CarSearchRequestDTO;
import com.carenation.carenation_project.dto.response.CarResponseDTO;

@Mapper
public interface CarMapper {
	List<CarResponseDTO> selectCarList(CarSearchRequestDTO params);

	CarResponseDTO selectCarById(int carId);

	void insertCar(CarRegistRequestDTO params);

	void updateCar(CarModifyRequestDTO params);
}
