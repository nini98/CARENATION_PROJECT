package com.carenation.carenation_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.carenation.carenation_project.entity.Car;
import com.carenation.carenation_project.entity.Category;

public interface CarRepository extends JpaRepository<Car, Integer>, JpaSpecificationExecutor<Car> {
	Page<Car> findByCategories(Category category, Pageable pageable);

}
