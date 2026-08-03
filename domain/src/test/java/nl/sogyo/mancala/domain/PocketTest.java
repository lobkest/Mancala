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
        assertEquals(1, pocket.getPocketNr());
    }

    @Test
    public void testFirstPocketStones() {
        Pocket pocket = new Pocket();
        assertEquals(4, pocket.getStonesAmount());
    }

    @Test
    public void testFirstPocketNextNr() {
        Pocket pocket = new Pocket();
        assertEquals(2, pocket.getNextPocket().getPocketNr());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14})
    public void testAllPocketNr(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate pocketFound = pocket.getPocketFinder(pocketNr);
        int pocketNrFound = pocketFound.getPocketNr();
        assertEquals(pocketNr, pocketNrFound);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13})
    public void testAllPocketStones(int PocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate pocketFound = pocket.getPocketFinder(PocketNr);
        int stones = pocketFound.getStonesAmount();
        assertEquals(4, stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13})
    public void testAllNextPocketNumbers(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate pocketFound = pocket.getPocketFinder(pocketNr);
        int pocketNrNext = pocketFound.getNextPocket().getPocketNr();
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
        PocketTemplate pocketFound = pocket.getPocketFinder(14);
        assertEquals(1, pocketFound.getNextPocket().getPocketNr());
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testMancalaHasZeroStones(int pocketNrMancala) {
        Pocket pocket = new Pocket();
        PocketTemplate pocketFound = pocket.getPocketFinder(pocketNrMancala);
        assertEquals(0, pocketFound.getStonesAmount());
    }

    @Test
    public void testMoveStonesFromFirstPocketThenStonesIsZero() {
        Pocket pocket = new Pocket();
        PocketTemplate pocketFound = pocket.getPocketFinder(1);
        pocketFound.setMoveStones();
        assertEquals(0, pocketFound.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5})
    public void testMoveStonesFromFirstPocketThenNextPocketsAreFiveStones(int nextPocketWithFiveStones) {
        Pocket pocket = new Pocket();
        PocketTemplate FirstPocket = pocket.getPocketFinder(1);
        FirstPocket.setMoveStones();
        PocketTemplate nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(5, nextPockets.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 8, 9, 10, 11, 12, 13})
    public void testMoveStonesFromFirstPocketThenAllOtherPocketsAreStillFourStonesExceptTwoToFive(int nextPocketWithFiveStones) {
        Pocket pocket = new Pocket();
        PocketTemplate FirstPocket = pocket.getPocketFinder(1);
        FirstPocket.setMoveStones();
        PocketTemplate nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(4, nextPockets.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testMoveStonesFromFirstPocketThenBothMancalasAreStillZeroStones(int nextPocketWithFiveStones) {
        Pocket pocket = new Pocket();
        PocketTemplate FirstPocket = pocket.getPocketFinder(1);
        FirstPocket.setMoveStones();
        PocketTemplate nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(0, nextPockets.getStonesAmount());
    }

    @ParameterizedTest
    @CsvSource({"6, 0", "7, 1", "8, 5", "9, 5", "10, 5", "11, 4", "12, 4", "13, 4"
            , "14, 0", "1, 4", "2, 4", "3, 4", "4, 4", "5, 4"})
    public void testMoveStonesFromSixthPocketAndCheckStonesInAllOtherPockets(int nextPocketToCheck, int stones) {
        Pocket pocket = new Pocket();
        PocketTemplate SixthPocket = pocket.getPocketFinder(6);
        SixthPocket.setMoveStones();
        PocketTemplate nextPockets = pocket.getPocketFinder(nextPocketToCheck);
        assertEquals(nextPockets.getStonesAmount(), stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerOneCanPlayPocketOneToSix(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.setMoveStones();

        assertEquals(0, PocketFound.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerOneCanNotPlayPocketEightToThirteen(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);

        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerTwoCanPlayPocketEightToThirteen(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.setChangeTurn();

        PocketFound.setMoveStones();
        assertEquals(0, PocketFound.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerTwoCanNotPlayPocketOneToSix(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.setChangeTurn();

        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerOneCanoNotPLayMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(mancalaNr);

        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerTwoCanoNotPLayMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(mancalaNr);
        PocketFound.setChangeTurn();

        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerTwoCanoNotMoveStonesOfMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(mancalaNr);
        PocketFound.setChangeTurn();
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerOneCanoNotMoveStonesOfMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(mancalaNr);
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerTwoCanNotMoveStonesFromPocketOneToSix(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.setChangeTurn();
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerOneCanNotMoveStonesFromPocketEightToThirteen(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        assertThrows(CanNotPlayThisPocket.class, PocketFound::setMoveStones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    public void testTurnSwitchedInPocketOneAndCheckTurnInRestOfPocketsOfPlayerOne(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketOne = pocket.getPocketFinder(1);
        PocketOne.setChangeTurn();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        assertFalse(PocketFound.isTurnOfThisPlayer());
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13, 14})
    public void testTurnSwitchedInPocketOneAndCheckTurnInRestOfPocketsOfPlayerTwo(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketOne = pocket.getPocketFinder(1);
        PocketOne.setChangeTurn();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        assertTrue(PocketFound.isTurnOfThisPlayer());
    }

    @Test
    public void testTurnInMancalaOneIsSetToPlayerOneWhenInitialized(){
        Pocket pocket = new Pocket();
        PocketTemplate MancalaOne = pocket.getPocketFinder(7);
        assertTrue(MancalaOne.isTurnOfThisPlayer());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    public void testTurnSwitchedInPocketEightAndCheckTurnInRestOfPocketsOfPlayerOne(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketEight = pocket.getPocketFinder(8);
        PocketEight.setChangeTurn();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        assertFalse(PocketFound.isTurnOfThisPlayer());
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13, 14})
    public void testTurnSwitchedInPocketEightAndCheckTurnInRestOfPocketsOfPlayerTwo(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketEight = pocket.getPocketFinder(8);
        PocketEight.setChangeTurn();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        assertTrue(PocketFound.isTurnOfThisPlayer());
    }

    @Test
    public void testLastStoneInOwnEmptyPocketMeansGettingThemToMancalaPlusNeighbor() {
        Pocket pocket = new Pocket();
        PocketTemplate PocketSix = pocket.getPocketFinder(6);
        PocketSix.setMoveStones();
        PocketSix.setChangeTurn();
        PocketTemplate PocketTwo = pocket.getPocketFinder(2);
        PocketTwo.setMoveStones();

        assertEquals(0, PocketSix.getStonesAmount());
        PocketTemplate PocketMancalaSeven = pocket.getPocketFinder(7);
        assertEquals(7, PocketMancalaSeven.getStonesAmount());
        PocketTemplate PocketMancalaEight = pocket.getPocketFinder(8);
        assertEquals(0, PocketMancalaEight.getStonesAmount());
    }

    @Test
    public void testLastStoneInOwnEmptyPocketMeansGettingThemToMancalaPlusNeighborForPlayerTwo() {
        Pocket pocket = new Pocket();
        pocket.setChangeTurn();
        PocketTemplate PocketTwelve = pocket.getPocketFinder(12);
        PocketTwelve.setMoveStones();
        PocketTwelve.setChangeTurn();
        PocketTemplate PocketEight = pocket.getPocketFinder(8);
        PocketEight.setMoveStones();

        assertEquals(0, PocketTwelve.getStonesAmount());
        PocketTemplate PocketMancalaFourteen = pocket.getPocketFinder(14);
        assertEquals(7, PocketMancalaFourteen.getStonesAmount());
        PocketTemplate PocketMancalaTwo = pocket.getPocketFinder(2);
        assertEquals(0, PocketMancalaTwo.getStonesAmount());
    }

    @Test
    public void testSkipOtherPlayersMancala() {
        Pocket pocket = new Pocket();
        pocket.setStones(14);
        pocket.setMoveStones();

        PocketTemplate PocketTwo = pocket.getPocketFinder(2);
        PocketTemplate PocketMancalaTwo = pocket.getPocketFinder(14);

        assertEquals(6, PocketTwo.getStonesAmount());
        assertEquals(0, PocketMancalaTwo.getStonesAmount());
    }

    @Test
    public void testLastStoneInOwnMancalaThenICanGoAgain() {
        Pocket pocket = new Pocket();
        PocketTemplate PocketThree = pocket.getPocketFinder(3);
        PocketThree.setMoveStones();
        assertTrue(PocketThree.isTurnOfThisPlayer());

    }

    @Test
    public void testLastStoneInAnyOtherPocketBesideMancalaThenICanNotGoAgain() {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFour = pocket.getPocketFinder(4);
        PocketFour.setMoveStones();
        assertFalse(PocketFour.isTurnOfThisPlayer());
    }

    @Test
    public void testLastStoneInEmptyPocketOfOtherPlayer() {
        Pocket pocket = new Pocket();
        PocketTemplate PocketTen = pocket.getPocketFinder(10);
        PocketTen.setStones(0);
        PocketTemplate PocketSix = pocket.getPocketFinder(6);
        PocketSix.setMoveStones();
        PocketTemplate PocketFour = pocket.getPocketFinder(4);

        assertEquals(4, PocketFour.getStonesAmount());
    }

//    @Test
//    public void testOwnerOfPocketSevenIsOne() {
//        Pocket pocket = new Pocket();
//        PocketTemplate MancalaOne = pocket.getPocketFinder(7);
//
//        assertEquals(1, MancalaOne.owner);
//    }
//
//    @Test
//    public void testOwnerOfPocketFourteenIsTwo() {
//        Pocket pocket = new Pocket();
//        PocketTemplate MancalaTwo = pocket.getPocketFinder(14);
//
//        assertEquals(2, MancalaTwo.owner);
//    }




    @Test
    public void testGameOverWhenPlayerOnePocketsAreEmptySoICanNotMakeMoveAgain(){
        Pocket pocket = new Pocket();
        PocketTemplate PocketOne = pocket.getPocketFinder(1);
        PocketTemplate PocketTwo = pocket.getPocketFinder(2);
        PocketTemplate PocketThree = pocket.getPocketFinder(3);
        PocketTemplate PocketFour = pocket.getPocketFinder(4);
        PocketTemplate PocketFive = pocket.getPocketFinder(5);
        PocketTemplate PocketSix = pocket.getPocketFinder(6);

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
        PocketTemplate PocketOne = pocket.getPocketFinder(1);
        PocketTemplate PocketTwo = pocket.getPocketFinder(2);
        PocketTemplate PocketThree = pocket.getPocketFinder(3);
        PocketTemplate PocketFour = pocket.getPocketFinder(4);
        PocketTemplate PocketFive = pocket.getPocketFinder(5);
        PocketTemplate PocketSix = pocket.getPocketFinder(6);

        PocketTemplate PocketEight = pocket.getPocketFinder(8);

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

        PocketTemplate mancalaOne = pocket.getPocketFinder(7);
        PocketTemplate mancalaTwo = pocket.getPocketFinder(14);
        mancalaOne.setStones(24);
        mancalaTwo.setStones(24);

        for (int i = 1; i <= 6; i++) {
            PocketTemplate currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(0);
        }

        for (int i = 8; i <= 13; i++) {
            PocketTemplate currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(0);
        }

        assertThrows(GameOver.class, pocket::setMoveStones);
        assertEquals(0, pocket.getWhoIsTheWinner());
    }

    @Test
    public void testGameOverPlayerOneWon(){
        Pocket pocket = new Pocket();

        PocketTemplate mancalaOne = pocket.getPocketFinder(7);
        PocketTemplate mancalaTwo = pocket.getPocketFinder(14);
        mancalaOne.setStones(22);
        mancalaTwo.setStones(2);

        for (int i = 1; i <= 6; i++) {
            PocketTemplate currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(2);
        }

        for (int i = 8; i <= 13; i++) {
            PocketTemplate currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(0);
        }

        pocket.setChangeTurn();

        assertThrows(GameOver.class, pocket::setMoveStones);
        assertEquals(1, pocket.getWhoIsTheWinner());
    }

    @Test
    public void testGameOverPlayerTwoWon(){
        Pocket pocket = new Pocket();

        PocketTemplate mancalaOne = pocket.getPocketFinder(7);
        PocketTemplate mancalaTwo = pocket.getPocketFinder(14);
        mancalaOne.setStones(2);
        mancalaTwo.setStones(24);

        for (int i = 1; i <= 6; i++) {
            PocketTemplate currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(0);
        }

        for (int i = 8; i <= 13; i++) {
            PocketTemplate currentPocket = pocket.getPocketFinder(i);
            currentPocket.setStones(2);
        }

//        pocket.setMoveStones();
        assertThrows(GameOver.class, pocket::setMoveStones);
        assertEquals(2, pocket.getWhoIsTheWinner());
    }
}
