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
@RequestMapping("/airplanes")
public class PlaneController {
    private final IPlaneService planeService;

    @PostMapping
    public ResponseEntity<String> addPlane(@RequestBody PlaneRequestDto requestDto){
        return planeService.addPlane(requestDto);
    }

    @PutMapping
    public ResponseEntity<String> updatePlane(@RequestBody Plane plane){
        return planeService.updatePlane(plane);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlane(@PathVariable Long id){
        return planeService.deletePlane(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchPlane(@PathVariable Long id,@RequestBody String isBusy){
        return planeService.patchPlane(id,isBusy);
    }

    @GetMapping
    public List<Plane> getAllPlanes(){
        return planeService.getAllPlanes();
    }

    @GetMapping("/{id}")
    public Plane getPlaneById(@PathVariable Long id){
        return planeService.getPlaneById(id);
    }

}
