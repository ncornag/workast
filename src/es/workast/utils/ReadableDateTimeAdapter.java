package es.workast.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

public class ReadableDateTimeAdapter extends XmlAdapter<String, ReadableDateTime> {

    private DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss 'GMT'+0000");

    @Override
    public ReadableDateTime unmarshal(String date) throws Exception {
        return new DateTime(date);
    }

    @Override
    public String marshal(ReadableDateTime date) throws Exception {
        return date.toString();
    }
}
