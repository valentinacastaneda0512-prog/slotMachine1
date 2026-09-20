
import static org.junit.Assert.*;
import org.junit.Test;

public class SlotMachineContestTest
{
    @Test
    public void shouldSolveMachineWithThreeWheels()
    {
        SlotMachineContest contest =
            new SlotMachineContest();
    
        int[][] actions = contest.solve(3);
    
        assertNotNull(actions);
        assertTrue(contest.isSolved());
        assertTrue(actions.length < 10000);
    }
    
    @Test
    public void shouldSolveMachineWithFiveWheels()
    {
        SlotMachineContest contest =
            new SlotMachineContest();
    
        int[][] actions = contest.solve(5);
    
        assertNotNull(actions);
        assertTrue(contest.isSolved());
        assertTrue(actions.length < 10000);
    }

    @Test
    public void shouldSolveMachineWithTenWheels()
    {
        SlotMachineContest contest =
            new SlotMachineContest();
    
        int[][] actions = contest.solve(10);
    
        assertNotNull(actions);
        assertTrue(contest.isSolved());
        assertTrue(actions.length < 10000);
    }
    
    @Test
    public void shouldSolveMachineWithFiftyWheels()
    {
        SlotMachineContest contest =
            new SlotMachineContest();
    
        int[][] actions = contest.solve(50);
    
        assertNotNull(actions);
        assertTrue(contest.isSolved());
        assertTrue(actions.length < 10000);
    }
    
    /**
     * Verifies that every returned action contains
     * exactly two values: the wheel and the number
     * of steps.
     */
    @Test
    public void shouldReturnActionsWithTwoValues()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        int[][] actions = contest.solve(6);

        for (int i = 0; i < actions.length; i++) {
            assertNotNull(actions[i]);
            assertEquals(2, actions[i].length);
        }
    }

    /**
     * Verifies that every wheel specified in a returned
     * action belongs to the machine.
     */
    @Test
    public void shouldReturnActionsForValidWheels()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        int[][] actions = contest.solve(7);

        for (int i = 0; i < actions.length; i++) {
            assertTrue(actions[i][0] >= 1);
            assertTrue(actions[i][0] <= 7);
        }
    }

    /**
     * Verifies that the number of actions returned by
     * the solution stays below the contest limit.
     */
    @Test
    public void shouldRespectMaximumNumberOfActions()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        int[][] actions = contest.solve(8);

        assertTrue(actions.length <= 10000);
    }

    /**
     * Verifies that the solution can be executed
     * repeatedly and leaves the machine solved.
     */
    @Test
    public void shouldRemainSolvedAfterSolvingAgain()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        contest.solve(6);
        assertTrue(contest.isSolved());

        contest.solve(6);
        assertTrue(contest.isSolved());
    }

    /**
     * Verifies that solving a machine with a different
     * number of wheels creates a new valid solution.
     */
    @Test
    public void shouldCreateNewSolutionForDifferentSize()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        contest.solve(4);
        assertTrue(contest.isSolved());

        int[][] actions = contest.solve(9);

        assertNotNull(actions);
        assertTrue(contest.isSolved());
    }

    /**
     * Verifies that calling simulate after solving
     * keeps the machine in the solved state.
     */
    @Test
    public void shouldKeepMachineSolvedAfterSimulation()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        contest.solve(6);
        contest.simulate(6);

        assertTrue(contest.isSolved());
    }

    /**
     * Verifies that simulation can be requested before
     * an explicit call to solve.
     */
    @Test
    public void shouldSolveAutomaticallyWhenSimulationStarts()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        contest.simulate(6);

        assertTrue(contest.isSolved());
    }

    /**
     * Verifies that solving a machine returns a sequence
     * of actions that contains no null action.
     */
    @Test
    public void shouldNotReturnNullActions()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        int[][] actions = contest.solve(6);

        for (int i = 0; i < actions.length; i++) {
            assertNotNull(actions[i]);
        }
    }
}
