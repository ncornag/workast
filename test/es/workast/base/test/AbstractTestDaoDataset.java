package es.workast.base.test;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Sergio R. Gianazza
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-config.xml" })
public abstract class AbstractTestDaoDataset {

    @Resource
    private DataSource datasource;

    @Before
    public void init() throws Exception {
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(),getDataSet());
    }

    @After
    public void destroy() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(getConnection(), getDataSet());

    }

    private IDatabaseConnection getConnection() throws Exception {
        Connection conn = datasource.getConnection();

        IDatabaseConnection idbConn = new DatabaseConnection(conn);

        return idbConn;
    }

    private IDataSet getDataSet() throws Exception {
        URL url = ClassLoader.getSystemResource(getDataSetPath());
        return new FlatXmlDataSet(new File(url.getFile()));
    }

    public abstract String getDataSetPath();
}
