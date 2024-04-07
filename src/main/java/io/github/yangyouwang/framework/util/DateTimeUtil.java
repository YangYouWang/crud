package io.github.yangyouwang.framework.util;

import cn.hutool.core.date.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description: 日期工具类<br/>
 * date: 2022/8/27 19:17<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
public class DateTimeUtil {
    /**MaNongXF
     * 判断时间是否处于某个时间段内
     *
     * @param time 需要比较的时间
     * @param from 起始时间
     * @param to 结束时间
     * @return
     */
    public static boolean belongCalendar(Date time, Date from, Date to) {
        Calendar date = Calendar.getInstance();
        date.setTime(time);
        Calendar after = Calendar.getInstance();
        after.setTime(from);
        Calendar before = Calendar.getInstance();
        before.setTime(to);
        if (date.after(after) && date.before(before)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取过去第几天的日期(- 操作) 或者 未来 第几天的日期( + 操作)
     *
     * @param past 天数
     * @return 未来日期
     */
    public static Date getAfterDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        return calendar.getTime();
    }


    /**
     * 日期差天数
     */
    public static Long getDisDay(String startDateStr){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date startDate = formatter.parse(startDateStr);
            Date endDate = new DateTime();
            // 租赁日期小于当前日期
            if(compare(startDate,endDate)) {
                return 0L;
            }
            long[] dis = getDisTime(startDate, endDate);
            long day = dis[0];
            if (dis[1] > 0 || dis[2] > 0 || dis[3] > 0) {
                day += 1;
            }
            return day;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 日期差天数、小时、分钟、秒数组
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return true
     */
    public static long[] getDisTime(Date startDate, Date endDate){
        long timesDis = Math.abs(startDate.getTime() - endDate.getTime());
        long day = timesDis / (1000 * 60 * 60 * 24);
        long hour = timesDis / (1000 * 60 * 60) - day * 24;
        long min = timesDis / (1000 * 60) - day * 24 * 60 - hour * 60;
        long sec = timesDis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        return new long[]{day, hour, min, sec};
    }

    /**
     * 比较日期
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return true
     */
    public static boolean compare(Date startDate,Date endDate) {
        return startDate.getTime() < endDate.getTime();
    }

    /**
     * 获取发送验证码后的失效时间
     * @return 失效时间
     */
    public static Date getSendSmsFailureTime() {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 5);
        return nowTime.getTime();
    }


    /**
     * 将时间转换为时间戳
     * @param time 时间
     * @return 时间戳
     */
    public static Long dateToStamp(String time) {
        //设置时间格式，将该时间格式的时间转换为时间戳
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 时间戳转日期
     * @param time 时间戳
     * @return 日期
     */
    public static String timeStamp2Date(String time) {
        Long timeLong = Long.parseLong(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(timeLong));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
