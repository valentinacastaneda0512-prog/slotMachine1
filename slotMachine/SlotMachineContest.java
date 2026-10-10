/**
 * Solver for the slot machine problem.
 * Operates blindly without looking at colors, using only allowed methods.
 * @author Cristian Anzola, Danik Castaneda
 * @version 1
 */
public class SlotMachineContest
{
    private static final int SIMULATION_DELAY = 400;

    /**
     * Default constructor.
     */
    public SlotMachineContest()
    {
    }

    /**
     * Solves a new machine with n wheels and n symbols invisibly.
     */
    public int[][] solve(int n)
    {
        return solveMachine(new SlotMachine(n), n);
    }

    /**
     * Checks if a new machine can be completely solved (jackpot).
     */
    public boolean solves(int n)
    {
        SlotMachine machine = new SlotMachine(n);

        solveMachine(machine, n);

        return machine.distinctSymbols() == 1;
    }

    /**
     * Simulates the solution, making the machine visible step by step.
     */
    public void simulate(int n)
    {
        SlotMachine machine = new SlotMachine(n);

        if (machine.distinctSymbols() == 0) {
            return;
        }
        int[][] actions = solveMachine(machine, n);

        rewind(machine, actions);
        machine.makeVisible();
        pause(SIMULATION_DELAY);
        replay(machine, actions);
    }

    /**
     * Solves the given machine and leaves it in a winning state.
     */
    private int[][] solveMachine(SlotMachine machine, int n)
    {
        if (machine.distinctSymbols() == 0) {
            return new int[0][2];
        }
        int[][] actions = actionsToWin(machine, n);
        applyActions(machine, actions);
        return actions;
    }

    /**
     * Calculates the actions needed to win according to the strategy.
     */
    protected int[][] actionsToWin(SlotMachine machine, int n)
    {
        return toActions(stepsForEveryWheel(machine, n));
    }

    /**
     * Converts the steps array into an actions array, filtering out zeroes.
     */
    protected int[][] toActions(int[] steps)
    {
        int[][] actions = new int[countMoving(steps)][2];
        int position = 0;

        for (int i = 0; i < steps.length; i++) {
            if (steps[i] != 0) {
                actions[position][0] = i + 1;
                actions[position][1] = steps[i];
                position++;
            }
        }
        return actions;
    }

    /**
     * Counts how many wheels actually need to move.
     */
    private int countMoving(int[] steps)
    {
        int count = 0;

        for (int value : steps) {
            if (value != 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculates the steps required for each wheel to align with the reference.
     */
    private int[] stepsForEveryWheel(SlotMachine machine, int n)
    {
        int[] steps = new int[n];
        int[] firstScan = scan(machine, 1, n);
        int minimum = minimum(firstScan);
        int lonelyStep = firstPositionAbove(firstScan, minimum);
        int sharedStep = firstPositionEqualTo(firstScan, minimum);

        steps[0] = lonelyStep;
        spinFromTo(machine, 1, 0, lonelyStep, n);

        for (int wheel = 2; wheel <= n; wheel++) {
            steps[wheel - 1] = stepsForWheel(machine, wheel, lonelyStep, sharedStep, n);
        }
        spinFromTo(machine, 1, lonelyStep, 0, n);
        return steps;
    }

    /**
     * Calculates the steps required for a specific wheel relative to wheel 1.
     */
    private int stepsForWheel(SlotMachine machine, int wheel,
                             int lonelyStep, int sharedStep, int n)
    {
        int[] lonelyScan = scan(machine, wheel, n);

        spinFromTo(machine, 1, lonelyStep, sharedStep, n);
        int[] sharedScan = scan(machine, wheel, n);
        spinFromTo(machine, 1, sharedStep, lonelyStep, n);

        return findTarget(lonelyScan, sharedScan, n);
    }

    /**
     * Scans all positions of a wheel through a full rotation.
     */
    private int[] scan(SlotMachine machine, int wheel, int n)
    {
        int[] values = new int[n];

        for (int i = 0; i < n; i++) {
            values[i] = machine.distinctSymbols();
            machine.spin(wheel, 1);
        }
        return values;
    }

    /**
     * Finds the minimum value in an array.
     */
    private int minimum(int[] values)
    {
        int minimum = values[0];

        for (int i = 1; i < values.length; i++) {
            if (values[i] < minimum) {
                minimum = values[i];
            }
        }
        return minimum;
    }

    /**
     * Finds the first index with a value above the minimum.
     */
    private int firstPositionAbove(int[] values, int minimum)
    {
        for (int i = 0; i < values.length; i++) {
            if (values[i] > minimum) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the first index with a value equal to the minimum.
     */
    private int firstPositionEqualTo(int[] values, int minimum)
    {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == minimum) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the target position by comparing two scans.
     */
    private int findTarget(int[] targetScan, int[] commonScan, int n)
    {
        int minimumTarget = minimum(targetScan);
        int minimumCommon = minimum(commonScan);

        for (int i = 0; i < n; i++) {
            if (targetScan[i] == minimumTarget && commonScan[i] > minimumCommon) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Spins a wheel from its current position to the target position.
     */
    private void spinFromTo(SlotMachine machine, int wheel,
                            int current, int target, int n)
    {
        int steps = (target - current + n) % n;

        if (steps != 0) {
            machine.spin(wheel, steps);
        }
    }

    /**
     * Applies the sequence of actions to the machine.
     */
    private void applyActions(SlotMachine machine, int[][] actions)
    {
        for (int[] action : actions) {
            machine.spin(action[0], action[1]);
        }
    }

    /**
     * Reverses the actions taken to restore the machine to its initial state.
     */
    private void rewind(SlotMachine machine, int[][] actions)
    {
        for (int i = actions.length - 1; i >= 0; i--) {
            machine.spin(actions[i][0], -actions[i][1]);
        }
    }

    /**
     * Replays actions one by one with a visual delay.
     */
    private void replay(SlotMachine machine, int[][] actions)
    {
        for (int[] action : actions) {
            machine.spin(action[0], action[1]);
            pause(SIMULATION_DELAY);
        }
    }

    /**
     * Pauses execution for the specified time in milliseconds.
     */
    private void pause(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}