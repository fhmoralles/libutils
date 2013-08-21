package br.com.libutils.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

public class DateUtil {

	private static final String CURRENTE_DATE_PROPERTY = "CURRENT_DATE";
	private static final String CURRENTE_TIME_PROPERTY = "CURRENT_TIME";
	private static final TimeZone TIMEZONE = TimeZone.getDefault();
	private static final Locale LOCALE_BRASIL = new Locale("pt", "BR");
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy");
	private static final SimpleDateFormat SIMPLE_DATE_HOUR_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy H:m:s");
	private static final SimpleDateFormat SIMPLE_HOUR_FORMAT = new SimpleDateFormat(
			"H:m:s");

	/***
	 * Retorna a data atual no caso do sistema em produção ou então, a data
	 * definida pela variável de sistema CURRENT_DATE (dd/MM/yyyy) para ser
	 * usado nos testes
	 * 
	 * @return Data atual sem hora
	 */
	public static Date today() {

		final String currentDate = System.getProperty(CURRENTE_DATE_PROPERTY);
		if (StringUtils.isNotBlank(currentDate)) {
			try {
				final Date today = SIMPLE_DATE_FORMAT.parse(currentDate);
				return resetDate(today);
			} catch (final ParseException e) {
				return getCurrentDate();
			}
		} else {
			return getCurrentDate();
		}
	}

	/**
	 * Retorna a data e hora atual ou então, a data/hora definida pela variável
	 * de sistema CURRENT_TIME (hh:mm:ss)
	 * 
	 * @return
	 */
	public static Date now() {
		final Date today = today();
		Date hour = getCurrentHour();
		final String systemCurrentTime = System
				.getProperty(CURRENTE_TIME_PROPERTY);
		if (StringUtils.isNotBlank(systemCurrentTime)) {
			try {
				hour = SIMPLE_HOUR_FORMAT.parse(systemCurrentTime);
			} catch (final ParseException e) {

			}
		}

		final Calendar currentTime = Calendar.getInstance();
		currentTime.setTime(hour);

		final Calendar currentDateHour = Calendar.getInstance(LOCALE_BRASIL);
		currentDateHour.setTimeZone(TIMEZONE);
		currentDateHour.setTime(today);
		currentDateHour.set(Calendar.HOUR_OF_DAY,
				currentTime.get(Calendar.HOUR_OF_DAY));
		currentDateHour.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE));
		currentDateHour.set(Calendar.SECOND, currentTime.get(Calendar.SECOND));
		currentDateHour.set(Calendar.MILLISECOND,
				currentTime.get(Calendar.MILLISECOND));

		return currentDateHour.getTime();
	}

	/***
	 * Retira a hora de uma determinada data/hora
	 * 
	 * @param date
	 * @return Retorna somente a data sem hora
	 */
	public static Date resetDate(final Date date) {
		return DateUtils.truncate(date, Calendar.DATE);
	}

	public static Date endOfDay(final Date date) {
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		Date aux = c.getTime();
		aux = DateUtils.setHours(aux, 23);
		aux = DateUtils.setMinutes(aux, 59);
		aux = DateUtils.setSeconds(aux, 59);
		aux = DateUtils.setMilliseconds(aux, 999);
		return aux;
	}

	/**
	 * verifica se a data passada por parametro é menor que data limite
	 * 
	 * @param days
	 * @param init
	 * @param compare
	 * @return
	 */
	public static boolean respectsSuperiorLimit(Date init, Integer days,
			Date compare) {
		final Date dataMax = getLimitDate(init, days);
		return compare.before(dataMax);
	}

	public static Date getLimitDate(Date init, Integer days) {
		return endOfDay(addDays(init, days));
	}

	/***
	 * Converte uma string no formato dd/MM/yyyy em data
	 * 
	 * @param date
	 * @return
	 */
	public static Date stringToDate(final String date) {
		try {
			return SIMPLE_DATE_FORMAT.parse(date);
		} catch (final ParseException e) {
			return today();
		}
	}

	public static Date stringToTime(final String time) {
		try {
			return resetTime(SIMPLE_HOUR_FORMAT.parse(time));
		} catch (final ParseException e) {
			return now();
		}
	}

	public static int compareTime(final Date time1, final Date time2) {
		if (time1 == null && time2 != null) {
			return 1;
		}
		if (time2 == null && time1 != null) {
			return -1;
		}

		if (time1 == null && time2 == null) {
			return 0;
		}

		return time1.compareTo(time2);
	}

	// HELPER METHODS

	private static Date getCurrentDate() {
		return resetDate(getCurrentHour());
	}

	private static Date getCurrentHour() {
		final Calendar c = Calendar.getInstance(LOCALE_BRASIL);
		c.setTimeZone(TIMEZONE);
		return c.getTime();
	}

	/**
	 * Remove a data deixando somente o horário
	 * 
	 * @param date
	 * @return
	 */
	private static Date resetTime(final Date date) {
		final Calendar c = Calendar.getInstance(LOCALE_BRASIL);
		c.setTimeZone(TIMEZONE);
		c.setTime(date);
		c.set(Calendar.YEAR, 1970);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * Adiciona um horário a uma data
	 * 
	 * @param date
	 * @return
	 */
	public static Date addTime(final Date date, final Date hour) {
		final Calendar c = Calendar.getInstance(LOCALE_BRASIL);
		c.setTimeZone(TIMEZONE);
		c.setTime(date);
		final Calendar d = Calendar.getInstance(LOCALE_BRASIL);
		d.setTimeZone(TIMEZONE);
		d.setTime(hour);

		c.set(Calendar.HOUR, d.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, d.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, d.get(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, d.get(Calendar.MILLISECOND));

		return c.getTime();
	}

	/**
	 * Adiciona um horário a uma data
	 * 
	 * @param date
	 * @return
	 */
	public static Date addDays(final Date date, final int amount) {
		final Calendar c = Calendar.getInstance(LOCALE_BRASIL);
		c.setTimeZone(TIMEZONE);
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, amount);
		return c.getTime();
	}

	public static int compareDate(final Date date1, final Date date2) {
		if (date1 == null && date2 == null) {
			return 0;
		} else if (date1 != null && date2 != null) {
			return date1.compareTo(date2);
		} else if (date1 != null && date2 == null) {
			return 1;
		} else if (date1 == null && date2 != null) {
			return -1;
		}
		return 0;
	}

	/*
	 * Método que retorna o próximo dia util da semana ignorando feriados,
	 * somente sabados e domingos
	 */
	public static Date getNextWorkDay(final Date date) {
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY) {
			c.add(Calendar.DAY_OF_MONTH, 2);
		} else if (dayOfWeek == Calendar.SUNDAY) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		return DateUtil.resetDate(c.getTime());
	}

	/*
	 * Método que retorna o dia util anterior da semana ignorando feriados,
	 * somente sabados e domingos
	 */
	public static Date getWorkDayBefore(final Date date) {
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -1);
		final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY) {
			c.add(Calendar.DAY_OF_MONTH, -1);
		} else if (dayOfWeek == Calendar.SUNDAY) {
			c.add(Calendar.DAY_OF_MONTH, -2);
		}
		return DateUtil.resetDate(c.getTime());
	}

	public static String dateToStringDate(final Date date) {
		return SIMPLE_DATE_FORMAT.format(date);
	}

	public static String dateToStringHour(final Date date) {
		return SIMPLE_HOUR_FORMAT.format(date);
	}

	public static String dateToStringDateHour(final Date date) {
		return SIMPLE_DATE_HOUR_FORMAT.format(date);
	}

	/*
	 * Método que retorna se a data é sabados ou domingos
	 */
	public static boolean isWorkDay(final Date date) {
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
			return false;
		}
		return true;
	}
}
