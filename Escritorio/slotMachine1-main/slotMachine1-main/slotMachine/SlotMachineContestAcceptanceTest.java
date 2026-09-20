import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Acceptance tests for the Slot Machine contest.
 */
public class SlotMachineContestAcceptanceTest
{
    /**
     * Acceptance test with 5 wheels.
     */
    @Test
    public void acceptanceTestFiveWheels()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        System.out.println("================================");
        System.out.println("PRUEBA DE ACEPTACION - 5 RUEDAS");
        System.out.println("================================");

        System.out.println("Resolviendo la maquina...");
        delay(1500);

        int[][] actions = contest.solve(5);

        assertNotNull(actions);
        assertTrue(contest.isSolved());
        assertTrue(actions.length < 10000);

        System.out.println("Maquina resuelta.");
        System.out.println(
            "Cantidad de acciones: " + actions.length
        );

        delay(1500);

        contest.simulate(5);

        System.out.println("La maquina esta visible.");
        System.out.println("Resultado final:");
        System.out.println("Todas las ruedas tienen el mismo simbolo.");

        delay(6000);
    }

    /**
     * Acceptance test with 50 wheels.
     */
    @Test
    public void acceptanceTestFiftyWheels()
    {
        SlotMachineContest contest =
            new SlotMachineContest();

        System.out.println("================================");
        System.out.println("PRUEBA DE ACEPTACION - 50 RUEDAS");
        System.out.println("================================");

        System.out.println("Resolviendo la maquina...");
        delay(1500);

        int[][] actions = contest.solve(50);

        assertNotNull(actions);
        assertTrue(contest.isSolved());
        assertTrue(actions.length < 10000);

        System.out.println("Maquina resuelta.");
        System.out.println(
            "Cantidad de acciones: " + actions.length
        );

        delay(1500);

        contest.simulate(50);

        System.out.println("La maquina esta visible.");
        System.out.println("Resultado final:");
        System.out.println("Todas las ruedas tienen el mismo simbolo.");

        delay(8000);
    }

    /**
     * Creates a delay so the result can be seen.
     *
     * @param milliseconds waiting time
     */
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

        /**
         * Shows the initial machine and then the solved machine.
         */
        @Test
        public void acceptanceTestInitialAndFinalConfiguration()
        {
            System.out.println("--------------------------------------");
            System.out.println("PRUEBA DE ACEPTACION");
            System.out.println("CONFIGURACION INICIAL Y FINAL");
            System.out.println("--------------------------------------");
    
            SlotMachine initialMachine =
                new SlotMachine(5);
    
            System.out.println("Configuracion inicial de la maquina.");
 
            initialMachine.makeVisible();
    
            delay(5000);
    

            initialMachine.makeInvisible();
    
            System.out.println("Iniciando solucion...");
            delay(1500);
    
    
            SlotMachineContest contest =
                new SlotMachineContest();
    
            int[][] actions = contest.solve(5);
    
            assertNotNull(actions);
            assertTrue(contest.isSolved());
            assertTrue(actions.length < 10000);
    
            System.out.println("Problema solucionado.");
            System.out.println(
                "Numero de acciones: " + actions.length
            );
    
            delay(1500);
    
            contest.simulate(5);
    
            System.out.println("--------------------------------------");
            System.out.println("CONFIGURACION FINAL");
            System.out.println("Todas las ruedas tienen el mismo simbolo.");
            System.out.println("---------------------------------------");
    
            delay(7000);
        }
}



