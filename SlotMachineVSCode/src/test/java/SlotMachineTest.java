import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the SlotMachine class (cycle 1 and SlotMachine(n) of cycle 3).
 *
 * All the tests are invisible. Each group answers two questions:
 * what should the method do? and what should it NOT do?
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 2
 */
public class SlotMachineTest
{
    private SlotMachine machine;

    /**
     * Creates an empty machine with three registered symbols.
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
     * Adds a wheel at the end with the given symbols and
     * leaves the first of them visible.
     * @param colors symbols of the new wheel (already registered)
     */
    private void addWheelWith(String... colors)
    {
        int position = machine.configuration().length + 1;
        machine.addWheel(position);
        for (String color : colors) {
            machine.placeSymbol(position, color);
        }
        machine.spin(position, 0);
    }

    /**
     * Checks that a machine created with SlotMachine(n) is valid:
     * n wheels, n different symbols and every wheel showing one of them.
     * @param n number of wheels and symbols
     */
    private void assertValidRandomMachine(int n)
    {
        SlotMachine random = new SlotMachine(n);
        assertTrue(random.ok());

        String[] symbols = random.symbols();
        String[] configuration = random.configuration();
        assertEquals(n, configuration.length);
        assertEquals(n, symbols.length);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                assertNotEquals(symbols[i], symbols[j]);
            }
        }
        for (String visible : configuration) {
            assertTrue(java.util.Arrays.asList(symbols).contains(visible));
        }
    }

    // Constructors

    /**
     * Verifies that the machine should create valid empty machine.
     */
    @Test
    public void shouldCreateValidEmptyMachine()
    {
        SlotMachine empty = new SlotMachine();

        assertTrue(empty.ok());
        assertEquals(0, empty.configuration().length);
        assertEquals(0, empty.symbols().length);
        assertEquals(0, empty.distinctSymbols());
        assertFalse(empty.isJackpot());
    }

    /**
     * Verifies that the machine should create valid machine with minimum wheels.
     */
    @Test
    public void shouldCreateValidMachineWithMinimumWheels()
    {
        assertValidRandomMachine(3);
    }

    /**
     * Verifies that the machine should create valid machine with ten wheels.
     */
    @Test
    public void shouldCreateValidMachineWithTenWheels()
    {
        assertValidRandomMachine(10);
    }

    /**
     * Verifies that the machine should create valid machine with maximum wheels.
     */
    @Test
    public void shouldCreateValidMachineWithMaximumWheels()
    {
        assertValidRandomMachine(50);
    }

    /**
     * Verifies that the machine should reject machine with less than three wheels.
     */
    @Test
    public void shouldRejectMachineWithLessThanThreeWheels()
    {
        SlotMachine small = new SlotMachine(2);

        assertFalse(small.ok());
        assertEquals(0, small.configuration().length);
        assertEquals(0, small.symbols().length);
    }

    /**
     * Verifies that the machine should reject machine with zero or negative wheels.
     */
    @Test
    public void shouldRejectMachineWithZeroOrNegativeWheels()
    {
        assertFalse(new SlotMachine(0).ok());
        assertFalse(new SlotMachine(-4).ok());
        assertEquals(0, new SlotMachine(-4).configuration().length);
    }

    /**
     * Verifies that the machine should reject machine with more than fifty wheels.
     */
    @Test
    public void shouldRejectMachineWithMoreThanFiftyWheels()
    {
        SlotMachine big = new SlotMachine(51);

        assertFalse(big.ok());
        assertEquals(0, big.configuration().length);
    }

    // addWheel

    /**
     * Verifies that the machine should insert wheel at beginning.
     */
    @Test
    public void shouldInsertWheelAtBeginning()
    {
        addWheelWith("red");

        machine.addWheel(1);

        assertTrue(machine.ok());
        assertArrayEquals(new String[] {"", "red"}, machine.configuration());
    }

    /**
     * Verifies that the machine should insert wheel in middle.
     */
    @Test
    public void shouldInsertWheelInMiddle()
    {
        addWheelWith("red");
        addWheelWith("blue");

        machine.addWheel(2);

        assertArrayEquals(new String[] {"red", "", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should insert wheel at end.
     */
    @Test
    public void shouldInsertWheelAtEnd()
    {
        addWheelWith("red");

        machine.addWheel(2);

        assertArrayEquals(new String[] {"red", ""}, machine.configuration());
    }

    /**
     * Verifies that the machine should correct position greater than current size.
     */
    @Test
    public void shouldCorrectPositionGreaterThanCurrentSize()
    {
        addWheelWith("red");

        machine.addWheel(100);

        assertTrue(machine.ok());
        assertArrayEquals(new String[] {"red", ""}, machine.configuration());
    }

    /**
     * Verifies that the machine should correct negative wheel position.
     */
    @Test
    public void shouldCorrectNegativeWheelPosition()
    {
        addWheelWith("red");

        machine.addWheel(-5);

        assertTrue(machine.ok());
        assertArrayEquals(new String[] {"", "red"}, machine.configuration());
    }

    /**
     * Verifies that the machine should not add wheel beyond maximum.
     */
    @Test
    public void shouldNotAddWheelBeyondMaximum()
    {
        for (int i = 1; i <= 50; i++) {
            machine.addWheel(i);
        }

        machine.addWheel(1);

        assertFalse(machine.ok());
        assertEquals(50, machine.configuration().length);
    }

    /**
     * Verifies that the machine should lose jackpot when adding empty wheel.
     */
    @Test
    public void shouldLoseJackpotWhenAddingEmptyWheel()
    {
        addWheelWith("red");
        addWheelWith("red");
        assertTrue(machine.isJackpot());

        machine.addWheel(3);

        assertFalse(machine.isJackpot());
    }

    // delWheel-

    /**
     * Verifies that the machine should delete first wheel.
     */
    @Test
    public void shouldDeleteFirstWheel()
    {
        addWheelWith("red");
        addWheelWith("blue");
        addWheelWith("green");

        machine.delWheel(1);

        assertTrue(machine.ok());
        assertArrayEquals(new String[] {"blue", "green"}, machine.configuration());
    }

    /**
     * Verifies that the machine should delete middle wheel.
     */
    @Test
    public void shouldDeleteMiddleWheel()
    {
        addWheelWith("red");
        addWheelWith("blue");
        addWheelWith("green");

        machine.delWheel(2);

        assertArrayEquals(new String[] {"red", "green"}, machine.configuration());
    }

    /**
     * Verifies that the machine should delete last wheel.
     */
    @Test
    public void shouldDeleteLastWheel()
    {
        addWheelWith("red");
        addWheelWith("blue");
        addWheelWith("green");

        machine.delWheel(3);

        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should ignore zero wheel deletion.
     */
    @Test
    public void shouldIgnoreZeroWheelDeletion()
    {
        addWheelWith("red");
        addWheelWith("blue");

        machine.delWheel(0);

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should ignore negative wheel deletion.
     */
    @Test
    public void shouldIgnoreNegativeWheelDeletion()
    {
        addWheelWith("red");
        addWheelWith("blue");

        machine.delWheel(-2);

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should ignore deletion beyond current wheels.
     */
    @Test
    public void shouldIgnoreDeletionBeyondCurrentWheels()
    {
        addWheelWith("red");
        addWheelWith("blue");

        machine.delWheel(3);

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should ignore deletion on empty machine.
     */
    @Test
    public void shouldIgnoreDeletionOnEmptyMachine()
    {
        machine.delWheel(1);

        assertFalse(machine.ok());
        assertEquals(0, machine.configuration().length);
    }

    /**
     * Verifies that the machine should reach jackpot when deleting the different wheel.
     */
    @Test
    public void shouldReachJackpotWhenDeletingTheDifferentWheel()
    {
        addWheelWith("red");
        addWheelWith("red");
        addWheelWith("blue");
        assertFalse(machine.isJackpot());

        machine.delWheel(3);

        assertTrue(machine.isJackpot());
    }

    /**
     * Verifies that the machine should not be jackpot after deleting all wheels.
     */
    @Test
    public void shouldNotBeJackpotAfterDeletingAllWheels()
    {
        addWheelWith("red");
        addWheelWith("red");

        machine.delWheel(1);
        machine.delWheel(1);

        assertEquals(0, machine.configuration().length);
        assertFalse(machine.isJackpot());
    }

    // addSymbol(color)

    /**
     * Verifies that the machine should add symbol to end of list.
     */
    @Test
    public void shouldAddSymbolToEndOfList()
    {
        machine.addSymbol("yellow");

        assertTrue(machine.ok());
        assertArrayEquals(new String[] {"red", "blue", "green", "yellow"}, machine.symbols());
    }

    /**
     * Verifies that the machine should ignore repeated symbol.
     */
    @Test
    public void shouldIgnoreRepeatedSymbol()
    {
        machine.addSymbol("red");

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue", "green"}, machine.symbols());
    }

    /**
     * Verifies that the machine should ignore null symbol.
     */
    @Test
    public void shouldIgnoreNullSymbol()
    {
        machine.addSymbol(null);

        assertFalse(machine.ok());
        assertEquals(3, machine.symbols().length);
    }

    // addSymbol(pos, color)

    /**
     * Verifies that the machine should insert symbol at beginning.
     */
    @Test
    public void shouldInsertSymbolAtBeginning()
    {
        machine.addSymbol(1, "yellow");

        assertTrue(machine.ok());
        assertArrayEquals(new String[] {"yellow", "red", "blue", "green"}, machine.symbols());
    }

    /**
     * Verifies that the machine should insert symbol in middle.
     */
    @Test
    public void shouldInsertSymbolInMiddle()
    {
        machine.addSymbol(2, "yellow");

        assertArrayEquals(new String[] {"red", "yellow", "blue", "green"}, machine.symbols());
    }

    /**
     * Verifies that the machine should insert symbol at end.
     */
    @Test
    public void shouldInsertSymbolAtEnd()
    {
        machine.addSymbol(4, "yellow");

        assertArrayEquals(new String[] {"red", "blue", "green", "yellow"}, machine.symbols());
    }

    /**
     * Verifies that the machine should correct symbol position below one.
     */
    @Test
    public void shouldCorrectSymbolPositionBelowOne()
    {
        machine.addSymbol(0, "yellow");

        assertTrue(machine.ok());
        assertEquals("yellow", machine.symbols()[0]);
    }

    /**
     * Verifies that the machine should correct symbol position greater than size.
     */
    @Test
    public void shouldCorrectSymbolPositionGreaterThanSize()
    {
        machine.addSymbol(99, "yellow");

        assertTrue(machine.ok());
        assertEquals("yellow", machine.symbols()[3]);
    }

    /**
     * Verifies that the machine should ignore duplicated color at position.
     */
    @Test
    public void shouldIgnoreDuplicatedColorAtPosition()
    {
        machine.addSymbol(1, "blue");

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue", "green"}, machine.symbols());
    }

    /**
     * Verifies that the machine should ignore null color at position.
     */
    @Test
    public void shouldIgnoreNullColorAtPosition()
    {
        machine.addSymbol(1, null);

        assertFalse(machine.ok());
        assertEquals(3, machine.symbols().length);
    }

    // delSymbol

    /**
     * Verifies that the machine should delete first symbol.
     */
    @Test
    public void shouldDeleteFirstSymbol()
    {
        machine.delSymbol("red");

        assertTrue(machine.ok());
        assertArrayEquals(new String[] {"blue", "green"}, machine.symbols());
    }

    /**
     * Verifies that the machine should delete middle symbol.
     */
    @Test
    public void shouldDeleteMiddleSymbol()
    {
        machine.delSymbol("blue");

        assertArrayEquals(new String[] {"red", "green"}, machine.symbols());
    }

    /**
     * Verifies that the machine should delete last symbol.
     */
    @Test
    public void shouldDeleteLastSymbol()
    {
        machine.delSymbol("green");

        assertArrayEquals(new String[] {"red", "blue"}, machine.symbols());
    }

    /**
     * Verifies that the machine should ignore null symbol deletion.
     */
    @Test
    public void shouldIgnoreNullSymbolDeletion()
    {
        machine.delSymbol(null);

        assertFalse(machine.ok());
        assertEquals(3, machine.symbols().length);
    }

    /**
     * Verifies that the machine should ignore deletion of unknown symbol.
     */
    @Test
    public void shouldIgnoreDeletionOfUnknownSymbol()
    {
        machine.delSymbol("yellow");

        assertFalse(machine.ok());
        assertArrayEquals(new String[] {"red", "blue", "green"}, machine.symbols());
    }

    /**
     * Verifies that the machine should remove deleted symbol from wheel.
     */
    @Test
    public void shouldRemoveDeletedSymbolFromWheel()
    {
        addWheelWith("red", "blue");

        machine.delSymbol("red");
        machine.spin(1, 0);

        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should lose jackpot when deleting visible symbol.
     */
    @Test
    public void shouldLoseJackpotWhenDeletingVisibleSymbol()
    {
        addWheelWith("red", "blue");
        addWheelWith("red", "blue");
        assertTrue(machine.isJackpot());

        machine.delSymbol("red");

        assertFalse(machine.isJackpot());
        assertArrayEquals(new String[] {"", ""}, machine.configuration());
    }

    // placeSymbol

    /**
     * Verifies that the machine should place symbol in first wheel.
     */
    @Test
    public void shouldPlaceSymbolInFirstWheel()
    {
        machine.addWheel(1);

        machine.placeSymbol(1, "red");

        assertTrue(machine.ok());
        machine.spin(1, 0);
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should place symbols only in the selected wheel.
     */
    @Test
    public void shouldPlaceSymbolsOnlyInTheSelectedWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);

        machine.placeSymbol(1, "red");
        machine.spin(1, 0);
        machine.spin(2, 0);

        assertArrayEquals(new String[] {"red", ""}, machine.configuration());
    }

    /**
     * Verifies that the machine should ignore symbol in invalid wheel.
     */
    @Test
    public void shouldIgnoreSymbolInInvalidWheel()
    {
        machine.addWheel(1);

        machine.placeSymbol(2, "red");

        assertFalse(machine.ok());
        machine.spin(1, 0);
        assertEquals("", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should ignore negative wheel position.
     */
    @Test
    public void shouldIgnoreNegativeWheelPosition()
    {
        machine.addWheel(1);

        machine.placeSymbol(-1, "red");

        assertFalse(machine.ok());
    }

    /**
     * Verifies that the machine should ignore null symbol when placing.
     */
    @Test
    public void shouldIgnoreNullSymbolWhenPlacing()
    {
        machine.addWheel(1);

        machine.placeSymbol(1, null);

        assertFalse(machine.ok());
        machine.spin(1, 0);
        assertEquals("", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should ignore symbol not registered in machine.
     */
    @Test
    public void shouldIgnoreSymbolNotRegisteredInMachine()
    {
        machine.addWheel(1);

        machine.placeSymbol(1, "yellow");

        assertFalse(machine.ok());
        machine.spin(1, 0);
        assertEquals("", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should not repeat a symbol in the same wheel.
     */
    @Test
    public void shouldIgnoreRepeatedSymbolInSameWheel()
    {
        addWheelWith("red", "blue");

        machine.placeSymbol(1, "red");

        assertFalse(machine.ok());
        machine.spin(1, -1);
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should allow the same symbol in different wheels.
     */
    @Test
    public void shouldAllowSameSymbolInDifferentWheels()
    {
        addWheelWith("red");
        machine.addWheel(2);

        machine.placeSymbol(2, "red");

        assertTrue(machine.ok());
        machine.spin(2, 0);
        assertArrayEquals(new String[] {"red", "red"}, machine.configuration());
    }

    /**
     * Verifies that the machine should leave no trace of a deleted symbol in the wheels.
     */
    @Test
    public void shouldLeaveNoTraceOfDeletedSymbolAfterTryingToRepeatIt()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");

        machine.delSymbol("red");

        machine.spin(1, 0);
        assertEquals("blue", machine.configuration()[0]);
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    // spin(wheel) and spin()

    /**
     * Verifies that the machine should show one of its symbols after spinning a wheel.
     */
    @Test
    public void shouldShowOneOfItsSymbolsAfterSpinningAWheel()
    {
        addWheelWith("red", "blue");

        for (int i = 0; i < 20; i++) {
            machine.spin(1);
            assertTrue(machine.ok());
            String visible = machine.configuration()[0];
            assertTrue(visible.equals("red") || visible.equals("blue"));
        }
    }

    /**
     * Verifies that the machine should show the only symbol of a wheel after spinning.
     */
    @Test
    public void shouldShowTheOnlySymbolOfAWheelAfterSpinning()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "green");

        machine.spin(1);

        assertEquals("green", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should ignore spin of invalid wheel.
     */
    @Test
    public void shouldIgnoreSpinOfInvalidWheel()
    {
        addWheelWith("red");

        machine.spin(2);

        assertFalse(machine.ok());
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that the machine should spin all wheels showing their own symbols.
     */
    @Test
    public void shouldSpinAllWheelsShowingTheirOwnSymbols()
    {
        addWheelWith("red", "blue");
        addWheelWith("green");

        for (int i = 0; i < 20; i++) {
            machine.spin();
            assertTrue(machine.ok());
            String first = machine.configuration()[0];
            assertTrue(first.equals("red") || first.equals("blue"));
            assertEquals("green", machine.configuration()[1]);
        }
    }

    /**
     * Verifies that the machine should ignore spin all on empty machine.
     */
    @Test
    public void shouldIgnoreSpinAllOnEmptyMachine()
    {
        machine.spin();

        assertFalse(machine.ok());
    }

    // symbols() and configuration()

    /**
     * Verifies that the machine should not allow changing symbols from outside.
     */
    @Test
    public void shouldNotAllowChangingSymbolsFromOutside()
    {
        String[] symbols = machine.symbols();

        symbols[0] = "black";

        assertEquals("red", machine.symbols()[0]);
    }

    /**
     * Verifies that the machine should return configuration from left to right.
     */
    @Test
    public void shouldReturnConfigurationFromLeftToRight()
    {
        addWheelWith("green");
        addWheelWith("red");
        addWheelWith("blue");

        assertArrayEquals(new String[] {"green", "red", "blue"}, machine.configuration());
    }

    /**
     * Verifies that the machine should show empty text for wheel without symbols.
     */
    @Test
    public void shouldShowEmptyTextForWheelWithoutSymbols()
    {
        machine.addWheel(1);

        assertArrayEquals(new String[] {""}, machine.configuration());
    }

    // distinctSymbols

    /**
     * Verifies that the machine should return zero when there are no wheels.
     */
    @Test
    public void shouldReturnZeroWhenThereAreNoWheels()
    {
        assertEquals(0, machine.distinctSymbols());
    }

    /**
     * Verifies that the machine should return one when all wheels show same symbol.
     */
    @Test
    public void shouldReturnOneWhenAllWheelsShowSameSymbol()
    {
        addWheelWith("red");
        addWheelWith("red");

        assertEquals(1, machine.distinctSymbols());
    }

    /**
     * Verifies that the machine should count repeated symbols only once.
     */
    @Test
    public void shouldCountRepeatedSymbolsOnlyOnce()
    {
        addWheelWith("red");
        addWheelWith("blue");
        addWheelWith("red");

        assertEquals(2, machine.distinctSymbols());
    }

    /**
     * Verifies that the machine should ignore wheels without visible symbols.
     */
    @Test
    public void shouldIgnoreWheelsWithoutVisibleSymbols()
    {
        addWheelWith("red");
        machine.addWheel(2);

        assertEquals(1, machine.distinctSymbols());
    }

    /**
     * Verifies that the machine should return three different visible symbols.
     */
    @Test
    public void shouldReturnThreeDifferentVisibleSymbols()
    {
        addWheelWith("red");
        addWheelWith("blue");
        addWheelWith("green");

        assertEquals(3, machine.distinctSymbols());
    }

    // isJackpot

    /**
     * Verifies that the machine should return false for empty machine.
     */
    @Test
    public void shouldReturnFalseForEmptyMachine()
    {
        assertFalse(machine.isJackpot());
    }

    /**
     * Verifies that the machine should return false when wheel has no visible symbol.
     */
    @Test
    public void shouldReturnFalseWhenWheelHasNoVisibleSymbol()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");

        assertFalse(machine.isJackpot());
    }

    /**
     * Verifies that the machine should return false when one wheel is empty.
     */
    @Test
    public void shouldReturnFalseWhenOneWheelIsEmpty()
    {
        addWheelWith("red");
        machine.addWheel(2);

        assertFalse(machine.isJackpot());
    }

    /**
     * Verifies that the machine should return true with two equal visible symbols.
     */
    @Test
    public void shouldReturnTrueWithTwoEqualVisibleSymbols()
    {
        addWheelWith("red");
        addWheelWith("red");

        assertTrue(machine.isJackpot());
    }

    /**
     * Verifies that the machine should return false when only one wheel differs.
     */
    @Test
    public void shouldReturnFalseWhenOnlyOneWheelDiffers()
    {
        addWheelWith("red");
        addWheelWith("red");
        addWheelWith("blue");

        assertFalse(machine.isJackpot());
    }

    /**
     * Verifies that the machine should return true after changing all wheels to same symbol.
     */
    @Test
    public void shouldReturnTrueAfterChangingAllWheelsToSameSymbol()
    {
        addWheelWith("red", "blue");
        addWheelWith("blue", "red");
        assertFalse(machine.isJackpot());

        machine.spin(2, 1);

        assertTrue(machine.isJackpot());
    }

    // ok

    /**
     * Verifies that the machine should be ok again after a valid operation.
     */
    @Test
    public void shouldBeOkAgainAfterAValidOperation()
    {
        machine.delWheel(7);
        assertFalse(machine.ok());

        machine.addWheel(1);

        assertTrue(machine.ok());
    }
}