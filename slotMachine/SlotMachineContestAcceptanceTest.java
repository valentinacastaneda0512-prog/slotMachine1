import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Acceptance tests for the Slot Machine contest.
 *
 * These tests are VISIBLE: simulate shows the machine
 * in its initial (random) configuration and then applies,
 * one by one, the actions found by solve.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 2
 */
public class SlotMachineContestAcceptanceTest
{
    /**
     * Acceptance test with 5 wheels.
     */
    @Test
    public void acceptanceTestFiveWheels()
    {
        SlotMachineContest contest = new SlotMachineContest();

        int[][] actions = contest.solve(5);

        assertNotNull(actions);
        assertTrue(actions.length < 10000);
        assertTrue(contest.isSolved());

        contest.simulate(5);
        delay(3000);

        assertTrue(contest.isSolved());
    }

    /**
     * Acceptance test with 10 wheels.
     * simulate is called without calling solve first:
     * it must solve the machine by itself.
     */
    @Test
    public void acceptanceTestTenWheelsWithoutSolvingFirst()
    {
        SlotMachineContest contest = new SlotMachineContest();

        contest.simulate(10);
        delay(3000);

        assertTrue(contest.isSolved());
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