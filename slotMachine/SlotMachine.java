import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * This is the principal class of the SlotMachine because it executes the different methods .
 * 
 * @author Cristian Anzola, Danik Castañeda 
 * @version 1
 */
public class SlotMachine
{
    private ArrayList<Wheel> wheels;
    private ArrayList<String> symbolList;
    private boolean visible;
    private boolean ok;
    private Rectangle rectangle;
    private static final int MAX_WHEELS = 50;

    /**
     * Constructor for objects of class SlotMachine.
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        symbolList = new ArrayList<String>();
        visible = false;
        ok = true;

        rectangle = new Rectangle();
        rectangle.changeColor("magenta");
        rectangle.changeSize(70, 260);
        rectangle.setPosition(20, 50);
    }

    /**
     * Creates a machine with n wheels and n symbols,
     * initialized randomly.
     * @param n number of wheels and symbols (between 3 and 50)
     */
    public SlotMachine(int n)
    {
        this();

        if (n < 3 || n > MAX_WHEELS) {
            ok = false;
            return;
        }
    
        // Crear las n ruedas
        for (int i = 1; i <= n; i++) {
            addWheel(i);
        }
    
        // Crear los n símbolos, todos de diferente color
        for (int i = 0; i < n; i++) {
            addSymbol(colorFor(i));
        }
    
        // Poner los mismos n símbolos, en el mismo orden,
        // en todas las ruedas
        for (int wheel = 1; wheel <= n; wheel++) {
            for (int symbol = 0; symbol < n; symbol++) {
                placeSymbol(wheel, symbolList.get(symbol));
            }
        }
    
        // Configuración inicial aleatoria
        for (int wheel = 1; wheel <= n; wheel++) {
            int steps = (int)(Math.random() * n);
            spin(wheel, steps);
        }
    }
    
    /**
     * Add a wheel position.
     * @param pos position of the wheel.
     */
    public void addWheel(int pos)
    {
        if (wheels.size() >= MAX_WHEELS) {
            fail("No se pueden agregar más de " + MAX_WHEELS + " ruedas.");
            return;
        }
        if (pos < 1) {
            pos = 1;
        }

        if (pos > wheels.size() + 1) {
            pos = wheels.size() + 1;
        }

        Wheel wheel = new Wheel();
        wheels.add(pos - 1, wheel);
        updateWheelPositions();
        if (visible) {
            wheel.makeVisible();
        }
        checkJackpot();
        ok = true;
    }

    /**
     * Delete wheel position
     * @param pos the position of the wheel 
     */
    public void delWheel(int pos) {
        if (pos < 1 || pos > wheels.size()) {
            fail("La rueda " + pos + " no existe.");
            return;
        }
        Wheel removed = wheels.remove(pos - 1);
        removed.makeInvisible();
        updateWheelPositions();
        checkJackpot();
        ok = true;
    }
    
    /**
     * Add symbol with a specific color
     * @param symbol color of the symbol
     */
    public void addSymbol(String symbol) {
        if (symbol == null || symbolList.contains(symbol)) {
            fail("El símbolo no es válido o ya existe.");
            return;
        }
        symbolList.add(symbol);
        ok = true;
    }

    /**
     * Add symbol with a position and specific color
     * @param pos position of the symbol
     * @param color color of the symbol
     */
    public void addSymbol(int pos, String color) {
        if (color == null || symbolList.contains(color)) {
            fail("El símbolo no es válido o ya existe.");
            return;
        }
        if (pos < 1) pos = 1;
        if (pos > symbolList.size() + 1) pos = symbolList.size() + 1;
        symbolList.add(pos - 1, color);
        ok = true;
    }
    
    /**
     * Deletes a symbol of the List.
     * @param symbol color of the symbol to delete
     */
    public void delSymbol(String symbol) {
        int pos = symbolList.indexOf(symbol);
        if (pos == -1) {
            fail("El símbolo " + symbol + " no existe.");
            return;
        }
        for (Wheel wheel : wheels) {
            wheel.delSymbol(symbol);
        }
        symbolList.remove(pos);
        checkJackpot();
        ok = true;
    }
    
    /**
     * Select the place of the symbol in the wheel.
     * A wheel cannot have the same symbol twice.
     * @param wheel the position of the wheel
     * @param symbol the color of the symbol
     */
    public void placeSymbol(int wheel, String symbol) {
        if (!isValidWheel(wheel)) {
            fail("La rueda " + wheel + " no existe.");
            return;
        }
        if (!symbolList.contains(symbol)) {
            fail("El símbolo " + symbol + " no existe en la máquina.");
            return;
        }
        if (wheels.get(wheel - 1).symbolsContains(symbol)) {
            fail("La rueda " + wheel + " ya tiene el símbolo " + symbol + ".");
            return;
        }
        wheels.get(wheel - 1).addSymbol(new Symbol(symbol));
        ok = true;
    }
    
    /**
     * Turn a specific wheel
     * @param wheel position of the wheel
     */
    public void spin(int wheel) {
        if (!canMove(wheel)) {
            return;
        }
        wheels.get(wheel - 1).spin();
        checkJackpot();
        ok = true;
    }
    
    /**
     * Spin all wheels
     */
    public void spin() {
        if (wheels.size() == 0) {
            fail("La máquina no tiene ruedas.");
            return;
        }
        for (Wheel wheel : wheels) {
            wheel.spin();
        }
        checkJackpot();
        ok = true;
    }

    /**
     * Checks the jackpot and changes
     * the color of the machine.
     */
    private void checkJackpot()
    {
        if (isJackpot()) {
            rectangle.changeColor("yellow");
        }
        else {
            rectangle.changeColor("magenta");
        }
    }
    
    /**
     * Return the symbols.
     * @return the symbols.
     */
    public String[] symbols()
    {
        String[] result = new String[symbolList.size()];
        for (int i = 0; i < symbolList.size(); i++) {
            result[i] = symbolList.get(i);
        }
        return result;
    }
    
    /**
     * Return the number of distinct symbols.
     * @return number of symbols 
     */
    public int distinctSymbols()
    {
        ArrayList<String> visibles = new ArrayList<String>();
        for (Wheel wheel : wheels) {
            String symbol = wheel.visibleSymbol();
    
            if (!symbol.equals("") && !visibles.contains(symbol)) {
                visibles.add(symbol);
            }
        }
        return visibles.size();
    }
    
    /**
     * Return the configuration of the wheel
     * @return the visible symbol of the wheel.
     */
    public String[] configuration()
    {
        String[] result = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            result[i] = wheels.get(i).visibleSymbol();
        }
        return result;
    }
    
    /**
     * Checks if the machine has a jackpot.
     * @return true if all visible symbols are the same.
     */
    public boolean isJackpot() {
        if (wheels.size() == 0) {
            return false;
        }

        String firstSymbol = wheels.get(0).visibleSymbol();

        if (firstSymbol.equals("")) {
            return false;
        }

        for (int i = 1; i < wheels.size(); i++) {
            String currentSymbol = wheels.get(i).visibleSymbol();

            if (currentSymbol.equals("")) {
                return false;
            }

            if (!currentSymbol.equals(firstSymbol)) {
                return false;
            }
        }

        return true;
    }
    
    /** 
     * slot machine is visible. 
     */
    public void makeVisible()
    {
        visible = true;
        ok = true;
        rectangle.makeVisible();
        for (Wheel wheel : wheels) {
            wheel.makeVisible();
        }
    }
    
    /**
     * slot machine is invisible
     */
    public void makeInvisible()
    {
        visible = false;
        ok = true;
        rectangle.makeInvisible();
        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
    }   
    
    /**
     * Close the slot machine
     */
    public void exit()
    {
        makeInvisible();
    }
    
    /**
     * Return if the slot machine is valid 
     * @return true if the machine is valid 
     */
    public boolean ok()
    {
        return ok;
    }
    
    /**
     * Interchanges two wheels.
     *
     * @param wheel1 first wheel position
     * @param wheel2 second wheel position
     */
    public void swap(int wheel1, int wheel2)
    {
        if (!isValidWheel(wheel1) || !isValidWheel(wheel2) || wheel1 == wheel2) {
            fail("No se pueden intercambiar las ruedas " + wheel1 + " y " + wheel2 + ".");
            return;
        }
        Wheel temporary = wheels.get(wheel1 - 1);
        wheels.set(wheel1 - 1, wheels.get(wheel2 - 1));
        wheels.set(wheel2 - 1, temporary);
        updateWheelPositions();
        checkJackpot();
        ok = true;
    }
    
    /**
     * Locks a wheel.
     *
     * @param wheel position of the wheel
     */
    public void lock(int wheel)
    {
        if (!isValidWheel(wheel)) {
            fail("La rueda " + wheel + " no existe.");
            return;
        }
        wheels.get(wheel - 1).lock();
        ok = true;
    }
    
    /**
     * Unlocks a wheel.
     *
     * @param wheel position of the wheel
     */
    public void unlock(int wheel)
    {
        if (!isValidWheel(wheel)) {
            fail("La rueda " + wheel + " no existe.");
            return;
        }
        wheels.get(wheel - 1).unlock();
        ok = true;
    }
    
    /**
     * Spins a wheel a specific number of steps.
     * @param wheel position of the wheel
     * @param steps number of steps
     */
    public void spin(int wheel, int steps)
    {
        if (!canMove(wheel)) {
            return;
        }
        wheels.get(wheel - 1).spin(steps);
        checkJackpot();
        ok = true;
    }
    
    /**
     * Leaves the machine in the given configuration.
     *
     * @param setSymbols desired visible symbols
     */
    public void spin(String[] setSymbols)
    {
        if (setSymbols == null || setSymbols.length != wheels.size()) {
            fail("La configuración debe tener un símbolo por cada rueda.");
            return;
        }
        boolean allChanged = true;
        for (int i = 0; i < wheels.size(); i++) {
            String desired = setSymbols[i];
            String[] wheelSymbols = wheels.get(i).symbols();
            int desiredPosition = -1;

            for (int j = 0; j < wheelSymbols.length; j++) {
                if (wheelSymbols[j].equals(desired)) {
                    desiredPosition = j;
                }
            }
            if (desiredPosition != -1 && !wheels.get(i).isLocked()) {
                String current = wheels.get(i).visibleSymbol();
                if (current.equals("")) {
                    wheels.get(i).spin(0);
                }
                int currentPosition = 0;
                for (int j = 0; j < wheelSymbols.length; j++) {
                    if (wheelSymbols[j].equals(current)) {
                        currentPosition = j;
                    }
                }
                int steps = (desiredPosition - currentPosition + wheelSymbols.length) % wheelSymbols.length;
                wheels.get(i).spin(steps);
            }
            else {
                allChanged = false;
            }
        }
        checkJackpot();
        ok = allChanged;
        if (!allChanged) {
            showError("Algunas ruedas no se pudieron cambiar (bloqueadas o sin ese símbolo).");
        }
    }

    /** 
     * Adjust wheels
     */
    private void updateWheelPositions()
    {
        for (int i = 0; i < wheels.size(); i++) {
            wheels.get(i).setPosition(
                i + 1,
                wheels.size()
            );
        }
    }
    
    /**
     * Shows an error message only when the machine is visible.
     * @param message text to show
     */
    private void showError(String message)
    {   
        if (visible) {
            JOptionPane.showMessageDialog(null, message);
        }
    }
    
    /**
     * Marks the last operation as failed and informs the user.
     * @param message explanation shown when the machine is visible
     */
    private void fail(String message)
    {
        ok = false;
        showError(message);
    }

    /**
     * Checks if a wheel position exists.
     * @param wheel position of the wheel (starting at 1)
     * @return true if the wheel exists
     */
    private boolean isValidWheel(int wheel)
    {
        return wheel >= 1 && wheel <= wheels.size();
    }

    /**
     * Checks if a wheel exists and is not locked.
     * If it cannot move, the operation is marked as failed.
     * @param wheel position of the wheel (starting at 1)
     * @return true if the wheel can spin
     */
    private boolean canMove(int wheel)
    {
        if (!isValidWheel(wheel)) {
            fail("La rueda " + wheel + " no existe.");
            return false;
        }
        if (wheels.get(wheel - 1).isLocked()) {
            fail("La rueda " + wheel + " está bloqueada.");
            return false;
        }
        return true;
    }

    /**
     * Generates a different color for each position.
     * @param position index of the symbol
     * @return color in hexadecimal format (#rrggbb)
     */
    private String colorFor(int position)
    {
        int red = 40 + ((position * 67) % 216);
        int green = 40 + ((position * 113) % 216);
        int blue = 40 + ((position * 157) % 216);
    
        return "#" + twoDigits(red) + twoDigits(green) + twoDigits(blue);
    }

    /**
     * Converts a number to a two-digit hexadecimal text.
     * @param value number between 0 and 255
     * @return two hexadecimal digits
     */
    private String twoDigits(int value)
    {
        String result = Integer.toHexString(value);
    
        if (result.length() == 1) {
            result = "0" + result;
        }
    
        return result;
    }
}