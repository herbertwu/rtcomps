package com.rtcomps.core.scheduler.base;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;

import com.rtcomps.core.scheduler.def.DayOfWeek;
import com.rtcomps.core.scheduler.def.RepeatInterval;

public class CronTriggerFactory {
	
	public static Trigger createTrigger(Date startTime,Date endTime,RepeatInterval repeatInterval,List<DayOfWeek> weekdays ){
		DateBuilder.IntervalUnit intervalUnit = deriveRepeatInterval(repeatInterval);
		Set<Integer> daysOfTheWeek = deriveDayOfWeek(weekdays);
		Trigger trigger = newTrigger().withSchedule(DailyTimeIntervalScheduleBuilder
						.dailyTimeIntervalSchedule()
						.startingDailyAt(TimeOfDay.hourAndMinuteFromDate(startTime))
						.withInterval(repeatInterval.repeatInterval, intervalUnit)
						.withRepeatCount(repeatInterval.numberOfTimes)
						.onDaysOfTheWeek(daysOfTheWeek))
						.endAt(endTime)
						.build();
				
		return trigger;
	}

	private static DateBuilder.IntervalUnit deriveRepeatInterval(RepeatInterval repeatInterval) {
		DateBuilder.IntervalUnit intervalUnit = null;
		switch (repeatInterval.repeatIntervalUnit) {
		case Hours:
			intervalUnit = DateBuilder.IntervalUnit.HOUR;
			break;
		case Minutes:
			intervalUnit = DateBuilder.IntervalUnit.MINUTE;
			break;
		case Seconds:
			intervalUnit = DateBuilder.IntervalUnit.SECOND;
			break;
		default:
			throw new RuntimeException("Invalid Interval unit " + repeatInterval.repeatIntervalUnit);
		}
		return intervalUnit;
	}

	private static Set<Integer> deriveDayOfWeek(List<DayOfWeek> weekdays) {
		Set<Integer> daysOfWeek = new HashSet<Integer>();
		for(DayOfWeek day: weekdays){
			Integer dayofTheWeek = null;
			switch (day) {
			case MONDAY:
				dayofTheWeek = Calendar.MONDAY;
				break;
			case TUESDAY:
				dayofTheWeek = Calendar.TUESDAY;
				break;
			case WEDNESDAY:
				dayofTheWeek = Calendar.WEDNESDAY;
				break;
			case THURSDAY:
				dayofTheWeek = Calendar.THURSDAY;
				break;
			case FRIDAY:
				dayofTheWeek = Calendar.FRIDAY;
				break;
			case SATURDAY:
				dayofTheWeek = Calendar.SATURDAY;
				break;
			case SUNDAY:
				dayofTheWeek = Calendar.SUNDAY;
				break;				
			default:
				throw new RuntimeException("Invalid day of the week " + day);
			}
			daysOfWeek.add(dayofTheWeek);
		}
		return daysOfWeek;
	}
	

	
}
