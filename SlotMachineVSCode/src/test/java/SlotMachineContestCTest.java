import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Shared unit test cases of the course.
 *
 * Each case keeps the intention and the name of the group that proposed
 * it, and the comment says which group it belongs to. Cases that reached
 * SlotMachine directly were rewritten to enter through SlotMachineContest,
 * which is the class under test here, without changing what they verify.
 *
 * @author Cristian Anzola, Danik Castaneda
 * @version 2
 */
public class SlotMachineContestCTest
{
    // ------------------------------------------------------------
    // Grupo: CanonA - PaezP
    // ------------------------------------------------------------

    /**
     * Verifies that every action has exactly the two values the problem
     * asks for: the wheel and the steps.
     *
     * Shared case of the group CanonA - PaezP, used as it was written.
     */
    @Test
    public void accordingCaPpshouldReturnValidMovesStructure() {
        SlotMachineContest contest = new SlotMachineContest();
        int n = 3;
        int[][] moves = contest.solve(n);

        assertNotNull(moves);
        for (int[] move : moves) {
            assertEquals(2, move.length);
        }
    }

    // ------------------------------------------------------------
    // Grupo: DiazR - RojasM
    // ------------------------------------------------------------

    /**
     * Verifies that a negative size builds no wheels and no symbols, so
     * there is nothing for the contest to do and nothing to win.
     *
     * Shared case of the group DiazR - RojasM. The original asked the
     * machine for its wheels and symbols; here the same situation is
     * checked through the contest, which answers with an empty sequence
     * and reports that it did not win.
     */
    @Test
    public void accordingDrRmShouldNotCreateWheelAndSymbolWithNegativeNumber() {
        int n = -3;
        SlotMachineContest contest = new SlotMachineContest();

        int[][] moves = contest.solve(n);

        assertNotNull(moves);
        assertEquals(0, moves.length);
        assertFalse(contest.solves(n));
    }
}
