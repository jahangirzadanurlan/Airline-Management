package com.example.airplanems.service;

import com.example.airplanems.model.dto.request.PlaneRequestDto;
import com.example.airplanems.model.dto.response.PlaneResponseDto;
import com.example.airplanems.model.entity.Plane;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPlaneService {
    ResponseEntity<String> addPlane(PlaneRequestDto requestDto);
    List<PlaneResponseDto> getAllPlanes();
    PlaneResponseDto getPlaneById(Long id);
    ResponseEntity<String> updatePlane(Plane plane);
    ResponseEntity<String> deletePlane(Long id);
    ResponseEntity<String> patchPlane(Long id);


}
