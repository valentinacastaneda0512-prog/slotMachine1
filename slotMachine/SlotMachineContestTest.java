import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for SlotMachineContest and SlotMachineContest2.
 *
 * All the tests are invisible: they only use solve and solves. simulate
 * makes the machine visible, so it is checked in
 * SlotMachineContestAcceptanceTest.
 *
 * The tests never build a machine of their own: they enter through the
 * contest, which is the class being tested. Every test is written against
 * the contract of SlotMachineContest, so the last one checks a different
 * strategy without changing a single assertion.
 *
 * @author Cristian Anzola, Danik Castaneda
 * @version 6
 */
public class SlotMachineContestTest
{
    private static final int MINIMUM_SIZE = 3;
    private static final int MAXIMUM_SIZE = 50;

    private SlotMachineContest contest;

    /**
     * Creates the solver that every test works with.
     */
    @Before
    public void setUp()
    {
        contest = new SlotMachineContest();
    }


    /**
     * Verifies that the contest should win machines of every valid size.
     */
    @Test
    public void shouldWinMachinesOfEveryValidSize()
    {
        for (int n = MINIMUM_SIZE; n <= MAXIMUM_SIZE; n++) {
            assertTrue("size " + n, contest.solves(n));
        }
    }

    /**
     * Verifies that the contest should accept exactly the sizes the
     * problem accepts, and reject the ones just outside.
     *
     * Three and fifty are the limits, so an error of one in the comparison
     * would either refuse a legal machine or accept an impossible one.
     */
    @Test
    public void shouldWinAtTheExactLimitsOfTheProblem()
    {
        assertTrue("minimum size", contest.solves(MINIMUM_SIZE));
        assertTrue("maximum size", contest.solves(MAXIMUM_SIZE));

        assertFalse("one below the minimum", contest.solves(MINIMUM_SIZE - 1));
        assertFalse("one above the maximum", contest.solves(MAXIMUM_SIZE + 1));
    }

    /**
     * Verifies that the contest should win many machines of the same size.
     *
     * Every machine is built with a random configuration, so one call says
     * very little. Repeating the smallest size reaches the rare cases: a
     * machine that is already a jackpot before starting, and machines where
     * several wheels share a symbol from the beginning.
     */
    @Test
    public void shouldWinManyMachinesOfTheSameSize()
    {
        for (int attempt = 0; attempt < 200; attempt++) {
            assertTrue("attempt " + attempt, contest.solves(MINIMUM_SIZE));
        }
        for (int attempt = 0; attempt < 50; attempt++) {
            assertTrue("attempt " + attempt, contest.solves(7));
        }
    }

    /**
     * Verifies that the contest should keep working after being asked for
     * an impossible machine.
     *
     * The contest has no attributes, so one call must not leave anything
     * behind for the next one. A failed attempt in the middle is the
     * cheapest way to notice if some state came back.
     */
    @Test
    public void shouldKeepWorkingAfterAnImpossibleMachine()
    {
        assertTrue(contest.solves(5));

        assertFalse(contest.solves(2));
        assertEquals(0, contest.solve(100).length);

        assertTrue(contest.solves(5));
        assertTrue(contest.solves(12));
    }


    /**
     * Verifies that the contest should never waste a round.
     *
     * Each action costs one of the rounds allowed by the problem. An
     * action of zero steps, one of n steps (a whole rotation, which leaves
     * the wheel where it was), a repeated wheel, a wheel that does not
     * exist or more actions than wheels are all rounds thrown away.
     */
    @Test
    public void shouldNotWasteRoundsInTheActionsItReturns()
    {
        for (int n = MINIMUM_SIZE; n <= 20; n++) {
            int[][] actions = contest.solve(n);
            boolean[] used = new boolean[n + 1];

            assertNotNull("size " + n, actions);
            assertTrue("size " + n, actions.length <= n);

            for (int[] action : actions) {
                assertEquals("size " + n, 2, action.length);
                assertTrue("size " + n, action[0] >= 1 && action[0] <= n);
                assertFalse("size " + n, used[action[0]]);
                assertTrue("size " + n + " steps " + action[1],
                           action[1] >= 1 && action[1] <= n - 1);
                used[action[0]] = true;
            }
        }
    }

    /**
     * Verifies that the contest should not solve a machine whose size is
     * below the minimum or above the maximum of the problem, and that it
     * answers with an empty sequence instead of failing.
     */
    @Test
    public void shouldNotSolveMachinesWithAnInvalidSize()
    {
        int[] invalidSizes = {-3, -1, 0, 1, 2, 51, 100};

        for (int n : invalidSizes) {
            int[][] actions = contest.solve(n);

            assertNotNull("size " + n, actions);
            assertEquals("size " + n, 0, actions.length);
            assertFalse("size " + n, contest.solves(n));
        }
    }


    /**
     * Verifies that SlotMachineContest2 should keep the promises of the
     * contest it extends.
     *
     * The solver is used through the type SlotMachineContest, so this test
     * never asks which strategy is running: it only checks the contract
     * that any contest has to honour.
     */
    @Test
    public void shouldKeepTheSamePromisesWithTheOtherStrategy()
    {
        SlotMachineContest another = new SlotMachineContest2();

        for (int n = MINIMUM_SIZE; n <= 20; n++) {
            assertTrue("size " + n, another.solves(n));

            for (int[] action : another.solve(n)) {
                assertTrue("size " + n, action[0] >= 1 && action[0] <= n);
                assertTrue("size " + n, action[1] >= 1 && action[1] <= n - 1);
            }
        }
        assertTrue(another.solves(MAXIMUM_SIZE));
        assertFalse(another.solves(MAXIMUM_SIZE + 1));
    }
}
