package com.carenation.carenation_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.carenation.carenation_project.common.exception.CarenationException;
import com.carenation.carenation_project.dto.request.CarModifyRequestDTO;
import com.carenation.carenation_project.dto.request.CarRegistRequestDTO;
import com.carenation.carenation_project.dto.request.CarSearchRequestDTO;
import com.carenation.carenation_project.dto.response.CarResponseDTO;
import com.carenation.carenation_project.mapper.CarMapper;
import com.carenation.carenation_project.common.response.ResultCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarService {
	private final CarMapper carMapper;

	public List<CarResponseDTO> getCarList(CarSearchRequestDTO params){
		return carMapper.selectCarList(params);
	};

	public void registCar(CarRegistRequestDTO params) { carMapper.insertCar(params); };

	public void modifyCar(CarModifyRequestDTO params) {
		Optional<CarResponseDTO> existingCar = Optional.ofNullable(carMapper.selectCarById(params.getCarId()));
		existingCar.orElseThrow(() -> new CarenationException(ResultCode.NO_DATA_FOUND));
		carMapper.updateCar(params);
	};
}
