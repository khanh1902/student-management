package com.springbot.tttn.application.utils;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.text.DateFormatSymbols;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Helper {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static Map PageToMap(Page page) {
        Map<String, Object> map = new HashMap<>();
        map.put("content", page.getContent());
        map.put("pageIndex", page.getNumber() + 1);
        map.put("pageSize", page.getSize());
        map.put("count", page.getTotalElements());
        return map;
    }

    public static String generateEmail(String mssv) {
        return mssv + "@ut.edu.vn";
    }

    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static String upperFirstCharacter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }


}
