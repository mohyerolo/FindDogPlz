package com.pesonal.FindDogPlz.map.application;

import com.pesonal.FindDogPlz.map.dto.MapDto;
import com.pesonal.FindDogPlz.map.dto.MapType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService {
    private final MarkableFactory markableFactory;

    public List<MapDto> getMapData(MapType mapType, Double longitude, Double latitude) {
        Markable factory = markableFactory.getMarkableFactory(mapType);
        return factory.getMarkableData(longitude, latitude);
    }
}
