package com.jianjun.jianjuntest.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

public class Common {

	static final Locale DEF_LOCALE = Locale.getDefault();
	static final TimeZone DEF_TIME_ZONE = TimeZone.getDefault();

	/**
	 * 生成订单编号
	 */
	public static String generateOrderNo() {
		return format("yyyyMMdd-HHmmssSSS", new Date()) + '-' + getFourRandom();
	}

	/**
	 * 获取距离某个时间i天的开始时间
	 * 
	 * @param i
	 * @return
	 */
	public static Date getBeginDayOfSomeDay(Date date, int i) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayStartTime(date));
		cal.add(Calendar.DAY_OF_MONTH, i);
		return cal.getTime();
	}

	/**
	 * 获取距离某个时间i天的结束时间
	 * 
	 * @param i
	 * @return
	 */
	public static Date getEndDayOfSomeDay(Date date, int i) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayEndTime(date));
		cal.add(Calendar.DAY_OF_MONTH, i);
		return cal.getTime();
	}

	/**
	 * 计算2个日期相差的天数
	 * 
	 * @param start 开始日期 （不能为null）
	 * @param end 结束日期 （如果不穿则为当前日期）
	 * @return
	 */
	public static int differentDaysByMillisecond(Date start, Date end) {
		if (start == null) {
			throw new IllegalArgumentException("开始日期不能为空!");
		}
		if (end == null) {
			end = new Date();
		}
		int days = (int) ((end.getTime() - start.getTime()) / (1000 * 3600 * 24));
		return days;
	}

	/**
	 * 产生4位随机数(0000-9999)
	 * 
	 * @return 4位随机数
	 */
	public static String getFourRandom() {
		Random random = new Random();
		String fourRandom = random.nextInt(10000) + "";
		int randLength = fourRandom.length();
		if (randLength < 4) {
			for (int i = 1; i <= 4 - randLength; i++)
				fourRandom = "0" + fourRandom;
		}
		return fourRandom;
	}

	/**
	 * 将指定的日期解析为指定的格式化字符串
	 */
	public static String format(final String pattern, final Date date) {
		if (date == null) {
			return "";
		}
		return FastDateFormat.getInstance(pattern, DEF_TIME_ZONE, DEF_LOCALE).format(date);
	}

	/**
	 * 将指定的日期解析为指定的格式化字符串
	 */
	public static Date formatString(final String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 小写的mm表示的是分钟
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("日期转换错误!");
		}
	}

	/**
	 * 将指定的日期解析为指定的格式化字符串
	 */
	public static Date formatStringOld(final String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 小写的mm表示的是分钟
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("日期转换错误!");
		}
	}

	/**
	 * 将指定的日期解析为指定的格式化字符串
	 */
	public static Date formatStringNew(final String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 小写的mm表示的是分钟
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("日期转换错误!");
		}
	}

	/**
	 * 获取当天的开始时间
	 * 
	 * @return
	 */
	public static Date getDayBegin() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取当天的结束时间
	 * 
	 * @return
	 */
	public static Date getDayEnd() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	/**
	 * 获取距离今天i天的开始时间
	 * 
	 * @param i
	 * @return
	 */
	public static Date getBeginDayOfDay(int i) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayBegin());
		cal.add(Calendar.DAY_OF_MONTH, i);
		return cal.getTime();
	}

	/**
	 * 获取距离今天i天的结束时间
	 * 
	 * @param i
	 * @return
	 */
	public static Date getEndDayOfDay(int i) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayEnd());
		cal.add(Calendar.DAY_OF_MONTH, i);
		return cal.getTime();
	}

	// 获取本周的开始时间
	public static Date getBeginDayOfWeek() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return getDayStartTime(cal.getTime());
	}

	// 获取本周的结束时间
	public static Date getEndDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}

	// 获取本月的开始时间
	public static Date getBeginDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 1, 1);
		return getDayStartTime(calendar.getTime());
	}

	// 获取本月的结束时间
	public static Date getEndDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 1, 1);
		int day = calendar.getActualMaximum(5);
		calendar.set(getNowYear(), getNowMonth() - 1, day);
		return getDayEndTime(calendar.getTime());
	}

	// 获取本年的开始时间
	public static Date getBeginDayOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getNowYear());
		// cal.set
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		return getDayStartTime(cal.getTime());
	}

	// 获取本年的结束时间
	public static Date getEndDayOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getNowYear());
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DATE, 31);
		return getDayEndTime(cal.getTime());
	}

	// 获取某个日期的开始时间
	public static Date getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d)
			calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return calendar.getTime();
	}

	// 获取某个日期的结束时间
	public static Date getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d)
			calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		return calendar.getTime();
	}

	// 获取今年是哪一年
	public static Integer getNowYear() {
		Date date = new Date();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		return Integer.valueOf(gc.get(1));
	}

	// 获取本月是哪一月
	public static int getNowMonth() {
		Date date = new Date();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		return gc.get(2) + 1;
	}

	// 计算两日期相差毫秒
	public static Long calTimeInterval(Date date1, Date date2) {
		return Math.abs(date1.getTime() - date2.getTime());
	}

	/**
	 * 汉语中数字大写
	 */
	private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆",
			"伍", "陆", "柒", "捌", "玖" };
	/**
	 * 汉语中货币单位大写，这样的设计类似于占位符
	 */
	private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元",
			"拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
			"佰", "仟" };
	/**
	 * 特殊字符：整
	 */
	private static final String CN_FULL = "整";
	/**
	 * 特殊字符：负
	 */
	private static final String CN_NEGATIVE = "负";
	/**
	 * 金额的精度，默认值为2
	 */
	private static final int MONEY_PRECISION = 2;
	/**
	 * 特殊字符：零元整
	 */
	private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

	/**
	 * 把输入的金额转换为汉语中人民币的大写
	 *
	 * @param numberOfMoney
	 *            输入的金额
	 * @return 对应的汉语大写
	 */
	public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
		StringBuffer sb = new StringBuffer();
		// -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
		// positive.
		int signum = numberOfMoney.signum();
		// 零元整的情况
		if (signum == 0) {
			return CN_ZEOR_FULL;
		}
		// 这里会进行金额的四舍五入
		long number = numberOfMoney.movePointRight(MONEY_PRECISION)
				.setScale(0, 4).abs().longValue();
		// 得到小数点后两位值
		long scale = number % 100;
		int numUnit = 0;
		int numIndex = 0;
		boolean getZero = false;
		// 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
		if (!(scale > 0)) {
			numIndex = 2;
			number = number / 100;
			getZero = true;
		}
		if ((scale > 0) && (!(scale % 10 > 0))) {
			numIndex = 1;
			number = number / 10;
			getZero = true;
		}
		int zeroSize = 0;
		while (true) {
			if (number <= 0) {
				break;
			}
			// 每次获取到最后一个数
			numUnit = (int) (number % 10);
			if (numUnit > 0) {
				if ((numIndex == 9) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
				}
				if ((numIndex == 13) && (zeroSize >= 3)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
				}
				sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				getZero = false;
				zeroSize = 0;
			} else {
				++zeroSize;
				if (!(getZero)) {
					sb.insert(0, CN_UPPER_NUMBER[numUnit]);
				}
				if (numIndex == 2) {
					if (number > 0) {
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					}
				} else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
				}
				getZero = true;
			}
			// 让number每次都去掉最后一个数
			number = number / 10;
			++numIndex;
		}
		// 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
		if (signum == -1) {
			sb.insert(0, CN_NEGATIVE);
		}
		// 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
		if (!(scale > 0)) {
			sb.append(CN_FULL);
		}
		return sb.toString();
	}


	/**
	 * list转JsonArray对象
	 * 
	 * @param list
	 * @return
	 */
	public static JSONArray listToJSONArray(List<?> list) {
		JSONArray jsr = new JSONArray();
		for (Object object : list) {
			jsr.add(JSONObject.toJSON(object));
		}
		return jsr;
	}

	/**
	 * 获取本机IP
	 * 
	 * @return
	 */
	public static String getLocalIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			return "";
		}
	}
}
