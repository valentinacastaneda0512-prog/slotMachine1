import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the cycle 2 requirements of SlotMachine:
 * swap, lock, unlock, spin(wheel, steps) and spin(setSymbols).
 *
 * All the tests are invisible. Each group answers two questions:
 * what should the method do? and what should it NOT do?
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachineC2Test
{
    private SlotMachine machine;

    /**
     * Creates an empty machine with three registered symbols.
     */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();
        machine.addSymbol("red");
        machine.addSymbol("blue");
        machine.addSymbol("green");
    }

    /**
     * Adds a wheel at the end with the given symbols and leaves the first of them visible.
     */
    private void addWheelWith(String... colors)
    {
        int position = machine.configuration().length + 1;
        machine.addWheel(position);
        for (String color : colors) {
            machine.placeSymbol(position, color);
        }
        machine.spin(position, 0);
    }

    // swap

    /**
     * Verifies that the machine should swap first and last wheel.
     */
    @Test
    public void shouldSwapFirstAndLastWheel()
    {
        addWheelWith("red");
        addWheelWith("blue");
        addWheelWith("green");

        machine.swap(1, 3);

        assertTrue(machine.ok());
        assertArrayEquals(new String[] {"green", "blue", "red"}, machine.configuration());
    }

    /**
     * Verifies that the machine should swap adjacent wheels.
     */
    @Test
    public void shouldSwapAdjacentWheels()
    {
        addWheelWith("red");
        addWheelWith("blue");

        machine.swap(2, 1);

        assertArrayEquals(new String[] {"blue", "red"}, machine.configuration());
    }

    /**
     * Verifies that the machine should move the whole wheel when swapping.
     */
    @Test
    public void shouldMoveTheWholeWheelWhenSwapping()
    {
        addWheelWith("red", "blue", "green");
        addWheelWith("green");

        machine.swap(1, 2);
        machine.spin(2, 1);

        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Verifies that the machine should ignore swap with first invalid position.
     */
    @Test
    public void shouldIgnoreSwapWithFirstInvalidPosition()
    {
        addWheelWith("red");
        addWheelWith("blue");

        machine.swap(0, 2);

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should ignore swap with second invalid position.
     */
    @Test
    public void shouldIgnoreSwapWithSecondInvalidPosition()
    {
        addWheelWith("red");
        addWheelWith("blue");

        machine.swap(1, 3);

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should ignore swap using same wheel.
     */
    @Test
    public void shouldIgnoreSwapUsingSameWheel()
    {
        addWheelWith("red");
        addWheelWith("blue");

        machine.swap(1, 1);

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    // lock and unlock

    /**
     * Verifies that the machine should not rotate locked wheel by steps.
     */
    @Test
    public void shouldNotRotateLockedWheelBySteps()
    {
        addWheelWith("red", "blue");

        machine.lock(1);
        assertTrue(machine.ok());
        machine.spin(1, 1);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should not spin locked wheel randomly.
     */
    @Test
    public void shouldNotSpinLockedWheelRandomly()
    {
        addWheelWith("red", "blue", "green");
        machine.lock(1);

        for (int i = 0; i < 20; i++) {
            machine.spin(1);
            assertFalse(machine.ok());
            assertEquals("red", machine.configuration()[0]);
        }
    }

    /**
     * Verifies that the machine should keep locked wheel when spinning all.
     */
    @Test
    public void shouldKeepLockedWheelWhenSpinningAll()
    {
        addWheelWith("red", "blue", "green");
        addWheelWith("red", "blue", "green");
        machine.lock(1);

        for (int i = 0; i < 20; i++) {
            machine.spin();
            assertEquals("red", machine.configuration()[0]);
        }
    }

    /**
     * Verifies that the machine should unlock wheel.
     */
    @Test
    public void shouldUnlockWheel()
    {
        addWheelWith("red", "blue");
        machine.lock(1);

        machine.unlock(1);
        assertTrue(machine.ok());
        machine.spin(1, 1);

        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should lock only the selected wheel.
     */
    @Test
    public void shouldLockOnlyTheSelectedWheel()
    {
        addWheelWith("red", "blue");
        addWheelWith("red", "blue");

        machine.lock(1);
        machine.spin(1, 1);
        machine.spin(2, 1);

        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should keep lock when wheel is swapped.
     */
    @Test
    public void shouldKeepLockWhenWheelIsSwapped()
    {
        addWheelWith("red", "blue");
        addWheelWith("green", "blue");
        machine.lock(1);

        machine.swap(1, 2);
        machine.spin(2, 1);
        machine.spin(1, 1);

        assertArrayEquals(new String[] {"blue", "red"}, machine.configuration());
    }

    /**
     * Verifies that the machine should ignore lock on invalid position.
     */
    @Test
    public void shouldIgnoreLockOnInvalidPosition()
    {
        addWheelWith("red", "blue");

        machine.lock(-1);
        assertFalse(machine.ok());
        machine.lock(2);
        assertFalse(machine.ok());

        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should ignore unlock on invalid position.
     */
    @Test
    public void shouldIgnoreUnlockOnInvalidPosition()
    {
        addWheelWith("red", "blue");
        machine.lock(1);

        machine.unlock(5);
        assertFalse(machine.ok());

        machine.spin(1, 1);
        assertEquals("red", machine.configuration()[0]);
    }

    // spin(wheel, steps)

    /**
     * Verifies that the machine should show first symbol with zero steps.
     */
    @Test
    public void shouldShowFirstSymbolWithZeroSteps()
    {
        addWheelWith("red", "blue", "green");

        machine.spin(1, 0);

        assertTrue(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should spin forward two steps.
     */
    @Test
    public void shouldSpinForwardTwoSteps()
    {
        addWheelWith("red", "blue", "green");

        machine.spin(1, 2);

        assertEquals("green", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should spin backward one step.
     */
    @Test
    public void shouldSpinBackwardOneStep()
    {
        addWheelWith("red", "blue", "green");

        machine.spin(1, -1);

        assertEquals("green", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should spin backward two steps.
     */
    @Test
    public void shouldSpinBackwardTwoSteps()
    {
        addWheelWith("red", "blue", "green");

        machine.spin(1, -2);

        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should return to initial position after full rotation.
     */
    @Test
    public void shouldReturnToInitialPositionAfterFullRotation()
    {
        addWheelWith("red", "blue", "green");

        machine.spin(1, 3);

        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should wrap around with more steps than symbols.
     */
    @Test
    public void shouldWrapAroundWithMoreStepsThanSymbols()
    {
        addWheelWith("red", "blue", "green");

        machine.spin(1, 7);

        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should wrap around with many backward steps.
     */
    @Test
    public void shouldWrapAroundWithManyBackwardSteps()
    {
        addWheelWith("red", "blue", "green");

        machine.spin(1, -4);

        assertEquals("green", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should only rotate the selected wheel.
     */
    @Test
    public void shouldOnlyRotateTheSelectedWheel()
    {
        addWheelWith("red", "blue");
        addWheelWith("red", "blue");

        machine.spin(2, 1);

        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should ignore steps on invalid wheel.
     */
    @Test
    public void shouldIgnoreStepsOnInvalidWheel()
    {
        addWheelWith("red", "blue");

        machine.spin(0, 1);
        assertFalse(machine.ok());
        machine.spin(2, 1);
        assertFalse(machine.ok());

        assertEquals("red", machine.configuration()[0]);
    }

    // spin(setSymbols)

    /**
     * Verifies that the machine should leave machine in given configuration.
     */
    @Test
    public void shouldLeaveMachineInGivenConfiguration()
    {
        addWheelWith("red", "blue", "green");
        addWheelWith("red", "blue", "green");
        addWheelWith("red", "blue", "green");
        String[] wanted = {"green", "red", "blue"};

        machine.spin(wanted);

        assertTrue(machine.ok());
        assertArrayEquals(wanted, machine.configuration());
    }

    /**
     * Verifies that the machine should reach jackpot with given configuration.
     */
    @Test
    public void shouldReachJackpotWithGivenConfiguration()
    {
        addWheelWith("red", "blue");
        addWheelWith("blue", "red");

        machine.spin(new String[] {"blue", "blue"});

        assertTrue(machine.isJackpot());
    }

    /**
     * Verifies that the machine should set configuration on wheels never spun.
     */
    @Test
    public void shouldSetConfigurationOnWheelsNeverSpun()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");

        machine.spin(new String[] {"blue"});

        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should ignore null configuration.
     */
    @Test
    public void shouldIgnoreNullConfiguration()
    {
        addWheelWith("red", "blue");

        machine.spin((String[]) null);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should ignore configuration with wrong size.
     */
    @Test
    public void shouldIgnoreConfigurationWithWrongSize()
    {
        addWheelWith("red", "blue");
        addWheelWith("red", "blue");

        machine.spin(new String[] {"blue"});
        assertFalse(machine.ok());
        machine.spin(new String[] {"blue", "blue", "blue"});
        assertFalse(machine.ok());

        assertArrayEquals(new String[] {"red", "red"}, machine.configuration());
    }

    /**
     * Verifies that the machine should keep wheel that does not have the symbol.
     */
    @Test
    public void shouldKeepWheelThatDoesNotHaveTheSymbol()
    {
        addWheelWith("red", "blue");
        addWheelWith("red", "blue");

        machine.spin(new String[] {"green", "blue"});

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should keep locked wheel when setting configuration.
     */
    @Test
    public void shouldKeepLockedWheelWhenSettingConfiguration()
    {
        addWheelWith("red", "blue");
        addWheelWith("red", "blue");
        machine.lock(1);

        machine.spin(new String[] {"blue", "blue"});

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }
}
