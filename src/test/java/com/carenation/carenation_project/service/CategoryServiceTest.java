package com.carenation.carenation_project.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
