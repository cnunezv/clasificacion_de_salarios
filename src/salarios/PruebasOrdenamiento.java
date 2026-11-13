package salarios;

/**
 * Casos de prueba del ordenamiento, sin librerías externas.
 * Cada prueba imprime [OK] o [FALLA]; al final se muestra el resumen.
 *
 * Se puede ejecutar sola:  java -cp out salarios.PruebasOrdenamiento
 * o desde la opción 6 del menú principal.
 */
public class PruebasOrdenamiento {

    private static int aprobadas;
    private static int fallidas;

    public static void main(String[] args) {
        boolean todoBien = ejecutarTodas();
        System.exit(todoBien ? 0 : 1);
    }

    /** Ejecuta todas las pruebas y retorna true si todas pasaron. */
    public static boolean ejecutarTodas() {
        aprobadas = 0;
        fallidas = 0;
        System.out.println("============ PRUEBAS DEL ORDENAMIENTO ============");

        // 1. Conjunto principal: orden completo esperado
        Empleado[] p = DatosEjemplo.empleadosPrincipal();
        String[] fichasAntes = VerificadorIntegridad.tomarFichas(p);
        OrdenadorEmpleados o = new OrdenadorEmpleados();
        o.ordenar(p);
        verificar("Principal: orden esperado (salario desc, nombre asc)",
                nombresSon(p, "Álvaro Núñez", "Mariana Torres", "Camila Herrera", "Daniela Gómez",
                        "Andrés Salgado", "Beatriz Castro", "Valentina Ríos", "Sebastián Mejía",
                        "Jorge Pérez", "Luis Martínez"));
        verificar("Principal: estaOrdenado() es true", OrdenadorEmpleados.estaOrdenado(p));
        verificar("Principal: cada empleado conserva sus 4 atributos",
                VerificadorIntegridad.conservaRelacion(fichasAntes, p));
        verificar("Principal: el salario $ 12.000.000 sigue siendo de Álvaro, Gerente general",
                p[0].getIdentificacion().equals("73001928") && p[0].getCargo().equals("Gerente general")
                        && p[0].getSalario() == 12_000_000);
        verificar("Principal: 10 elementos -> 2n - 1 = 19 llamadas recursivas",
                o.getLlamadasRecursivas() == 19);
        verificar("Principal: comparaciones <= n·log2(n) ≈ 33,2 (fueron " + o.getComparaciones() + ")",
                o.getComparaciones() <= 33);

        // 2. Segundo caso, diferente al principal
        Empleado[] alt = DatosEjemplo.empleadosAlternativa();
        new OrdenadorEmpleados().ordenar(alt);
        verificar("Alternativo: Ernesto, Iván, Natalia, Paola, Ana, Óscar",
                nombresSon(alt, "Ernesto Lara", "Iván Cárdenas", "Natalia Ortiz", "Paola Villa",
                        "Ana Benítez", "Óscar Bello"));

        // 3. Desempate por nombre: tildes y mayúsculas
        Empleado[] tildes = {
            new Empleado("1", "Andrés", "X", 3_000_000),
            new Empleado("2", "Álvaro", "X", 3_000_000),
            new Empleado("3", "beatriz", "X", 3_000_000)
        };
        new OrdenadorEmpleados().ordenar(tildes);
        verificar("Empate: 'Álvaro' va antes de 'Andrés' (se ignoran tildes)",
                nombresSon(tildes, "Álvaro", "Andrés", "beatriz"));

        // 4. Estabilidad: mismo salario y mismo nombre -> conserva el orden original
        Empleado[] estable = {
            new Empleado("A-1", "Laura Díaz", "X", 2_000_000),
            new Empleado("B-2", "Pedro Ruiz", "X", 5_000_000),
            new Empleado("A-2", "Laura Díaz", "X", 2_000_000),
            new Empleado("A-3", "Laura Díaz", "X", 2_000_000)
        };
        new OrdenadorEmpleados().ordenar(estable);
        verificar("Estable: las tres 'Laura Díaz' conservan su orden A-1, A-2, A-3",
                estable[1].getIdentificacion().equals("A-1") && estable[2].getIdentificacion().equals("A-2")
                        && estable[3].getIdentificacion().equals("A-3"));

        // 5. Distribuciones iniciales
        Empleado[] yaOrdenado = DatosEjemplo.empleadosPrincipal();
        new OrdenadorEmpleados().ordenar(yaOrdenado);
        String[] fichasOrdenadas = VerificadorIntegridad.tomarFichas(yaOrdenado);
        new OrdenadorEmpleados().ordenar(yaOrdenado);
        verificar("Arreglo ya ordenado: queda exactamente igual",
                mismasFichasEnOrden(fichasOrdenadas, yaOrdenado));

        Empleado[] inverso = new Empleado[yaOrdenado.length];
        for (int i = 0; i < inverso.length; i++) {
            inverso[i] = yaOrdenado[yaOrdenado.length - 1 - i];
        }
        new OrdenadorEmpleados().ordenar(inverso);
        verificar("Arreglo en orden inverso: queda ordenado",
                mismasFichasEnOrden(fichasOrdenadas, inverso));

        Empleado[] iguales = {
            new Empleado("1", "Carlos", "X", 1_500_000),
            new Empleado("2", "Ana", "X", 1_500_000),
            new Empleado("3", "Bruno", "X", 1_500_000)
        };
        new OrdenadorEmpleados().ordenar(iguales);
        verificar("Todos con el mismo salario: quedan en orden alfabético",
                nombresSon(iguales, "Ana", "Bruno", "Carlos"));

        // 6. Casos borde
        Empleado[] vacio = new Empleado[0];
        new OrdenadorEmpleados().ordenar(vacio);
        verificar("Arreglo vacío: no falla", vacio.length == 0);

        Empleado[] uno = {new Empleado("1", "Único", "X", 1_000_000)};
        new OrdenadorEmpleados().ordenar(uno);
        verificar("Un solo empleado: queda igual", uno[0].getNombre().equals("Único"));

        verificar("Arreglo null -> IllegalArgumentException",
                lanzaExcepcion(() -> new OrdenadorEmpleados().ordenar(null)));
        verificar("Empleado null dentro del arreglo -> IllegalArgumentException",
                lanzaExcepcion(() -> new OrdenadorEmpleados().ordenar(
                        new Empleado[] {new Empleado("1", "A", "X", 1), null})));
        verificar("Salario negativo -> IllegalArgumentException",
                lanzaExcepcion(() -> new Empleado("1", "A", "X", -5)));

        // 7. El verificador detecta cuando se rompe la relación entre atributos
        Empleado[] original = {
            new Empleado("1", "Ana", "Gerente", 9_000_000),
            new Empleado("2", "Luis", "Auxiliar", 1_000_000)
        };
        String[] fichas = VerificadorIntegridad.tomarFichas(original);
        Empleado[] salariosCruzados = {   // como si se hubieran ordenado SOLO los salarios
            new Empleado("1", "Ana", "Gerente", 1_000_000),
            new Empleado("2", "Luis", "Auxiliar", 9_000_000)
        };
        verificar("Verificador detecta salarios intercambiados entre empleados",
                !VerificadorIntegridad.conservaRelacion(fichas, salariosCruzados));

        System.out.println("--------------------------------------------------");
        System.out.println("Resultado: " + aprobadas + " pruebas OK, " + fallidas + " fallidas.");
        return fallidas == 0;
    }

    private static boolean nombresSon(Empleado[] a, String... esperados) {
        if (a.length != esperados.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (!a[i].getNombre().equals(esperados[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean mismasFichasEnOrden(String[] fichas, Empleado[] a) {
        if (fichas.length != a.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (!fichas[i].equals(VerificadorIntegridad.ficha(a[i]))) {
                return false;
            }
        }
        return true;
    }

    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            aprobadas++;
            System.out.println("[OK]    " + descripcion);
        } else {
            fallidas++;
            System.out.println("[FALLA] " + descripcion);
        }
    }

    private static boolean lanzaExcepcion(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
