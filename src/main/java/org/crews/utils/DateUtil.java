package org.crews.utils;

import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Component
public class DateUtil {
    public static int getLastDayOfMonth(int year, int month) {
        // YearMonth 객체를 생성하여 해당 월의 마지막 날을 반환
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }
    public static LocalDateTime generateStandardDate(Integer year, Integer month, LocalDateTime dueDate) {
        if((dueDate.getMonthValue() >= month && dueDate.getYear() == year) || dueDate.getYear() > year) {
            return LocalDateTime.of(year,month,dueDate.getDayOfMonth(),dueDate.getHour(),dueDate.getMinute(),dueDate.getSecond());
        }
        throw new CustomException(ErrorCode.DATE_AFTER_NOW);
    }
}
