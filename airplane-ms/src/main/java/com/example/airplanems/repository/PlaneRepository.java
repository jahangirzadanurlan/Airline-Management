package com.example.airplanems.repository;

import com.example.airplanems.model.entity.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneRepository extends JpaRepository<Plane,Long> {
}
