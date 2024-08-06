package com.pesonal.FindDogPlz.global.util;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class PointParser {
    public static Point parsePoint(Double latitude, Double longitude) {
        try {
            return latitude != null && longitude != null ?
                    (Point) new WKTReader().read(String.format("POINT(%s %s)", longitude, latitude))
                    : null;
        } catch (ParseException e) {
            throw new CustomException(ErrorCode.POINT_PARSING_ERROR);
        }
    }
}
