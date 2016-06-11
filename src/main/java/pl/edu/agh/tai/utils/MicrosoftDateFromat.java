package pl.edu.agh.tai.utils;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public class MicrosoftDateFromat extends DateFormat {

    @Override
    public StringBuffer format(Date date, StringBuffer buffer, FieldPosition field) {
        long millis = date.getTime();

        return new StringBuffer("/Date(" + millis + ")/");
    }

    @Override
    public Date parse(String string, ParsePosition position) {
        int start = string.indexOf("(") + 1;
        int end = string.indexOf(")");

        String ms = string.substring(start, end);
        Date date = new Date(Long.parseLong(ms));

        position.setIndex(string.length() - 1); // MUST SET THIS
        return date;
    }

    @Override
    public Object clone() {
        return new MicrosoftDateFromat(); // MUST SET THIS
    }

}