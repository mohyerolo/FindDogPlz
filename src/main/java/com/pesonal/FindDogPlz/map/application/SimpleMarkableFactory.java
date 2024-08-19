package com.pesonal.FindDogPlz.map.application;

import com.pesonal.FindDogPlz.map.dto.MapType;
import com.pesonal.FindDogPlz.post.repository.FindPostRepository;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class SimpleMarkableFactory implements MarkableFactory {

    private final LostPostRepository lostPostRepository;
    private final FindPostRepository findPostRepository;
    private final EntityManager entityManager;

    public SimpleMarkableFactory(LostPostRepository lostPostRepository, FindPostRepository findPostRepository, EntityManager entityManager) {
        this.lostPostRepository = lostPostRepository;
        this.findPostRepository = findPostRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Markable getMapData(MapType mapType) {
        if (mapType.equals(MapType.FIND)) {
            return new FindMarkableData();
        } else if (mapType.equals(MapType.LOST)) {
            return new LostMarkableData(entityManager, lostPostRepository);
        } else {
            return new AllMarkableData();
        }
    }
}
