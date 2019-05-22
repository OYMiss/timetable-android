package edu.cczu.table.db.converter;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WeekListConverter {
    @TypeConverter
    public List<Integer> toList(String s) {
        return Arrays.stream(s.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @TypeConverter
    public String toString(List<Integer> s) {
        System.out.println(s);
        return s.stream().map(Object::toString)
                .reduce((result, x) -> result + "," + x).get();
    }
}
