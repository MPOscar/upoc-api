package rondanet.upoc.core.utils.converters;

import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ReadingConverter
public class ArrayStringToStringCustomConverter implements Converter<List<ObjectId>, String> {

  @Override
  public String convert(List<ObjectId> arrayObjects) {
    List<String> arrayString = new ArrayList<>();
    for (ObjectId objectId: arrayObjects) {
     arrayString.add(objectId.toString());
    }
    return arrayString.stream().collect(Collectors.joining(","));
  }

} 