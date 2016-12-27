package io.realskill.tasks;

import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.database.annotations.TestDataSource;
import org.unitils.dbunit.annotation.DataSet;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import org.unitils.reflectionassert.ReflectionComparatorMode;

@DataSet("dataSeed.xml")
public class TaskIT extends UnitilsJUnit4 {

    @TestDataSource
    private DataSource dataSource;

    @Test
    public void getAllFishNamesMethodTest() {
        GenericDAO dao = new GenericDAO();
        dao.establishDbConnection();

        List<String> firstTaskResultList = dao.getAllFishNames();

        assertReflectionEquals("Error while testing getAllFishNames method",
                Arrays.asList("Nemo", "Sharky", "Bandit", "Biggie", "Cody", "Bella", "Rush", "Larch", "Wrigley", "Disco", "Zim", "Kelsey", "Senior"),
                firstTaskResultList,
                ReflectionComparatorMode.LENIENT_ORDER);
        dao.closeDbConnection();
    }

    @Test
    public void getFishInAquariumMethodTest() {
        GenericDAO dao = new GenericDAO();
        dao.establishDbConnection();

        List<String> secondTaskResultList = dao.getFishInAquarium();
        assertReflectionEquals("Error while testing getFishInAquarium method",
                Arrays.asList("Sharky", "Bandit", "Biggie", "Cody", "Bella", "Rush", "Larch", "Zim", "Senior"),
                secondTaskResultList,
                ReflectionComparatorMode.LENIENT_ORDER);

        dao.closeDbConnection();
    }

    @Test
    public void findOldestFishMethodTest() {
        GenericDAO dao = new GenericDAO();
        dao.establishDbConnection();
        String thirdTaskResult = dao.findOldestFish();
        assertEquals("Senior", thirdTaskResult);

        dao.closeDbConnection();
    }

    @Test
    public void getAgeOfFishByNameTest() {
        GenericDAO dao = new GenericDAO();
        dao.establishDbConnection();
        Integer fourthTaskResult = dao.getFishAgeByName("Senior");
        assertTrue("Error while testing getAgeOfFishByName method", fourthTaskResult == 4);

        dao.getFishAgeByName("Senior'; DELETE FROM FISH WHERE 1='1");
        fourthTaskResult = dao.getFishAgeByName("Senior");

        assertTrue("Error, code is vulnerable to sql injection", fourthTaskResult == 4);
    }
}
