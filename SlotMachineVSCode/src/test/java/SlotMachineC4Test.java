import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit tests for the fourth cycle of SlotMachine.
 *
 * These tests verify the behavior of the different
 * wheel and symbol types introduced in Cycle 4.
 *
 * The tests do not use the graphical interface.
 * The graphical interface will be tested through
 * acceptance tests.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachineC4Test
{
    /**
     * Verifies that a LeftyWheel copies the state of
     * the wheel immediately to its left.
     */
    @Test
    public void shouldCopyTheStateOfTheImmediateLeftWheel()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("lefty", 2);

        machine.addSymbol("red");
        machine.addSymbol("blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");

        machine.spin(1, 0);

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
            "The LeftyWheel must copy the state of "
            + "the wheel immediately to its left.",
            finalConfiguration[0],
            finalConfiguration[1]
        );
    }

    /**
     * Verifies that a LeftyWheel copies only the
     * immediate left wheel and not another wheel.
     */
    @Test
    public void shouldCopyOnlyTheImmediateLeftWheel()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("normal", 2);
        machine.addWheel("lefty", 3);

        machine.addSymbol("red");
        machine.addSymbol("blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");

        machine.placeSymbol(3, "red");
        machine.placeSymbol(3, "blue");

        machine.spin(1, 0);
        machine.spin(2, 1);

        String[] before =
            machine.configuration();

        assertNotEquals(
            "The two normal wheels should have different states.",
            before[0],
            before[1]
        );

        machine.spin(3);

        String[] after =
            machine.configuration();

        assertEquals(
            "The LeftyWheel must copy wheel 2.",
            after[1],
            after[2]
        );

        assertNotEquals(
            "The LeftyWheel must not copy wheel 1.",
            after[0],
            after[2]
        );
    }

    /**
     * Verifies that a RebelWheel cannot be locked,
     * while a normal wheel can still be locked.
     */
    @Test
    public void shouldPreventLockingOnlyTheRebelWheel()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);

        machine.lock(1);

        assertTrue(
            "The normal wheel should be lockable.",
            machine.ok()
        );

        machine.lock(2);

        assertFalse(
            "The RebelWheel must not be lockable.",
            machine.ok()
        );
    }

    /**
     * Verifies that a RebelWheel cannot be deleted.
     *
     * A normal wheel is deleted first so that the RebelWheel
     * changes its position. The test then verifies that the
     * RebelWheel remains protected at its new position.
     */
    @Test
    public void shouldPreventDeletingTheRebelWheel()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);
        machine.addWheel("normal", 3);

        machine.delWheel(1);

        assertTrue(
            "The normal wheel should be deletable.",
            machine.ok()
        );

        String[] configuration =
            machine.configuration();

        assertEquals(
            "Two wheels should remain after deleting "
            + "the normal wheel.",
            2,
            configuration.length
        );

        machine.delWheel(1);

        assertFalse(
            "The RebelWheel should not be deletable.",
            machine.ok()
        );

        configuration =
            machine.configuration();

        assertEquals(
            "The RebelWheel must remain in the machine.",
            2,
            configuration.length
        );
    }

    /**
     * Verifies that a RebelWheel cannot participate
     * in a swap.
     *
     * The test also verifies that a failed swap does not
     * modify the configuration of either wheel.
     */
    @Test
    public void shouldPreventSwappingTheRebelWheel()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("rebel", 2);

        machine.addSymbol("red");
        machine.addSymbol("blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");

        machine.spin(1, 0);
        machine.spin(2, 1);

        String[] before =
            machine.configuration();

        assertNotEquals(
            "The wheels should have different states.",
            before[0],
            before[1]
        );

        machine.swap(1, 2);

        assertFalse(
            "A RebelWheel must not participate in a swap.",
            machine.ok()
        );

        String[] after =
            machine.configuration();

        assertEquals(
            "The first wheel must keep its state.",
            before[0],
            after[0]
        );

        assertEquals(
            "The RebelWheel must keep its state.",
            before[1],
            after[1]
        );
    }

    /**
     * Verifies that two normal wheels can still be exchanged
     * when a RebelWheel is present in the same machine.
     */
    @Test
    public void shouldKeepRebelWheelUnaffectedByOtherValidSwaps()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("normal", 2);
        machine.addWheel("rebel", 3);

        machine.addSymbol("red");
        machine.addSymbol("blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");

        machine.placeSymbol(3, "red");
        machine.placeSymbol(3, "blue");

        machine.spin(1, 0);
        machine.spin(2, 1);
        machine.spin(3, 0);

        String[] before =
            machine.configuration();

        machine.swap(1, 2);

        assertTrue(
            "Two normal wheels should be exchangeable.",
            machine.ok()
        );

        String[] after =
            machine.configuration();

        assertEquals(
            "The RebelWheel must remain unchanged.",
            before[2],
            after[2]
        );

        assertEquals(
            "Wheel 1 should receive wheel 2 state.",
            before[1],
            after[0]
        );

        assertEquals(
            "Wheel 2 should receive wheel 1 state.",
            before[0],
            after[1]
        );
    }

    /**
     * Verifies that the Lefty relationship is updated after
     * two normal wheels are exchanged.
     *
     * The LeftyWheel must follow the wheel that is currently
     * immediately to its left.
     */
    @Test
    public void shouldUpdateLeftyRelationshipAfterSwap()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel("normal", 1);
        machine.addWheel("normal", 2);
        machine.addWheel("lefty", 3);

        machine.addSymbol("red");
        machine.addSymbol("blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");

        machine.placeSymbol(3, "red");
        machine.placeSymbol(3, "blue");

        machine.spin(1, 0);
        machine.spin(2, 1);

        machine.spin(3);

        String[] beforeSwap =
            machine.configuration();

        assertEquals(
            "red",
            beforeSwap[0]
        );

        assertEquals(
            "blue",
            beforeSwap[1]
        );

        assertEquals(
            "blue",
            beforeSwap[2]
        );

        machine.swap(1, 2);

        machine.spin(3);

        String[] afterSwap =
            machine.configuration();

        assertEquals(
            "blue",
            afterSwap[0]
        );

        assertEquals(
            "red",
            afterSwap[1]
        );

        assertEquals(
            "red",
            afterSwap[2]
        );

        assertNotEquals(
            "The LeftyWheel must follow wheel 2, "
            + "not wheel 1.",
            afterSwap[0],
            afterSwap[2]
        );
    }

    /**
     * Verifies that an EphemeralSymbol decreases its
     * size every time it is selected.
     *
     * This is a pure unit test and does not create a Wheel
     * or open the graphical interface.
     */
    @Test
    public void shouldDecreaseEphemeralSymbolSizeAfterSeveralSelections()
    {
        EphemeralSymbol ephemeral =
            new EphemeralSymbol("red");

        int initialSize =
            ephemeral.getSize();

        ephemeral.selected();

        assertEquals(
            initialSize - 1,
            ephemeral.getSize()
        );

        ephemeral.selected();
        ephemeral.selected();
        ephemeral.selected();
        ephemeral.selected();

        assertEquals(
            initialSize - 5,
            ephemeral.getSize()
        );
    }

    /**
     * Verifies that an EphemeralSymbol cannot become
     * smaller than one.
     */
    @Test
    public void shouldKeepEphemeralSymbolAtMinimumSize()
    {
        EphemeralSymbol ephemeral =
            new EphemeralSymbol("red");

        int initialSize =
            ephemeral.getSize();

        for (int i = 0; i < initialSize + 10; i++) {
            ephemeral.selected();
        }

        assertEquals(
            1,
            ephemeral.getSize()
        );
    }

    /**
     * Verifies that different EphemeralSymbols maintain
     * their sizes independently.
     *
     * Selecting one symbol must not modify the other.
     */
    @Test
    public void shouldMaintainIndependentEphemeralSymbolSizes()
    {
        EphemeralSymbol first =
            new EphemeralSymbol("red");

        EphemeralSymbol second =
            new EphemeralSymbol("blue");

        int firstInitialSize =
            first.getSize();

        int secondInitialSize =
            second.getSize();

        first.selected();

        assertEquals(
            firstInitialSize - 1,
            first.getSize()
        );

        assertEquals(
            secondInitialSize,
            second.getSize()
        );

        second.selected();

        assertEquals(
            firstInitialSize - 1,
            first.getSize()
        );

        assertEquals(
            secondInitialSize - 1,
            second.getSize()
        );

        second.selected();

        assertEquals(
            firstInitialSize - 1,
            first.getSize()
        );

        assertEquals(
            secondInitialSize - 2,
            second.getSize()
        );
    }

    /**
     * Verifies that a ShySymbol starts with its initial
     * visibility state and alternates every time it is selected.
     *
     * This test does not use Wheel or the graphical interface.
     */
    @Test
    public void shouldAlternateShySymbolVisibilityAfterSeveralSelections()
    {
        ShySymbol shy =
            new ShySymbol("blue");

        boolean initialState =
            shy.isVisible();

        shy.selected();

        boolean firstState =
            shy.isVisible();

        assertNotEquals(
            "The first selection must change visibility.",
            initialState,
            firstState
        );

        shy.selected();

        boolean secondState =
            shy.isVisible();

        assertEquals(
            "Two selections must return to the initial state.",
            initialState,
            secondState
        );

        shy.selected();

        boolean thirdState =
            shy.isVisible();

        assertEquals(
            "The third selection must have the same state "
            + "as the first selection.",
            firstState,
            thirdState
        );

        shy.selected();

        boolean fourthState =
            shy.isVisible();

        assertEquals(
            "The fourth selection must return to the state "
            + "after two selections.",
            secondState,
            fourthState
        );
    }

    /**
     * Verifies that a ShySymbol changes only when its
     * own selected() method is executed.
     *
     * No Wheel or graphical interface is used.
     */
    @Test
    public void shouldChangeShyOnlyWhenItIsSelected()
    {
        ShySymbol shy =
            new ShySymbol("blue");

        Symbol normal =
            new Symbol("red");

        boolean initialShyState =
            shy.isVisible();

        normal.selected();

        assertEquals(
            "Selecting a normal symbol must not affect Shy.",
            initialShyState,
            shy.isVisible()
        );

        shy.selected();

        boolean afterFirstSelection =
            shy.isVisible();

        assertNotEquals(
            "The ShySymbol must change when selected.",
            initialShyState,
            afterFirstSelection
        );

        normal.selected();

        assertEquals(
            "Selecting another symbol must not affect Shy.",
            afterFirstSelection,
            shy.isVisible()
        );

        shy.selected();

        assertEquals(
            "Two Shy selections must return to the initial state.",
            initialShyState,
            shy.isVisible()
        );
    }

    /**
     * Verifies that normal, ephemeral and shy symbols
     * maintain their own behavior independently.
     *
     * This is a pure unit test. It does not create a Wheel,
     * SlotMachine or graphical interface.
     */
    @Test
    public void shouldMaintainIndependentBehaviorAmongThreeSymbolTypes()
    {
        Symbol normal =
            new Symbol("red");

        EphemeralSymbol ephemeral =
            new EphemeralSymbol("blue");

        ShySymbol shy =
            new ShySymbol("green");

        int initialEphemeralSize =
            ephemeral.getSize();

        boolean initialShyVisibility =
            shy.isVisible();

        normal.selected();

        assertEquals(
            initialEphemeralSize,
            ephemeral.getSize()
        );

        assertEquals(
            initialShyVisibility,
            shy.isVisible()
        );

        ephemeral.selected();

        assertEquals(
            initialEphemeralSize - 1,
            ephemeral.getSize()
        );

        assertEquals(
            initialShyVisibility,
            shy.isVisible()
        );

        shy.selected();

        assertEquals(
            initialEphemeralSize - 1,
            ephemeral.getSize()
        );

        assertNotEquals(
            initialShyVisibility,
            shy.isVisible()
        );

        boolean shyAfterFirstSelection =
            shy.isVisible();

        normal.selected();

        assertEquals(
            initialEphemeralSize - 1,
            ephemeral.getSize()
        );

        assertEquals(
            shyAfterFirstSelection,
            shy.isVisible()
        );

        ephemeral.selected();

        assertEquals(
            initialEphemeralSize - 2,
            ephemeral.getSize()
        );

        assertEquals(
            shyAfterFirstSelection,
            shy.isVisible()
        );

        shy.selected();

        assertEquals(
            initialShyVisibility,
            shy.isVisible()
        );

        assertEquals(
            initialEphemeralSize - 2,
            ephemeral.getSize()
        );
    }

    /**
     * Verifies that a normal Symbol does not acquire
     * the special behavior of EphemeralSymbol or ShySymbol.
     *
     * The test uses selected() directly and does not
     * open the graphical interface.
     */
    @Test
    public void shouldKeepNormalSymbolUnchangedAfterSeveralSelections()
    {
        Symbol normal =
            new Symbol("red");

        int initialSize =
            normal.getSize();

        boolean initialVisibility =
            normal.isVisible();

        normal.selected();
        normal.selected();
        normal.selected();
        normal.selected();
        normal.selected();

        assertEquals(
            "A normal symbol must keep its size.",
            initialSize,
            normal.getSize()
        );

        assertEquals(
            "A normal symbol must not alternate visibility.",
            initialVisibility,
            normal.isVisible()
        );
    }

    /**
     * Creates a machine with the symbols red, blue and yellow
     * and the given wheel types, each wheel showing red.
     *
     * @param types types of the wheels, from left to right
     * @return the new machine
     */
    private SlotMachine machineWith(String... types)
    {
        SlotMachine machine = new SlotMachine();
        machine.addSymbol("red");
        machine.addSymbol("blue");
        machine.addSymbol("yellow");
        for (int wheel = 1; wheel <= types.length; wheel++) {
            machine.addWheel(types[wheel - 1], wheel);
            machine.placeSymbol(wheel, "red");
            machine.placeSymbol(wheel, "blue");
            machine.placeSymbol(wheel, "yellow");
            machine.spin(wheel, 0);
        }
        return machine;
    }

    /**
     * Creates a wheel with the symbols red, blue and yellow,
     * showing red.
     *
     * @param wheel wheel to fill
     * @return the same wheel
     */
    private Wheel filled(Wheel wheel)
    {
        wheel.addSymbol(new Symbol("red"));
        wheel.addSymbol(new Symbol("blue"));
        wheel.addSymbol(new Symbol("yellow"));
        wheel.spin(0);
        return wheel;
    }

    /**
     * Verifies that a normal wheel placed between a LeftyWheel
     * and a RebelWheel can still be locked, refuses to spin while
     * locked and spins again after being unlocked.
     */
    @Test
    public void shouldKeepNormalWheelBehaviorBetweenLeftyAndRebel()
    {
        SlotMachine machine = machineWith("lefty", "normal", "rebel");

        machine.lock(2);
        assertTrue(machine.ok());

        machine.spin(2, 1);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[1]);

        machine.unlock(2);
        machine.spin(2, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Verifies that a locked LeftyWheel does not copy its left
     * neighbor when the whole machine spins.
     */
    @Test
    public void shouldNotChangeLockedLeftyWheelWhenSpinningAll()
    {
        SlotMachine machine = machineWith("normal", "lefty");
        machine.spin(1, 1);
        machine.lock(2);

        for (int i = 0; i < 20; i++) {
            machine.spin();
            assertEquals("red", machine.configuration()[1]);
        }
    }

    /**
     * Verifies that a locked LeftyWheel ignores both spin methods,
     * even when it has a left neighbor to copy.
     */
    @Test
    public void shouldIgnoreBothSpinMethodsWhenLeftyWheelIsLocked()
    {
        Wheel left = filled(new Wheel());
        LeftyWheel lefty = new LeftyWheel();
        filled(lefty);
        lefty.setLeftWheel(left);
        left.spin(2);
        lefty.lock();

        lefty.spin();
        assertEquals("red", lefty.visibleSymbol());

        lefty.spin(1);
        assertEquals("red", lefty.visibleSymbol());
    }

    /**
     * Verifies that a LeftyWheel spun by steps copies its left
     * neighbor instead of moving the given number of steps.
     */
    @Test
    public void shouldCopyLeftWheelWhenLeftyIsSpunBySteps()
    {
        SlotMachine machine = machineWith("normal", "lefty");
        machine.spin(1, 2);

        machine.spin(2, 1);

        assertTrue(machine.ok());
        assertEquals("yellow", machine.configuration()[1]);
    }

    /**
     * Verifies that a LeftyWheel in the first position has no
     * neighbor to copy, so it spins like a normal wheel.
     */
    @Test
    public void shouldSpinLeftyWheelNormallyWhenItIsFirst()
    {
        SlotMachine machine = machineWith("lefty");

        machine.spin(1, 2);

        assertTrue(machine.ok());
        assertEquals("yellow", machine.configuration()[0]);
    }

    /**
     * Verifies that a LeftyWheel takes the new left neighbor after
     * the wheel to its left is deleted.
     */
    @Test
    public void shouldCopyTheNewLeftNeighborAfterDeletingAWheel()
    {
        SlotMachine machine = machineWith("normal", "normal", "lefty");
        machine.spin(1, 2);

        machine.delWheel(2);
        machine.spin(2, 1);

        assertEquals("yellow", machine.configuration()[1]);
    }

    /**
     * Verifies that a RebelWheel still spins and can be unlocked:
     * it only refuses lock, swap and delete.
     */
    @Test
    public void shouldLetRebelWheelSpinAndUnlock()
    {
        SlotMachine machine = machineWith("rebel");

        machine.spin(1, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);

        machine.unlock(1);
        assertTrue(machine.ok());
    }

    /**
     * Verifies that an unknown symbol type is rejected and the
     * symbol is not registered.
     */
    @Test
    public void shouldRejectUnknownSymbolType()
    {
        SlotMachine machine = machineWith();

        machine.addSymbol("magic", 1, "green");

        assertFalse(machine.ok());
        assertEquals(3, machine.symbols().length);
    }

    /**
     * Verifies that an ephemeral symbol reaches the size of a point
     * exactly after 29 selections and never goes below it.
     */
    @Test
    public void shouldReachMinimumSizeAfterExactlyTwentyNineSelections()
    {
        EphemeralSymbol ephemeral = new EphemeralSymbol("red");

        for (int i = 0; i < 28; i++) {
            ephemeral.selected();
        }
        assertEquals(2, ephemeral.getSize());

        ephemeral.selected();
        assertEquals(1, ephemeral.getSize());

        ephemeral.selected();
        assertEquals(1, ephemeral.getSize());
    }

    /**
     * Verifies that a shy symbol starts hidden.
     */
    @Test
    public void shouldStartShySymbolHidden()
    {
        ShySymbol shy = new ShySymbol("green");

        assertFalse(shy.isVisible());
    }

    /**
     * Verifies that a machine with shy symbols keeps working while
     * invisible: selecting a shy symbol must not draw anything.
     */
    @Test
    public void shouldUseShySymbolsInsideAnInvisibleMachine()
    {
        SlotMachine machine = new SlotMachine();
        machine.addSymbol("shy", 1, "green");
        machine.addSymbol("red");
        machine.addWheel(1);
        machine.placeSymbol(1, "green");
        machine.placeSymbol(1, "red");

        for (int i = 0; i < 4; i++) {
            machine.spin(1, 2);
            assertTrue(machine.ok());
            assertEquals("green", machine.configuration()[0]);
        }
    }

    /**
     * Verifies that a yellow rainbow symbol grows five by five
     * until the maximum size and then stays there.
     */
    @Test
    public void shouldGrowYellowRainbowSymbolUntilMaximum()
    {
        RainbowSymbol symbol = new RainbowSymbol("yellow");
        int[] expected = {35, 40, 45, 50, 55, 60, 60, 60};

        for (int size : expected) {
            symbol.selected();
            assertEquals(size, symbol.getSize());
        }
    }

    /**
     * Verifies that a red rainbow symbol shrinks five by five
     * until the minimum size and then stays there.
     */
    @Test
    public void shouldShrinkRedRainbowSymbolUntilMinimum()
    {
        RainbowSymbol symbol = new RainbowSymbol("red");
        int[] expected = {25, 20, 15, 10, 5, 5, 5};

        for (int size : expected) {
            symbol.selected();
            assertEquals(size, symbol.getSize());
        }
    }

    /**
     * Verifies that blue and any other color keep the normal size.
     */
    @Test
    public void shouldKeepBlueAndOtherRainbowColorsAtNormalSize()
    {
        RainbowSymbol blue = new RainbowSymbol("blue");
        RainbowSymbol green = new RainbowSymbol("green");

        for (int i = 0; i < 5; i++) {
            blue.selected();
            green.selected();
        }

        assertEquals(30, blue.getSize());
        assertEquals(30, green.getSize());
    }

    /**
     * Verifies that two rainbow symbols change their sizes
     * independently.
     */
    @Test
    public void shouldMaintainIndependentRainbowSymbolSizes()
    {
        RainbowSymbol yellow = new RainbowSymbol("yellow");
        RainbowSymbol red = new RainbowSymbol("red");

        yellow.selected();
        yellow.selected();
        red.selected();

        assertEquals(40, yellow.getSize());
        assertEquals(25, red.getSize());
    }
}