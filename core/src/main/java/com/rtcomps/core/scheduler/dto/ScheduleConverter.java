package com.rtcomps.core.scheduler.dto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtcomps.core.scheduler.def.DayOfWeek;
import com.rtcomps.core.scheduler.def.RepeatInterval;
import com.rtcomps.core.scheduler.def.TimeInterval;

@Component
public class ScheduleConverter implements Converter<String, ScheduleDto> {

	//String source = "{\"startTime\":\"09/22/2017 12:07 PM\",\"repeatInterval\":\"2\",\"repeatIntervalUnit\":\"Minutes\",\"numberOfTimes\":\"32\",\"weekdays\":[\"WEDNESDAY\",\"THURSDAY\",\"SATURDAY\"],\"endTime\":\"09/23/2017 12:07 PM\"}";

	@Override
	public ScheduleDto convert(String source) {
		ObjectMapper mapper = new ObjectMapper();
		ScheduleDto result = new ScheduleDto();
		try {
			JsonNode actualObj = mapper.readTree(source);
			result.setEndTime(convertToDate(actualObj.get("endTime").asText()));
			result.setStartTime(convertToDate(actualObj.get("startTime").asText()));
			JsonNode weekdaysNode = actualObj.get("weekdays");
			List<DayOfWeek> weekdays = new ArrayList<DayOfWeek>();
			if (weekdaysNode.isArray()) {
				for (final JsonNode objNode : weekdaysNode) {
					weekdays.add(DayOfWeek.valueOf(objNode.asText()));
				}
			}
			result.setWeekdays(weekdays);
			RepeatInterval repeatInterval = new RepeatInterval(
					Integer.parseInt(actualObj.get("repeatInterval").asText()),
					TimeInterval.valueOf(actualObj.get("repeatIntervalUnit").asText()),
					Integer.parseInt(actualObj.get("numberOfTimes").asText()));
			result.setRepeatInterval(repeatInterval);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private Date convertToDate(String dateStr) throws ParseException {
		return DateUtils.parseDate(dateStr, "MM/dd/yyyy hh:mm a");
	}

}