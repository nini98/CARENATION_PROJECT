package com.carenation.carenation_project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carenation.carenation_project.common.exception.CarenationException;
import com.carenation.carenation_project.common.response.ResultCode;
import com.carenation.carenation_project.dto.request.CarCategorySearchRequestDTO;
import com.carenation.carenation_project.dto.response.CarResponseDTO;
import com.carenation.carenation_project.entity.Car;
import com.carenation.carenation_project.entity.Category;
import com.carenation.carenation_project.mapper.CarMapper;
import com.carenation.carenation_project.repository.CarRepository;
import com.carenation.carenation_project.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CarRepository carRepository;
	private final CategoryRepository categoryRepository;
	private final CarMapper carMapper;

	@Transactional(readOnly = true)
	public Page<CarResponseDTO> getCarListByCategory(Integer categoryId, CarCategorySearchRequestDTO params) {
		Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.by("manufactureYear").descending());

		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new CarenationException(ResultCode.NO_DATA_FOUND));

		Page<Car> cars = carRepository.findByCategories(category, pageable);

		return cars.map(carMapper::toCarResponseDTO);
	}
}
