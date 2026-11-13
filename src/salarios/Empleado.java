package salarios;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Empleado de la empresa. Cada objeto agrupa sus cuatro atributos, de modo que al
 * mover el objeto dentro del arreglo se mueven todos sus datos juntos.
 */
public class Empleado {

    private static final NumberFormat PESOS = NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CO"));

    private final String identificacion;
    private final String nombre;
    private final String cargo;
    private final long salario;   // en pesos colombianos

    public Empleado(String identificacion, String nombre, String cargo, long salario) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La identificación es obligatoria");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new IllegalArgumentException("El cargo es obligatorio");
        }
        if (salario < 0) {
            throw new IllegalArgumentException("El salario no puede ser negativo: " + salario);
        }
        this.identificacion = identificacion.trim();
        this.nombre = nombre.trim();
        this.cargo = cargo.trim();
        this.salario = salario;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public long getSalario() {
        return salario;
    }

    /** Salario con separador de miles, por ejemplo $ 4.200.000 */
    public String getSalarioFormateado() {
        return "$ " + PESOS.format(salario);
    }

    @Override
    public String toString() {
        return identificacion + " | " + nombre + " | " + cargo + " | " + getSalarioFormateado();
    }
}
