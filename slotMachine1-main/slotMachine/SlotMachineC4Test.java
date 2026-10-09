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
     * Verifies that a normal wheel keeps its normal behavior
     * when it is combined with Lefty and Rebel wheels.
     */
    @Test
    public void shouldKeepNormalWheelBehaviorAmongDifferentTypes()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel(1, "normal");
        machine.addWheel(2, "lefty");
        machine.addWheel(3, "rebel");

        machine.lock(1);

        assertTrue(
            "The normal wheel should be locked.",
            machine.ok()
        );

        machine.unlock(1);

        assertTrue(
            "The normal wheel should be unlocked.",
            machine.ok()
        );
    }

    /**
     * Verifies that a LeftyWheel copies the state of
     * the wheel immediately to its left.
     */
    @Test
    public void shouldCopyTheStateOfTheImmediateLeftWheel()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel(1, "normal");
        machine.addWheel(2, "lefty");

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

        machine.addWheel(1, "normal");
        machine.addWheel(2, "normal");
        machine.addWheel(3, "lefty");

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
     * Verifies that the LeftyWheel follows the current
     * state of its left neighbor instead of keeping
     * an old copied state.
     */
    @Test
    public void shouldUseTheCurrentStateOfItsLeftNeighbor()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel(1, "normal");
        machine.addWheel(2, "lefty");

        machine.addSymbol("red");
        machine.addSymbol("blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");

        machine.spin(1, 0);
        machine.spin(2);

        String[] first =
            machine.configuration();

        assertEquals(
            first[0],
            first[1]
        );

        machine.spin(1, 1);
        machine.spin(2);

        String[] second =
            machine.configuration();

        assertEquals(
            "The LeftyWheel must update its state "
            + "according to its current neighbor.",
            second[0],
            second[1]
        );

        assertNotEquals(
            "The normal wheel should have changed.",
            first[0],
            second[0]
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

        machine.addWheel(1, "normal");
        machine.addWheel(2, "rebel");

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

        machine.addWheel(1, "normal");
        machine.addWheel(2, "rebel");
        machine.addWheel(3, "normal");

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

        machine.addWheel(1, "normal");
        machine.addWheel(2, "rebel");

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

        machine.addWheel(1, "normal");
        machine.addWheel(2, "normal");
        machine.addWheel(3, "rebel");

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

        machine.addWheel(1, "normal");
        machine.addWheel(2, "normal");
        machine.addWheel(3, "lefty");

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
     * Verifies a combined scenario involving the three
     * wheel types.
     *
     * Normal performs its normal operations.
     * Lefty follows its neighbor.
     * Rebel rejects forbidden operations.
     */
    @Test
    public void shouldHandleAllThreeWheelTypesTogether()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel(1, "normal");
        machine.addWheel(2, "lefty");
        machine.addWheel(3, "rebel");

        machine.addSymbol("red");
        machine.addSymbol("blue");

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");

        machine.placeSymbol(3, "red");
        machine.placeSymbol(3, "blue");

        machine.spin(1, 0);
        machine.spin(2);

        String[] configuration =
            machine.configuration();

        assertEquals(
            "The LeftyWheel must copy the normal wheel.",
            configuration[0],
            configuration[1]
        );

        machine.lock(3);

        assertFalse(
            "The RebelWheel cannot be locked.",
            machine.ok()
        );

        machine.delWheel(3);

        assertFalse(
            "The RebelWheel cannot be deleted.",
            machine.ok()
        );

        machine.swap(1, 3);

        assertFalse(
            "The RebelWheel cannot participate in a swap.",
            machine.ok()
        );

        machine.spin(1, 1);

        assertTrue(
            "The normal wheel must continue working.",
            machine.ok()
        );

        machine.spin(2);

        configuration =
            machine.configuration();

        assertEquals(
            "The LeftyWheel must continue following "
            + "its left neighbor.",
            configuration[0],
            configuration[1]
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
     * Verifies that a machine can contain normal,
     * ephemeral and shy symbols at the same time.
     *
     * This test verifies the coexistence of the three
     * symbol types without using the graphical interface.
     */
    @Test
    public void shouldHandleDifferentSymbolTypesInTheSameMachine()
    {
        SlotMachine machine =
            new SlotMachine();

        machine.addWheel(1, "normal");

        machine.addSymbol(
            "red",
            "normal"
        );

        machine.addSymbol(
            "blue",
            "ephemeral"
        );

        machine.addSymbol(
            "green",
            "shy"
        );

        machine.placeSymbol(
            1,
            "red"
        );

        machine.placeSymbol(
            1,
            "blue"
        );

        machine.placeSymbol(
            1,
            "green"
        );

        assertTrue(
            "The machine should accept the three symbol types.",
            machine.ok()
        );

        String[] symbols =
            machine.symbols();

        assertEquals(
            3,
            symbols.length
        );

        assertEquals(
            "red",
            symbols[0]
        );

        assertEquals(
            "blue",
            symbols[1]
        );

        assertEquals(
            "green",
            symbols[2]
        );
    }
    
    /**
     * Tests that a yellow RainbowSymbol increases its size.
     */
    @Test
    public void shouldIncreaseYellowRainbowSymbolSize()
    {
        RainbowSymbol symbol =
            new RainbowSymbol("yellow");
    
        int initialSize = symbol.getSize();
    
        symbol.selected();
    
        assertEquals(
            initialSize + 5,
            symbol.getSize()
        );
    }
    
    /**
     * Tests that a yellow RainbowSymbol cannot exceed
     * the maximum size.
     */
    @Test
    public void shouldLimitYellowRainbowSymbolSize()
    {
        RainbowSymbol symbol =
            new RainbowSymbol("yellow");
    
        for (int i = 0; i < 10; i++) {
            symbol.selected();
        }
    
        assertEquals(
            60,
            symbol.getSize()
        );
    }
    
    /**
     * Tests that a red RainbowSymbol decreases its size.
     */
    @Test
    public void shouldDecreaseRedRainbowSymbolSize()
    {
        RainbowSymbol symbol =
            new RainbowSymbol("red");
    
        int initialSize = symbol.getSize();
    
        symbol.selected();
    
        assertEquals(
            initialSize - 5,
            symbol.getSize()
        );
    }
    
    /**
     * Tests that a red RainbowSymbol cannot go below
     * the minimum size.
     */
    @Test
    public void shouldLimitRedRainbowSymbolSize()
    {
        RainbowSymbol symbol =
            new RainbowSymbol("red");
    
        for (int i = 0; i < 10; i++) {
            symbol.selected();
        }
    
        assertEquals(
            5,
            symbol.getSize()
        );
    }
    
    /**
     * Tests that a blue RainbowSymbol keeps its normal size.
     */
    @Test
    public void shouldKeepBlueRainbowSymbolNormalSize()
    {
        RainbowSymbol symbol =
            new RainbowSymbol("blue");
    
        symbol.selected();
        symbol.selected();
        symbol.selected();
    
        assertEquals(
            30,
            symbol.getSize()
        );
    }
    
    /**
     * Tests that other colors keep the normal size.
     */
    @Test
    public void shouldKeepOtherRainbowColorsNormalSize()
    {
        RainbowSymbol symbol =
            new RainbowSymbol("green");
    
        symbol.selected();
        symbol.selected();
        symbol.selected();
    
        assertEquals(
            30,
            symbol.getSize()
        );
    }
    
    /**
     * Tests that different RainbowSymbols maintain
     * independent sizes.
     */
    @Test
    public void shouldMaintainIndependentRainbowSymbolSizes()
    {
        RainbowSymbol yellow =
            new RainbowSymbol("yellow");
    
        RainbowSymbol red =
            new RainbowSymbol("red");
    
        yellow.selected();
        yellow.selected();
    
        red.selected();
    
        assertEquals(
            40,
            yellow.getSize()
        );
    
        assertEquals(
            25,
            red.getSize()
        );
    }
}