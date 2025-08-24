# 🧵 Laboratorio 1 - Introducción al Paralelismo

**Escuela Colombiana de Ingeniería Julio Garavito**  
**Curso:** Arquitectura de Software (ARSW)

---

## 👥 Integrantes del grupo

- Vicente Garzón Ríos
- Daniel Alejandro Díaz Camelo

---

## 📌 Descripción

Este laboratorio introduce los conceptos básicos de **paralelismo** en java, ademas de la aplicacion a un caso concreto.

---

## 📂 Parte I - Introducción a Hilos en Java

Se implementó la clase `CountThread`, la cual recibe un intervalo `[A..B]` e imprime los números dentro de ese rango.  
En la clase `CountThreadsMain` se crean tres hilos con los intervalos:

- Hilo 1 → `[0..99]`
- Hilo 2 → `[99..199]`
- Hilo 3 → `[200..299]`

### 🔀 Diferencia entre `start()` y `run()`

Con `start()` se crea un nuevo hilo de ejecución en la JVM, lo que permite que los tres hilos ejecuten sus ciclos de manera paralela. La salida puede aparecer intercalada, ya que depende del _scheduler_ (el componente encargado de decidir qué proceso se ejecuta en la CPU y en qué momento). En cambio, con `run()` no se crea un nuevo hilo: simplemente se invoca el método como cualquier otro, por lo que los tres hilos se ejecutan de forma secuencial. En este caso, la salida será ordenada y sin intercalación.

---

## 📂 Parte II - Ejercicio Black List Search

Actualmente se tienen **N hilos** encargados de buscar direcciones IP en diferentes listas negras.  
Sin embargo, la estrategia presenta una **ineficiencia**: aun cuando ya se ha alcanzado el **mínimo de ocurrencias** necesarias para catalogar una dirección como maliciosa, los hilos continúan realizando búsquedas hasta finalizar su recorrido completo.

### ✅ Propuesta de mejora

Una alternativa para optimizar este proceso es implementar un **estado global compartido** entre los hilos.  
De esta manera, cuando alguno de ellos detecte que se ha alcanzado el **umbral requerido**, los demás puedan **detener inmediatamente sus búsquedas**, reduciendo así el número de consultas innecesarias.

### ⚠️ Nuevo problema

No obstante, esta solución introduce un nuevo **problema de sincronización**.  
Al existir una **variable compartida**, se debe garantizar que tanto su **lectura** como su **escritura** se realicen de forma **segura en un entorno concurrente**, evitando **inconsistencias** o **condiciones de carrera**.

---

## 📂 Parte III - Evaluación de Desempeño

### ⚙️ Configuración del experimento

Se realizaron pruebas bajo las siguientes configuraciones:

1. **Un solo hilo.**
2. **Tantos hilos como núcleos disponibles en la máquina** (obtenidos mediante `Runtime.getRuntime().availableProcessors()`).
3. **El doble de los núcleos disponibles.**
4. **50 hilos.**
5. **100 hilos.**

Durante la ejecución, se utilizó **jVisualVM** para observar el consumo de CPU y memoria en cada escenario.

---

### 📊 Resultados obtenidos

| Número de hilos  | Tiempo de ejecución (s) |
| ---------------- | ----------------------- |
| 1                | 118.82                  |
| 12 (núcleos)     | 9.83                    |
| 24 (2 × núcleos) | 4.84                    |
| 50               | 2.29                    |
| 100              | 1.18                    |

---

### 📈 Gráfica – Tiempo de ejecución vs. Número de hilos

![![Gráfica tiempo vs hilos](./src/assets/tiempo_vs_hilos.png.)](src/assets/tiempo_vs_hilos.png)

> La gráfica evidencia una fuerte reducción en el tiempo a medida que aumenta el número de hilos, aunque con ganancias decrecientes después de cierto punto.

---

### 🔎 Análisis e hipótesis

1. **1 hilo (118.8 s):** El procesamiento es secuencial, desaprovechando los múltiples núcleos. El CPU se mantiene muy por debajo de su capacidad.

2. **12 hilos (9.83 s):** El mejor salto en rendimiento. Cada núcleo ejecuta un hilo en paralelo, lo que maximiza la utilización del procesador.

3. **24 hilos (4.84 s):** Mejora adicional gracias a la concurrencia (_time-slicing_), aunque ya no es un paralelismo “real”. El overhead de planificación empieza a ser notorio, pero sigue siendo beneficioso.

4. **50 hilos (2.29 s):** Se obtienen menores tiempos, desde este punto el jVisualVM dejo de ser capaz de mostrar un grafico adecuado ya que no lo soportaba.

5. **100 hilos (1.18 s):** Es el tiempo mínimo registrado, aunque los beneficios adicionales son cada vez menores.

---

### 🧠 Hipótesis general

- **Escalabilidad hasta el número de núcleos:** El mejor desempeño relativo se obtiene cuando el número de hilos coincide con los núcleos físicos disponibles.
- **Más hilos ≠ siempre mejor:** Al aumentar los hilos más allá de los núcleos, se aprovecha la concurrencia, pero con un costo en overhead (planificación, cambios de contexto, mayor consumo de memoria).
- **Punto de saturación:** Existe un límite en el que añadir más hilos deja de ser útil, e incluso puede volverse contraproducente en escenarios con mayor carga de trabajo.

---

## 📂 Parte IV - Ejercicio Black List Search

### 1) ¿Por qué el mejor desempeño **no** se logra con **500 hilos**? ¿Cómo se compara con **200**?

De acuerdo con la ley de Amdahl, más hilos deberían implicar mayor aceleración, pero en la práctica no es así porque al poner 500 hilos en una sola máquina, el procesador se satura porque no tiene tantos núcleos. Eso obliga al sistema a estar cambiando constantemente entre hilos (cambio de contexto), y ese cambio gasta tiempo en lugar de ayudar. Por eso, muchas veces 200 hilos funcionan igual o mejor que 500, porque la sobrecarga de manejar tantos hilos anula cualquier beneficio.

### 2) ¿Hilos = núcleos vs. **2× núcleos**?

Si se usan exactamente tantos hilos como núcleos de la máquina, normalmente es lo más eficiente, porque cada núcleo ejecuta un hilo sin tener que turnarse. Si usas el doble de hilos que núcleos, puede que mejore un poco en casos donde los hilos pasan tiempo esperando. Ahí los hilos extra ayudan a que el procesador no se quede ocioso.  
Pero si el trabajo es muy de CPU, tener el doble de hilos solo añade más cambios de contexto y en realidad puede empeorar el rendimiento.

### 3) ¿Y si en vez de 100 hilos en una CPU usamos **1 hilo en cada una de 100 máquinas**?

Si pudieras repartir el trabajo en 100 máquinas distintas, cada una trabajando con un solo hilo, en principio podrías mejorar el rendimiento porque no sobrecargas una sola máquina. El problema es que entra en juego un nuevo costo: la comunicación entre máquinas. Hay que coordinar, mandar datos por red y combinar los resultados. Esa coordinación puede hacer que la ganancia real no sea tan buena como lo esperado.  
Entonces, si las máquinas casi no necesitan hablar entre sí, la mejora sería grande. Pero si necesitan sincronizarse mucho, el tiempo de comunicación puede comerse la ganancia.

### 4) ¿Usar **c** hilos en **100/c** máquinas (siendo **c** los núcleos de cada máquina) mejora?

Eso sería más equilibrado, porque cada máquina trabajaría con un número de hilos ajustado a sus núcleos. Así no se desperdician recursos con sobrecarga de hilos, y se aprovecha al máximo la capacidad de cada nodo.  
En ese escenario, el rendimiento sí mejoraría comparado con poner todos los hilos en una sola máquina, siempre y cuando la coordinación entre máquinas sea eficiente y no genere demasiado tiempo perdido en comunicación.
