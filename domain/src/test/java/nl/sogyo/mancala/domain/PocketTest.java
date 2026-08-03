package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nl.sogyo.mancala.domain.exceptions.GameOver;
import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;


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
    public void testAllNextPocketNumbers(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(pocketNr);
        int pocketNrNext = pocketFound.nextPocket.pocketNr;
        assertEquals(pocketNr + 1, pocketNrNext);
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
    public void testMancalaHasZeroStones(int pocketNrMancala) {
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(pocketNrMancala);
        assertEquals(0, pocketFound.stones);
    }

    @Test
    public void testMoveStonesFromFirstPocketThenStonesIsZero() {
        Pocket pocket = new Pocket();
        PocketAbstract pocketFound = pocket.getPocketFinder(1);
        pocketFound.setMoveStones();
        assertEquals(0, pocketFound.stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5})
    public void testMoveStonesFromFirstPocketThenNextPocketsAreFiveStones(int nextPocketWithFiveStones) {
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(1);
        FirstPocket.setMoveStones();
        PocketAbstract nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(5, nextPockets.stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 8, 9, 10, 11, 12, 13})
    public void testMoveStonesFromFirstPocketThenAllOtherPocketsAreStillFourStonesExceptTwoToFive(int nextPocketWithFiveStones) {
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(1);
        FirstPocket.setMoveStones();
        PocketAbstract nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(4, nextPockets.stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testMoveStonesFromFirstPocketThenBothMancalasAreStillZeroStones(int nextPocketWithFiveStones) {
        Pocket pocket = new Pocket();
        PocketAbstract FirstPocket = pocket.getPocketFinder(1);
        FirstPocket.setMoveStones();
        PocketAbstract nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(0, nextPockets.stones);
    }

    @ParameterizedTest
    @CsvSource({"6, 0", "7, 1", "8, 5", "9, 5", "10, 5", "11, 4", "12, 4", "13, 4"
            , "14, 0", "1, 4", "2, 4", "3, 4", "4, 4", "5, 4"})
    public void testMoveStonesFromSixthPocketAndCheckStonesInAllOtherPockets(int nextPocketToCheck, int stones) {
        Pocket pocket = new Pocket();
        PocketAbstract SixthPocket = pocket.getPocketFinder(6);
        SixthPocket.setMoveStones();
        PocketAbstract nextPockets = pocket.getPocketFinder(nextPocketToCheck);
        assertEquals(nextPockets.stones, stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerOneCanPlayPocketOneToSix(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.setMoveStones();

        assertEquals(0, PocketFound.stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerOneCanNotPlayPocketEightToThirteen(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);

        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerTwoCanPlayPocketEightToThirteen(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.turn.setChangeTurn();

        PocketFound.setMoveStones();
        assertEquals(0, PocketFound.stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerTwoCanNotPlayPocketOneToSix(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.turn.setChangeTurn();

        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerOneCanoNotPLayMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(mancalaNr);

        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerTwoCanoNotPLayMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(mancalaNr);
        PocketFound.turn.setChangeTurn();

        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerTwoCanoNotMoveStonesOfMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(mancalaNr);
        PocketFound.turn.setChangeTurn();
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerOneCanoNotMoveStonesOfMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(mancalaNr);
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerTwoCanNotMoveStonesFromPocketOneToSix(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.turn.setChangeTurn();
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerOneCanNotMoveStonesFromPocketEightToThirteen(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    public void testTurnSwitchedInPocketOneAndCheckTurnInRestOfPocketsOfPlayerOne(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketOne = pocket.getPocketFinder(1);
        PocketOne.turn.setChangeTurn();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        assertFalse(PocketFound.turn.isTurnOfThisPlayer());
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13, 14})
    public void testTurnSwitchedInPocketOneAndCheckTurnInRestOfPocketsOfPlayerTwo(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketOne = pocket.getPocketFinder(1);
        PocketOne.turn.setChangeTurn();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        assertTrue(PocketFound.turn.isTurnOfThisPlayer());
    }

    @Test
    public void testTurnInMancalaOneIsSetToPlayerOneWhenInitialized(){
        Pocket pocket = new Pocket();
        PocketAbstract MancalaOne = pocket.getPocketFinder(7);
        assertTrue(MancalaOne.turn.isTurnOfThisPlayer());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    public void testTurnSwitchedInPocketEightAndCheckTurnInRestOfPocketsOfPlayerOne(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketEight = pocket.getPocketFinder(8);
        PocketEight.turn.setChangeTurn();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        assertFalse(PocketFound.turn.isTurnOfThisPlayer());
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13, 14})
    public void testTurnSwitchedInPocketEightAndCheckTurnInRestOfPocketsOfPlayerTwo(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketAbstract PocketEight = pocket.getPocketFinder(8);
        PocketEight.turn.setChangeTurn();
        PocketAbstract PocketFound = pocket.getPocketFinder(pocketNr);
        assertTrue(PocketFound.turn.isTurnOfThisPlayer());
    }

    @Test
    public void testLastStoneInOwnEmptyPocketMeansGettingThemToMancalaPlusNeighbor() {
        Pocket pocket = new Pocket();
        PocketAbstract PocketSix = pocket.getPocketFinder(6);
        PocketSix.setMoveStones();
        PocketSix.turn.setChangeTurn();
        PocketAbstract PocketTwo = pocket.getPocketFinder(2);
        PocketTwo.setMoveStones();

        assertEquals(0, PocketSix.stones);
        PocketAbstract PocketMancalaSeven = pocket.getPocketFinder(7);
        assertEquals(7, PocketMancalaSeven.stones);
        PocketAbstract PocketMancalaEight = pocket.getPocketFinder(8);
        assertEquals(0, PocketMancalaEight.stones);
    }

    @Test
    public void testLastStoneInOwnEmptyPocketMeansGettingThemToMancalaPlusNeighborForPlayerTwo() {
        Pocket pocket = new Pocket();
        pocket.turn.setChangeTurn();
        PocketAbstract PocketTwelve = pocket.getPocketFinder(12);
        PocketTwelve.setMoveStones();
        PocketTwelve.turn.setChangeTurn();
        PocketAbstract PocketEight = pocket.getPocketFinder(8);
        PocketEight.setMoveStones();

        assertEquals(0, PocketTwelve.stones);
        PocketAbstract PocketMancalaFourteen = pocket.getPocketFinder(14);
        assertEquals(7, PocketMancalaFourteen.stones);
        PocketAbstract PocketMancalaTwo = pocket.getPocketFinder(2);
        assertEquals(0, PocketMancalaTwo.stones);
    }

    @Test
    public void testSkipOtherPlayersMancala() {
        Pocket pocket = new Pocket();
        pocket.setStones(14);
        pocket.setMoveStones();

        PocketAbstract PocketTwo = pocket.getPocketFinder(2);
        PocketAbstract PocketMancalaTwo = pocket.getPocketFinder(14);

        assertEquals(6, PocketTwo.stones);
        assertEquals(0, PocketMancalaTwo.stones);
    }

    @Test
    public void testLastStoneInOwnMancalaThenICanGoAgain() {
        Pocket pocket = new Pocket();
        PocketAbstract PocketThree = pocket.getPocketFinder(3);
        PocketThree.setMoveStones();
        assertTrue(PocketThree.turn.isTurnOfThisPlayer());

    }

    @Test
    public void testLastStoneInAnyOtherPocketBesideMancalaThenICanNotGoAgain() {
        Pocket pocket = new Pocket();
        PocketAbstract PocketFour = pocket.getPocketFinder(4);
        PocketFour.setMoveStones();
        assertFalse(PocketFour.turn.isTurnOfThisPlayer());
    }

    @Test
    public void testLastStoneInEmptyPocketOfOtherPlayer() {
        Pocket pocket = new Pocket();
        PocketAbstract PocketTen = pocket.getPocketFinder(10);
        PocketTen.setStones(0);
        PocketAbstract PocketSix = pocket.getPocketFinder(6);
        PocketSix.setMoveStones();
        PocketAbstract PocketFour = pocket.getPocketFinder(4);

        assertEquals(4, PocketFour.stones);
    }

    @Test
    public void testOwnerOfPocketSevenIsOne() {
        Pocket pocket = new Pocket();
        PocketAbstract MancalaOne = pocket.getPocketFinder(7);

        assertEquals(1, MancalaOne.owner);
    }

    @Test
    public void testOwnerOfPocketFourteenIsTwo() {
        Pocket pocket = new Pocket();
        PocketAbstract MancalaTwo = pocket.getPocketFinder(14);

        assertEquals(2, MancalaTwo.owner);
    }




    @Test
    public void testGameOverWhenPlayerOnePocketsAreEmptySoICanNotMakeMoveAgain(){
        Pocket pocket = new Pocket();
        PocketAbstract PocketOne = pocket.getPocketFinder(1);
        PocketAbstract PocketTwo = pocket.getPocketFinder(2);
        PocketAbstract PocketThree = pocket.getPocketFinder(3);
        PocketAbstract PocketFour = pocket.getPocketFinder(4);
        PocketAbstract PocketFive = pocket.getPocketFinder(5);
        PocketAbstract PocketSix = pocket.getPocketFinder(6);

        PocketOne.setStones(0);
        PocketTwo.setStones(0);
        PocketThree.setStones(0);
        PocketFour.setStones(0);
        PocketFive.setStones(0);
        PocketSix.setStones(0);

        assertThrows(GameOver.class, PocketSix::setMoveStones);
    }

    @Test
    public void testGameOverWhenPlayerOnePocketsAreEmptyThenICanFindWinner(){
        Pocket pocket = new Pocket();
        PocketAbstract PocketOne = pocket.getPocketFinder(1);
        PocketAbstract PocketTwo = pocket.getPocketFinder(2);
        PocketAbstract PocketThree = pocket.getPocketFinder(3);
        PocketAbstract PocketFour = pocket.getPocketFinder(4);
        PocketAbstract PocketFive = pocket.getPocketFinder(5);
        PocketAbstract PocketSix = pocket.getPocketFinder(6);

        PocketAbstract PocketEight = pocket.getPocketFinder(8);

        PocketOne.setStones(0);
        PocketTwo.setStones(0);
        PocketThree.setStones(0);
        PocketFour.setStones(0);
        PocketFive.setStones(0);
        PocketSix.setStones(0);
        assertThrows(GameOver.class, PocketSix::setMoveStones);
        assertEquals(2, pocket.getWhoIsTheWinner());
    }

    @Test
    public void testGameOverDrawPossible(){
        Pocket pocket = new Pocket();

        PocketAbstract mancalaOne = pocket.getPocketFinder(7);
        PocketAbstract mancalaTwo = pocket.getPocketFinder(14);
        mancalaOne.setStones(24);
        mancalaTwo.setStones(24);

        for (int i = 1; i <= 6; i++) {
            PocketAbstract currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(0);
        }

        for (int i = 8; i <= 13; i++) {
            PocketAbstract currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(0);
        }

        assertThrows(GameOver.class, pocket::setMoveStones);
        assertEquals(0, pocket.getWhoIsTheWinner());
    }

    @Test
    public void testGameOverPlayerOneWon(){
        Pocket pocket = new Pocket();

        PocketAbstract mancalaOne = pocket.getPocketFinder(7);
        PocketAbstract mancalaTwo = pocket.getPocketFinder(14);
        mancalaOne.setStones(22);
        mancalaTwo.setStones(2);

        for (int i = 1; i <= 6; i++) {
            PocketAbstract currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(2);
        }

        for (int i = 8; i <= 13; i++) {
            PocketAbstract currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(0);
        }

        pocket.turn.setChangeTurn();

        assertThrows(GameOver.class, pocket::setMoveStones);
        assertEquals(1, pocket.getWhoIsTheWinner());
    }

    @Test
    public void testGameOverPlayerTwoWon(){
        Pocket pocket = new Pocket();

        PocketAbstract mancalaOne = pocket.getPocketFinder(7);
        PocketAbstract mancalaTwo = pocket.getPocketFinder(14);
        mancalaOne.setStones(2);
        mancalaTwo.setStones(24);

        for (int i = 1; i <= 6; i++) {
            PocketAbstract currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(0);
        }

        for (int i = 8; i <= 13; i++) {
            PocketAbstract currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(2);
        }

//        pocket.setMoveStones();
        assertThrows(GameOver.class, pocket::setMoveStones);
        assertEquals(2, pocket.getWhoIsTheWinner());
    }
}
