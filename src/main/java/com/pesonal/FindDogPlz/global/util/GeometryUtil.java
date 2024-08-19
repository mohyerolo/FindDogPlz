package com.pesonal.FindDogPlz.global.util;

import com.pesonal.FindDogPlz.map.dto.Direction;
import com.pesonal.FindDogPlz.map.dto.Location;
import org.springframework.stereotype.Component;

@Component
public class GeometryUtil {
    public static Location calculate(Double baseLatitude, Double baseLongitude, Double distance,
                                     Double bearing) {
        Double radianLatitude = toRadian(baseLatitude);
        Double radianLongitude = toRadian(baseLongitude);
        Double radianAngle = toRadian(bearing);
        Double distanceRadius = distance / 6371.01;

        Double latitude = Math.asin(sin(radianLatitude) * cos(distanceRadius) +
                cos(radianLatitude) * sin(distanceRadius) * cos(radianAngle));
        Double longitude = radianLongitude + Math.atan2(sin(radianAngle) * sin(distanceRadius) *
                cos(radianLatitude), cos(distanceRadius) - sin(radianLatitude) * sin(latitude));

        longitude = normalizeLongitude(longitude);
        return new Location(toDegree(latitude), toDegree(longitude));
    }

    private static Double toRadian(Double coordinate) {
        return coordinate * Math.PI / 180.0;
    }

    private static Double toDegree(Double coordinate) {
        return coordinate * 180.0 / Math.PI;
    }

    private static Double sin(Double coordinate) {
        return Math.sin(coordinate);
    }

    private static Double cos(Double coordinate) {
        return Math.cos(coordinate);
    }

    private static Double normalizeLongitude(Double longitude) {
        return (longitude + 540) % 360 - 180;
    }

    public static String calculateAndFormatPoint(Double latitude, Double longitude) {
        Location northEast = calculate(latitude, longitude, 2.0, Direction.NORTHEAST.getBearing());
        Location southWest = calculate(latitude, longitude, 2.0, Direction.SOUTHWEST.getBearing());

        return String.format(
                "'LINESTRING(%f %f, %f %f)'",
                northEast.getLongitude(), northEast.getLatitude(), southWest.getLongitude(), southWest.getLatitude()
        );
    }
}

