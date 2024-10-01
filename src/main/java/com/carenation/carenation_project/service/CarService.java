package com.carenation.carenation_project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carenation.carenation_project.common.exception.CarenationException;
import com.carenation.carenation_project.dto.request.CarModifyRequestDTO;
import com.carenation.carenation_project.dto.request.CarRegistRequestDTO;
import com.carenation.carenation_project.dto.request.CarSearchRequestDTO;
import com.carenation.carenation_project.dto.response.CarResponseDTO;
import com.carenation.carenation_project.entity.Car;
import com.carenation.carenation_project.entity.Category;
import com.carenation.carenation_project.mapper.CarMapper;
import com.carenation.carenation_project.common.response.ResultCode;
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

		boolean carExists = carRepository.existsByCategoryAndManufacturerAndModelNameAndManufactureYear(
			category, params.getManufacturer(), params.getModelName(), params.getManufactureYear());

		if (carExists) {
			throw new CarenationException(ResultCode.DUPLICATE_DATA);
		}

		Car car = Car.builder()
			.category(category)
			.manufacturer(params.getManufacturer())
			.modelName(params.getModelName())
			.manufactureYear(params.getManufactureYear())
			.rentalStatus(true)
			.build();

		carRepository.save(car);
	}

	@Transactional(readOnly = true)
	public List<CarResponseDTO> getCarList(CarSearchRequestDTO params) {
		Specification<Car> spec = Specification.where(CarSpecification.hasCategoryId(params.getCategoryId()))
			.and(CarSpecification.hasManufacturer(params.getManufacturer()))
			.and(CarSpecification.hasModelName(params.getModelName()))
			.and(CarSpecification.hasManufactureYear(params.getManufactureYear()))
			.and(CarSpecification.isRentalAvailable(params.getRentalStatus()));

		List<Car> cars = carRepository.findAll(spec, Sort.by("category.categoryId"));
		return cars.stream()
			.map(carMapper::toCarResponseDTO)
			.collect(Collectors.toList());
	}

	public void modifyCar(CarModifyRequestDTO params) {
		Car existingCar = carRepository.findById(params.getCarId())
			.orElseThrow(() -> new CarenationException(ResultCode.NO_DATA_FOUND));

		Category category = categoryRepository.findById(params.getCategoryId())
			.orElseThrow(() -> new CarenationException(ResultCode.NO_DATA_FOUND));

		Car updatedCar = Car.builder()
			.carId(existingCar.getCarId())
			.category(category)
			.manufacturer(params.getManufacturer())
			.modelName(params.getModelName())
			.manufactureYear(params.getManufactureYear())
			.rentalStatus(params.getRentalStatus())
			.build();

		carRepository.save(updatedCar);
	}
}
