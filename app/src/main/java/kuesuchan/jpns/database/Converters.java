package kuesuchan.jpns.database;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Converters {

    @TypeConverter
    public static Set<String> fromStringToStringSet(String strs) {
        return new HashSet<>(Arrays.asList(strs.split(",")));
    }

    @TypeConverter
    public static String stringSetToString(Set<String> strings) {
        StringBuilder rtnval = new StringBuilder();
        for ( String str: strings) {
            rtnval.append(str);
            rtnval.append(",");
        }
        rtnval.deleteCharAt(rtnval.lastIndexOf(","));
        return rtnval.toString();
    }

    @TypeConverter
    public static Set<Integer> fromStringToIntegerSet(String strs) {
        return new HashSet<>(Arrays.asList(strs.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList()));
    }

    @TypeConverter
    public static String integerSetToString(Set<Integer> integers) {
        StringBuilder rtnval = new StringBuilder();
        for ( Integer integer: integers) {
            rtnval.append(integer);
            rtnval.append(",");
        }
        rtnval.deleteCharAt(rtnval.lastIndexOf(","));
        return rtnval.toString();
    }
}
