package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;
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
        assertNotNull(pocket);
    }

    @Test
    public void testFirstPocketNumber() {
        Pocket pocket = new Pocket();
        assertEquals(1, pocket.pocketNr);
    }

    @Test
    public void testFirstPocketStones() {
        Pocket pocket = new Pocket();
        assertEquals(4, pocket.stones);
    }

    @Test
    public void testFirstPocketNextNr() {
        Pocket pocket = new Pocket();
        assertEquals(2, pocket.nextPocket.pocketNr);
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
        assertEquals(4, stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13})
    public void testAllNextPocketNumbers(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(pocketNr);
        int pocketNrNext = pocketFound.nextPocket.pocketNr;
        assertEquals(pocketNr+1, pocketNrNext);
    }

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
        assertEquals(1, pocketFound.nextPocket.pocketNr);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testMancalaHasZeroStones(int pocketNrMancala){
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(pocketNrMancala);
        assertEquals(0, pocketFound.stones);
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
    public void testPlayerOneCanPlayPocketOneToSix(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        boolean validMove = PocketFound.canIDoMove();
        assertTrue(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerOneCanNotPlayPocketEightToThirteen(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        boolean validMove = PocketFound.canIDoMove();
        assertFalse(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerTwoCanPlayPocketEightToThirteen(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.beurt.setChangeBeurt();
        boolean validMove = PocketFound.canIDoMove();
        assertTrue(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerTwoCanNotPlayPocketOneToSix(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.beurt.setChangeBeurt();
        boolean validMove = PocketFound.canIDoMove();
        assertFalse(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerOneCanoNotPLayMancalas(int mancalaNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(mancalaNr);
        boolean validMove = PocketFound.canIDoMove();
        assertFalse(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerTwoCanoNotPLayMancalas(int mancalaNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(mancalaNr);
        PocketFound.beurt.setChangeBeurt();
        boolean validMove = PocketFound.canIDoMove();
        assertFalse(validMove);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerTwoCanoNotMoveStonesOfMancalas(int mancalaNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(mancalaNr);
        PocketFound.beurt.setChangeBeurt();
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerOneCanoNotMoveStonesOfMancalas(int mancalaNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(mancalaNr);
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerTwoCanNotMoveStonesFromPocketOneToSix(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.beurt.setChangeBeurt();
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerOneCanNotMoveStonesFromPocketEightToThirteen(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14})
    public void testBeurtIfSwitchedInPocketOne(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketOne = pocket.getPocketFinder(1);
        PocketOne.beurt.setChangeBeurt();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        assertEquals(2, PocketFound.beurt.getWhichPlayerIsNow());
    }
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14})
    public void testBeurtIfSwitchedInPocketEight(int pocketNr){
        Pocket pocket = new Pocket();
        PocketAbstract PocketOne = pocket.getPocketFinder(8);
        PocketOne.beurt.setChangeBeurt();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        assertEquals(2, PocketFound.beurt.getWhichPlayerIsNow());
    }

    @Test
    public void testLastStoneInOwnEmptyPocketMeansGettingThemToMancalaPlusNeighbor(){
        Pocket pocket = new Pocket();
        PocketAbstract PocketSix = pocket.getPocketFinder(6);
        PocketSix.setMoveStones();
        PocketSix.beurt.setChangeBeurt();
        PocketAbstract PocketTwo = pocket.getPocketFinder(2);
        PocketTwo.setMoveStones();

        assertEquals(0, PocketSix.stones);
        PocketAbstract PocketMancalaSeven = pocket.getPocketFinder(7);
        assertEquals(7, PocketMancalaSeven.stones);
        PocketAbstract PocketMancalaEight = pocket.getPocketFinder(8);
        assertEquals(0, PocketMancalaEight.stones);
    }

    @Test
    public void testLastStoneInOwnEmptyPocketMeansGettingThemToMancalaPlusNeighborForPlayerTwo(){
        Pocket pocket = new Pocket();
        pocket.beurt.setChangeBeurt();
        PocketAbstract PocketTwelve = pocket.getPocketFinder(12);
        PocketTwelve.setMoveStones();
        PocketTwelve.beurt.setChangeBeurt();
        PocketAbstract PocketEight = pocket.getPocketFinder(8);
        PocketEight.setMoveStones();

        assertEquals(0, PocketTwelve.stones);
        PocketAbstract PocketMancalaFourteen = pocket.getPocketFinder(14);
        assertEquals(7, PocketMancalaFourteen.stones);
        PocketAbstract PocketMancalaTwo = pocket.getPocketFinder(2);
        assertEquals(0, PocketMancalaTwo.stones);
    }
}
