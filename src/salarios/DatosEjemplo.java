package salarios;

/**
 * Conjuntos de empleados para la demostración y las pruebas.
 * Los salarios están en pesos colombianos e incluyen valores repetidos a propósito
 * para probar el criterio de desempate por nombre.
 */
public final class DatosEjemplo {

    private DatosEjemplo() {
    }

    /** Ejemplo principal: 10 empleados con salarios repetidos. */
    public static Empleado[] empleadosPrincipal() {
        return new Empleado[] {
            new Empleado("1047382910", "Valentina Ríos",   "Analista de datos",     4_200_000),
            new Empleado("73145882",   "Jorge Pérez",      "Auxiliar contable",     1_800_000),
            new Empleado("1050963271", "Camila Herrera",   "Desarrolladora",        5_500_000),
            new Empleado("45528190",   "Andrés Salgado",   "Analista de datos",     4_200_000),
            new Empleado("1143367025", "Mariana Torres",   "Coordinadora de TI",    7_300_000),
            new Empleado("9876543",    "Luis Martínez",    "Auxiliar contable",     1_800_000),
            new Empleado("1001234567", "Daniela Gómez",    "Desarrolladora",        5_500_000),
            new Empleado("30812456",   "Beatriz Castro",   "Analista de datos",     4_200_000),
            new Empleado("1128456903", "Sebastián Mejía",  "Soporte técnico",       2_600_000),
            new Empleado("73001928",   "Álvaro Núñez",     "Gerente general",      12_000_000)
        };
    }

    /** Segundo caso, distinto al principal: 6 empleados de otra área. */
    public static Empleado[] empleadosAlternativa() {
        return new Empleado[] {
            new Empleado("22001", "Paola Villa",    "Enfermera",       3_100_000),
            new Empleado("22002", "Ernesto Lara",   "Médico general",  8_900_000),
            new Empleado("22003", "Óscar Bello",    "Camillero",       1_500_000),
            new Empleado("22004", "Natalia Ortiz",  "Enfermera",       3_100_000),
            new Empleado("22005", "Iván Cárdenas",  "Médico general",  8_900_000),
            new Empleado("22006", "Ana Benítez",    "Recepcionista",   1_500_000)
        };
    }

    /** Copia del arreglo (los objetos se comparten, solo se copia el arreglo de referencias). */
    public static Empleado[] copiar(Empleado[] original) {
        Empleado[] copia = new Empleado[original.length];
        for (int i = 0; i < original.length; i++) {
            copia[i] = original[i];
        }
        return copia;
    }
}
