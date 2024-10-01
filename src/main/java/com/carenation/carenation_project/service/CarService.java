package com.carenation.carenation_project.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carenation.carenation_project.common.exception.CarenationException;
import com.carenation.carenation_project.common.response.ResultCode;
import com.carenation.carenation_project.dto.request.CarModifyRequestDTO;
import com.carenation.carenation_project.dto.request.CarRegistRequestDTO;
import com.carenation.carenation_project.dto.request.CarSearchRequestDTO;
import com.carenation.carenation_project.dto.response.CarResponseDTO;
import com.carenation.carenation_project.entity.Car;
import com.carenation.carenation_project.entity.Category;
import com.carenation.carenation_project.mapper.CarMapper;
import com.carenation.carenation_project.repository.CarRepository;
import com.carenation.carenation_project.repository.CategoryRepository;
import com.carenation.carenation_project.specification.CarSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarService {
	private final CarRepository carRepository;
	private final CategoryRepository categoryRepository;
	private final CarMapper carMapper;

	public void registCar(CarRegistRequestDTO params) {
		Category category = categoryRepository.findById(params.getCategoryId())
			.orElseThrow(() -> new CarenationException(ResultCode.NO_DATA_FOUND));

		boolean carExists = carRepository.existsByManufacturerAndModelNameAndManufactureYear(
			params.getManufacturer(), params.getModelName(), params.getManufactureYear());

		if (carExists) {
			throw new CarenationException(ResultCode.DUPLICATE_DATA);
		}

		Car car = Car.builder()
			.manufacturer(params.getManufacturer())
			.modelName(params.getModelName())
			.manufactureYear(params.getManufactureYear())
			.rentalStatus(true)
			.categories(new HashSet<>(List.of(category)))
			.build();

		carRepository.save(car);
	}

	@Transactional(readOnly = true)
	public CarResponseDTO getCarRentalStatus(Integer carId) {
		Car car = carRepository.findById(carId)
			.orElseThrow(() -> new CarenationException(ResultCode.NO_DATA_FOUND));

		if (!car.getRentalStatus()) {
			throw new CarenationException(ResultCode.CAR_NOT_AVAILABLE);
		}

		return carMapper.toCarResponseDTO(car);
	}

	@Transactional(readOnly = true)
	public Page<CarResponseDTO> getCarList(CarSearchRequestDTO params) {
		Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.by("manufactureYear").descending());

		Specification<Car> spec = Specification.where(CarSpecification.hasManufacturer(params.getManufacturer()))
			.and(CarSpecification.hasModelName(params.getModelName()))
			.and(CarSpecification.hasManufactureYear(params.getManufactureYear()));

		Page<Car> cars = carRepository.findAll(spec, pageable);
		return cars.map(carMapper::toCarResponseDTO);
	}

	public void modifyCar(CarModifyRequestDTO params) {
		Car existingCar = carRepository.findById(params.getCarId())
			.orElseThrow(() -> new CarenationException(ResultCode.NO_DATA_FOUND));

		Category category = categoryRepository.findById(params.getCategoryId())
			.orElseThrow(() -> new CarenationException(ResultCode.NO_DATA_FOUND));

		existingCar.setManufacturer(params.getManufacturer());
		existingCar.setModelName(params.getModelName());
		existingCar.setManufactureYear(params.getManufactureYear());
		existingCar.setRentalStatus(params.getRentalStatus());
		existingCar.setCategories(new HashSet<>(List.of(category)));

		carRepository.save(existingCar);
	}
}
