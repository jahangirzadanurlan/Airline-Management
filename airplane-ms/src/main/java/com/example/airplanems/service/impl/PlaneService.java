package com.example.airplanems.service.impl;

import com.example.airplanems.model.dto.request.PlaneRequestDto;
import com.example.airplanems.model.dto.response.PlaneResponseDto;
import com.example.airplanems.model.entity.Plane;
import com.example.airplanems.service.IPlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaneService implements IPlaneService {
    @Override
    public ResponseEntity<String> addPlane(PlaneRequestDto requestDto) {

        return null;
    }

    @Override
    public List<PlaneResponseDto> getAllPlanes() {
        return null;
    }

    @Override
    public PlaneResponseDto getPlaneById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<String> updatePlane(Plane plane) {
        return null;
    }

    @Override
    public ResponseEntity<String> deletePlane(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<String> patchPlane(Long id) {
        return null;
    }
}
