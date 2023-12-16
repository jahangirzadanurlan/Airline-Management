package com.example.airplanems.service.impl;

import com.example.airplanems.model.dto.request.PlaneRequestDto;
import com.example.airplanems.model.entity.Plane;
import com.example.airplanems.repository.PlaneRepository;
import com.example.airplanems.service.IPlaneService;
import com.example.commonexception.enums.ExceptionsEnum;
import com.example.commonexception.exceptions.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    public List<Plane> getAllPlanes(String isBusy) {
        List<Plane> allPlanes = planeRepository.findAll();

        if (isBusy.equalsIgnoreCase("false")){
            return allPlanes.stream()
                    .filter(plane -> !plane.isBusy())
                    .collect(Collectors.toList());
        }else {
            return allPlanes;
        }
    }

    @Override
    public Plane getPlaneById(Long id) {
        return planeRepository.findById(id).orElseThrow(() -> new GeneralException(ExceptionsEnum.PLANE_NOT_FOUND));
    }

    @Override
    public ResponseEntity<String> updatePlane(Plane plane) {
        planeRepository.findById(plane.getId()).orElseThrow(() -> new GeneralException(ExceptionsEnum.PLANE_NOT_FOUND));
        planeRepository.save(plane);
        return ResponseEntity.ok().body(plane.getName() + " updated");
    }

    @Override
    public ResponseEntity<String> deletePlane(Long id) {
        Optional<Plane> plane = planeRepository.findById(id);
        planeRepository.delete(plane.orElseThrow(() -> new GeneralException(ExceptionsEnum.PLANE_NOT_FOUND)));
        return ResponseEntity.ok().body(plane.get().getName() + " plane deleted!");
    }

    @Override
    public ResponseEntity<String> patchPlane(Long id,String isBusy) {
        Optional<Plane> plane = planeRepository.findById(id);

        if (isBusy.equalsIgnoreCase("true") || isBusy.equalsIgnoreCase("false")){
            plane.orElseThrow(() -> new GeneralException(ExceptionsEnum.PLANE_NOT_FOUND))
                    .setBusy(isBusy.equalsIgnoreCase("true"));
            planeRepository.save(plane.get());
            return ResponseEntity.ok().body("Plane updated!");
        }else {
            log.info("isBusy request => {}",isBusy);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is not true!");
        }
    }
}
