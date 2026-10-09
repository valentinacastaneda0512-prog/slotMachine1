import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Shared unit test cases of the course for the cycle 2 requirements.
 * They cover swap together with lock, and spin(setSymbols).
 *
 * The cases were taken from the collective wiki. Each one keeps the name
 * and the intention of the group that proposed it, and the comment says
 * which group it belongs to. All the tests are invisible.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachineCC2Test
{
    private SlotMachine machine;

    /**
     * Creates a machine of three wheels, each one with the symbols red,
     * blue and green, showing its first symbol.
     */
    @Before
    public void setUp()
    {
        String[] colors = {"red", "blue", "green"};

        machine = new SlotMachine();
        for (String color : colors) {
            machine.addSymbol(color);
        }
        for (int wheel = 1; wheel <= 3; wheel++) {
            machine.addWheel(wheel);
            for (String color : colors) {
                machine.placeSymbol(wheel, color);
            }
            machine.spin(wheel, 0);
        }
    }

    // ------------------------------------------------------------
    // Grupo 7: Murillo
    // ------------------------------------------------------------

    /**
     * What should it do? Swap two wheels even if one of them is locked,
     * keeping the lock in the new position.
     * What should it not do? Cancel the swap or lose the lock.
     *
     * Shared case of the group Murillo, used as it was written.
     */
    @Test
    public void accordingMurShouldSwapLockedWheelsAndMaintainLockState()
    {
        machine.lock(1);
        assertTrue(machine.ok());

        machine.swap(1, 2);
        assertTrue(machine.ok());

        machine.spin(2, 3);
        assertFalse(machine.ok());

        machine.spin(1, 3);
        assertTrue(machine.ok());
    }

    // ------------------------------------------------------------
    // G02: MeloR - SanabriaE
    // ------------------------------------------------------------

    /**
     * What should it not do? Apply any change if one of the requested
     * colors does not exist in its wheel.
     *
     * Shared case of the group MeloR - SanabriaE, used as it was written.
     */
    @Test
    public void accordingMrSeShouldRejectSpinSetSymbolsWhenColorMissing()
    {
        SlotMachine m = new SlotMachine();
        m.addWheel(1);
        m.addWheel(2);
        m.addSymbol(1, "red");
        m.addSymbol(2, "green");

        String[] before = m.configuration();
        m.spin(new String[] {"red", "purple"});

        assertFalse(m.ok());
        assertArrayEquals(before, m.configuration());
    }
}