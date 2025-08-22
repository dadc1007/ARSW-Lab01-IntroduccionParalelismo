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

Con `start()` se crea un nuevo hilo de ejecución en la JVM, lo que permite que los tres hilos ejecuten sus ciclos de manera paralela. La salida puede aparecer intercalada, ya que depende del *scheduler* (el componente encargado de decidir qué proceso se ejecuta en la CPU y en qué momento). En cambio, con `run()` no se crea un nuevo hilo: simplemente se invoca el método como cualquier otro, por lo que los tres hilos se ejecutan de forma secuencial. En este caso, la salida será ordenada y sin intercalación.

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

