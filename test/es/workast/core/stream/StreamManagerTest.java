package es.workast.core.stream;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.workast.activity.Activity;
import es.workast.stream.StreamManager;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class StreamManagerTest {

    @Resource
    StreamManager streamManager;

    @Test
    public void testSearchBlogByKeyword() throws Exception {
        List<Activity> items = streamManager.getStream(1L);
        System.out.println("-");
        for (Activity item : items) {
            System.out.println(item.getIconUrl() + item.getTitle() + " (" + item.getPostedTime() + ")");
            if (item.hasBody()) {
                System.out.println(item.getBody());
            }
            System.out.println("-");
        }
    }
}
