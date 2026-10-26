/**
 * Solver that reads the machine configuration to find a solution.
 * Inherits the process flow from SlotMachineContest and redefines actionsToWin.
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachineContest2 extends SlotMachineContest
{
    /**
     * Default constructor.
     */
    public SlotMachineContest2()
    {
        super();
    }

    /**
     * Decides winning actions by inspecting current wheel states.
     */
    @Override
    protected int[][] actionsToWin(SlotMachine machine, int n)
    {
        String[] visible = machine.configuration();
        String[] order = machine.symbols();
        String winner = mostRepeated(visible);

        return toActions(stepsToReach(visible, order, winner));
    }

    /**
     * Finds the most repeated symbol among visible wheels.
     */
    private String mostRepeated(String[] visible)
    {
        String winner = visible[0];
        int best = 0;

        for (String candidate : visible) {
            int repetitions = occurrences(visible, candidate);

            if (repetitions > best) {
                best = repetitions;
                winner = candidate;
            }
        }
        return winner;
    }

    /**
     * Counts how many times a given string appears in an array.
     */
    private int occurrences(String[] values, String value)
    {
        int count = 0;

        for (String current : values) {
            if (current.equals(value)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculates required steps for each wheel to match the winning symbol.
     */
    private int[] stepsToReach(String[] visible, String[] order, String winner)
    {
        int[] steps = new int[visible.length];

        for (int wheel = 0; wheel < visible.length; wheel++) {
            steps[wheel] = stepsBetween(order, visible[wheel], winner);
        }
        return steps;
    }

    /**
     * Calculates forward steps between two symbols in the wheel order.
     */
    private int stepsBetween(String[] order, String from, String to)
    {
        int size = order.length;

        return (positionOf(order, to) - positionOf(order, from) + size) % size;
    }

    /**
     * Finds the index of a value in an array.
     */
    private int positionOf(String[] values, String value)
    {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }
}