package com.carenation.carenation_project.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carenation.carenation_project.common.response.Response;
import com.carenation.carenation_project.dto.request.CarModifyRequestDTO;
import com.carenation.carenation_project.dto.request.CarRegistRequestDTO;
import com.carenation.carenation_project.dto.request.CarSearchRequestDTO;
import com.carenation.carenation_project.dto.response.CarResponseDTO;
import com.carenation.carenation_project.service.CarService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CarApiController {

	private final CarService carService;

	@GetMapping("/cars")
	public Response<List<CarResponseDTO>> getCarList(@Valid @ModelAttribute CarSearchRequestDTO params){
		return Response.success(carService.getCarList(params));
	}

	@PostMapping("/cars")
	public Response<Void> registCar(@Valid @RequestBody CarRegistRequestDTO params){
		carService.registCar(params);
		return Response.success(null);
	}

	@PutMapping("/cars")
	public Response<Void> modifyCar(@Valid @RequestBody CarModifyRequestDTO params){
		carService.modifyCar(params);
		return Response.success(null);
	}

}
