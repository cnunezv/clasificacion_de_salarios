# Clasificación de salarios - Merge Sort

Actividad de la Unidad 2 (Algoritmos de ordenamiento) de **Estructuras de Datos** — Ingeniería de Software, Universidad de Cartagena.
**Ejercicio asignado: 14. Clasificación de salarios.**

Autor: Carlos Andrés Núñez Vargas

## Descripción

Se crea un arreglo de objetos `Empleado` con **identificación, nombre, cargo y salario** y se ordena con **Merge Sort implementado manualmente** (sin `Arrays.sort`, `Collections.sort` ni `List.sort`):

1. Por **salario descendente** (primero quien más gana).
2. Si dos salarios son iguales, por **nombre ascendente** (A–Z, sin distinguir mayúsculas ni tildes).

El programa muestra los empleados antes y después del ordenamiento (de mayor a menor salario) y **verifica que ningún objeto pierda la relación entre sus atributos**.

| Requisito del ejercicio | Dónde se resuelve |
| --- | --- |
| Arreglo de objetos `Empleado` | `Empleado.java`, `DatosEjemplo.java` |
| Merge Sort por salario descendente | `OrdenadorEmpleados.ordenar(...)` |
| En salarios iguales, nombre ascendente | `OrdenadorEmpleados.criterio(...)` |
| Mostrar de mayor a menor salario | `Main.ordenarYMostrar(...)` |
| Verificar que no se pierda la relación entre atributos | `VerificadorIntegridad.conservaRelacion(...)` |

## Estructura del proyecto

```
src/salarios/
├── Empleado.java               Modelo: identificación, nombre, cargo y salario (con validaciones)
├── OrdenadorEmpleados.java     Merge Sort, criterio de comparación, contadores y traza
├── VerificadorIntegridad.java  Compara las "fichas" de cada empleado antes y después de ordenar
├── DatosEjemplo.java           Conjunto principal (10 empleados) y alternativo (6 empleados)
├── PruebasOrdenamiento.java    18 casos de prueba sin librerías externas
└── Main.java                   Menú de consola
```

## Cómo ejecutar

Requiere Java 11 o superior.

```bash
# Compilar
javac -encoding UTF-8 -d out src/salarios/*.java

# Ejecutar el programa (menú)
java -cp out salarios.Main

# Ejecutar solo las pruebas
java -cp out salarios.PruebasOrdenamiento
```

También se puede abrir la carpeta en NetBeans, IntelliJ o VS Code y ejecutar `Main.java`.

### Menú

```
1. Ver empleados (orden original)
2. Ordenar con Merge Sort y mostrar de mayor a menor salario
3. Ordenar mostrando la traza de divisiones y mezclas
4. Cambiar de conjunto de datos (principal / alternativo)
5. Agregar un empleado
6. Ejecutar casos de prueba
0. Salir
```

El ordenamiento se hace sobre una **copia** del arreglo, así que el orden original siempre puede volver a consultarse.

## Criterio de ordenamiento

```java
public static int criterio(Empleado x, Empleado y) {
    if (x.getSalario() != y.getSalario()) {
        return x.getSalario() > y.getSalario() ? -1 : 1;   // mayor salario primero
    }
    return compararNombres(x.getNombre(), y.getNombre());  // empate: nombre A-Z
}
```

- **Salario** es el criterio principal porque es lo que pide clasificar el ejercicio.
- **Nombre** desempata porque es el dato que un usuario reconoce y da un orden predecible.
- Los nombres se comparan sin tildes ni mayúsculas (`Normalizer`), para que `Álvaro` quede junto a `Andrés` y no después de la `Z`.
- La identificación y el cargo no participan en la comparación, pero viajan con el objeto.

## ¿Por qué no se pierde la relación entre atributos?

Merge Sort no mueve salarios sueltos: mueve **referencias a objetos `Empleado`** completos. Cada objeto guarda sus cuatro atributos juntos, así que al cambiar de posición se mueve todo el registro. Si se ordenara solo un arreglo de salarios por separado, los salarios quedarían asignados a otras personas.

`VerificadorIntegridad` lo comprueba: antes de ordenar guarda una ficha `id|nombre|cargo|salario` de cada empleado y después verifica que todas las fichas sigan existiendo exactamente una vez.

## Funcionamiento de Merge Sort

1. **Dividir:** el arreglo se parte en dos mitades hasta llegar a tramos de un solo elemento.
2. **Ordenar recursivamente** cada mitad.
3. **Mezclar:** se comparan los primeros elementos pendientes de cada mitad y se copia al buffer el que debe ir antes; al final el buffer se copia al arreglo.

Cuando dos empleados son equivalentes se toma el de la mitad izquierda (`criterio <= 0`), por eso el algoritmo es **estable**.

## Complejidad

| Caso | Tiempo | Motivo |
| --- | --- | --- |
| Mejor | O(n log n) | Siempre divide en log₂ n niveles y mezcla n elementos por nivel |
| Promedio | O(n log n) | Igual |
| Peor | O(n log n) | Igual: no depende del orden inicial |

- **Memoria auxiliar:** O(n) por el arreglo `buffer` + O(log n) por la pila de recursión.
- **Llamadas recursivas:** 2n − 1 (para 10 empleados, 19 llamadas).
- **Comparaciones:** como máximo ≈ n·log₂ n (para 10 empleados fueron 24).
