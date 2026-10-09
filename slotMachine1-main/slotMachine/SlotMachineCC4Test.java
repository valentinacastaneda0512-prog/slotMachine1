import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Shared unit test cases of the course for Cycle 4 requirements.
 *
 * They cover the shared behaviors of the new wheel types:
 * LeftyWheel and RebelWheel.
 *
 * All the tests are invisible and do not use the graphical interface.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachineCC4Test
{
    private SlotMachine machine;

    /**
     * Creates a machine with three wheels.
     *
     * The first wheel is normal, the second is lefty and
     * the third is rebel. Each wheel contains the symbols
     * red, blue and yellow.
     */
    @Before
    public void setUp()
    {
        String[] colors = {"red", "blue", "yellow"};

        machine = new SlotMachine();

        for (String color : colors) {
            machine.addSymbol(color);
        }

        machine.addWheel(1, "normal");
        machine.addWheel(2, "lefty");
        machine.addWheel(3, "rebel");

        for (int wheel = 1; wheel <= 3; wheel++) {

            for (String color : colors) {
                machine.placeSymbol(wheel, color);
            }

            machine.spin(wheel, 0);
        }
    }


    /**
     * Tests that a LeftyWheel copies the state of the wheel
     * immediately to its left.
     *
     * The normal wheel is changed first. Then the LeftyWheel
     * is spun so that it copies the current state of its
     * immediate left wheel.
     */
    @Test
    public void shouldMakeLeftyWheelCopyImmediateLeftWheel()
    {
        String[] firstConfiguration =
            machine.configuration();

        machine.spin(1, 1);

        String[] secondConfiguration =
            machine.configuration();

        assertNotEquals(
            "The normal wheel should have changed state.",
            firstConfiguration[0],
            secondConfiguration[0]
        );

        machine.spin(2);

        String[] finalConfiguration =
            machine.configuration();

        assertEquals(
            "The LeftyWheel must copy the symbol "
            + "shown by the wheel immediately to its left.",
            finalConfiguration[0],
            finalConfiguration[1]
        );
    }

    /**
     * Tests that a RebelWheel cannot be locked,
     * swapped or deleted.
     *
     * The RebelWheel must remain in the machine after
     * all the restricted operations are attempted.
     */
    @Test
    public void shouldKeepRebelWheelUnaffectedByRestrictedOperations()
    {
        String[] before =
            machine.configuration();

        machine.lock(3);

        assertFalse(
            "The RebelWheel cannot be locked.",
            machine.ok()
        );

        machine.swap(1, 3);

        assertFalse(
            "The RebelWheel cannot participate in a swap.",
            machine.ok()
        );

        machine.delWheel(3);

        assertFalse(
            "The RebelWheel cannot be deleted.",
            machine.ok()
        );

        String[] after =
            machine.configuration();

        assertEquals(
            "The RebelWheel must remain in the machine.",
            3,
            after.length
        );

        assertArrayEquals(
            "The configuration must remain unchanged.",
            before,
            after
        );
    }
}