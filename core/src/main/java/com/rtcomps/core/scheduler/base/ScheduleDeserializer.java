package com.rtcomps.core.scheduler.base;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rtcomps.core.scheduler.def.DayOfWeek;
import com.rtcomps.core.scheduler.def.RepeatInterval;
import com.rtcomps.core.scheduler.def.TimeInterval;
import com.rtcomps.core.scheduler.dto.ScheduleDto;

public class ScheduleDeserializer extends StdDeserializer<ScheduleDto> {

	public  ScheduleDeserializer(Class<?> vc) {
		super(vc);
	}
	
	public ScheduleDeserializer() {
		this(null);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4048850707366498290L;

	@Override
	public ScheduleDto deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ScheduleDto result = new ScheduleDto();
		try {
			JsonNode actualObj =  p.getCodec().readTree(p);
		//	result.setEndTime(convertToDate(actualObj.get("endTime").asText()));
			Iterator<JsonNode> jsonNodes = actualObj.iterator();
			actualObj.fieldNames().hasNext();
			while(jsonNodes.hasNext()){
				JsonNode node = jsonNodes.next();
				node.asText();
			}
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
