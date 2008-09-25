package es.workast.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * @author Nicolás Cornaglia
 */
public class PersistentDateTimeFromString extends org.joda.time.contrib.hibernate.PersistentDateTime {

    public final static int OFFSET = (new org.joda.time.DateTime()).getZone().getOffset(0);

    @Override
    public Object nullSafeGet(ResultSet resultSet, String string) throws SQLException {
        String timestamp = (String) Hibernate.STRING.nullSafeGet(resultSet, string);

        if (timestamp == null) {
            return null;
        }

        return new DateTime(timestamp, DateTimeZone.forID("UTC"));
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            Hibernate.STRING.nullSafeSet(preparedStatement, null, index);
        } else {
            Hibernate.STRING.nullSafeSet(preparedStatement, ((DateTime) value).toString(), index);
        }
    }

}
