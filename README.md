
# Desafío 3 DSM – To-Do List App

**Autores:**  
- Alvarenga Claros Carlos Alexander AC211104 
- Moreno Lozano Cruz Enrique ML210800

---

## 📋 Descripción

Esta aplicación móvil en **Android Studio (Kotlin)** permite a los usuarios:

1. **Registrarse** e **iniciar sesión** mediante **Firebase Authentication**.  
2. Gestionar su propia **lista de tareas** (To-Do List) usando una **API REST externa**.  
3. Realizar operaciones **CRUD**:
   - **GET** `/to-do` : Obtener todas las tareas, filtradas por usuario (`createdBy`).  
   - **GET** `/to-do/:id` : Obtener los detalles de una tarea.  
   - **POST** `/to-do` : Crear una nueva tarea.  
   - **PUT** `/to-do/:id` : Modificar una tarea existente.  
   - **DELETE** `/to-do/:id` : Eliminar una tarea.  

Además, cada ítem de la lista cuenta con:
- **Checkbox** para marcarla como completada (cambia el campo `done` y el color de texto).  
- **Botones** de editar y eliminar directamente en la lista.  

---

## 🚀 Tecnologías y librerías

- **Lenguaje:** Kotlin  
- **IDE:** Android Studio  
- **Autenticación:** Firebase Authentication  
- **HTTP client:** OkHttp  
- **UI:** RecyclerView, CardView, Material Components  
- **API REST:**  
  `https://68163b7232debfe95dbdd500.mockapi.io/academic/v1/to-do`

