package com.example.airplanems.service.impl;

import com.example.airplanems.model.dto.request.PlaneRequestDto;
import com.example.airplanems.model.entity.Plane;
import com.example.airplanems.repository.PlaneRepository;
import com.example.airplanems.service.IPlaneService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaneService implements IPlaneService {
    private final ModelMapper modelMapper;
    private final PlaneRepository planeRepository;

    @Override
    public ResponseEntity<String> addPlane(PlaneRequestDto requestDto) {
        planeRepository.save(modelMapper.map(requestDto, Plane.class));
        return ResponseEntity.status(HttpStatus.CREATED).body("Plane Created!");
    }

    @Override
    public List<Plane> getAllPlanes() {
        return planeRepository.findAll();
    }

    @Override
    public Plane getPlaneById(Long id) {
        return planeRepository.findById(id).orElseThrow(() -> new RuntimeException("Plane not found!"));
    }

    @Override
    public ResponseEntity<String> updatePlane(Plane plane) {
        planeRepository.save(plane);
        return ResponseEntity.ok().body(plane.getName() + " updated");
    }

    @Override
    public ResponseEntity<String> deletePlane(Long id) {
        Optional<Plane> plane = planeRepository.findById(id);
        planeRepository.delete(plane.orElseThrow(() -> new RuntimeException("Plane not found")));
        return ResponseEntity.ok().body(plane.get().getName() + " plane deleted!");
    }

    @Override
    public ResponseEntity<String> patchPlane(Long id,String isBusy) {
        Optional<Plane> plane = planeRepository.findById(id);
        plane.orElseThrow(() -> new RuntimeException("Plane not found!"))
                .setBusy(isBusy.equalsIgnoreCase("true"));

        return ResponseEntity.ok().body("Plane updated!");
    }
}
