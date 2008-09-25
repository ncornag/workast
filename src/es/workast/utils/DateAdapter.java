package es.workast.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {

    private DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    public Date unmarshal(String date) throws Exception {
        return df.parse(date);
    }

    public String marshal(Date date) throws Exception {
        return df.format(date);
    }
}
