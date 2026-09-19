import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the SlotMachine class.
 *
 * These tests verify the main behaviors of the slot machine:
 * creation, wheels, symbols, spinning, configuration and jackpot.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachineTest
{
    private SlotMachine machine;

    /**
     * Create a new slot machine before each test.
     * Three symbols are added so that tests can use them 
     * without repeating them.
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
     * Verifies that a new machine starts without wheels
     * and without a jackpot.
     */
    @Test
    public void shouldCreateAnEmptyMachine()
    {
        SlotMachine emptyMachine = new SlotMachine();

        assertEquals(0, emptyMachine.configuration().length);
        assertEquals(0, emptyMachine.distinctSymbols());
        assertFalse(emptyMachine.isJackpot());
    }

    /**
     * Verifies that symbols can be added to the machine
     * and that they are not duplicated
     */
    @Test
    public void shouldAddSymbolsWithoutDuplicates()
    {
        machine.addSymbol("yellow");
        machine.addSymbol("yellow");
        assertEquals(4, machine.distinctSymbols());
        String[] symbols = machine.symbols();
        assertEquals("red", symbols[0]);
        assertEquals("blue", symbols[1]);
        assertEquals("green", symbols[2]);
        assertEquals("yellow", symbols[3]);
    }

    /**
     * Checks that the null symbol is ignored 
     * since a null symbol should not be added to the list
     */
    @Test
    public void shouldIgnoreNullSymbols()
    {
        machine.addSymbol(null);
        assertEquals(3, machine.distinctSymbols());
    }

    /**
     * Verify that the machine can have 50 wheels.
     */
    @Test
    public void shouldAllowMaximumOfFiftyWheels()
    {
        for (int i = 1; i <= 50; i++) {
            machine.addWheel(i);
        }
        assertEquals(50, machine.configuration().length);
    }

    /**
     * Checks that the machine does not add more than
    * 50 wheels.
     */
    @Test
    public void shouldNotAllowMoreThanFiftyWheels()
    {
        for (int i = 1; i <= 50; i++) {
            machine.addWheel(i);
        }
        machine.addWheel(51);
        assertEquals(50, machine.configuration().length);
    }

    /**
     * Checks that an invalid position less than 1 is valid
     * when adding a wheel.
     */
    @Test
    public void shouldCorrectInvalidWheelPosition()
    {
        machine.addWheel(0);
        assertEquals(1, machine.configuration().length);
    }

    /**
     * Verifies that a wheel can be deleted from the machine.
     */
    @Test
    public void shouldDeleteWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        assertEquals(3, machine.configuration().length);
        machine.delWheel(2);
        assertEquals(2, machine.configuration().length);
    }

    /**
     * Verifies that deleting an invalid wheel position
     * does not modify the machine.
     */
    @Test
    public void shouldIgnoreInvalidWheelDeletion()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.delWheel(0);
        machine.delWheel(10);
        assertEquals(2, machine.configuration().length);
    }

    /**
     * Verifies that a symbol can be placed in a specific wheel.
     */
    @Test
    public void shouldPlaceSymbolInTheCorrectWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.spin(1);
        machine.spin(2);
        String[] configuration = machine.configuration();
        assertEquals("red", configuration[0]);
        assertEquals("blue", configuration[1]);
    }

    /**
     * Verifies that an unknown symbol cannot be placed
     * in a wheel.
     */
    @Test
    public void shouldNotPlaceUnknownSymbol()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "purple");
        machine.spin(1);
        assertEquals("", machine.configuration()[0]);
    }

    /**
     * Verifies that spinning only one wheel does not spin
     * the other wheels.
     */
    @Test
    public void shouldSpinOnlyTheSelectedWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.spin(1);
        String[] configuration = machine.configuration();
        assertEquals("red", configuration[0]);
        assertEquals("", configuration[1]);
    }

    /**
     * Verifies that the spin() method spins all the wheels.
     */
    @Test
    public void shouldSpinAllWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");
        machine.spin();
        String[] configuration = machine.configuration();
        assertEquals("red", configuration[0]);
        assertEquals("blue", configuration[1]);
        assertEquals("green", configuration[2]);
    }

    /**
    * Checks that a jackpot is detected when all wheels
    * have the same symbol.
    */
    @Test
    public void shouldDetectJackpotWhenAllWheelsHaveTheSameSymbol()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "red");
        machine.spin();
        assertTrue(machine.isJackpot());
    }

    /**
     * Verifies that a jackpot is not detected when the
     */
    @Test
    public void shouldNotDetectJackpotWithDifferentSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "blue");
        machine.spin();
        assertFalse(machine.isJackpot());
    }

    /**
     * Verifies that the machine cannot have a jackpot if
     * not all wheels have a visible symbol.
     */
    @Test
    public void shouldNotDetectJackpotIfOneWheelHasNoVisibleSymbol()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "red");
        machine.spin(1);
        machine.spin(2);
        assertFalse(machine.isJackpot());
    }

    /**
     * Verifies that the jackpot works with more than
     *three wheels.
     */
    @Test
    public void shouldDetectJackpotWithTenWheels()
    {
        for (int i = 1; i <= 10; i++) {
            machine.addWheel(i);
            machine.placeSymbol(i, "red");
        }
        machine.spin();
        assertTrue(machine.isJackpot());
        assertEquals(10, machine.configuration().length);
    }

    /**
     * Verifies that a symbol can be deleted from the machine.
     */
    @Test
    public void shouldDeleteSymbol()
    {
        assertEquals(3, machine.distinctSymbols());
        machine.delSymbol("red");
        assertEquals(2, machine.distinctSymbols());
        String[] symbols = machine.symbols();
        assertEquals("blue", symbols[0]);
        assertEquals("green", symbols[1]);
    }

    /**
     * Verifies that deleting a symbol that does not exist
     * does not modify the symbol list.
     */
    @Test
    public void shouldIgnoreDeletingUnknownSymbol()
    {
        machine.delSymbol("purple");
        assertEquals(3, machine.distinctSymbols());
    }

    /**
     * Verifies that deleting a symbol also removes it from
     * the wheels where it was placed.
     */
    @Test
    public void shouldRemoveDeletedSymbolFromTheWheels()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.delSymbol("red");
        machine.spin(1);
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that the configuration method returns the
     * visible symbol of every wheel in the correct order.
     */
    @Test
    public void shouldReturnCorrectConfiguration()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");
        machine.spin();
        String[] configuration = machine.configuration();
        assertEquals(3, configuration.length);
        assertEquals("red", configuration[0]);
        assertEquals("blue", configuration[1]);
        assertEquals("green", configuration[2]);
    }
    
    /**
     * SlotMachineTest
     */

     /**
     * Verifies that two valid wheels can be exchanged.
     */
    @Test
    public void shouldSwapTwoWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");
        machine.spin();
        machine.swap(1, 3);
        String[] configuration = machine.configuration();
        assertEquals("green", configuration[0]);
        assertEquals("blue", configuration[1]);
        assertEquals("red", configuration[2]);
    }

    /**
     * Verifies that swapping with an invalid first wheel
     * does not modify the machine.
     */
    @Test
    public void shouldNotSwapWithInvalidFirstWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.spin();
        String[] before = machine.configuration();
        machine.swap(0, 2);
        String[] after = machine.configuration();
        assertArrayEquals(before, after);
    }

    /**
     * Verifies that swapping with an invalid second wheel
     * does not modify the machine.
     */
    @Test
    public void shouldNotSwapWithInvalidSecondWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.spin();
        String[] before = machine.configuration();
        machine.swap(1, 10);
        String[] after = machine.configuration();
        assertArrayEquals(before, after);
    }

    /**
     * Verifies that a locked wheel cannot be spun.
     */
    @Test
    public void shouldNotSpinLockedWheel()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
        machine.spin(1, 0);
        assertEquals("red", machine.configuration()[0]);
        machine.lock(1);
        machine.spin(1, 1);
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that an unlocked wheel can be spun again.
     */
    @Test
    public void shouldSpinWheelAfterUnlockingIt()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
        machine.spin(1, 0);
        machine.lock(1);
        machine.unlock(1);
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that locking an invalid wheel does not
     * modify the machine.
     */
    @Test
    public void shouldIgnoreInvalidWheelLock()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.spin(1, 0);
        machine.lock(10);
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that unlocking an invalid wheel does not
     * modify the machine.
     */
    @Test
    public void shouldIgnoreInvalidWheelUnlock()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.spin(1, 0);
        machine.lock(1);
        machine.unlock(10);
        machine.spin(1, 1);
        assertEquals("red", machine.configuration()[0]);
    }

    /**
     * Verifies that spinning a wheel one step moves it
     * to the next symbol.
     */
    @Test
    public void shouldSpinOneStep()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
        machine.spin(1, 0);
        machine.spin(1, 1);
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * Verifies that spinning a wheel two steps moves it
     * two positions.
     */
    @Test
    public void shouldSpinTwoSteps()
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
     * Verifies that spinning a wheel three steps returns
     * it to its original position when it has three symbols.
     */
    @Test
    public void shouldReturnToOriginalPositionAfterThreeSteps()
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
     * Verifies that spinning zero steps does not change
     * the visible symbol.
     */
    @Test
    public void shouldNotChangeWithZeroSteps()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.spin(1, 0);
        String before = machine.configuration()[0];
        machine.spin(1, 0);
        String after = machine.configuration()[0];
        assertEquals(before, after);
    }

    /**
     * Verifies that spinning an invalid wheel does not
     * modify the machine.
     */
    @Test
    public void shouldNotSpinInvalidWheel()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.spin(1, 0);
        String before = machine.configuration()[0];
        machine.spin(10, 2);
        String after = machine.configuration()[0];
        assertEquals(before, after);
    }

    /**
     * Verifies that the machine can be left in a given
     * configuration.
     */
    @Test
    public void shouldSetGivenConfiguration()
    {
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
        String[] desired = {
            "red",
            "blue",
            "green"
        };
        machine.spin(desired);
        assertArrayEquals(
            desired,
            machine.configuration()
        );
    }

    /**
     * Verifies that another valid configuration can be established.
     */
    @Test
    public void shouldSetAnotherGivenConfiguration()
    {
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
        String[] desired = {
            "green",
            "green",
            "red"
        };
        machine.spin(desired);
        assertArrayEquals(
            desired,
            machine.configuration()
        );
    }

    /**
     * Verifies that a null configuration does not modify
     * the machine.
     */
    @Test
    public void shouldIgnoreNullConfiguration()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.spin(1, 0);
        machine.spin(2, 0);
        String[] before = machine.configuration();
        machine.spin(null);
        String[] after = machine.configuration();
        assertArrayEquals(before, after);
    }

    /**
     * Verifies that a configuration with a different number
     * of symbols does not modify the machine.
     */
    @Test
    public void shouldIgnoreConfigurationWithWrongSize()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");
        machine.spin();
        String[] before = machine.configuration();
        String[] desired = {
            "red",
            "blue"
        };

        machine.spin(desired);
        String[] after = machine.configuration();
        assertArrayEquals(before, after);
    }

    /**
     * Verifies that a locked wheel is not changed when
     * a given configuration is requested.
     */
    @Test
    public void shouldNotChangeLockedWheelInGivenConfiguration()
    {
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
        machine.spin(1, 0);
        machine.lock(1);
        String[] desired = {
            "green",
            "blue",
            "red"
        };
        machine.spin(desired);
        assertEquals("red", machine.configuration()[0]);
    }
    
    /**
     * SlorMachineCC2Test 
     */
    
    /**
     * Shared test identified with Aa.
     *
     * Verifies that swapping two valid wheels exchanges
     * their visible symbols.
     */
    @Test
    public void accordingAaSwapShouldExchangeTwoWheels()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.spin();
        machine.swap(1, 2);
        assertEquals("blue",machine.configuration()[0]);
        assertEquals( "red",machine.configuration()[1]);
    }

    /**
     * Shared test identified with Cb.
     *
     * Verifies that a locked wheel cannot be rotated.
     */
    @Test
    public void accordingCbLockShouldPreventWheelFromSpinning()
    {
        machine.addWheel(1);

        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
        machine.spin(1, 0);
        machine.lock(1);
        machine.spin(1, 2);
        assertEquals(
            "red",
            machine.configuration()[0]
        );
    }

    /**
     * Shared test identified with Aa.
     *
     * Verifies that an unlocked wheel can rotate again.
     */
    @Test
    public void accordingAaUnlockShouldAllowWheelToSpin()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
        machine.spin(1, 0);
        machine.lock(1);
        machine.unlock(1);
        machine.spin(1, 1);
        assertEquals(
            "blue",
            machine.configuration()[0]
        );
    }

    /**
     * Shared test identified with Cb.
     *
     * Verifies that spin with a number of steps moves
     * the wheel the requested number of positions.
     */
    @Test
    public void accordingCbSpinShouldMoveTheRequestedSteps()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "green");
        machine.spin(1, 0);
        machine.spin(1, 2);
        assertEquals(
            "green",
            machine.configuration()[0]
        );
    }

    /**
     * Shared test identified with Aa.
     *
     * Verifies that the machine can be placed in a
     * specified configuration.
     */
    @Test
    public void accordingAaSpinShouldSetGivenConfiguration()
    {
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
        String[] desired = {
            "red",
            "blue",
            "green"
        };
        machine.spin(desired);
        assertArrayEquals(
            desired,
            machine.configuration()
        );
    }

    /**
     * Shared test identified with Cb.
     *
     * Verifies that an invalid wheel cannot be exchanged.
     */
    @Test
    public void accordingCbSwapShouldIgnoreInvalidWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.spin();
        String[] before = machine.configuration();
        machine.swap(1, 10);
        String[] after = machine.configuration();
        assertArrayEquals(before,after);
    }
}
    
    
