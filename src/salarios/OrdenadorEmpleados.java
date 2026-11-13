package salarios;

import java.text.Normalizer;

/**
 * Ordena un arreglo de objetos Empleado con Merge Sort (implementación manual).
 *
 * Criterio de ordenamiento:
 *   1. Salario descendente (primero el que más gana).
 *   2. Si los salarios son iguales, nombre ascendente (A - Z).
 */
public class OrdenadorEmpleados {

    /**
     * Criterio compuesto. Retorna:
     *   negativo si x debe ir ANTES que y,
     *   positivo si x debe ir DESPUÉS que y,
     *   0 si para el criterio son equivalentes.
     */
    public static int criterio(Empleado x, Empleado y) {
        // 1) Salario descendente: el de MAYOR salario va primero
        if (x.getSalario() != y.getSalario()) {
            return x.getSalario() > y.getSalario() ? -1 : 1;
        }
        // 2) Empate en salario: nombre ascendente
        return compararNombres(x.getNombre(), y.getNombre());
    }

    /**
     * Compara dos nombres alfabéticamente sin distinguir mayúsculas ni tildes,
     * para que "Álvaro" quede junto a "Andrés" y no después de la "Z".
     */
    public static int compararNombres(String a, String b) {
        return normalizar(a).compareTo(normalizar(b));
    }

    /** "Álvaro Núñez" -> "alvaro nunez" */
    static String normalizar(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");   // elimina las marcas de acento
        return sinTildes.toLowerCase();
    }
}
