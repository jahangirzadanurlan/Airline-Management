package com.example.airplanems.controller;

import com.example.airplanems.model.dto.request.PlaneRequestDto;
import com.example.airplanems.service.IPlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/airplane-ms")
public class PlaneController {
    private final IPlaneService planeService;

    @PostMapping("/airplanes")
    public ResponseEntity<String> addPlane(PlaneRequestDto requestDto){
        return planeService.addPlane(requestDto);
    }
}
