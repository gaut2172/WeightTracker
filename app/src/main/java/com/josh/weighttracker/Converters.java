package com.josh.weighttracker;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Room Database class to convert special data types to something Room can use
 */
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
