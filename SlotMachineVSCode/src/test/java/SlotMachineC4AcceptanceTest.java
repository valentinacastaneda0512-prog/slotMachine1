import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Acceptance tests for Cycle 4 of the Slot Machine.
 *
 * These tests verify the new wheel and symbol behaviors
 * through the graphical interface.
 *
 * The tests include delays so the different behaviors
 * can be observed visually.
 *
 * @author Cristian Anzola, Danik Castaneda
 * @version 1
 */
public class SlotMachineC4AcceptanceTest
{
    /**
     * First acceptance test: verifies the three wheel types.
     *
     * A normal wheel behaves normally.
     * A lefty wheel copies the state of the wheel immediately
     * to its left.
     * A rebel wheel cannot be locked, swapped or deleted.
     */
    @Test
    public void shouldShowTheThreeWheelTypes()
    {
        SlotMachine machine = new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.addWheel("rebel", 3);

        machine.addSymbol("red");
        machine.addSymbol("blue");
        machine.addSymbol("yellow");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "yellow");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(2, "yellow");

        machine.placeSymbol(3, "red");
        machine.placeSymbol(3, "blue");
        machine.placeSymbol(3, "yellow");

        machine.makeVisible();
        delay(2000);
        machine.spin(1, 1);
        delay(2000);
        assertEquals("blue", machine.configuration()[0]);

        machine.spin(2, 1);
        delay(2000);
        assertEquals("blue", machine.configuration()[1]);

        machine.lock(3);
        delay(2000);
        assertFalse(machine.ok());

        machine.swap(1, 3);
        delay(2000);
        assertFalse(machine.ok());

        machine.delWheel(3);
        delay(2000);
        assertFalse(machine.ok());
        assertEquals(3, machine.configuration().length);
        machine.exit();
    }

    /**
     * Second acceptance test: verifies the different symbol types.
     *
     * The machine contains RainbowSymbol, EphemeralSymbol
     * and ShySymbol objects.
     *
     * RainbowSymbol changes its size according to its color:
     * yellow increases, red decreases and blue remains normal.
     *
     * EphemeralSymbol decreases its size when selected.
     *
     * ShySymbol alternates its visibility when selected.
     *
     * The test also shows the jackpot behavior when all
     * wheels display the same symbol.
     */
    @Test
    public void shouldShowTheDifferentSymbolTypes()
    {
        SlotMachine machine = new SlotMachine();

        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);

        machine.addSymbol("rainbow", 1, "yellow");
        machine.addSymbol("rainbow", 2, "red");
        machine.addSymbol("rainbow", 3, "blue");
        machine.addSymbol("ephemeral", 4, "green");
        machine.addSymbol("shy", 5, "orange");

        machine.placeSymbol(1, "yellow");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
        machine.placeSymbol(1, "orange");

        machine.placeSymbol(2, "yellow");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(2, "green");
        machine.placeSymbol(2, "orange");

        machine.placeSymbol(3, "yellow");
        machine.placeSymbol(3, "red");
        machine.placeSymbol(3, "blue");
        machine.placeSymbol(3, "green");
        machine.placeSymbol(3, "orange");

        machine.makeVisible();
        delay(2000);
        machine.spin(new String[]{"yellow", "yellow", "yellow"});
        delay(2500);
        assertTrue(machine.isJackpot());
        machine.spin(new String[]{"yellow", "yellow", "yellow"});
        delay(2500);
        assertTrue(machine.isJackpot());
        machine.spin(new String[]{"yellow", "yellow", "yellow"});
        delay(2500);
        assertTrue(machine.isJackpot());
        machine.spin(new String[]{"red", "red", "red"});
        delay(2500);
        assertTrue(machine.isJackpot());
        machine.spin(new String[]{"blue", "blue", "blue"});
        delay(2500);
        assertTrue(machine.isJackpot());
        machine.spin(new String[]{"green", "green", "green"});
        delay(2500);
        assertTrue(machine.isJackpot());
        machine.spin(new String[]{"orange", "orange", "orange"});
        delay(2500);
        assertTrue(machine.isJackpot());
        machine.spin(new String[]{"orange", "orange", "orange"});
        delay(2500);
        assertTrue(machine.isJackpot());
        machine.exit();
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