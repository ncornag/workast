package es.workast.core.persistence.hibernate;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.workast.dao.person.PersonDao;
import es.workast.model.person.Person;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-config.xml" })
public class PersonDaoTest {

    @Resource
    PersonDao personDao;

    @Test
    public void testSearchBlogByKeyword() {
        List<Person> persons = personDao.findAll();
        for (Person person : persons) {
            System.out.println(person.getDisplayName());
            for (Person followed : person.getFollowing()) {
                System.out.println("->" + followed.getDisplayName());
            }
            for (Person follower : person.getFollowers()) {
                System.out.println("<-" + follower.getDisplayName());
            }
        }
    }

}
