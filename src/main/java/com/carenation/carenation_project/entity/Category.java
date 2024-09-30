package com.carenation.carenation_project.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "category")
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer categoryId;

	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "category")
	private List<Car> cars;

	@Builder
	public Category(Integer categoryId, String name) {
		this.categoryId = categoryId;
		this.name = name;
	}
}
