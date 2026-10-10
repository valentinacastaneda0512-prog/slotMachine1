import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Shared unit test cases of the course for Cycle 4 requirements.
 *
 * They cover the shared behaviors of the new wheel types:
 * a locked LeftyWheel and the restrictions of a RebelWheel.
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

        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);
        machine.addWheel("rebel", 3);

        for (int wheel = 1; wheel <= 3; wheel++) {

            for (String color : colors) {
                machine.placeSymbol(wheel, color);
            }

            machine.spin(wheel, 0);
        }
    }


    /**
     * Tests that a locked LeftyWheel keeps its symbol when the
     * whole machine spins, even if its left wheel changes.
     */
    @Test
    public void shouldNotMoveLockedLeftyWheelWhenTheMachineSpins()
    {
        machine.spin(1, 1);
        machine.lock(2);

        assertTrue(
            "The LeftyWheel can be locked.",
            machine.ok()
        );

        for (int i = 0; i < 20; i++) {
            machine.spin();

            assertEquals(
                "A locked LeftyWheel must keep its symbol.",
                "red",
                machine.configuration()[1]
            );
        }
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