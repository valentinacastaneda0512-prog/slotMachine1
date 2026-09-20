
/**
 * This class is the solution of the marathon.
 * 
 * Cristian Anzola- Danik Castañeda 
 * @version1 
 */

/**
 * Solves and simulates the Slot Machine contest problem.
 */
public class SlotMachineContest
{
    private SlotMachine slotMachine;
    private int n;
    private int[][] actions;

    /**
     * Creates a contest object.
     */
    public SlotMachineContest()
    {
        slotMachine = null;
        n = 0;
        actions = null;
    }

    /**
     * Solves the slot machine problem.
     *
     * @param n number of wheels and symbols
     * @return sequence of actions {wheel, steps} necessary to win
     */
    public int[][] solve(int n)
    {
        this.n = n;

        slotMachine = new SlotMachine(n);

        actions = solveMachine();

        return actions;
    }

    /**
     * Simulates the solution.
     *
     * The machine used by solve is made visible.
     *
     * @param n number of wheels and symbols
     */
    public void simulate(int n)
    {
        if (slotMachine == null || this.n != n) {
            solve(n);
        }

        slotMachine.makeVisible();
    }

    /**
     * Finds the actions necessary to win.
     *
     * @return sequence of actions {wheel, steps}
     */
    private int[][] solveMachine()
    {
        int[] targetSteps = new int[n];

        int[] firstScan = scan(1);

        int minimum = minimum(firstScan);

        int targetStep = -1;
        int commonStep = -1;

        for (int i = 0; i < n; i++) {

            if (firstScan[i] > minimum && targetStep == -1) {
                targetStep = i;
            }

            if (firstScan[i] == minimum && commonStep == -1) {
                commonStep = i;
            }
        }

        targetSteps[0] = targetStep;

        spinFromTo(1, 0, targetStep);

        
        for (int wheel = 2; wheel <= n; wheel++) {

            int[] targetScan = scan(wheel);

     
            spinFromTo(1, targetStep, commonStep);

    
            int[] commonScan = scan(wheel);

 
            int targetForWheel =
                findTarget(targetScan, commonScan);

            targetSteps[wheel - 1] = targetForWheel;

            spinFromTo(1, commonStep, targetStep);
        }

      
        spinFromTo(1, targetStep, 0);

        
        int actionCount = 0;

        for (int i = 0; i < n; i++) {

            if (targetSteps[i] != 0) {
                actionCount++;
            }
        }

 
        int[][] result = new int[actionCount][2];

        int position = 0;

        for (int i = 0; i < n; i++) {

            if (targetSteps[i] != 0) {

                result[position][0] = i + 1;
                result[position][1] = targetSteps[i];

                position++;
            }
        }

        
        for (int i = 0; i < n; i++) {

            if (targetSteps[i] != 0) {
                slotMachine.spin(
                    i + 1,
                    targetSteps[i]
                );
            }
        }

        return result;
    }

    /**
     * Scans all positions of a wheel.
     *
     * After the scan, the wheel returns to its original position.
     *
     * @param wheel wheel to scan
     * @return distinct-symbol values for every position
     */
    private int[] scan(int wheel)
    {
        int[] values = new int[n];

        for (int i = 0; i < n; i++) {

            values[i] = slotMachine.distinctSymbols();

            slotMachine.spin(wheel, 1);
        }

        return values;
    }

    /**
     * Finds the minimum value in an array.
     *
     * @param values values to analyze
     * @return minimum value
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
     * Finds the target position by comparing two scans.
     *
     * @param targetScan scan with the target symbol in wheel 1
     * @param commonScan scan with the common symbol in wheel 1
     * @return position of the target symbol
     */
    private int findTarget(
        int[] targetScan,
        int[] commonScan)
    {
        int minimumTarget = minimum(targetScan);
        int minimumCommon = minimum(commonScan);

        for (int i = 0; i < n; i++) {

            if (targetScan[i] == minimumTarget &&
                commonScan[i] > minimumCommon) {

                return i;
            }
        }

        return 0;
    }

    /**
     * Rotates a wheel from one relative position to another.
     *
     * @param wheel wheel to rotate
     * @param current current position
     * @param target target position
     */
    private void spinFromTo(
        int wheel,
        int current,
        int target)
    {
        int steps = (target - current + n) % n;

        if (steps != 0) {
            slotMachine.spin(wheel, steps);
        }
    }
    
    /**
     * verify tests
     */
    public boolean isSolved()
    {
        return slotMachine.isJackpot();
    }
}