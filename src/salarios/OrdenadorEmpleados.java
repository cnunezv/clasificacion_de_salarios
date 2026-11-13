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

    private long comparaciones;
    private int llamadasRecursivas;
    private int mezclas;
    private long copiasAlBuffer;

    public long getComparaciones() {
        return comparaciones;
    }

    public int getLlamadasRecursivas() {
        return llamadasRecursivas;
    }

    public int getMezclas() {
        return mezclas;
    }

    public long getCopiasAlBuffer() {
        return copiasAlBuffer;
    }

    /**
     * Ordena el arreglo "en su lugar": al terminar, el mismo arreglo queda ordenado.
     * Se ordenan las REFERENCIAS a los objetos, así que cada empleado conserva
     * juntos su identificación, nombre, cargo y salario.
     *
     * Complejidad: O(n log n) en el mejor, promedio y peor caso.
     * Memoria auxiliar: O(n) por el arreglo buffer + O(log n) de la pila de recursión.
     */
    public void ordenar(Empleado[] empleados) {
        comparaciones = 0;
        llamadasRecursivas = 0;
        mezclas = 0;
        copiasAlBuffer = 0;
        if (empleados == null) {
            throw new IllegalArgumentException("El arreglo de empleados no puede ser null");
        }
        for (int i = 0; i < empleados.length; i++) {
            if (empleados[i] == null) {
                throw new IllegalArgumentException("El empleado en la posición " + i + " es null");
            }
        }
        if (empleados.length < 2) {
            return;   // 0 o 1 elemento: ya está ordenado
        }
        Empleado[] buffer = new Empleado[empleados.length];   // memoria auxiliar para mezclar
        mergeSort(empleados, buffer, 0, empleados.length);
    }

    /** Ordena el tramo [inicio, fin) — el índice fin NO se incluye. */
    private void mergeSort(Empleado[] a, Empleado[] buffer, int inicio, int fin) {
        llamadasRecursivas++;
        if (fin - inicio < 2) {
            return;   // caso base: un solo elemento
        }
        int medio = (inicio + fin) / 2;
        mergeSort(a, buffer, inicio, medio);   // 1) ordenar mitad izquierda
        mergeSort(a, buffer, medio, fin);      // 2) ordenar mitad derecha
        mezclar(a, buffer, inicio, medio, fin); // 3) mezclar las dos mitades
    }

    /**
     * Mezcla dos tramos ya ordenados: [inicio, medio) y [medio, fin).
     * En cada paso compara el primer elemento pendiente de cada mitad y copia al
     * buffer el que debe ir antes. Si son equivalentes toma el de la IZQUIERDA
     * (criterio <= 0): eso hace que Merge Sort sea ESTABLE.
     */
    private void mezclar(Empleado[] a, Empleado[] buffer, int inicio, int medio, int fin) {
        mezclas++;
        int i = inicio;   // recorre la mitad izquierda
        int j = medio;    // recorre la mitad derecha
        int k = inicio;   // posición de escritura en el buffer
        while (i < medio && j < fin) {
            if (comparar(a[i], a[j]) <= 0) {
                buffer[k++] = a[i++];
            } else {
                buffer[k++] = a[j++];
            }
            copiasAlBuffer++;
        }
        while (i < medio) {   // sobrantes de la izquierda
            buffer[k++] = a[i++];
            copiasAlBuffer++;
        }
        while (j < fin) {     // sobrantes de la derecha
            buffer[k++] = a[j++];
            copiasAlBuffer++;
        }
        // copiar el tramo mezclado de vuelta al arreglo original
        for (int p = inicio; p < fin; p++) {
            a[p] = buffer[p];
        }
    }

    /** Aplica el criterio y cuenta la comparación. */
    private int comparar(Empleado x, Empleado y) {
        comparaciones++;
        return criterio(x, y);
    }

    /** Verifica que cada empleado vaya antes o igual que el siguiente según el criterio. */
    public static boolean estaOrdenado(Empleado[] a) {
        for (int i = 1; i < a.length; i++) {
            if (criterio(a[i - 1], a[i]) > 0) {
                return false;
            }
        }
        return true;
    }

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
