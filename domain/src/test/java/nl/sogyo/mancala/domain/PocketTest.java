package nl.sogyo.mancala.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14})
    public void testAllPocketNr(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(pocketNr);
        int pocketNrFound = pocketFound.pocketNr;
        assertEquals(pocketNr, pocketNrFound);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13})
    public void testAllPocketStones(int PocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(PocketNr);
        int stones = pocketFound.stones;
        assertEquals(stones, 4);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13})
    public void testAllNextPocketNumbers(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(pocketNr);
        int pocketNrNext = pocketFound.nextPocket.pocketNr;
        assertEquals(pocketNr+1, pocketNrNext);
    }

    // KIJKEN OF IK DIT WIL HOUDEN OF DAT HET OKE IS DAT HIJ 15 ROEPT EN DAN GEWOON 1 PAKT
    @Test
    public void testThirteenPocketsNotPossible() {
        Pocket pocket = new Pocket();
        assertThrows(OngeldigBordException.class, () -> {
            pocket.getPocketFinder(15);
        });
    }

    @Test
    public void testLastPocketHasFirstPocketAsNext() {
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(14);
        assertEquals(pocketFound.nextPocket.pocketNr, 1);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testMancalaHasZeroStones(int pocketNrMancala){
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(pocketNrMancala);
        assertEquals(pocketFound.stones, 0);
    }

    @Test
    public void testMoveStonesFromFirstPocketThenStonesIsZero(){
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(1);
        pocketFound.setMoveStones();
        assertEquals(0, pocketFound.stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5})
    public void testMoveStonesFromFirstPocketThenNextPocketsAreFiveStones(int nextPocketWithFiveStones){
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(1);
        FirstPocket.setMoveStones();
        PocketAbstract nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(5, nextPockets.stones);
    }
    @ParameterizedTest
    @ValueSource(ints = {6, 8, 9, 10, 11, 12, 13})
    public void testMoveStonesFromFirstPocketThenAllOtherPocketsAreStillFourStonesExceptTwoToFive(int nextPocketWithFiveStones){
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(1);
        FirstPocket.setMoveStones();
        PocketAbstract nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(4, nextPockets.stones);
    }
    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testMoveStonesFromFirstPocketThenBothMancalasAreStillZeroStones(int nextPocketWithFiveStones){
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(1);
        FirstPocket.setMoveStones();
        PocketAbstract nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(0, nextPockets.stones);
    }

    @ParameterizedTest
    @CsvSource({"6, 0", "7, 1", "8, 5", "9, 5", "10, 5", "11, 4", "12, 4", "13, 4"
            , "14, 0", "1, 4", "2, 4", "3, 4", "4, 4", "5, 4"})
    public void testMoveStonesFromSixthPocketAndCheckStonesInAllOtherPockets(int nextPocketToCheck, int stones){
        Pocket pocket = new Pocket();
        PocketAbstract SixthPocket = pocket.getPocketFinder(6);
        SixthPocket.setMoveStones();
        PocketAbstract nextPockets = pocket.getPocketFinder(nextPocketToCheck);
        assertEquals(nextPockets.stones, stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerOneCanPlayPocketOneToSix(int pocketnr){
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(pocketnr);
        boolean validMove = FirstPocket.canIDoMove();
        assertTrue(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerOneCanNotPlayPocketEightToThirteen(int pocketnr){
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(pocketnr);
        boolean validMove = FirstPocket.canIDoMove();
        assertFalse(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerTwoCanPlayPocketEightToThirteen(int pocketnr){
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(pocketnr);
        FirstPocket.beurt.setChangeBeurt();
        boolean validMove = FirstPocket.canIDoMove();
        assertTrue(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerTwoCanNotPlayPocketOneToSix(int pocketnr){
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(pocketnr);
        FirstPocket.beurt.setChangeBeurt();
        boolean validMove = FirstPocket.canIDoMove();
        assertFalse(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerOneCanoNotPLayMancalas(int mancalaNr){
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(mancalaNr);
        boolean validMove = FirstPocket.canIDoMove();
        assertFalse(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerTwoCanoNotPLayMancalas(int mancalaNr){
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(mancalaNr);
        FirstPocket.beurt.setChangeBeurt();
        boolean validMove = FirstPocket.canIDoMove();
        assertFalse(validMove);
    }




}
