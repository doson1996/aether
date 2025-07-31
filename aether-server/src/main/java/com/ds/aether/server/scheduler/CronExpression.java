package com.ds.aether.server.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ds
 * @date 2025/7/31
 * @description
 */
public class CronExpression {
    private final String expression;
    private final List<FieldParser> parsers;

    public CronExpression(String expression) {
        this.expression = expression;
        this.parsers = new ArrayList<>();
        parseExpression();
    }

    private void parseExpression() {
        String[] parts = expression.split("\\s+");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Cron表达式必须包含6个字段: 秒 分 时 日 月 周");
        }

        parsers.add(new FieldParser(parts[0], 0, 59));  // 秒
        parsers.add(new FieldParser(parts[1], 0, 59));  // 分
        parsers.add(new FieldParser(parts[2], 0, 23));  // 时
        parsers.add(new FieldParser(parts[3], 1, 31));  // 日
        parsers.add(new FieldParser(parts[4], 1, 12));  // 月
        parsers.add(new FieldParser(parts[5], 1, 7));   // 周
    }

    public boolean matches(LocalDateTime dateTime) {
        return parsers.get(0).matches(dateTime.get(ChronoField.SECOND_OF_MINUTE)) &&
                parsers.get(1).matches(dateTime.get(ChronoField.MINUTE_OF_HOUR)) &&
                parsers.get(2).matches(dateTime.get(ChronoField.HOUR_OF_DAY)) &&
                parsers.get(3).matches(dateTime.get(ChronoField.DAY_OF_MONTH)) &&
                parsers.get(4).matches(dateTime.get(ChronoField.MONTH_OF_YEAR)) &&
                parsers.get(5).matches(dateTime.get(ChronoField.DAY_OF_WEEK));
    }

    private static class FieldParser {
        private final List<Integer> values = new ArrayList<>();

        public FieldParser(String field, int min, int max) {
            parseField(field, min, max);
        }

        private void parseField(String field, int min, int max) {
            if ("*".equals(field)) {
                for (int i = min; i <= max; i++) {
                    values.add(i);
                }
            } else if (field.contains("/")) {
                String[] parts = field.split("/");
                int start = "*".equals(parts[0]) ? min : Integer.parseInt(parts[0]);
                int step = Integer.parseInt(parts[1]);
                for (int i = start; i <= max; i += step) {
                    values.add(i);
                }
            } else if (field.contains("-")) {
                String[] parts = field.split("-");
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                for (int i = start; i <= end; i++) {
                    values.add(i);
                }
            } else if (field.contains(",")) {
                String[] parts = field.split(",");
                for (String part : parts) {
                    values.add(Integer.parseInt(part));
                }
            } else {
                values.add(Integer.parseInt(field));
            }
        }

        public boolean matches(int value) {
            return values.contains(value);
        }
    }
}
