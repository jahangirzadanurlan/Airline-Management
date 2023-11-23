package com.example.airplanems.controller;

import com.example.airplanems.model.dto.request.PlaneRequestDto;
import com.example.airplanems.model.entity.Plane;
import com.example.airplanems.service.IPlaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/airplane-ms")
public class PlaneController {
    private final IPlaneService planeService;

    @PostMapping("/airplanes")
    public ResponseEntity<String> addPlane(PlaneRequestDto requestDto){
        return planeService.addPlane(requestDto);
    }

    @PutMapping("/airplanes")
    public ResponseEntity<String> updatePlane(Plane plane){
        return planeService.updatePlane(plane);
    }

    @DeleteMapping("/airplanes/{id}")
    public ResponseEntity<String> updatePlane(@PathVariable Long id){
        return planeService.deletePlane(id);
    }

    @PatchMapping("/airplanes/{id}")
    public ResponseEntity<String> patchPlane(@PathVariable Long id,@RequestBody String isBusy){
        return planeService.patchPlane(id,isBusy);
    }

    @GetMapping("/airplanes")
    public List<Plane> getAllPlanes(){
        return planeService.getAllPlanes();
    }

    @GetMapping("/airplanes/{id}")
    public Plane getPlaneById(@PathVariable Long id){
        return planeService.getPlaneById(id);
    }

}
