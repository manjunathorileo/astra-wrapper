package com.orileo.attendance.wrapper.util;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

public class DateUtil implements Serializable {

    /**
     *
     */

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");

    private static final long serialVersionUID = -3224601291474978013L;


    public static Date getTodayDate() {
        Date currentDate = null;
        String dateStr = "";
        String day = "";
        String month = "";
        int year = 0;

        Calendar cal = new GregorianCalendar();
        TimeZone tz = TimeZone.getTimeZone("IST");
        cal.setTimeZone(tz);
        if (cal != null) {
            int dd = cal.get(Calendar.DAY_OF_MONTH);
            day = getDoubleDigits(dd);

            int mm = cal.get(Calendar.MONTH) + 1;
            month = getDoubleDigits(mm);

            year = cal.get(Calendar.YEAR);
        }

        dateStr = day + "/" + month + "/" + year;

        currentDate = convertToDate(dateStr);

        return currentDate;
    }

    public static String getDoubleDigits(int inputValue) {
        String returnValue = "";

        if (inputValue < 10) {
            returnValue = "0" + inputValue;
        } else {
            returnValue = "" + inputValue;
        }

        return returnValue;
    }

    public static Date convertToDate(String date) {
        Date convertedDate = null;
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException pe) {
        }
        return convertedDate;
    }

}