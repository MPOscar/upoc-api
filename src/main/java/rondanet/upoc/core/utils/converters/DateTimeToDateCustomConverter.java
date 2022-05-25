package rondanet.upoc.core.utils.converters;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ReadingConverter
public class DateTimeToDateCustomConverter implements Converter<DateTime, Date> {

  @Override
  public Date convert(DateTime dateTime) {
    return dateTime.toDate();
  }

} 