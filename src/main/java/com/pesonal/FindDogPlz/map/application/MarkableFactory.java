package com.pesonal.FindDogPlz.map.application;

import com.pesonal.FindDogPlz.map.dto.MapType;

public interface MarkableFactory {
    Markable getMarkableFactory(MapType mapType);
}
