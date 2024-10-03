package com.carenation.carenation_project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.carenation.carenation_project.common.exception.CarenationException;
import com.carenation.carenation_project.common.response.ResultCode;
import com.carenation.carenation_project.dto.request.CarCategorySearchRequestDTO;
import com.carenation.carenation_project.dto.response.CarResponseDTO;
import com.carenation.carenation_project.entity.Car;
import com.carenation.carenation_project.entity.Category;
import com.carenation.carenation_project.mapper.CarMapper;
import com.carenation.carenation_project.repository.CarRepository;
import com.carenation.carenation_project.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
	@Mock
	CarRepository carRepository;
	@Mock
	CategoryRepository categoryRepository;
	@Mock
	CarMapper carMapper;
	@InjectMocks
	CategoryService categoryService;

	@Test
	void 카테고리로_자동차_목록_조회_성공() {
		// given
		Integer categoryId = 1;
		CarCategorySearchRequestDTO params = new CarCategorySearchRequestDTO(0, 10);
		Category category = Category.builder()
			.categoryId(1)
			.name("SUV")
			.build();
		Car car = Car.builder()
			.carId(1)
			.modelName("코나")
			.manufactureYear(2024)
			.categories(new HashSet<>(List.of(category)))
			.rentalStatus(true)
			.build();
		Page<Car> cars = new PageImpl<>(List.of(car));
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		when(carRepository.findByCategories(any(Category.class), any(Pageable.class))).thenReturn(cars);
		when(carMapper.toCarResponseDTO(any(Car.class)))
			.thenReturn(new CarResponseDTO(1, "미니SUV", "현대", "코나", 2024, true));

		// when
		Page<CarResponseDTO> result = categoryService.getCarListByCategory(categoryId, params);

		// then
		assertEquals("코나", result.getContent().get(0).getModelName());
		assertEquals("미니SUV", result.getContent().get(0).getCategoryName());
	}

	@Test
	void 존재하지_않는_카테고리로_조회시_NO_DATA_FOUND_예외_발생() {
		// given
		Integer categoryId = 99999;
		CarCategorySearchRequestDTO params = new CarCategorySearchRequestDTO(0, 10);
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		// when
		CarenationException carenationException = assertThrows(CarenationException.class, () -> {
			categoryService.getCarListByCategory(categoryId, params);
		});

		// then
		assertEquals(ResultCode.NO_DATA_FOUND, carenationException.getResultCode());
	}

}
