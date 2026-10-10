import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Acceptance tests for SlotMachine.
 *
 * These tests are VISIBLE: they are prepared to be
 * shown in the presentation, so every action waits
 * a moment to let the user see what happens.
 *
 * @author Cristian Anzola, Danik Castañeda
 */
public class SlotMachineAcceptanceTest
{
    private SlotMachine machine;

    /**
     * Creates a preloaded slot machine and makes it visible.
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

        machine.makeVisible();
        delay(1000);
    }

    /**
     * Closes the machine after each acceptance test.
     */
    @After
    public void tearDown()
    {
        machine.exit();
    }

    /**
     * Acceptance test that performs a complete sequence:
     * random spins, a given configuration, lock/unlock,
     * swap and finally a jackpot.
     */
    @Test
    public void shouldPlayCompleteSlotMachineSequence()
    {
        machine.spin(1);
        delay(1000);
        machine.spin(2);
        delay(1000);
        machine.spin(3);
        delay(1000);

        String[] firstConfiguration = {"red", "blue", "green"};
        machine.spin(firstConfiguration);
        delay(1500);

        assertArrayEquals(firstConfiguration, machine.configuration());
        assertFalse(machine.isJackpot());

        machine.lock(1);
        delay(1000);

        // Wheel 1 is locked: a message is shown and ok() becomes false.
        String[] secondConfiguration = {"green", "green", "red"};
        machine.spin(secondConfiguration);
        delay(1500);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
        assertEquals("green", machine.configuration()[1]);
        assertEquals("red", machine.configuration()[2]);

        machine.unlock(1);
        machine.spin(secondConfiguration);
        delay(1500);

        assertTrue(machine.ok());
        assertArrayEquals(secondConfiguration, machine.configuration());

        machine.swap(1, 3);
        delay(1500);

        assertEquals("red", machine.configuration()[0]);
        assertEquals("green", machine.configuration()[1]);
        assertEquals("green", machine.configuration()[2]);

        String[] jackpotConfiguration = {"red", "red", "red"};
        machine.spin(jackpotConfiguration);
        delay(2000);

        assertTrue(machine.isJackpot());
    }

    /**
     * Acceptance test that shows a step by step rotation
     * and how wheels are added and deleted while visible.
     */
    @Test
    public void shouldManageWheelsWhileVisible()
    {
        machine.spin(new String[] {"red", "red", "red"});
        delay(1500);
        assertTrue(machine.isJackpot());

        // The rotation is shown step by step (5 steps).
        machine.spin(2, 5);
        delay(1500);
        assertEquals("green", machine.configuration()[1]);
        assertFalse(machine.isJackpot());

        machine.addWheel(4);
        machine.placeSymbol(4, "blue");
        machine.spin(4, 0);
        delay(1500);
        assertEquals(4, machine.configuration().length);
        assertEquals("blue", machine.configuration()[3]);

        machine.delWheel(2);
        delay(1500);
        assertArrayEquals(new String[] {"red", "red", "blue"}, machine.configuration());

        machine.delWheel(3);
        delay(2000);
        assertTrue(machine.isJackpot());
    }

    /**
     * Waits so the user can see the machine.
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