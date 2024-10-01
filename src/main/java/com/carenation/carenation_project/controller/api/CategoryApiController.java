package com.carenation.carenation_project.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carenation.carenation_project.common.response.Response;
import com.carenation.carenation_project.dto.request.CarCategorySearchRequestDTO;
import com.carenation.carenation_project.dto.response.CarResponseDTO;
import com.carenation.carenation_project.service.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryApiController {

	private final CategoryService categoryService;

	@GetMapping("/categories/{categoryId}/cars")
	public Response<Page<CarResponseDTO>> getCarListByCategory(@PathVariable @NotNull @Min(1) Integer categoryId, @Valid @ModelAttribute CarCategorySearchRequestDTO params) {
		return Response.success(categoryService.getCarListByCategory(categoryId, params));
	}
}
