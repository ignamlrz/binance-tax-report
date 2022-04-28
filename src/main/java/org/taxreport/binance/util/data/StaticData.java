package org.taxreport.binance.util.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.taxreport.binance.util.io.IOManager;

@Data
public abstract class StaticData implements IntervalSearchable {
    protected long beginTime;
    protected long endTime;

    public static<T extends StaticData> void write(T data) throws JsonProcessingException {
        write(data, "");
    }
    public static<T extends StaticData> void write(T data, String prefix) throws JsonProcessingException {
        String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data);
        if(data.getBeginTime() == 0 || data.getEndTime() < data.getBeginTime()) {
            throw new RuntimeException("beginTime and endTime contain unexpected values");
        }
        IOManager.write(prefix + data.getClass().getSimpleName().replace("Response", ""), json);
    }

    public static<T extends StaticData> T read(Class<T> valueType) throws JsonProcessingException {
        return read(valueType, "");
    }
    public static<T extends StaticData> T read(Class<T> valueType, String prefix) throws JsonProcessingException {
        String result = IOManager.read(prefix + valueType.getSimpleName().replace("Response", ""));
        if(result == null)  return null;
        return new ObjectMapper().readValue(result, valueType);
    }
}
