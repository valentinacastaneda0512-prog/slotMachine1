/**
 * A wheel that copies the visible state of the wheel
 * located immediately to its left when it is spun.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class LeftyWheel extends Wheel
{
    /**
     * Creates a LeftyWheel.
     */
    public LeftyWheel()
    {
        super();
    }

    /**
     * Spins the wheel.
     *
     * If there is a wheel to the left, this wheel copies
     * its visible state.
     */
    @Override
    public void spin()
    {
        if (getLeftWheel() != null) {
            copyStateFrom(getLeftWheel());
        }
        else {
            super.spin();
        }
    }

    /**
     * Spins the wheel a specific number of steps.
     *
     * If there is a wheel to the left, this wheel copies
     * its visible state.
     *
     * @param steps number of positions to rotate
     */
    @Override
    public void spin(int steps)
    {
        if (getLeftWheel() != null) {
            copyStateFrom(getLeftWheel());
        }
        else {
            super.spin(steps);
        }
    }
}