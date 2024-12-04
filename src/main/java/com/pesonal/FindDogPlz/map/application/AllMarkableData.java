package com.pesonal.FindDogPlz.map.application;

import com.pesonal.FindDogPlz.map.dto.MapDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllMarkableData implements Markable {
    @Override
    public List<MapDto> getMarkableData(Double longitude, Double latitude) {
        return null;
    }
}
