package com.pesonal.FindDogPlz.map.application;

import com.pesonal.FindDogPlz.map.dto.MapType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleMarkableFactory implements MarkableFactory {

    private final FindMarkableData findMarkableFactory;
    private final LostMarkableData lostMarkableFactory;
    private final AllMarkableData allMarkableFactory;

    @Autowired
    public SimpleMarkableFactory(FindMarkableData findMarkableFactory, LostMarkableData lostMarkableFactory, AllMarkableData allMarkableFactory) {
        this.findMarkableFactory = findMarkableFactory;
        this.lostMarkableFactory = lostMarkableFactory;
        this.allMarkableFactory = allMarkableFactory;
    }

    @Override
    public Markable getMarkableFactory(MapType mapType) {
        return switch (mapType) {
            case FIND -> findMarkableFactory;
            case LOST -> lostMarkableFactory;
            case ALL -> allMarkableFactory;
        };
    }
}
