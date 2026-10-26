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

    // ------------------------------------------------------------
    // Grupo 6: CañonA - PaezP
    // ------------------------------------------------------------

    /**
     * Verifies that a RebelWheel cannot be deleted, but a normal
     * wheel of the same machine can.
     *
     * Shared case of the group CañonA - PaezP, used as it was written.
     */
    @Test
    public void acordingCaPpShouldNotDeleteRebelWheelbutShouldDeleteNormalWheel() {
        SlotMachine machine = new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);

        machine.delWheel(2);

        assertFalse(machine.ok());
        assertEquals(2, machine.configuration().length);

        machine.delWheel(1);
        assertTrue(machine.ok());
        assertEquals(1, machine.configuration().length);
    }

    // ------------------------------------------------------------
    // Grupo 03: Juan Carreño - Juan Castaño
    // ------------------------------------------------------------

    /**
     * Verifies that ephemeral and shy symbols are still symbols of
     * their color: they are listed by their color and can be removed
     * by their color.
     *
     * Shared case of the group of Juan Carreño - Juan Castaño, used as it was written.
     */
    @Test
    public void accordingCgCpShouldHandleEphemeralAndShySymbolsByTheirColor()
    {
        SlotMachine slotMachine = new SlotMachine();
        slotMachine.makeInvisible();
        slotMachine.addSymbol(1, "red");
        slotMachine.addSymbol("ephemeral", 2, "yellow");
        assertTrue(slotMachine.ok());
        slotMachine.addSymbol("shy", 3, "purple");
        assertTrue(slotMachine.ok());
        assertArrayEquals(new String[]{"red", "yellow", "purple"},
                          slotMachine.symbols());
        slotMachine.delSymbol("yellow");
        assertTrue(slotMachine.ok());
        assertArrayEquals(new String[]{"red", "purple"},
                          slotMachine.symbols());
    }

    // ------------------------------------------------------------
    // Grupo 04: Gomez Boada Julian
    // ------------------------------------------------------------

    /**
     * Verifies that a symbol of another type cannot introduce a color
     * that already exists in the machine.
     *
     * Shared case of the group of Gomez Boada Julian. The original called
     * addSymbol(type, color, pos); here the parameters follow the order
     * of the class diagram, addSymbol(type, pos, color), without
     * changing what the case verifies.
     */
    @Test
    public void accordingGbShouldNotAllowDuplicateColorAcrossSymbolTypes() {
        SlotMachine machine = new SlotMachine();
        machine.addWheel("normal", 1);
        machine.addSymbol("normal", 1, "red");
        assertTrue(machine.ok());

        machine.addSymbol("pulsing", 1, "red");
        assertFalse(machine.ok());

        assertArrayEquals(new String[]{"red"}, machine.symbols());
    }
}