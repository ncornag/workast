package es.workast.core.persistence.hibernate;

import es.workast.base.test.AbstractTestDaoDataset;
import es.workast.dao.person.PendingPasswordDao;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sergio R. Gianazza
 */

public class PendingPasswordadoTest extends AbstractTestDaoDataset {

    @Resource
    PendingPasswordDao pendingPasswordDao;

    public PendingPasswordadoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


     @Test
     public void testDeletePendingPasswordSince() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int deletedItems = pendingPasswordDao.deletePendingPasswordSince(cal.getTime());

        
        assertEquals(5,deletedItems);
     }

    @Override
    public String getDataSetPath() {
        return "es/workast/core/persistence/hibernate/pending-password.xml";
    }

}