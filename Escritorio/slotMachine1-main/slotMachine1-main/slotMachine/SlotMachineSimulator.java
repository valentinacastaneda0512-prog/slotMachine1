/**
 * Visual simulator for the SlotMachine.
 *
 * This is not a JUnit test.
 * It is used to execute the machine visually.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachineSimulator
{
    /**
     * Executes the slot machine visually.
     */
    public static void main(String[] args) throws InterruptedException
    {
        SlotMachine machine = new SlotMachine();

        // Create symbols
        machine.addSymbol("red");
        machine.addSymbol("blue");
        machine.addSymbol("yellow");

        // Create wheels
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);

        // Place symbols in the wheels
        machine.placeSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        machine.placeSymbol(1, "yellow");

        machine.placeSymbol(2, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(2, "yellow");

        machine.placeSymbol(3, "red");
        machine.placeSymbol(3, "blue");
        machine.placeSymbol(3, "yellow");

        // Show the machine
        machine.makeVisible();

        delay(1000);

        // Spin first wheel
        machine.spin(1);
        delay(1000);

        // Spin second wheel
        machine.spin(2);
        delay(1000);

        // Spin third wheel
        machine.spin(3);
        delay(1000);

        // Set a specific configuration
        String[] configuration = {
            "red",
            "blue",
            "yellow"
        };

        machine.spin(configuration);
        delay(1500);

        // Lock first wheel
        machine.lock(1);
        delay(1000);

        // Try to change the locked wheel
        machine.spin(1, 1);
        delay(1000);

        // Unlock first wheel
        machine.unlock(1);
        delay(1000);

        // Spin again
        machine.spin(1, 1);
        delay(1000);

        // Swap first and third wheels
        machine.swap(1, 3);
        delay(1500);

        // Set jackpot configuration
        String[] jackpotConfiguration = {
            "red",
            "red",
            "red"
        };

        machine.spin(jackpotConfiguration);
        delay(2000);

        // Finish
        machine.exit();
    }

    /**
     * Creates a delay between simulator actions.
     */
    private static void delay(int milliseconds)
        throws InterruptedException
    {
        Thread.sleep(milliseconds);
    }
}