package com.pesonal.FindDogPlz.map.application;

import com.pesonal.FindDogPlz.global.util.GeometryUtil;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.map.dto.Location;
import com.pesonal.FindDogPlz.map.dto.MapDto;
import com.pesonal.FindDogPlz.post.domain.FindPost;
import com.pesonal.FindDogPlz.post.repository.FindPostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindMarkableData implements Markable {
    private final EntityManager entityManager;
    private final FindPostRepository findPostRepository;

    @Override
    public List<MapDto> getMarkableData(final Double longitude, final Double latitude) {
        String pointFormat = GeometryUtil.calculateAndFormatPoint(latitude, longitude);
        List<FindPost> findPostList = findFindPostByLocation(pointFormat);
        Location location = new Location(latitude, longitude);
        return convertToMapDtoList(findPostList, location);
    }

    private List<FindPost> findFindPostByLocation(final String pointFormat) {
        String queryStr = buildQueryString(pointFormat);
        Query query = entityManager.createNativeQuery(queryStr, FindPost.class);
        return query.getResultList();
    }

    private String buildQueryString(final String pointFormat) {
        return String.format(
                "SELECT * FROM find_post AS fp " +
                        "WHERE fp.completed = 0 and MBRContains(ST_LINESTRINGFROMTEXT(%s), fp.loc_point) " +
                        "ORDER BY fp.id",
                pointFormat
        );
    }

    private List<MapDto> convertToMapDtoList(final List<FindPost> findPostList, final Location location) {
        return findPostList.stream()
                .map(p -> convertFindPostToMapDto(p, location))
                .toList();
    }

    private MapDto convertFindPostToMapDto(final FindPost findPost, final Location location) {
        Double dist = calcDist(location.getLongitude(), location.getLatitude(), findPost.getId());
        return MapDto.findPostBuilder()
                .findPost(findPost)
                .dist(dist)
                .findPostBuild();
    }

    private Double calcDist(final Double longitude, final Double latitude, final Long findPostId) {
        Point point = PointParser.parsePoint(latitude, longitude);
        return point != null ? findPostRepository.findDistByPoint(point.getX(), point.getY(), findPostId) : null;
    }
}
