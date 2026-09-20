import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Acceptance tests for SlotMachine.
 *
 * The machine is preloaded before the acceptance test.
 * These tests are invisible and do not use the canvas.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachineAcceptanceTest
{
    private SlotMachine machine;

    /**
     * Creates a preloaded slot machine.
     */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();

        machine.addSymbol("red");
        machine.addSymbol("blue");
        machine.addSymbol("green");

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(2, "green");

        machine.placeSymbol(3, "red");
        machine.placeSymbol(3, "blue");
        machine.placeSymbol(3, "green");
    }

    /**
     * Acceptance test that performs a complete
     * sequence of operations on the preloaded machine.
     */
    @Test
    public void shouldPlayCompleteSlotMachineSequence()
    {
        String[] firstConfiguration = {
            "red",
            "blue",
            "green"
        };

        machine.spin(firstConfiguration);

        assertArrayEquals(
            firstConfiguration,
            machine.configuration()
        );

        assertFalse(machine.isJackpot());

        machine.lock(1);

        String[] secondConfiguration = {
            "green",
            "green",
            "red"
        };

        machine.spin(secondConfiguration);

        assertEquals(
            "red",
            machine.configuration()[0]
        );

        assertEquals(
            "green",
            machine.configuration()[1]
        );

        assertEquals(
            "red",
            machine.configuration()[2]
        );

        machine.unlock(1);

        machine.spin(secondConfiguration);

        assertArrayEquals(
            secondConfiguration,
            machine.configuration()
        );

        machine.swap(1, 3);

        assertEquals(
            "red",
            machine.configuration()[0]
        );

        assertEquals(
            "green",
            machine.configuration()[1]
        );

        assertEquals(
            "green",
            machine.configuration()[2]
        );
    }
}