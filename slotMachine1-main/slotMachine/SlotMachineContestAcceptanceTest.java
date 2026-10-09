import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Acceptance tests for the Slot Machine contest.
 *
 * This is a simulation of how the algorithm solves the machine.
 * It scans the wheels and identifies the most repeated symbol.
 * From there, it moves the remaining wheels to match it and solve the problem.
 *
 * @author Cristian Anzola, Danik Castaneda
 * @version 7
 */
public class SlotMachineContestAcceptanceTest
{
    /**
     * First simulation: a machine of five wheels is created and solved.
     */
    @Test
    public void shouldSimulateTheSolutionOfFiveWheels()
    {
        SlotMachineContest2 contest = new SlotMachineContest2();

        int[][] actions = contest.solve(5);

        assertNotNull(actions);
        assertTrue(actions.length <= 5);

        contest.simulate(5);
        delay(2000);
    }

    /**
     * Second simulation: a bigger machine, of ten wheels, is created and
     * solved, to show that the contest works at a different scale.
     */
    @Test
    public void shouldSimulateTheSolutionOfTenWheels()
    {
        SlotMachineContest2 contest = new SlotMachineContest2();

        int[][] actions = contest.solve(10);

        assertNotNull(actions);
        assertTrue(actions.length <= 10);

        contest.simulate(10);
        delay(2000);
    }

    /**
     * Waits so the result can be seen.
     *
     * @param milliseconds waiting time
     */
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
