package salarios;

/**
 * Comprueba que el ordenamiento no rompa la relación entre los atributos de cada empleado.
 *
 * Idea: ANTES de ordenar se toma una "ficha" de texto de cada empleado con sus cuatro
 * atributos juntos ("id|nombre|cargo|salario"). DESPUÉS de ordenar se toman de nuevo
 * las fichas y se comprueba que sean exactamente las mismas, solo que en otro orden:
 *   - misma cantidad de empleados,
 *   - cada ficha original aparece una vez en el resultado (nadie se perdió ni se duplicó),
 *   - y ningún salario quedó pegado al nombre o al cargo de otra persona.
 */
public final class VerificadorIntegridad {

    private VerificadorIntegridad() {
    }

    /** Ficha con los cuatro atributos de un empleado. */
    public static String ficha(Empleado e) {
        return e.getIdentificacion() + "|" + e.getNombre() + "|" + e.getCargo() + "|" + e.getSalario();
    }

    /** Toma la ficha de cada empleado del arreglo (se usa antes de ordenar). */
    public static String[] tomarFichas(Empleado[] empleados) {
        String[] fichas = new String[empleados.length];
        for (int i = 0; i < empleados.length; i++) {
            fichas[i] = ficha(empleados[i]);
        }
        return fichas;
    }

    /**
     * true si el arreglo ordenado contiene exactamente las mismas fichas que había antes.
     * Para cada ficha original se busca (búsqueda lineal) una ficha igual en el resultado
     * que no se haya usado todavía. Complejidad O(n²): suficiente para una verificación.
     */
    public static boolean conservaRelacion(String[] fichasAntes, Empleado[] despues) {
        if (fichasAntes.length != despues.length) {
            return false;
        }
        String[] fichasDespues = tomarFichas(despues);
        boolean[] usada = new boolean[fichasDespues.length];
        for (int i = 0; i < fichasAntes.length; i++) {
            boolean encontrada = false;
            for (int j = 0; j < fichasDespues.length && !encontrada; j++) {
                if (!usada[j] && fichasAntes[i].equals(fichasDespues[j])) {
                    usada[j] = true;
                    encontrada = true;
                }
            }
            if (!encontrada) {
                return false;   // esta ficha se perdió o se alteró
            }
        }
        return true;
    }
}
