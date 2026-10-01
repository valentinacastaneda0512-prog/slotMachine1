import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for SlotMachineContest.
 *
 * All the tests are invisible: they only use solve.
 * simulate is visible, so it is checked in
 * SlotMachineContestAcceptanceTest.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 2
 */
public class SlotMachineContestTest
{
    /**
     * Solves a machine of size n and checks that the answer
     * has the expected form and leaves the machine solved.
     * @param n number of wheels and symbols
     * @return the actions returned by solve
     */
    private int[][] solveAndCheck(int n)
    {
        SlotMachineContest contest = new SlotMachineContest();

        int[][] actions = contest.solve(n);

        assertNotNull(actions);
        assertTrue(contest.isSolved());
        assertTrue(actions.length <= 10000);
        return actions;
    }

    /**
     * Verifies that the contest should solve machine with three wheels.
     */
    @Test
    public void shouldSolveMachineWithThreeWheels()
    {
        solveAndCheck(3);
    }

    /**
     * Verifies that the contest should solve machine with five wheels.
     */
    @Test
    public void shouldSolveMachineWithFiveWheels()
    {
        solveAndCheck(5);
    }

    /**
     * Verifies that the contest should solve machine with ten wheels.
     */
    @Test
    public void shouldSolveMachineWithTenWheels()
    {
        solveAndCheck(10);
    }

    /**
     * Verifies that the contest should solve machine with fifty wheels.
     */
    @Test
    public void shouldSolveMachineWithFiftyWheels()
    {
        solveAndCheck(50);
    }

    /**
     * Verifies that the contest should solve many random machines.
     */
    @Test
    public void shouldSolveManyRandomMachines()
    {
        for (int i = 0; i < 200; i++) {
            int n = 3 + (i % 48);
            solveAndCheck(n);
        }
    }

    /**
     * Verifies that the contest should return actions with two values.
     */
    @Test
    public void shouldReturnActionsWithTwoValues()
    {
        int[][] actions = solveAndCheck(6);

        for (int[] action : actions) {
            assertNotNull(action);
            assertEquals(2, action.length);
        }
    }

    /**
     * Verifies that the contest should return actions for valid wheels.
     */
    @Test
    public void shouldReturnActionsForValidWheels()
    {
        for (int i = 0; i < 20; i++) {
            int[][] actions = solveAndCheck(7);
            for (int[] action : actions) {
                assertTrue(action[0] >= 1);
                assertTrue(action[0] <= 7);
            }
        }
    }

    /**
     * Verifies that the contest should return steps between one and n minus one.
     */
    @Test
    public void shouldReturnStepsBetweenOneAndNMinusOne()
    {
        for (int i = 0; i < 20; i++) {
            int[][] actions = solveAndCheck(8);
            for (int[] action : actions) {
                assertTrue(action[1] >= 1);
                assertTrue(action[1] <= 7);
            }
        }
    }

    /**
     * Verifies that the contest should not repeat wheels in actions.
     */
    @Test
    public void shouldNotRepeatWheelsInActions()
    {
        for (int i = 0; i < 20; i++) {
            int[][] actions = solveAndCheck(9);
            boolean[] used = new boolean[10];
            for (int[] action : actions) {
                assertFalse(used[action[0]]);
                used[action[0]] = true;
            }
        }
    }

    /**
     * Verifies that the contest should use at most one action per wheel.
     */
    @Test
    public void shouldUseAtMostOneActionPerWheel()
    {
        for (int i = 0; i < 20; i++) {
            assertTrue(solveAndCheck(12).length <= 12);
        }
    }

    /**
     * Verifies that the contest should return no actions for machines
     * with an invalid size (below 3 or above 50).
     */
    @Test
    public void shouldReturnNoActionsForInvalidSizes()
    {
        int[] invalidSizes = {-1, 0, 1, 2, 51, 100};

        for (int n : invalidSizes) {
            SlotMachineContest contest = new SlotMachineContest();
            int[][] actions = contest.solve(n);

            assertNotNull(actions);
            assertEquals(0, actions.length);
            assertFalse(contest.isSolved());
        }
    }

    /**
     * Verifies that the contest should not be solved before calling solve.
     */
    @Test
    public void shouldNotBeSolvedBeforeSolving()
    {
        SlotMachineContest contest = new SlotMachineContest();

        assertFalse(contest.isSolved());
    }

    /**
     * Verifies that the contest should solve a valid machine
     * after an attempt with an invalid size.
     */
    @Test
    public void shouldSolveValidMachineAfterInvalidAttempt()
    {
        SlotMachineContest contest = new SlotMachineContest();

        contest.solve(2);
        assertFalse(contest.isSolved());

        solveAndCheck(5);
        contest.solve(5);
        assertTrue(contest.isSolved());
    }

    /**
     * Verifies that the contest should ignore the simulation of an
     * invalid machine (nothing is shown and nothing fails).
     */
    @Test
    public void shouldIgnoreSimulationOfInvalidMachine()
    {
        SlotMachineContest contest = new SlotMachineContest();

        contest.simulate(0);

        assertFalse(contest.isSolved());
    }

    /**
     * Verifies that the contest should remain solved after solving again.
     */
    @Test
    public void shouldRemainSolvedAfterSolvingAgain()
    {
        SlotMachineContest contest = new SlotMachineContest();

        contest.solve(6);
        assertTrue(contest.isSolved());

        contest.solve(6);
        assertTrue(contest.isSolved());
    }

    /**
     * Verifies that the contest should create new solution for different size.
     */
    @Test
    public void shouldCreateNewSolutionForDifferentSize()
    {
        SlotMachineContest contest = new SlotMachineContest();

        contest.solve(4);
        assertTrue(contest.isSolved());

        int[][] actions = contest.solve(9);

        assertNotNull(actions);
        assertTrue(contest.isSolved());
        assertTrue(actions.length <= 9);
    }
}