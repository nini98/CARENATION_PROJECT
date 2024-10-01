package com.carenation.carenation_project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
		Category category = new Category(1, "경형 RV");
		when(categoryRepository.findById(params.getCategoryId())).thenReturn(Optional.of(category));
		when(carRepository.existsByCategoryAndManufacturerAndModelNameAndManufactureYear(category,
			params.getManufacturer(), params.getModelName(), params.getManufactureYear())).thenReturn(false);

		// when
		carService.registCar(params);

		// then
		verify(categoryRepository, times(1)).findById(params.getCategoryId());
		verify(carRepository, times(1)).save(any(Car.class));
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
	void 자동차_중복_등록시_DUPLICATE_DATA_예외_발생() {
		// given
		CarRegistRequestDTO params = new CarRegistRequestDTO(1, "현대", "코나", 2024);
		Category category = new Category(1, "경형 RV");
		when(categoryRepository.findById(params.getCategoryId())).thenReturn(Optional.of(category));
		when(carRepository.existsByCategoryAndManufacturerAndModelNameAndManufactureYear(category,
			params.getManufacturer(), params.getModelName(), params.getManufactureYear())).thenReturn(true);

		// when
		CarenationException carenationException = assertThrows(CarenationException.class, () -> {
			carService.registCar(params);
		});

		// then
		assertEquals(ResultCode.DUPLICATE_DATA, carenationException.getResultCode());
	}

	@Test
	void 자동차_조회_성공() {
		// given
		CarSearchRequestDTO params = new CarSearchRequestDTO(null, 1, "현대", null, 2024, null);
		when(carRepository.findAll(any(Specification.class), any(Sort.class)))
			.thenReturn(Collections.emptyList()); // 테스트에서는 빈 리스트 반환

		// when
		List<CarResponseDTO> result = carService.getCarList(params);

		// then
		assertTrue(result.isEmpty());
		verify(carRepository, times(1)).findAll(any(Specification.class), any(Sort.class));
	}

	@Test
	void 자동차_정보_수정_성공() {
		// given
		CarModifyRequestDTO params = new CarModifyRequestDTO(1, 2, "현대", "코나", 2024, false);
		Category category = new Category(2, "미니SUV");
		Car existingCar = Car.builder()
			.category(category)
			.manufacturer("현대")
			.modelName("코나")
			.manufactureYear(2024)
			.rentalStatus(true)
			.build();
		when(categoryRepository.findById(params.getCategoryId())).thenReturn(Optional.of(category));
		when(carRepository.findById(params.getCarId())).thenReturn(Optional.of(existingCar));

		// when
		carService.modifyCar(params);

		// then
		verify(categoryRepository, times(1)).findById(params.getCategoryId());
		verify(carRepository, times(1)).findById(params.getCarId());
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
		Car existingCar = Car.builder()
			.category(new Category(2, "미니SUV"))
			.manufacturer("현대")
			.modelName("코나")
			.manufactureYear(2024)
			.rentalStatus(true)
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
