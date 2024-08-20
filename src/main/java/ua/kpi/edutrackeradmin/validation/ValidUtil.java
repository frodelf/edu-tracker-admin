package ua.kpi.edutrackeradmin.validation;

public class ValidUtil {
    public static boolean notNullAndBlank(String text) {
        return text != null && !text.isBlank();
    }
}