package com.pesonal.FindDogPlz.map.application;

import com.pesonal.FindDogPlz.map.dto.MapDto;

import java.util.List;

public interface Markable {
    List<MapDto> getMarkableData(Double longitude, Double latitude);
}
