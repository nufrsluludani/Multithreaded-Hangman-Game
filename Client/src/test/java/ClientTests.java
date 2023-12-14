import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ClientTests {

    SerializedData test = new SerializedData();

    @Test
    void testSetCategory(){
        test.setCategory("JMRBands");
        assertEquals(test.getCategory(), "JMRBands");
    }

    @Test
    void testSetUpdate(){
        test.setUpdate("Category");
        assertEquals(test.getUpdate(), "Category");
    }

    @Test
    void testSetClientNum(){
        test.setClientNum(1);
        assertEquals(test.getClientNum(), 1);
    }

    @Test
    void testSetDisplay(){
        test.setDisplay("toe");
        assertEquals(test.getDisplay(), "toe");
    }

    @Test
    void testSetChar(){
        test.setChar('a');
        assertEquals(test.getChar(), 'a');
    }

    @Test
    void testDecrementGuess(){
        test.decrementGuess();
        assertEquals(test.getGuess(), 5);
    }

    @Test
    void testSetAttempts(){
        test.setAttempts(6);
        assertEquals(test.getAttempts(), 6);
    }

    @Test
    void testSetCurrentCategory(){
        test.setCurrentCategory("Food");
        assertEquals(test.getCurrentCategory(), "Food");
    }

    @Test
    void testGetCategory(){
        test.setCategory("Scampire Beats");
        assertEquals(test.getCategory(), "Scampire Beats");
    }

    @Test
    void testGetAttempts(){
        test.setAttempts(6);
        assertEquals(test.getAttempts(), 6);
    }

}
