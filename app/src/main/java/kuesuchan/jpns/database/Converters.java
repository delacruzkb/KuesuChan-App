package kuesuchan.jpns.database;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import kuesuchan.jpns.database.entity.Source;
import kuesuchan.jpns.database.tuple.SourceTuple;
import kuesuchan.jpns.util.KuesuChanUtil;

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

    @TypeConverter
    public static Source.TYPE fromStringToSourceType(String str) {
        return Source.TYPE.valueOf(str);
    }

    @TypeConverter
    public static String sourceTypeToString(Source.TYPE type) {
        return type.name();
    }

    @TypeConverter
    public static Set<SourceTuple> fromStringToSourceTupleSet(String strs) {
        return Arrays.asList(strs.split(",")).stream().map(str ->{
            String[] values = str.split(KuesuChanUtil.SOURCE_TUPLE_DELIM);
            return new SourceTuple(values[0], Integer.parseInt(values[1]));
        }).collect(Collectors.toSet());
    }

    @TypeConverter
    public static String sourceTupleSetToString(Set<SourceTuple> sourceTuples) {
        StringBuilder rtnval = new StringBuilder();
        for ( SourceTuple sourceTuple : sourceTuples) {
            rtnval.append(sourceTuple.toString());
            rtnval.append(",");
        }
        rtnval.deleteCharAt(rtnval.lastIndexOf(","));
        return rtnval.toString();
    }

}
