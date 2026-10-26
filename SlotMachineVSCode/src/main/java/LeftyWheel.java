/**
 * A wheel that copies the visible state of the wheel
 * located immediately to its left when it is spun.
 *
 * It is light gray so it can be told apart from the other wheels.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 2
 */
public class LeftyWheel extends Wheel
{
    /**
     * Creates a LeftyWheel.
     */
    public LeftyWheel()
    {
        super();
        changeColor("lightGray");
    }

    /**
     * Spins the wheel.
     *
     * If there is a wheel to the left, this wheel copies
     * its visible state. A locked wheel does not change.
     */
    @Override
    public void spin()
    {
        if (isLocked()) {
            return;
        }
        if (getLeftWheel() != null) {
            copyLeftWheel();
        }
        else {
            super.spin();
        }
    }

    /**
     * Spins the wheel a specific number of steps.
     *
     * If there is a wheel to the left, this wheel copies
     * its visible state. A locked wheel does not change.
     *
     * @param steps number of positions to rotate
     */
    @Override
    public void spin(int steps)
    {
        if (isLocked()) {
            return;
        }
        if (getLeftWheel() != null) {
            copyLeftWheel();
        }
        else {
            super.spin(steps);
        }
    }

    /**
     * Copies the state of the left wheel and shows the result.
     */
    private void copyLeftWheel()
    {
        copyStateFrom(getLeftWheel());
        showCurrentSymbol();
    }
}