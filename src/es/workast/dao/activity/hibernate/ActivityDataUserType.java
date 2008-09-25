package es.workast.dao.activity.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import es.workast.model.activity.ActivityData;
import es.workast.model.activity.type.ActivityType;
import es.workast.service.activity.ActivityTypeHelper;

/**
 * @author Nicolás Cornaglia
 */
public class ActivityDataUserType implements UserType {

    private static final int[] SQL_TYPES = new int[] { Types.VARCHAR };

    // {"data":{"type":"NEWGROUP", "groupId":"8"}}
    private Pattern p = Pattern.compile("^.*type\":\"([A-Z]*)\".*$");

    public Object nullSafeGet(ResultSet resultSet, String[] strings, Object object) throws HibernateException, SQLException {
        String data = (String) Hibernate.STRING.nullSafeGet(resultSet, strings[0]);

        if (data == null || "".equals(data)) {
            return null;
        }

        Matcher m = p.matcher(data);
        m.find();

        String type = m.group(1);
        ActivityType activityType = ActivityTypeHelper.getActivityType(type);
        return activityType.getData(data);
    }

    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            Hibernate.STRING.nullSafeSet(preparedStatement, null, index);
        } else {
            ActivityType activityType = ActivityTypeHelper.getActivityType(((ActivityData) value).getType());
            Hibernate.STRING.nullSafeSet(preparedStatement, activityType.getData((ActivityData) value), index);
        }
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        ActivityData dtx = (ActivityData) x;
        ActivityData dty = (ActivityData) y;

        return dtx.equals(dty);
    }

    public int hashCode(Object object) throws HibernateException {
        return object.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Class returnedClass() {
        return ActivityData.class;
    }

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

}
