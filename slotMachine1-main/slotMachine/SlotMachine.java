import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * This is the principal class of the SlotMachine because it executes
 * the different methods.
 *
 * @author Cristian Anzola, Danik Castañeda
 * @version 1
 */
public class SlotMachine
{
    private ArrayList<Wheel> wheels;
    private ArrayList<String> symbolList;
    private ArrayList<String> symbolTypes;

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
        symbolTypes = new ArrayList<String>();

        visible = false;
        ok = true;

        rectangle = new Rectangle();
        rectangle.changeColor("magenta");
        rectangle.changeSize(70, 260);
        rectangle.setPosition(20, 50);
    }

    /**
     * Creates a machine with n wheels and n normal symbols.
     *
     * @param n number of wheels and symbols
     */
    public SlotMachine(int n)
    {
        this();

        if (n < 3 || n > MAX_WHEELS) {
            ok = false;
            return;
        }

        for (int i = 1; i <= n; i++) {
            addWheel(i);
        }

        for (int i = 0; i < n; i++) {
            addSymbol(colorFor(i));
        }

        for (int wheel = 1; wheel <= n; wheel++) {

            for (int symbol = 0; symbol < n; symbol++) {

                placeSymbol(
                    wheel,
                    symbolList.get(symbol)
                );
            }
        }

        for (int wheel = 1; wheel <= n; wheel++) {

            int steps = (int)(Math.random() * n);

            spin(wheel, steps);
        }
    }

    /**
     * Adds a normal wheel.
     *
     * @param pos position
     */
    public void addWheel(int pos)
    {
        addWheel(pos, "normal");
    }

    /**
     * Adds a wheel of a specific type.
     *
     * @param pos position
     * @param type type of wheel
     */
    public void addWheel(int pos, String type)
    {
        if (wheels.size() >= MAX_WHEELS) {
            fail(
                "No se pueden agregar más de "
                + MAX_WHEELS + " ruedas."
            );
            return;
        }

        if (pos < 1) {
            pos = 1;
        }

        if (pos > wheels.size() + 1) {
            pos = wheels.size() + 1;
        }

        Wheel wheel;

        if (type.equals("lefty")) {
            wheel = new LeftyWheel();
        }
        else if (type.equals("rebel")) {
            wheel = new RebelWheel();
        }
        else {
            wheel = new Wheel();
        }

        wheels.add(pos - 1, wheel);

        updateWheelPositions();

        if (visible) {
            wheel.makeVisible();
        }

        checkJackpot();

        ok = true;
    }

    /**
     * Deletes a wheel.
     *
     * @param pos position of the wheel
     */
    public void delWheel(int pos)
    {
        if (!isValidWheel(pos)) {
            fail("La rueda " + pos + " no existe.");
            return;
        }

        if (!wheels.get(pos - 1).canDelete()) {
            fail("La rueda no se puede eliminar.");
            return;
        }

        Wheel removed = wheels.remove(pos - 1);

        removed.makeInvisible();

        updateWheelPositions();

        checkJackpot();

        ok = true;
    }

    /**
     * Adds a normal symbol.
     *
     * @param symbol color of the symbol
     */
    public void addSymbol(String symbol)
    {
        addSymbol(symbol, "normal");
    }

    /**
     * Adds a symbol of a specific type.
     *
     * @param symbol color of the symbol
     * @param type type of symbol
     */
    public void addSymbol(String symbol, String type)
    {
        if (symbol == null ||
            symbolList.contains(symbol)) {

            fail("El símbolo no es válido o ya existe.");
            return;
        }

        /*
         * The machine accepts four symbol types:
         * normal, ephemeral, shy and rainbow.
         */
        if (!type.equals("normal") &&
            !type.equals("ephemeral") &&
            !type.equals("shy") &&
            !type.equals("rainbow")) {

            fail("El tipo de símbolo no es válido.");
            return;
        }

        symbolList.add(symbol);
        symbolTypes.add(type);

        ok = true;
    }

    /**
     * Adds a normal symbol at a position.
     *
     * @param pos position
     * @param color color
     */
    public void addSymbol(int pos, String color)
    {
        if (color == null ||
            symbolList.contains(color)) {

            fail("El símbolo no es válido o ya existe.");
            return;
        }

        if (pos < 1) {
            pos = 1;
        }

        if (pos > symbolList.size() + 1) {
            pos = symbolList.size() + 1;
        }

        symbolList.add(pos - 1, color);
        symbolTypes.add(pos - 1, "normal");

        ok = true;
    }

    /**
     * Deletes a symbol from the machine.
     *
     * @param symbol color of the symbol
     */
    public void delSymbol(String symbol)
    {
        int pos = symbolList.indexOf(symbol);

        if (pos == -1) {
            fail(
                "El símbolo " + symbol
                + " no existe."
            );
            return;
        }

        for (Wheel wheel : wheels) {
            wheel.delSymbol(symbol);
        }

        symbolList.remove(pos);
        symbolTypes.remove(pos);

        checkJackpot();

        ok = true;
    }

    /**
     * Places a symbol in a wheel.
     *
     * @param wheel position of the wheel
     * @param symbol color of the symbol
     */
    public void placeSymbol(int wheel, String symbol)
    {
        if (!isValidWheel(wheel)) {
            fail(
                "La rueda " + wheel
                + " no existe."
            );
            return;
        }

        if (!symbolList.contains(symbol)) {
            fail(
                "El símbolo " + symbol
                + " no existe en la máquina."
            );
            return;
        }

        if (wheels.get(wheel - 1)
            .symbolsContains(symbol)) {

            fail(
                "La rueda " + wheel
                + " ya tiene el símbolo "
                + symbol + "."
            );
            return;
        }

        int position = symbolList.indexOf(symbol);

        String type = symbolTypes.get(position);

        Symbol newSymbol;

        if (type.equals("ephemeral")) {
            newSymbol = new EphemeralSymbol(symbol);
        }
        else if (type.equals("shy")) {
            newSymbol = new ShySymbol(symbol);
        }
        else if (type.equals("rainbow")) {
            newSymbol = new RainbowSymbol(symbol);
        }
        else {
            newSymbol = new Symbol(symbol);
        }

        wheels.get(wheel - 1)
            .addSymbol(newSymbol);

        ok = true;
    }

    /**
     * Spins a wheel randomly.
     *
     * @param wheel position of the wheel
     */
    public void spin(int wheel)
    {
        if (!canMove(wheel)) {
            return;
        }

        wheels.get(wheel - 1).spin();

        checkJackpot();

        ok = true;
    }

    /**
     * Spins all wheels.
     */
    public void spin()
    {
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
     * Spins a wheel a specific number of steps.
     *
     * @param wheel position
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
     * Leaves the machine in a given configuration.
     *
     * @param setSymbols desired symbols
     */
    public void spin(String[] setSymbols)
    {
        if (setSymbols == null ||
            setSymbols.length != wheels.size()) {

            fail(
                "La configuración debe tener "
                + "un símbolo por cada rueda."
            );
            return;
        }

        boolean allChanged = true;

        for (int i = 0; i < wheels.size(); i++) {

            String desired = setSymbols[i];

            String[] wheelSymbols =
                wheels.get(i).symbols();

            int desiredPosition = -1;

            for (int j = 0;
                 j < wheelSymbols.length;
                 j++) {

                if (wheelSymbols[j].equals(desired)) {
                    desiredPosition = j;
                }
            }

            if (desiredPosition != -1 &&
                !wheels.get(i).isLocked()) {

                String current =
                    wheels.get(i).visibleSymbol();

                if (current.equals("")) {
                    wheels.get(i).spin(0);
                }

                int currentPosition = 0;

                for (int j = 0;
                     j < wheelSymbols.length;
                     j++) {

                    if (wheelSymbols[j].equals(current)) {
                        currentPosition = j;
                    }
                }

                int steps =
                    (desiredPosition - currentPosition
                     + wheelSymbols.length)
                    % wheelSymbols.length;

                wheels.get(i).spin(steps);
            }
            else {
                allChanged = false;
            }
        }

        checkJackpot();

        ok = allChanged;

        if (!allChanged) {
            showError(
                "Algunas ruedas no se pudieron cambiar "
                + "(bloqueadas o sin ese símbolo)."
            );
        }
    }

    /**
     * Returns the symbols registered in the machine.
     *
     * @return symbols
     */
    public String[] symbols()
    {
        String[] result =
            new String[symbolList.size()];

        for (int i = 0;
             i < symbolList.size();
             i++) {

            result[i] = symbolList.get(i);
        }

        return result;
    }

    /**
     * Returns the number of distinct visible symbols.
     *
     * @return number of distinct symbols
     */
    public int distinctSymbols()
    {
        ArrayList<String> visibles =
            new ArrayList<String>();

        for (Wheel wheel : wheels) {

            String symbol =
                wheel.visibleSymbol();

            if (!symbol.equals("") &&
                !visibles.contains(symbol)) {

                visibles.add(symbol);
            }
        }

        return visibles.size();
    }

    /**
     * Returns the current configuration.
     *
     * @return visible symbol of every wheel
     */
    public String[] configuration()
    {
        String[] result =
            new String[wheels.size()];

        for (int i = 0;
             i < wheels.size();
             i++) {

            result[i] =
                wheels.get(i).visibleSymbol();
        }

        return result;
    }

    /**
     * Checks if the machine has a jackpot.
     *
     * @return true if all visible symbols are equal
     */
    public boolean isJackpot()
    {
        if (wheels.size() == 0) {
            return false;
        }

        String firstSymbol =
            wheels.get(0).visibleSymbol();

        if (firstSymbol.equals("")) {
            return false;
        }

        for (int i = 1;
             i < wheels.size();
             i++) {

            String currentSymbol =
                wheels.get(i).visibleSymbol();

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
     * Makes the machine visible.
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
     * Makes the machine invisible.
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
     * Closes the slot machine.
     */
    public void exit()
    {
        makeInvisible();
    }

    /**
     * Returns whether the last operation was valid.
     *
     * @return true if valid
     */
    public boolean ok()
    {
        return ok;
    }

    /**
     * Swaps two wheels.
     *
     * @param wheel1 first wheel
     * @param wheel2 second wheel
     */
    public void swap(int wheel1, int wheel2)
    {
        if (!isValidWheel(wheel1) ||
            !isValidWheel(wheel2) ||
            wheel1 == wheel2) {

            fail(
                "No se pueden intercambiar "
                + "las ruedas "
                + wheel1 + " y " + wheel2 + "."
            );
            return;
        }

        if (!wheels.get(wheel1 - 1).canSwap() ||
            !wheels.get(wheel2 - 1).canSwap()) {

            fail("Una de las ruedas no se puede intercambiar.");
            return;
        }

        Wheel temporary =
            wheels.get(wheel1 - 1);

        wheels.set(
            wheel1 - 1,
            wheels.get(wheel2 - 1)
        );

        wheels.set(
            wheel2 - 1,
            temporary
        );

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
            fail(
                "La rueda " + wheel
                + " no existe."
            );
            return;
        }

        if (!wheels.get(wheel - 1).canLock()) {
            fail("La rueda no se puede bloquear.");
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
            fail(
                "La rueda " + wheel
                + " no existe."
            );
            return;
        }

        wheels.get(wheel - 1).unlock();

        ok = true;
    }

    /**
     * Updates wheel positions and left-wheel relationships.
     */
    private void updateWheelPositions()
    {
        for (int i = 0;
             i < wheels.size();
             i++) {

            wheels.get(i).setPosition(
                i + 1,
                wheels.size()
            );

            if (i == 0) {
                wheels.get(i).setLeftWheel(null);
            }
            else {
                wheels.get(i).setLeftWheel(
                    wheels.get(i - 1)
                );
            }
        }
    }

    /**
     * Checks whether the machine has a jackpot
     * and changes its color.
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
     * Shows an error only when visible.
     *
     * @param message error message
     */
    private void showError(String message)
    {
        if (visible) {
            JOptionPane.showMessageDialog(
                null,
                message
            );
        }
    }

    /**
     * Marks an operation as invalid.
     *
     * @param message error message
     */
    private void fail(String message)
    {
        ok = false;
        showError(message);
    }

    /**
     * Checks if a wheel position exists.
     *
     * @param wheel wheel position
     * @return true if valid
     */
    private boolean isValidWheel(int wheel)
    {
        return wheel >= 1 &&
               wheel <= wheels.size();
    }

    /**
     * Checks if a wheel can move.
     *
     * @param wheel position of the wheel
     * @return true if it can move
     */
    private boolean canMove(int wheel)
    {
        if (!isValidWheel(wheel)) {
            fail(
                "La rueda " + wheel
                + " no existe."
            );
            return false;
        }

        if (wheels.get(wheel - 1).isLocked()) {
            fail(
                "La rueda " + wheel
                + " está bloqueada."
            );
            return false;
        }

        return true;
    }

    /**
     * Generates a different color for each position.
     *
     * @param position position
     * @return color
     */
    private String colorFor(int position)
    {
        int red =
            40 + ((position * 67) % 216);

        int green =
            40 + ((position * 113) % 216);

        int blue =
            40 + ((position * 157) % 216);

        return "#"
            + twoDigits(red)
            + twoDigits(green)
            + twoDigits(blue);
    }

    /**
     * Converts a number into two hexadecimal digits.
     *
     * @param value number
     * @return hexadecimal text
     */
    private String twoDigits(int value)
    {
        String result =
            Integer.toHexString(value);

        if (result.length() == 1) {
            result = "0" + result;
        }

        return result;
    }
}