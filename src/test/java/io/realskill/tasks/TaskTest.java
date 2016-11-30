package io.realskill.tasks;

import org.junit.Assert;
import org.junit.Test;

public class TaskTest {

    @Test
    public void firstCase() throws Exception {
        Aquarium firstCaseAquarium = new Aquarium();

        Fish nemo = new Fish("Nemo");
        Fish swifty = new Fish("Swifty");
        Fish pati = new Fish("Pati");
        Fish andy = new Fish("Andy");
        Fish boris = new Fish("Boris");

        firstCaseAquarium.add(nemo);
        firstCaseAquarium.add(swifty);
        firstCaseAquarium.add(pati);
        firstCaseAquarium.add(andy);
        firstCaseAquarium.add(boris);
        firstCaseAquarium.add(new Fish("Boris"));

        Assert.assertTrue("Only universal named fish can be stored in same aquarium", firstCaseAquarium.size() == 5);
    }

    @Test
    public void secondCase() throws Exception {
        Aquarium secondCaseAquarium = new Aquarium();
        secondCaseAquarium.add(new Fish("Nemo"));
        secondCaseAquarium.add(new Fish("Swifty"));
        secondCaseAquarium.add(new Fish("Sharky", Boolean.TRUE));

        Assert.assertTrue("Predator fish can not be put in same aquarium as non predator fish", secondCaseAquarium.size() == 2);
    }

    @Test
    public void thirdCase() throws Exception {
        Aquarium thirdCaseAquarium = new Aquarium();
        thirdCaseAquarium.add(new Fish("Pira", Boolean.TRUE));
        thirdCaseAquarium.add(new Fish("Sharky", Boolean.TRUE));
        thirdCaseAquarium.add(new Fish("Nemo"));

        Assert.assertTrue("Non predator fish can not be put in same aquarium as predator fish", thirdCaseAquarium.size() == 2);
    }

}
