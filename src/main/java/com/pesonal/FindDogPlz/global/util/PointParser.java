package com.pesonal.FindDogPlz.global.util;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class PointParser {
    public static Point parsePoint(final Double latitude, final Double longitude) {
        try {
            return parseUsingReader(latitude, longitude);
        } catch (ParseException e) {
            throw new CustomException(ErrorCode.POINT_PARSING_ERROR);
        }
    }

    private static Point parseUsingReader(final Double latitude, final Double longitude) throws ParseException {
        if (latitude == null && longitude == null) {
            throw new CustomException(ErrorCode.POINT_PARSING_ERROR);
        }
        return (Point) new WKTReader().read(String.format("POINT(%s %s)", longitude, latitude));
    }
}
