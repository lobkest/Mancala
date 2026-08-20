package nl.sogyo.mancala.domain;

import nl.sogyo.mancala.domain.exceptions.CanNotPlayThisPocket;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nl.sogyo.mancala.domain.exceptions.OngeldigBordException;

import java.util.List;

public class PocketTest {

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
        pocket.setMoveStones(1);
        assertEquals(0, pocketFound.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5})
    public void testMoveStonesFromFirstPocketThenNextPocketsAreFiveStones(int nextPocketWithFiveStones) {
        Pocket pocket = new Pocket();
        pocket.setMoveStones(1);
        PocketTemplate nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(5, nextPockets.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 8, 9, 10, 11, 12, 13})
    public void testMoveStonesFromFirstPocketThenAllOtherPocketsAreStillFourStonesExceptTwoToFive(int nextPocketWithFiveStones) {
        Pocket pocket = new Pocket();
        pocket.setMoveStones(1);
        PocketTemplate nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(4, nextPockets.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testMoveStonesFromFirstPocketThenBothMancalasAreStillZeroStones(int nextPocketWithFiveStones) {
        Pocket pocket = new Pocket();
        pocket.setMoveStones(1);
        PocketTemplate nextPockets = pocket.getPocketFinder(nextPocketWithFiveStones);
        assertEquals(0, nextPockets.getStonesAmount());
    }

    @ParameterizedTest
    @CsvSource({"6, 0", "7, 1", "8, 5", "9, 5", "10, 5", "11, 4", "12, 4", "13, 4"
            , "14, 0", "1, 4", "2, 4", "3, 4", "4, 4", "5, 4"})
    public void testMoveStonesFromSixthPocketAndCheckStonesInAllOtherPockets(int nextPocketToCheck, int stones) {
        Pocket pocket = new Pocket();
        pocket.setMoveStones(6);
        PocketTemplate nextPockets = pocket.getPocketFinder(nextPocketToCheck);
        assertEquals(nextPockets.getStonesAmount(), stones);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerOneCanPlayPocketOneToSix(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        pocket.setMoveStones(pocketNr);

        assertEquals(0, PocketFound.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerOneCanNotPlayPocketEightToThirteen(int pocketNr) {
        Pocket pocket = new Pocket();
        assertThrows(CanNotPlayThisPocket.class, () -> pocket.setMoveStones(pocketNr));
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerTwoCanPlayPocketEightToThirteen(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.setChangeTurn();
        pocket.setMoveStones(pocketNr);

        assertEquals(0, PocketFound.getStonesAmount());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerTwoCanNotPlayPocketOneToSix(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.setChangeTurn();

        assertThrows(CanNotPlayThisPocket.class, () -> pocket.setMoveStones(pocketNr));
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerOneCanoNotPLayMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();

        assertThrows(CanNotPlayThisPocket.class, () -> pocket.setMoveStones(mancalaNr));
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerTwoCanoNotPLayMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(mancalaNr);
        PocketFound.setChangeTurn();

        assertThrows(CanNotPlayThisPocket.class, () -> pocket.setMoveStones(mancalaNr));
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerTwoCanoNotMoveStonesOfMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(mancalaNr);
        PocketFound.setChangeTurn();
        assertThrows(CanNotPlayThisPocket.class, () -> pocket.setMoveStones(mancalaNr));
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 14})
    public void testPlayerOneCanoNotMoveStonesOfMancalas(int mancalaNr) {
        Pocket pocket = new Pocket();
        assertThrows(CanNotPlayThisPocket.class, () -> pocket.setMoveStones(mancalaNr));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testPlayerTwoCanNotMoveStonesFromPocketOneToSix(int pocketNr) {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFound = pocket.getPocketFinder(pocketNr);
        PocketFound.setChangeTurn();
        assertThrows(CanNotPlayThisPocket.class, () -> pocket.setMoveStones(pocketNr));
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 11, 12, 13})
    public void testPlayerOneCanNotMoveStonesFromPocketEightToThirteen(int pocketNr) {
        Pocket pocket = new Pocket();
        assertThrows(CanNotPlayThisPocket.class, () -> pocket.setMoveStones(pocketNr));
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
        pocket.setMoveStones(6);

        PocketSix.setChangeTurn();
        pocket.setMoveStones(2);

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
        pocket.setMoveStones(12);

        PocketTwelve.setChangeTurn();
        pocket.setMoveStones(8);

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
        pocket.setMoveStones(1);

        PocketTemplate PocketTwo = pocket.getPocketFinder(2);
        PocketTemplate PocketMancalaTwo = pocket.getPocketFinder(14);

        assertEquals(6, PocketTwo.getStonesAmount());
        assertEquals(0, PocketMancalaTwo.getStonesAmount());
    }

    @Test
    public void testLastStoneInOwnMancalaThenICanGoAgain() {
        Pocket pocket = new Pocket();
        PocketTemplate PocketThree = pocket.getPocketFinder(3);
        pocket.setMoveStones(3);
        assertTrue(PocketThree.isTurnOfThisPlayer());

    }

    @Test
    public void testLastStoneInAnyOtherPocketBesideMancalaThenICanNotGoAgain() {
        Pocket pocket = new Pocket();
        PocketTemplate PocketFour = pocket.getPocketFinder(4);
        pocket.setMoveStones(4);
        assertFalse(PocketFour.isTurnOfThisPlayer());
    }

    @Test
    public void testLastStoneInEmptyPocketOfOtherPlayer() {
        Pocket pocket = new Pocket();
        PocketTemplate PocketTen = pocket.getPocketFinder(10);
        PocketTen.setStones(0);
        pocket.setMoveStones(6);
        PocketTemplate PocketFour = pocket.getPocketFinder(4);

        assertEquals(4, PocketFour.getStonesAmount());
    }

    @Test
    public void testGameOverWhenPlayerOnePocketsAreEmptySoICanNotMakeMoveAgain(){
        List<Integer> customBoard = List.of(
                0, 0, 0, 0, 0, 0, 0,
                4, 4, 4, 4, 4, 4, 0
        );

        Pocket pocket = new Pocket(customBoard);

        assertTrue(pocket.isGameOver());
    }

    @Test
    public void testGameOverWhenPlayerOnePocketsAreEmptyThenICanFindWinner() {
        List<Integer> board = List.of(
                0, 0, 0, 0, 0, 0, 0,
                4, 4, 4, 4, 4, 4, 24
        );
        Pocket pocket = new Pocket(board);

        assertTrue(pocket.isGameOver());
        assertEquals(2, pocket.getWhoIsTheWinner());
    }

    @Test
    public void testGameOverDrawPossible() {
        List<Integer> board = List.of(
                0, 0, 0, 0, 0, 0, 24,
                0, 0, 0, 0, 0, 0, 24
        );
        Pocket pocket = new Pocket(board);

        assertTrue(pocket.isGameOver());
        assertEquals(0, pocket.getWhoIsTheWinner());
    }

    @Test
    public void testGameOverPlayerOneWon() {
        List<Integer> board = List.of(
                2, 2, 2, 2, 2, 2, 22,
                0, 0, 0, 0, 0, 0, 2
        );
        Pocket pocket = new Pocket(board);
        pocket.setChangeTurn();

        assertTrue(pocket.isGameOver());
        assertEquals(1, pocket.getWhoIsTheWinner());
    }

    @Test
    public void testGameOverPlayerTwoWon() {
        List<Integer> board = List.of(
                0, 0, 0, 0, 0, 0, 2,
                2, 2, 2, 2, 2, 2, 24
        );
        Pocket pocket = new Pocket(board);

        assertTrue(pocket.isGameOver());
        assertEquals(2, pocket.getWhoIsTheWinner());
    }

    @Test
    public void testPlayerOneSkipsPlayerTwoMancala() {
        Pocket pocket = new Pocket();
        PocketTemplate pocketOne = pocket.getPocketFinder(1);
        pocketOne.setStones(14);

        pocket.setMoveStones(1);

        PocketTemplate MancalaOne = pocket.getPocketFinder(7);
        PocketTemplate MancalaTwo = pocket.getPocketFinder(14);

        assertEquals(1, MancalaOne.getStonesAmount(), "Mancala one should receive 1 stone");
        assertEquals(0, MancalaTwo.getStonesAmount(), "Mancala two should be skipped and not receive stone.");
    }

    @Test
    public void testCannotPlayEmptyPocketOnOwnSide() {
        Pocket pocket = new Pocket();
        PocketTemplate pocketOne = pocket.getPocketFinder(1);
        pocketOne.setStones(0);

        assertThrows(CanNotPlayThisPocket.class,  () -> pocket.setMoveStones(1));
        assertTrue(pocketOne.isTurnOfThisPlayer(), "Turn should remain with player one after an invalid move attempt.");
    }

    @Test
    public void testLandingInOpponentEmptyPocketDoesNotCapture() {
        Pocket pocket = new Pocket();
        PocketTemplate pocketSix = pocket.getPocketFinder(6);
        PocketTemplate pocketNine = pocket.getPocketFinder(9);

        pocketSix.setStones(3);
        pocketNine.setStones(0);

        pocket.setMoveStones(6);

        assertEquals(1, pocketNine.getStonesAmount(), "Stone should remain in P2's pocket.");
        assertEquals(1, pocket.getPocketFinder(7).getStonesAmount(), "P1 Mancala should not gain more than 1 stone.");
    }

    @Test
    public void testSetMoveStonesWithPocketNrExecutesMove() {
        Pocket pocket = new Pocket();

        pocket.setMoveStones(3);

        PocketTemplate pocketThree = pocket.getPocketFinder(3);
        assertEquals(0, pocketThree.getStonesAmount());
    }

    @Test
    public void testSetMoveStonesWithOpponentsPocketNrThrowsException() {
        Pocket pocket = new Pocket();

        assertThrows(CanNotPlayThisPocket.class, () -> {
            pocket.setMoveStones(9);
        });
    }

    @Test
    public void testSetMoveStonesWithMancalaPocketNrThrowsException() {
        Pocket pocket = new Pocket();

        assertThrows(CanNotPlayThisPocket.class, () -> {
            pocket.setMoveStones(7);
        });
    }

    @Test
    public void testPlayerTwoCanPlayTheirPocketUsingOverloadedMethod() {
        Pocket pocket = new Pocket();
        pocket.setChangeTurn();

        pocket.setMoveStones(8);

        PocketTemplate pocketEight = pocket.getPocketFinder(8);
        assertEquals(0, pocketEight.getStonesAmount());
    }

    @Test
    public void testGameEndsWhenPlayerTwoSideIsEmpty() {
        List<Integer> board = List.of(
                4, 4, 4, 4, 4, 4, 0,
                0, 0, 0, 0, 0, 0, 0
        );
        Pocket pocket = new Pocket(board);
        pocket.setChangeTurn();

        assertTrue(pocket.isGameOver());
    }

    @Test
    public void testMoveStonesThrowsGameOverEvenWithOverloadedMethod() {
        List<Integer> board = List.of(
                0, 0, 0, 0, 0, 0, 0,
                4, 4, 4, 4, 4, 4, 0
        );
        Pocket pocket = new Pocket(board);

        assertTrue(pocket.isGameOver());
    }

    @Test
    public void testGameOverWhenOtherPlayerTurnIsAlreadyFalse() {
        List<Integer> board = List.of(
                0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0
        );
        Pocket pocket = new Pocket(board);

        assertTrue(pocket.isGameOver());
    }

    @Test
    public void testGameOverAfterLastMoveIsMade() {
        List<Integer> board = List.of(
                0, 0, 0, 0, 0, 1, 0,
                4, 4, 4, 4, 4, 4, 0
        );
        Pocket pocket = new Pocket(board);

        pocket.setMoveStones(6);

        assertTrue(pocket.isGameOver());
    }

    @Test
    public void testGameOverAfterLastMoveIsMadeForPlayerTwo() {
        List<Integer> board = List.of(
                4, 4, 4, 4, 4, 4, 0,
                0, 0, 0, 0, 0, 1, 0
        );
        Pocket pocket = new Pocket(board);
        pocket.setChangeTurn();

        pocket.setMoveStones(13);

        assertTrue(pocket.isGameOver());
    }

    @Test
    public void testRemainingStonesAreMovedToMancalaOnGameOver() {
        List<Integer> board = List.of(
                0, 0, 0, 0, 0, 1, 0,
                4, 4, 4, 4, 4, 4, 0
        );
        Pocket pocket = new Pocket(board);

        pocket.setMoveStones(6);

        assertEquals(0, pocket.getPocketFinder(6).getStonesAmount());
    }

    @Test
    public void testGameOverWhenLastStoneInEmptyPocketAndYouGetNeighborStones() {
        List<Integer> board = List.of(
                0, 0, 0, 0, 1, 0, 0,
                0, 0, 0, 0, 0, 0, 0
        );
        Pocket pocket = new Pocket(board);

        pocket.setMoveStones(5);

        assertTrue(pocket.isGameOver());
    }

    @Test
    public void testListBoardSetup() {
        List<Integer> customBoard = List.of(
                0, 0, 0, 0, 0, 0, 0,
                4, 4, 4, 4, 4, 4, 0
        );

        Pocket pocket = new Pocket(customBoard);

        assertEquals(0, pocket.getStonesAmount());
        assertEquals(4, pocket.getPocketFinder(8).getStonesAmount());
    }

}
