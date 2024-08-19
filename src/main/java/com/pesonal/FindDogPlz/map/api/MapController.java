package com.pesonal.FindDogPlz.map.api;

import com.pesonal.FindDogPlz.map.application.MapService;
import com.pesonal.FindDogPlz.map.dto.MapDto;
import com.pesonal.FindDogPlz.map.dto.MapType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    @GetMapping
    public ResponseEntity<List<MapDto>> getMapData(@RequestParam MapType mapType, @RequestParam Double longitude, @RequestParam Double latitude) {
        return ResponseEntity.ok(mapService.getMapData(mapType, longitude, latitude));
    }
}
