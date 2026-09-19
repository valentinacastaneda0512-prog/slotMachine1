import java.util.ArrayList;
import java.util.Random;

/**
 * This contains the private attributes and the diferrent methods of the Wheel class
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
    private static final int START_X = 25;
    private static final int START_Y = 60;
    private static final int AVAILABLE_WIDTH = 250;
    private static final int WHEEL_HEIGHT = 50;

    // Guarda la última posición real (calculada por setPosition) donde
    // deben ubicarse los círculos de esta rueda, para que los símbolos
    // agregados después con addSymbol queden en el lugar correcto y no
    // se sobrepongan con los de otras ruedas.
    private int circleX;
    private int circleY;

    /**
     * Constructor for objects of class Wheel.
     */
    public Wheel() {
        symbols = new ArrayList<Symbol>();

        rectangle = new Rectangle();
        rectangle.changeColor("green");

        visibleSymbol = -1;
        visible = false;
        locked = false;

        random = new Random();

        // Posición por defecto (equivalente a una sola rueda ocupando
        // todo el ancho disponible), por si se agregan símbolos antes
        // de que la rueda reciba una posición real desde SlotMachine.
        int wheelWidth = AVAILABLE_WIDTH;
        int circleSize = 30;
        circleX = START_X + (wheelWidth - circleSize) / 2;
        circleY = START_Y + (WHEEL_HEIGHT - circleSize) / 2;
    }

    /**
     * Add symbol to the wheel. 
     * @param symbol Symbol object to add. 
     */
    public void addSymbol(Symbol symbol) {
        if (symbol != null) {
            symbols.add(symbol);

            // Usa la posición real de esta rueda (guardada por setPosition,
            // o la de por defecto si aún no se ha posicionado) en lugar de
            // recalcularla asumiendo que es la única rueda.
            symbol.setPosition(circleX, circleY);

            if (visible) {
                symbol.makeInvisible();
            }
        }
    }

    /**
     * Delete symbol of the wheel by color.
     * @param color color of the symbol to delete.
     */
    public void delSymbol(String color) {
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getColor().equals(color)) {
                symbols.get(i).makeInvisible();
                symbols.remove(i);
                if (visibleSymbol == i) {
                    visibleSymbol = -1;
                } else if (visibleSymbol > i) {
                    visibleSymbol--;
                }
                break;
            }
        }
    }

    /**
     * Delete symbol of the wheel by position.
     * @param symbolPosition position of the symbol.
     */
    public void removeSymbol(int symbolPosition) {
        if (symbolPosition >= 0 && symbolPosition < symbols.size()) {
            symbols.get(symbolPosition).makeInvisible();
            symbols.remove(symbolPosition);

            if (visibleSymbol == symbolPosition) {
                visibleSymbol = -1;
            } else if (visibleSymbol > symbolPosition) {
                visibleSymbol--;
            }
        }
    }

    /**
     * Updates symbol positions when a symbol is deleted.
     * @param deletedPosition deleted position index
     */
    public void updateSymbolPositions(int deletedPosition) {
        if (visibleSymbol > deletedPosition) {
            visibleSymbol--;
        }
    }

    /**
     * Spins the wheel.
     */
    public void spin()
    {
        if (!locked && symbols.size() > 0) {
            int position = random.nextInt(symbols.size());
            visibleSymbol = position;
            showCurrentSymbol();
        }
    }
    
    /**
     * Spins the wheel a specific number of steps.
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
                currentPosition = (currentPosition + direction + symbols.size()) % symbols.size();
            }
            visibleSymbol = currentPosition;
            showCurrentSymbol();
        }
    }
    
    /**
     * Shows the current symbol.
     */
    public void showCurrentSymbol()
    {
        for (int i = 0; i < symbols.size(); i++) {
            if (i == visibleSymbol && visible) {
                symbols.get(i).makeVisible();
            } else {
                symbols.get(i).makeInvisible();
            }
        }
    }
    
    /**
     * Return the colors of the symbols.
     * @return colors of the symbols.
     */
    public String[] symbols() {
        String[] result = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++) {
            result[i] = symbols.get(i).getColor();
        }
        return result;
    }
    
    /**
     * Return the color of the visible symbol.
     * @return the color of visible symbol.
     */
    public String visibleSymbol() {
        if (visibleSymbol == -1 || visibleSymbol >= symbols.size()) {
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
     * @return true if locked
     */
    public boolean isLocked()
    {
        return locked;
    }

    /**
     * Makes wheels visible.
     */
    public void makeVisible()
    {
        visible = true;
        rectangle.makeVisible();
        if (visibleSymbol != -1 && visibleSymbol < symbols.size()) {
            symbols.get(visibleSymbol).makeVisible();
        }
    }
    
    /**
     * Makes wheels invisible.
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
     * @param position position of wheel
     * @param totalWheels total wheels
     */
    public void setPosition(int position, int totalWheels) {
        int wheelWidth = AVAILABLE_WIDTH / totalWheels;

        int x = START_X + (position - 1) * wheelWidth;
        int y = START_Y;

        rectangle.changeSize(WHEEL_HEIGHT, wheelWidth);
        rectangle.setPosition(x, y);

        int circleSize = 30;
        if (wheelWidth < circleSize) {
            circleSize = wheelWidth;
        }

        // Guarda la posición calculada para que addSymbol la use en
        // símbolos que se agreguen más adelante.
        circleX = x + (wheelWidth - circleSize) / 2;
        circleY = y + (WHEEL_HEIGHT - circleSize) / 2;

        for (Symbol symbol : symbols) {
            symbol.setPosition(circleX, circleY);
        }
    }
    
    /**
     * Checks if the wheel contains a symbol with the given color.
     * @param color color to check
     * @return true if contained
     */
    public boolean symbolsContains(String color) {
        for (Symbol symbol : symbols) {
            if (symbol.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the wheel contains a symbol position.
     * @param symbolPosition symbol position index
     * @return true if contained
     */
    public boolean symbolsContains(int symbolPosition) {
        return symbolPosition >= 0 && symbolPosition < symbols.size();
    }
}