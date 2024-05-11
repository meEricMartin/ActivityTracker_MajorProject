package ToDoList.Resources.FXUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateConverter
{
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate valueOf(String val)
    {
        return LocalDate.parse(val, DATE_FORMAT);
    }

    public static String toString(LocalDate date)
    {
        return date == null ? null : date.format(DATE_FORMAT);
    }
}
