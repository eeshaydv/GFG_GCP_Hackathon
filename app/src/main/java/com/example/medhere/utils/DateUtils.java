package com.example.medhere.utils;


import java.util.Calendar;

public class DateUtils {

    /**
     * Return a string with the month name like it appears in the Julian and Gregorian calendars.
     *
     * @param month value is 0-based: 0 for January, 11 for December.
     * @return the month name like it appears in the Julian and Gregorian calendars as a string.
     */
    public static String monthToString(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return "January";

            case Calendar.FEBRUARY:
                return "Febraury";

            case Calendar.MARCH:
                return "March";

            case Calendar.APRIL:
                return "April";

            case Calendar.MAY:
                return "May";

            case Calendar.JUNE:
                return "June";

            case Calendar.JULY:
                return "July";

            case Calendar.AUGUST:
                return "August";

            case Calendar.SEPTEMBER:
                return "September";

            case Calendar.OCTOBER:
                return "October";

            case Calendar.NOVEMBER:
                return "November";

            case Calendar.DECEMBER:
                return "December";
        }
        return "";
    }

    /**
     * Return a string with the day name like it appears in the Julian and Gregorian calendars.
     *
     * @param day is the day of the week value.
     * @return the day of week name like it appears in the Julian and Gregorian calendars as a string.
     */
    public static String dayOfWeekToString(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                return "Sunday";

            case Calendar.MONDAY:
                return "Monday";

            case Calendar.TUESDAY:
                return "Tuesday";

            case Calendar.WEDNESDAY:
                return "Wednesday";

            case Calendar.THURSDAY:
                return "Thursday";

            case Calendar.FRIDAY:
                return "Friday";

            case Calendar.SATURDAY:
                return "Saturday";
        }
        return "";
    }
}
