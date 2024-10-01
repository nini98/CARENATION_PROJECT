package com.carenation.carenation_project.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CAR_ID")
	private Long carId;

	@Column(name = "MANUFACTURER", nullable = false)
	private String manufacturer;

	@Column(name = "MODEL_NAME", nullable = false)
	private String modelName;

	@Column(name = "MANUFACTURE_YEAR", nullable = false)
	private Integer manufactureYear;

	@Column(name = "RENTAL_STATUS", nullable = false)
	private Boolean rentalStatus;

	@CreatedDate
	@Column(name = "CREATED_AT", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "UPDATED_AT")
	private LocalDateTime updatedAt;

	@ManyToMany
	@JoinTable(
		name = "car_category",
		joinColumns = @JoinColumn(name = "CAR_ID"),
		inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
	)
	private Set<Category> categories = new HashSet<>();
}
