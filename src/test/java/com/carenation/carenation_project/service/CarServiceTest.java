package com.carenation.carenation_project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

	@Mock
	CarRepository carRepository;
	@Mock
	CategoryRepository categoryRepository;
	@Mock
	CarMapper carMapper;
	@InjectMocks
	CarService carService;

	@Test
	void 자동차_등록_성공() {
		// given
		CarRegistRequestDTO params = new CarRegistRequestDTO(1, "현대", "코나", 2024);
		Category category = Category.builder()
			.categoryId(1)
			.name("경형 RV")
			.build();
		when(categoryRepository.findById(params.getCategoryId())).thenReturn(Optional.of(category));

		// when
		carService.registCar(params);

		// then
		ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
		verify(carRepository, times(1)).save(carCaptor.capture());
		Car savedCar = carCaptor.getValue();
		assertEquals(params.getManufacturer(), savedCar.getManufacturer());
		assertEquals(params.getModelName(), savedCar.getModelName());
		assertEquals(params.getManufactureYear(), savedCar.getManufactureYear());
		assertTrue(savedCar.getRentalStatus());
		assertEquals(category, savedCar.getCategories().iterator().next());
	}

	@Test
	void 자동차_등록시_존재하지_않는_CATEGORY_ID_사용했을때_NO_DATA_FOUND_예외_발생() {
		// given
		CarRegistRequestDTO params = new CarRegistRequestDTO(99999, "현대", "코나", 2024);
		when(categoryRepository.findById(params.getCategoryId())).thenReturn(Optional.empty());

		// when
		CarenationException carenationException = assertThrows(CarenationException.class, () -> {
			carService.registCar(params);
		});

		// then
		assertEquals(ResultCode.NO_DATA_FOUND, carenationException.getResultCode());
	}

	@Test
	void 존재하지않는_자동차_rentalStatus_조회시_NO_DATA_FOUND_예외_발생 () {
		// given
		when(carRepository.findById(-1)).thenReturn(Optional.empty());

		// when
		CarenationException carenationException = assertThrows(CarenationException.class, () -> {
			carService.getCarRentalStatus(-1);
		});

		// then
		assertEquals(ResultCode.NO_DATA_FOUND, carenationException.getResultCode());
	}

	@Test
	void 자동차_리스트_조회_성공() {
		// given
		CarSearchRequestDTO params = new CarSearchRequestDTO(null, "코나", 2024, 0, 10);
		Car car = Car.builder()
			.carId(1)
			.modelName("코나")
			.manufactureYear(2024)
			.categories(new HashSet<>(List.of(Category.builder().categoryId(1).name("미니SUV").build())))
			.rentalStatus(true)
			.build();
		Page<Car> cars = new PageImpl<>(List.of(car));
		when(carRepository.findAll(any(Specification.class), any(Pageable.class)))
			.thenReturn(cars);
		when(carMapper.toCarResponseDTO(any(Car.class)))
			.thenReturn(new CarResponseDTO(1, "미니SUV", "현대", "코나", 2024, true));

		// when
		Page<CarResponseDTO> result = carService.getCarList(params);

		// then
		assertEquals("코나", result.getContent().get(0).getModelName());
		assertEquals("미니SUV", result.getContent().get(0).getCategoryName());
	}

	@Test
	void 자동차_정보_수정_성공() {
		// given
		CarModifyRequestDTO params = new CarModifyRequestDTO(1, 2, "현대", "코나", 2024, false);
		Category category = Category.builder()
			.categoryId(2)
			.name("미니SUV")
			.build();
		Car existingCar = Car.builder()
			.carId(1)
			.manufacturer("현대")
			.modelName("코나")
			.manufactureYear(2024)
			.rentalStatus(true)
			.categories(new HashSet<>(List.of(category)))
			.build();
		when(categoryRepository.findById(params.getCategoryId())).thenReturn(Optional.of(category));
		when(carRepository.findById(params.getCarId())).thenReturn(Optional.of(existingCar));

		// when
		carService.modifyCar(params);

		// then
		verify(carRepository, times(1)).save(any(Car.class));
	}

	@Test
	void 자동차_정보_수정시_존재하지않는_차_정보_수정하려고하면_NO_DATA_FOUND_예외_발생() {
		// given
		CarModifyRequestDTO params = new CarModifyRequestDTO(1, 2, "현대", "코나", 2024, false);
		when(carRepository.findById(params.getCarId())).thenReturn(Optional.empty());

		// when
		CarenationException carenationException = assertThrows(CarenationException.class, () -> {
			carService.modifyCar(params);
		});

		// then
		assertEquals(ResultCode.NO_DATA_FOUND, carenationException.getResultCode());
	}

	@Test
	void 자동차_정보_수정시_존재하지않는_CATEGORY_ID_사용하면_NO_DATA_FOUND_예외_발생() {
		// given
		CarModifyRequestDTO params = new CarModifyRequestDTO(1, 99999, "현대", "코나", 2024, false);
		Category category = Category.builder()
			.categoryId(2)
			.name("미니SUV")
			.build();
		Car existingCar = Car.builder()
			.carId(1)
			.manufacturer("현대")
			.modelName("코나")
			.manufactureYear(2024)
			.rentalStatus(true)
			.categories(new HashSet<>(List.of(category)))
			.build();
		when(carRepository.findById(params.getCarId())).thenReturn(Optional.of(existingCar));
		when(categoryRepository.findById(params.getCategoryId())).thenReturn(Optional.empty());

		// when
		CarenationException carenationException = assertThrows(CarenationException.class, () -> {
			carService.modifyCar(params);
		});

		// then
		assertEquals(ResultCode.NO_DATA_FOUND, carenationException.getResultCode());
	}

}
