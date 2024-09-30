package com.carenation.carenation_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "car")
@Entity
public class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer carId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	private Category category;

	@Column(length = 100, nullable = false)
	private String manufacturer;

	@Column(length = 100, nullable = false)
	private String modelName;

	@Column(nullable = false)
	private Integer manufactureYear;

	@Column(nullable = false)
	private Boolean rentalStatus;

	@Builder
	public Car(Integer carId, Category category, String manufacturer, String modelName, Integer manufactureYear, Boolean rentalStatus) {
		this.carId = carId;
		this.category = category;
		this.manufacturer = manufacturer;
		this.modelName = modelName;
		this.manufactureYear = manufactureYear;
		this.rentalStatus = rentalStatus;
	}
}
