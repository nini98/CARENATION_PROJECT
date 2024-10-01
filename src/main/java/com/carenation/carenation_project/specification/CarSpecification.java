package com.carenation.carenation_project.specification;

import org.springframework.data.jpa.domain.Specification;

import com.carenation.carenation_project.entity.Car;

public class CarSpecification {
	public static Specification<Car> hasManufacturer(String manufacturer) {
		return (root, query, builder) -> manufacturer == null ? null : builder.equal(root.get("manufacturer"), manufacturer);
	}

	public static Specification<Car> hasModelName(String modelName) {
		return (root, query, builder) -> modelName == null ? null : builder.equal(root.get("modelName"), modelName);
	}

	public static Specification<Car> hasManufactureYear(Integer year) {
		return (root, query, builder) -> year == null ? null : builder.equal(root.get("manufactureYear"), year);
	}

	public static Specification<Car> isRentalAvailable(Boolean rentalStatus) {
		return (root, query, builder) -> rentalStatus == null ? null : builder.equal(root.get("rentalStatus"), rentalStatus);
	}
}
