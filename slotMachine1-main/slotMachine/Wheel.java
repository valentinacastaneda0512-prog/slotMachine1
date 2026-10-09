import java.util.ArrayList;
import java.util.Random;

/**
 * This contains the private attributes and the different methods
 * of the Wheel class.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class Wheel
{
    private ArrayList<Symbol> symbols;
    private Rectangle rectangle;
    private int visibleSymbol;
    private boolean locked;
    private boolean visible;
    private Random random;

    private Wheel leftWheel;

    private static final int START_X = 25;
    private static final int START_Y = 60;
    private static final int AVAILABLE_WIDTH = 250;
    private static final int WHEEL_HEIGHT = 50;
    private static final int STEP_DELAY = 60;

    private int circleX;
    private int circleY;

    /**
     * Constructor for objects of class Wheel.
     */
    public Wheel()
    {
        symbols = new ArrayList<Symbol>();

        rectangle = new Rectangle();
        rectangle.changeColor("green");

        visibleSymbol = -1;
        visible = false;
        locked = false;

        random = new Random();

        leftWheel = null;

        int wheelWidth = AVAILABLE_WIDTH;
        int circleSize = 30;

        circleX = START_X + (wheelWidth - circleSize) / 2;
        circleY = START_Y + (WHEEL_HEIGHT - circleSize) / 2;
    }

    /**
     * Adds a symbol to the wheel.
     *
     * @param symbol Symbol object to add
     */
    public void addSymbol(Symbol symbol)
    {
        if (symbol != null) {
            symbols.add(symbol);

            symbol.setPosition(circleX, circleY);

            if (visible) {
                symbol.makeInvisible();
            }
        }
    }

    /**
     * Deletes a symbol by color.
     *
     * @param color color of the symbol
     */
    public void delSymbol(String color)
    {
        for (int i = 0; i < symbols.size(); i++) {

            if (symbols.get(i).getColor().equals(color)) {

                symbols.get(i).makeInvisible();
                symbols.remove(i);

                if (visibleSymbol == i) {
                    visibleSymbol = -1;
                }
                else if (visibleSymbol > i) {
                    visibleSymbol--;
                }

                break;
            }
        }
    }

    /**
     * Spins the wheel randomly.
     */
    public void spin()
    {
        if (!locked && symbols.size() > 0) {

            int position = random.nextInt(symbols.size());

            visibleSymbol = position;

            symbols.get(visibleSymbol).selected();

            showCurrentSymbol();
        }
    }

    /**
     * Spins the wheel a specific number of steps.
     *
     * @param steps number of positions to rotate
     */
    public void spin(int steps)
    {
        if (!locked && symbols.size() > 0) {

            if (visibleSymbol == -1) {
                visibleSymbol = 0;
            }

            int currentPosition = visibleSymbol;
            int direction = 1;

            if (steps < 0) {
                direction = -1;
                steps = -steps;
            }

            for (int i = 0; i < steps; i++) {

                currentPosition =
                    (currentPosition + direction + symbols.size())
                    % symbols.size();

                if (visible) {
                    visibleSymbol = currentPosition;
                    showCurrentSymbol();
                    Canvas.getCanvas().wait(STEP_DELAY);
                }
            }

            visibleSymbol = currentPosition;

            symbols.get(visibleSymbol).selected();

            showCurrentSymbol();
        }
    }

    /**
     * Shows the current symbol.
     *
     * The symbol is only displayed when the wheel is visible.
     * This prevents unit tests from opening the graphical interface.
     */
    public void showCurrentSymbol()
    {
        if (visible) {

            for (int i = 0; i < symbols.size(); i++) {
                symbols.get(i).hide();
            }

            if (visibleSymbol >= 0 &&
                visibleSymbol < symbols.size()) {

                symbols.get(visibleSymbol).show();
            }
        }
    }

    /**
     * Returns the colors of the symbols.
     *
     * @return array with the colors
     */
    public String[] symbols()
    {
        String[] result = new String[symbols.size()];

        for (int i = 0; i < symbols.size(); i++) {
            result[i] = symbols.get(i).getColor();
        }

        return result;
    }

    /**
     * Returns the color of the visible symbol.
     *
     * @return color of visible symbol
     */
    public String visibleSymbol()
    {
        if (visibleSymbol == -1 ||
            visibleSymbol >= symbols.size()) {
            return "";
        }

        return symbols.get(visibleSymbol).getColor();
    }

    /**
     * Locks the wheel.
     */
    public void lock()
    {
        locked = true;
    }

    /**
     * Unlocks the wheel.
     */
    public void unlock()
    {
        locked = false;
    }

    /**
     * Returns whether the wheel is locked.
     *
     * @return true if locked
     */
    public boolean isLocked()
    {
        return locked;
    }

    /**
     * Makes the wheel visible.
     */
    public void makeVisible()
    {
        visible = true;

        rectangle.makeVisible();

        if (visibleSymbol != -1 &&
            visibleSymbol < symbols.size()) {

            symbols.get(visibleSymbol).makeVisible();
        }
    }

    /**
     * Makes the wheel invisible.
     */
    public void makeInvisible()
    {
        visible = false;

        rectangle.makeInvisible();

        for (Symbol symbol : symbols) {
            symbol.makeInvisible();
        }
    }

    /**
     * Sets the position of the wheel.
     *
     * @param position position of wheel
     * @param totalWheels total number of wheels
     */
    public void setPosition(int position, int totalWheels)
    {
        int wheelWidth = AVAILABLE_WIDTH / totalWheels;

        int x = START_X + (position - 1) * wheelWidth;
        int y = START_Y;

        rectangle.changeSize(WHEEL_HEIGHT, wheelWidth);
        rectangle.setPosition(x, y);

        int circleSize = 30;

        if (wheelWidth < circleSize) {
            circleSize = wheelWidth;
        }

        circleX = x + (wheelWidth - circleSize) / 2;
        circleY = y + (WHEEL_HEIGHT - circleSize) / 2;

        for (Symbol symbol : symbols) {
            symbol.setPosition(circleX, circleY);
        }
    }

    /**
     * Checks if the wheel contains a symbol.
     *
     * @param color color to check
     * @return true if contained
     */
    public boolean symbolsContains(String color)
    {
        for (Symbol symbol : symbols) {

            if (symbol.getColor().equals(color)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Sets the wheel located immediately to the left.
     *
     * @param leftWheel wheel to the left
     */
    public void setLeftWheel(Wheel leftWheel)
    {
        this.leftWheel = leftWheel;
    }

    /**
     * Returns the wheel located immediately to the left.
     *
     * @return left wheel
     */
    public Wheel getLeftWheel()
    {
        return leftWheel;
    }

    /**
     * Copies the current visible state from another wheel.
     *
     * @param other wheel whose state is copied
     */
    public void copyStateFrom(Wheel other)
    {
        String color = other.visibleSymbol();

        if (!color.equals("")) {

            for (int i = 0; i < symbols.size(); i++) {

                if (symbols.get(i).getColor().equals(color)) {

                    visibleSymbol = i;
                    showCurrentSymbol();
                    symbols.get(visibleSymbol).selected();

                    return;
                }
            }
        }
    }

    /**
     * Returns whether this wheel can be locked.
     *
     * @return true if it can be locked
     */
    public boolean canLock()
    {
        return true;
    }

    /**
     * Returns whether this wheel can be exchanged.
     *
     * @return true if it can be exchanged
     */
    public boolean canSwap()
    {
        return true;
    }

    /**
     * Returns whether this wheel can be deleted.
     *
     * @return true if it can be deleted
     */
    public boolean canDelete()
    {
        return true;
    }
}