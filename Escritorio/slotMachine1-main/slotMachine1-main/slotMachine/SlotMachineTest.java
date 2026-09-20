import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the SlotMachine class.
 *
 * These tests are invisible. They verify the main
 * behaviors of the slot machine without using the canvas.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachineTest
{
    private SlotMachine machine;

    /**
     * Creates a basic empty machine and adds
     * three symbols for the tests that need them.
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
     * Verifies that the default constructor creates
     * a valid empty machine.
     */
    @Test
    public void shouldCreateValidEmptyMachine()
    {
        SlotMachine nueva = new SlotMachine();
    
        assertTrue(nueva.ok());
        assertEquals(0, nueva.configuration().length);
    }
    
    /**
     * Verifies that a machine with the minimum
     * number of wheels is valid.
     */
    @Test
    public void shouldCreateValidMachineWithMinimumWheels()
    {
        SlotMachine nueva = new SlotMachine(3);
    
        assertTrue(nueva.ok());
        assertEquals(3, nueva.configuration().length);
    }
    
    /**
     * Verifies that a machine with ten wheels
     * is created correctly.
     */
    @Test
    public void shouldCreateValidMachineWithTenWheels()
    {
        SlotMachine nueva = new SlotMachine(10);
    
        assertTrue(nueva.ok());
        assertEquals(10, nueva.configuration().length);
    }
    
    /**
     * Verifies that a machine with the maximum
     * allowed number of wheels is valid.
     */
    @Test
    public void shouldCreateValidMachineWithMaximumWheels()
    {
        SlotMachine nueva = new SlotMachine(50);
    
        assertTrue(nueva.ok());
        assertEquals(50, nueva.configuration().length);
    }

    /**
     * Verifies that a machine with more than
     * fifty wheels is rejected.
     */
    @Test
    public void shouldRejectMachineWithMoreThanFiftyWheels()
    {
        SlotMachine nueva = new SlotMachine(51);
    
        assertFalse(nueva.ok());
        assertEquals(0, nueva.configuration().length);
    }
    
    /**
     * Verifies that a wheel can be inserted
     * at the first position.
     */
    @Test
    public void shouldInsertWheelAtBeginning()
    {
        machine.addWheel(1);
    
        assertEquals(1, machine.configuration().length);
        assertEquals("", machine.configuration()[0]);
    }
    
    /**
     * Verifies that a wheel can be inserted
     * at the end of the current wheel list.
     */
    @Test
    public void shouldInsertWheelAtEnd()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
    
        assertEquals(3, machine.configuration().length);
    }
    
    /**
     * Verifies that a position greater than the
     * current number of wheels is corrected.
     */
    @Test
    public void shouldCorrectPositionGreaterThanCurrentSize()
    {
        machine.addWheel(100);
        machine.addWheel(100);
    
        assertEquals(2, machine.configuration().length);
    }
    
    /**
     * Verifies that a negative wheel position is
     * corrected to a valid position.
     */
    @Test
    public void shouldCorrectNegativeWheelPosition()
    {
        machine.addWheel(-5);
        machine.addWheel(-10);
    
        assertEquals(2, machine.configuration().length);
    }
    
    /**
     * Verifies that no wheel is added when the
     * maximum number of wheels has been reached.
     */
    @Test
    public void shouldNotAddWheelBeyondMaximum()
    {
        for (int i = 1; i <= 50; i++) {
            machine.addWheel(i);
        }
    
        machine.addWheel(1);
    
        assertEquals(50, machine.configuration().length);
    }

    /**
     * Verifies that the first wheel can be deleted.
     */
    @Test
    public void shouldDeleteFirstWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.delWheel(1);
    
        assertEquals(1, machine.configuration().length);
    }
    
    /**
     * Verifies that the last wheel can be deleted.
     */
    @Test
    public void shouldDeleteLastWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
    
        machine.delWheel(3);
    
        assertEquals(2, machine.configuration().length);
    }
    
    /**
     * Verifies that deleting wheel position zero
     * does not modify the machine.
     */
    @Test
    public void shouldIgnoreZeroWheelDeletion()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.delWheel(0);
    
        assertEquals(2, machine.configuration().length);
    }
    
    /**
     * Verifies that deleting a negative wheel position
     * does not modify the machine.
     */
    @Test
    public void shouldIgnoreNegativeWheelDeletion()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.delWheel(-2);
    
        assertEquals(2, machine.configuration().length);
    }
    
    /**
     * Verifies that deleting a wheel beyond the
     * current number of wheels is ignored.
     */
    @Test
    public void shouldIgnoreDeletionBeyondCurrentWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.delWheel(5);
    
        assertEquals(2, machine.configuration().length);
    }

    /**
     * Verifies that a new symbol is added
     * at the end of the symbol list.
     */
    @Test
    public void shouldAddSymbolToEndOfList()
    {
        machine.addSymbol("yellow");
    
        String[] symbols = machine.symbols();
    
        assertEquals("yellow", symbols[3]);
    }
    
    /**
     * Verifies that adding a symbol preserves
     * the order of existing symbols.
     */
    @Test
    public void shouldKeepOriginalSymbolOrder()
    {
        machine.addSymbol("yellow");
    
        String[] symbols = machine.symbols();
    
        assertEquals("red", symbols[0]);
        assertEquals("blue", symbols[1]);
        assertEquals("green", symbols[2]);
        assertEquals("yellow", symbols[3]);
    }
    
    /**
     * Verifies that adding a duplicated first symbol
     * does not increase the symbol list.
     */
    @Test
    public void shouldIgnoreRepeatedFirstSymbol()
    {
        machine.addSymbol("red");
    
        assertEquals(3, machine.symbols().length);
    }
    
    /**
     * Verifies that adding a duplicated last symbol
     * does not increase the symbol list.
     */
    @Test
    public void shouldIgnoreRepeatedLastSymbol()
    {
        machine.addSymbol("green");
    
        assertEquals(3, machine.symbols().length);
    }
    
    /**
     * Verifies that a null symbol is ignored.
     */
    @Test
    public void shouldIgnoreNullSymbol()
    {
        machine.addSymbol(null);
    
        assertEquals(3, machine.symbols().length);
    }

    /**
     * Verifies that a symbol can be inserted
     * at the beginning of the list.
     */
    @Test
    public void shouldInsertSymbolAtBeginning()
    {
        machine.addSymbol(1, "yellow");
    
        String[] symbols = machine.symbols();
    
        assertEquals("yellow", symbols[0]);
        assertEquals("red", symbols[1]);
    }
    
    /**
     * Verifies that a symbol can be inserted
     * in the middle of the list.
     */
    @Test
    public void shouldInsertSymbolInMiddle()
    {
        machine.addSymbol(2, "yellow");
    
        String[] symbols = machine.symbols();
    
        assertEquals("red", symbols[0]);
        assertEquals("yellow", symbols[1]);
        assertEquals("blue", symbols[2]);
    }
    
    /**
     * Verifies that a symbol can be inserted
     * at the end of the list.
     */
    @Test
    public void shouldInsertSymbolAtEnd()
    {
        machine.addSymbol(4, "yellow");
    
        String[] symbols = machine.symbols();
    
        assertEquals("yellow", symbols[3]);
    }
    
    /**
     * Verifies that a position below one is corrected
     * to the first position.
     */
    @Test
    public void shouldCorrectPositionBelowOne()
    {
        machine.addSymbol(0, "yellow");
    
        String[] symbols = machine.symbols();
    
        assertEquals("yellow", symbols[0]);
    }
    
    /**
     * Verifies that a duplicated color is not added
     * even when a position is specified.
     */
    @Test
    public void shouldIgnoreDuplicatedColorAtPosition()
    {
        machine.addSymbol(1, "blue");
    
        assertEquals(3, machine.symbols().length);
        assertEquals("red", machine.symbols()[0]);
    }

    /**
     * Verifies that the first symbol can be deleted
     * from the symbol list.
     */
    @Test
    public void shouldDeleteFirstSymbol()
    {
        machine.delSymbol("red");
    
        String[] symbols = machine.symbols();
    
        assertEquals(2, symbols.length);
        assertEquals("blue", symbols[0]);
    }
    
    /**
     * Verifies that a symbol in the middle of the
     * list can be deleted.
     */
    @Test
    public void shouldDeleteMiddleSymbol()
    {
        machine.delSymbol("blue");
    
        String[] symbols = machine.symbols();
    
        assertEquals(2, symbols.length);
        assertEquals("red", symbols[0]);
        assertEquals("green", symbols[1]);
    }
    
    /**
     * Verifies that the last symbol can be deleted
     * from the symbol list.
     */
    @Test
    public void shouldDeleteLastSymbol()
    {
        machine.delSymbol("green");
    
        String[] symbols = machine.symbols();
    
        assertEquals(2, symbols.length);
        assertEquals("blue", symbols[1]);
    }
    
    /**
     * Verifies that deleting a null symbol is ignored.
     */
    @Test
    public void shouldIgnoreNullSymbolDeletion()
    {
        machine.delSymbol(null);
    
        assertEquals(3, machine.symbols().length);
    }
    
    /**
     * Verifies that deleting a symbol also removes it
     * from the wheels where it was placed.
     */
    @Test
    public void shouldRemoveDeletedSymbolFromWheel()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
    
        machine.delSymbol("red");
    
        machine.spin(1, 0);
    
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that a symbol can be placed
     * in the first wheel.
     */
    @Test
    public void shouldPlaceSymbolInFirstWheel()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.spin(1, 0);
    
        assertEquals("red", machine.configuration()[0]);
    }
    
    /**
     * Verifies that different symbols can be placed
     * in different wheels.
     */
    @Test
    public void shouldPlaceDifferentSymbolsInDifferentWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "green");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
    
        assertEquals("red", machine.configuration()[0]);
        assertEquals("green", machine.configuration()[1]);
    }
    
    /**
     * Verifies that a symbol is not placed when
     * the wheel position is invalid.
     */
    @Test
    public void shouldIgnoreSymbolInInvalidWheel()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(2, "red");
    
        assertEquals("", machine.configuration()[0]);
    }
    
    /**
     * Verifies that a negative wheel position is ignored.
     */
    @Test
    public void shouldIgnoreNegativeWheelPosition()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(-1, "red");
    
        assertEquals("", machine.configuration()[0]);
    }
    
    /**
     * Verifies that a null symbol cannot be placed
     * in a wheel.
     */
    @Test
    public void shouldIgnoreNullSymbolWhenPlacing()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, null);
        machine.spin(1, 0);
    
        assertEquals("", machine.configuration()[0]);
    }

    /**
     * Verifies that a wheel can move forward
     * by two positions.
     */
    @Test
    public void shouldSpinForwardTwoSteps()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
    
        machine.spin(1, 0);
        machine.spin(1, 2);
    
        assertEquals("green", machine.configuration()[0]);
    }
    
    /**
     * Verifies that a wheel can move backward
     * by one position.
     */
    @Test
    public void shouldSpinBackwardOneStep()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
    
        machine.spin(1, 0);
        machine.spin(1, -1);
    
        assertEquals("green", machine.configuration()[0]);
    }
    
    /**
     * Verifies that a wheel can move backward
     * by two positions.
     */
    @Test
    public void shouldSpinBackwardTwoSteps()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
    
        machine.spin(1, 0);
        machine.spin(1, -2);
    
        assertEquals("blue", machine.configuration()[0]);
    }
    
    /**
     * Verifies that a complete rotation returns
     * the wheel to its original position.
     */
    @Test
    public void shouldReturnToInitialPositionAfterFullRotation()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
    
        machine.spin(1, 0);
        machine.spin(1, 3);
    
        assertEquals("red", machine.configuration()[0]);
    }
    
    /**
     * Verifies that a locked wheel does not change
     * when a spin operation is requested.
     */
    @Test
    public void shouldIgnoreSpinOnLockedWheel()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
    
        machine.spin(1, 0);
        machine.lock(1);
        machine.spin(1, 5);
    
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that the first and last wheels
     * exchange their positions.
     */
    @Test
    public void shouldSwapFirstAndLastWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
        machine.spin(3, 0);
    
        machine.swap(1, 3);
    
        assertEquals("green", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
        assertEquals("red", machine.configuration()[2]);
    }
    
    /**
     * Verifies that two adjacent wheels can
     * exchange their positions.
     */
    @Test
    public void shouldSwapAdjacentWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
    
        machine.swap(1, 2);
    
        assertEquals("blue", machine.configuration()[0]);
        assertEquals("red", machine.configuration()[1]);
    }
    
    /**
     * Verifies that a swap with an invalid first
     * position is ignored.
     */
    @Test
    public void shouldIgnoreSwapWithFirstInvalidPosition()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.placeSymbol(1, "red");
        machine.spin(1, 0);
    
        String[] before = machine.configuration();
    
        machine.swap(0, 2);
    
        assertArrayEquals(before, machine.configuration());
    }
    
    /**
     * Verifies that a swap with an invalid second
     * position is ignored.
     */
    @Test
    public void shouldIgnoreSwapWithSecondInvalidPosition()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.placeSymbol(1, "red");
        machine.spin(1, 0);
    
        String[] before = machine.configuration();
    
        machine.swap(1, 3);
    
        assertArrayEquals(before, machine.configuration());
    }
    
    /**
     * Verifies that swapping a wheel with itself
     * does not modify the configuration.
     */
    @Test
    public void shouldIgnoreSwapUsingSameWheel()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.spin(1, 0);
    
        String[] before = machine.configuration();
    
        machine.swap(1, 1);
    
        assertArrayEquals(before, machine.configuration());
    }

    /**
     * Verifies that locking a wheel prevents
     * it from changing its visible symbol.
     */
    @Test
    public void shouldLockFirstWheel()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
    
        machine.spin(1, 0);
        machine.lock(1);
        machine.spin(1, 1);
    
        assertEquals("red", machine.configuration()[0]);
    }
    
    /**
     * Verifies that unlocking a wheel allows it
     * to spin again.
     */
    @Test
    public void shouldUnlockFirstWheel()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
    
        machine.spin(1, 0);
        machine.lock(1);
        machine.unlock(1);
        machine.spin(1, 1);
    
        assertEquals("blue", machine.configuration()[0]);
    }
    
    /**
     * Verifies that locking an invalid negative
     * wheel position has no effect.
     */
    @Test
    public void shouldIgnoreLockOnNegativePosition()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
    
        machine.spin(1, 0);
        machine.lock(-1);
        machine.spin(1, 1);
    
        assertEquals("blue", machine.configuration()[0]);
    }
    
    /**
     * Verifies that unlocking an invalid wheel
     * position has no effect on a locked wheel.
     */
    @Test
    public void shouldIgnoreUnlockOnPositionBeyondWheels()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
    
        machine.spin(1, 0);
        machine.lock(1);
        machine.unlock(5);
        machine.spin(1, 1);
    
        assertEquals("red", machine.configuration()[0]);
    }
    
    /**
     * Verifies that locking one wheel does not prevent
     * another unlocked wheel from changing.
     */
    @Test
    public void shouldAllowOnlyUnlockedWheelToChange()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
    
        machine.lock(1);
        machine.unlock(2);
    
        machine.spin(1, 1);
        machine.spin(2, 1);
    
        assertEquals("red", machine.configuration()[0]);
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Verifies that a machine without wheels has
     * zero distinct visible symbols.
     */
    @Test
    public void shouldReturnZeroWhenThereAreNoWheels()
    {
        assertEquals(0, machine.distinctSymbols());
    }
    
    /**
     * Verifies that two wheels showing the same
     * symbol count as one distinct symbol.
     */
    @Test
    public void shouldReturnOneWhenAllWheelsShowSameSymbol()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
    
        assertEquals(1, machine.distinctSymbols());
    }
    
    /**
     * Verifies that two different visible symbols
     * are counted as two distinct symbols.
     */
    @Test
    public void shouldReturnTwoForTwoDifferentVisibleSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
    
        assertEquals(2, machine.distinctSymbols());
    }
    
    /**
     * Verifies that wheels without visible symbols
     * are not counted.
     */
    @Test
    public void shouldIgnoreWheelsWithoutVisibleSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.placeSymbol(1, "red");
    
        machine.spin(1, 0);
    
        assertEquals(1, machine.distinctSymbols());
    }
    
    /**
     * Verifies that three different visible symbols
     * are counted correctly.
     */
    @Test
    public void shouldReturnThreeDifferentVisibleSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
        machine.spin(3, 0);
    
        assertEquals(3, machine.distinctSymbols());
    }
    
    /**
     * Verifies that an empty machine does not
     * have a jackpot.
     */
    @Test
    public void shouldReturnFalseForEmptyMachine()
    {
        SlotMachine nueva = new SlotMachine();
    
        assertFalse(nueva.isJackpot());
    }
    
    /**
     * Verifies that a wheel without a visible symbol
     * prevents a jackpot.
     */
    @Test
    public void shouldReturnFalseWhenWheelHasNoVisibleSymbol()
    {
        machine.addWheel(1);
    
        machine.placeSymbol(1, "red");
    
        assertFalse(machine.isJackpot());
    }
    
    /**
     * Verifies that two wheels showing the same
     * symbol produce a jackpot.
     */
    @Test
    public void shouldReturnTrueWithTwoEqualVisibleSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
    
        assertTrue(machine.isJackpot());
    }
    
    /**
     * Verifies that different visible symbols prevent
     * a jackpot.
     */
    @Test
    public void shouldReturnFalseWhenOnlyOneWheelDiffers()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "blue");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
        machine.spin(3, 0);
    
        assertFalse(machine.isJackpot());
    }
    
    /**
     * Verifies that several wheels showing the same
     * symbol produce a jackpot.
     */
    @Test
    public void shouldReturnTrueAfterChangingAllWheelsToSameSymbol()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
    
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "red");
    
        machine.spin(1, 0);
        machine.spin(2, 0);
        machine.spin(3, 0);
    
        assertTrue(machine.isJackpot());
    }
}   

    
