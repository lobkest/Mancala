package nl.sogyo.mancala.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;

public class PocketTest {

    @Test
    public void testFirstPocketCreation() {
        Pocket pocket = new Pocket();
        assertTrue(pocket != null);
    }

    @Test
    public void testFirstPocketNumber() {
        Pocket pocket = new Pocket();
        assertEquals(pocket.pocketNr, 1);
    }

    @Test
    public void testFirstPocketStones() {
        Pocket pocket = new Pocket();
        assertEquals(pocket.stones, 4);
    }

    @Test
    public void testFirstPocketNextNr() {
        Pocket pocket = new Pocket();
        assertEquals(pocket.nextPocket.pocketNr, 2);
    }

    @Test
    public void testLastPocketNr() {
        Pocket pocket = new Pocket();
        Pocket pocketFound = pocket.getPocketFinder(12);
        int pocketNr = pocketFound.pocketNr;
        assertEquals(pocketNr, 12);
    }

    @Test
    public void testLastPocketStones() {
        Pocket pocket = new Pocket();
        Pocket pocketFound = pocket.getPocketFinder(12);
        int stones = pocketFound.stones;
        assertEquals(stones, 4);
    }


    @Test
    public void testThirteenPocketsNotPossible() {
        Pocket pocket = new Pocket();
        assertThrows(OngeldigBordException.class, () -> {
            pocket.getPocketFinder(15);
        });
    }


}
