package rondanet.upoc.core.utils.converters;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ReadingConverter
public class DateToDateTimeCustomConverter implements Converter<Date, DateTime> {

  @Override
  public DateTime convert(Date date) {
    return new DateTime(date);
  }

} 