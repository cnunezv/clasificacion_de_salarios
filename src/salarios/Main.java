package salarios;

import java.util.Scanner;

/**
 * Programa principal: menú de consola para clasificar empleados por salario con Merge Sort.
 */
public class Main {

    private static final Scanner ENTRADA = new Scanner(System.in);

    private static Empleado[] empleados = DatosEjemplo.empleadosPrincipal();
    private static String nombreConjunto = "principal";

    public static void main(String[] args) {
        System.out.println("Clasificación de salarios - Merge Sort");
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String opcion = leerLinea("Opción: ");
            if (opcion == null) {
                break;   // no hay más entrada
            }
            switch (opcion.trim()) {
                case "1":
                    imprimirTabla("Empleados en el orden original", empleados);
                    break;
                case "2":
                    ordenarYMostrar(false);
                    break;
                case "3":
                    ordenarYMostrar(true);
                    break;
                case "4":
                    cambiarConjunto();
                    break;
                case "5":
                    agregarEmpleado();
                    break;
                case "6":
                    PruebasOrdenamiento.ejecutarTodas();
                    break;
                case "0":
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        System.out.println("Fin del programa.");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("========= MENÚ (conjunto " + nombreConjunto + ", " + empleados.length + " empleados) =========");
        System.out.println("1. Ver empleados (orden original)");
        System.out.println("2. Ordenar con Merge Sort y mostrar de mayor a menor salario");
        System.out.println("3. Ordenar mostrando la traza de divisiones y mezclas");
        System.out.println("4. Cambiar de conjunto de datos (principal / alternativo)");
        System.out.println("5. Agregar un empleado");
        System.out.println("6. Ejecutar casos de prueba");
        System.out.println("0. Salir");
    }

    /** Ordena una COPIA del arreglo para que el orden original siga disponible. */
    static void ordenarYMostrar(boolean conTraza) {
        Empleado[] copia = DatosEjemplo.copiar(empleados);
        imprimirTabla("ANTES de ordenar", copia);

        String[] fichasAntes = VerificadorIntegridad.tomarFichas(copia);
        OrdenadorEmpleados ordenador = new OrdenadorEmpleados();
        ordenador.setMostrarTraza(conTraza);
        if (conTraza) {
            System.out.println("Traza de Merge Sort (salario en millones de pesos):");
        }
        ordenador.ordenar(copia);

        imprimirTabla("DESPUÉS de ordenar (salario descendente, empates por nombre A-Z)", copia);
        System.out.println("Comparaciones          : " + ordenador.getComparaciones());
        System.out.println("Llamadas recursivas    : " + ordenador.getLlamadasRecursivas());
        System.out.println("Mezclas realizadas     : " + ordenador.getMezclas());
        System.out.println("Copias al buffer       : " + ordenador.getCopiasAlBuffer());
        System.out.println("¿Quedó ordenado?       : " + (OrdenadorEmpleados.estaOrdenado(copia) ? "Sí" : "NO"));
        System.out.println("¿Se conservó la relación entre atributos? : "
                + (VerificadorIntegridad.conservaRelacion(fichasAntes, copia) ? "Sí" : "NO"));
        if (copia.length > 0) {
            System.out.println("Mayor salario : " + copia[0]);
            System.out.println("Menor salario : " + copia[copia.length - 1]);
        }
    }

    static void imprimirTabla(String titulo, Empleado[] arreglo) {
        System.out.println();
        System.out.println(titulo + ":");
        if (arreglo.length == 0) {
            System.out.println("(no hay empleados)");
            return;
        }
        String formato = "%-4s %-15s %-18s %-20s %13s%n";
        System.out.printf(formato, "#", "Identificación", "Nombre", "Cargo", "Salario");
        System.out.println("----------------------------------------------------------------------------");
        for (int i = 0; i < arreglo.length; i++) {
            Empleado e = arreglo[i];
            System.out.printf(formato, (i + 1) + ".", e.getIdentificacion(), recortar(e.getNombre(), 18),
                    recortar(e.getCargo(), 20), e.getSalarioFormateado());
        }
    }

    private static void cambiarConjunto() {
        if (nombreConjunto.equals("principal")) {
            empleados = DatosEjemplo.empleadosAlternativa();
            nombreConjunto = "alternativo";
        } else {
            empleados = DatosEjemplo.empleadosPrincipal();
            nombreConjunto = "principal";
        }
        imprimirTabla("Ahora se usa el conjunto " + nombreConjunto, empleados);
    }

    private static void agregarEmpleado() {
        String id = leerLinea("Identificación: ");
        String nombre = leerLinea("Nombre: ");
        String cargo = leerLinea("Cargo: ");
        String textoSalario = leerLinea("Salario (sin puntos, ej. 3500000): ");
        if (id == null || nombre == null || cargo == null || textoSalario == null) {
            return;
        }
        try {
            long salario = Long.parseLong(textoSalario.trim().replace(".", "").replace(",", ""));
            Empleado nuevo = new Empleado(id, nombre, cargo, salario);
            Empleado[] ampliado = new Empleado[empleados.length + 1];
            for (int i = 0; i < empleados.length; i++) {
                ampliado[i] = empleados[i];
            }
            ampliado[empleados.length] = nuevo;
            empleados = ampliado;
            System.out.println("Empleado agregado: " + nuevo);
        } catch (NumberFormatException e) {
            System.out.println("Salario no válido: escriba solo números.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo agregar: " + e.getMessage());
        }
    }

    private static String recortar(String texto, int max) {
        return texto.length() <= max ? texto : texto.substring(0, max - 1) + ".";
    }

    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        if (!ENTRADA.hasNextLine()) {
            return null;
        }
        return ENTRADA.nextLine();
    }
}
