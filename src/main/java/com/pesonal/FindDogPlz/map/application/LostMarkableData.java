package com.pesonal.FindDogPlz.map.application;

import com.pesonal.FindDogPlz.global.util.GeometryUtil;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.map.dto.Location;
import com.pesonal.FindDogPlz.map.dto.MapDto;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LostMarkableData implements Markable {

    private final EntityManager entityManager;
    private final LostPostRepository lostPostRepository;

    @Override
    public List<MapDto> getMarkableData(Double longitude, Double latitude) {
        String pointFormat = GeometryUtil.calculateAndFormatPoint(latitude, longitude);
        List<LostPost> lostPostList = findLostPostByLocation(pointFormat);
        Location location = new Location(latitude, longitude);
        return convertToMapDtoList(lostPostList, location);
    }

    private List<LostPost> findLostPostByLocation(String pointFormat) {
        String queryStr = buildQueryString(pointFormat);
        Query query = entityManager.createNativeQuery(queryStr, LostPost.class);
        return query.getResultList();
    }

    private String buildQueryString(String pointFormat) {
        return String.format(
                "SELECT * FROM lost_post AS lp " +
                "WHERE lp.completed = 0 and MBRContains(ST_LINESTRINGFROMTEXT(%s), lp.lost_point) " +
                "ORDER BY lp.id",
                pointFormat
        );
    }

    private List<MapDto> convertToMapDtoList(List<LostPost> lostPostList, Location location) {
        return lostPostList.stream()
                .map(p -> convertLostPostToMapDto(p, location))
                .toList();
    }

    private MapDto convertLostPostToMapDto(LostPost lostPost, Location location) {
        Double dist = calcDist(location.getLongitude(), location.getLatitude(), lostPost.getId());
        return MapDto.lostPostBuilder()
                .lostPost(lostPost)
                .dist(dist)
                .lostPostBuild();
    }

    private Double calcDist(Double longitude, Double latitude, Long lostPostId) {
        Point point = PointParser.parsePoint(latitude, longitude);
        return point != null ? lostPostRepository.findDistByPoint(point.getX(), point.getY(), lostPostId) : null;
    }
}
