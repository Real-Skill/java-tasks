package io.realskill.tasks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests that checks if user did not change anything in fish class template
 */
public class BasicFishClassTest {

    @Test
    public void classFishHavePropertyName() throws Exception {
        Field nameField = Fish.class.getDeclaredField("name");
        assertFalse(nameField.isAccessible());
        assertTrue(Modifier.isFinal(nameField.getModifiers()));
        assertNotNull(Fish.class.getMethod("getName"));
    }

    @Test
    public void classFishHavePropertyPredator() throws Exception {
        Field predatorField = Fish.class.getDeclaredField("predator");
        assertFalse(predatorField.isAccessible());
        assertNotNull(Fish.class.getMethod("getPredator"));
    }

    @Test
    public void classFishImplementsComparable() throws Exception {
        assertNotNull(Fish.class.getInterfaces());
    }

    @Test
    public void classFishHaveConstructorWithString() throws Exception {
        assertNotNull(Fish.class.getConstructor(String.class));
    }

    @Test
    public void classFishHaveConstructorWithStringAndBoolean() throws Exception {
        assertNotNull(Fish.class.getConstructor(String.class, Boolean.class));
    }
}
