# InventoryApp

Aplicación Android nativa para la gestión de inventario desarrollada con **Kotlin** y **Jetpack Compose**.

La aplicación consume una API REST desarrollada con **ASP.NET Core 8** y permite autenticarse, administrar productos y gestionar inventarios por proveedor y lote.

---

## Tecnologías

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- StateFlow
- Kotlin Coroutines
- Retrofit
- OkHttp
- Gson
- JWT Authentication
- Android SDK
- Gradle

---

## Arquitectura

La aplicación utiliza una arquitectura basada en **MVVM**:

```text
Compose Screen
      ↓
ViewModel
      ↓
Repository
      ↓
ApiService
      ↓
Retrofit / OkHttp
      ↓
ASP.NET Core API
      ↓
SQL Server
```

### Screens

Representan la interfaz construida con Jetpack Compose.

Las pantallas principales son:

- Login
- Productos
- Inventario

### ViewModels

Manejan:

- Estado de la interfaz
- Validaciones
- Llamadas asíncronas
- Manejo de errores
- Comunicación con los repositories

Los estados se exponen mediante `StateFlow`.

### Repositories

Separan la lógica de acceso a datos del ViewModel.

Los repositories utilizados son:

- `AuthRepository`
- `ProductoRepository`
- `ProveedorRepository`
- `InventarioRepository`

### ApiService

Define los endpoints REST utilizados por Retrofit.

### DTOs

Representan los datos enviados y recibidos desde la API.

---

## Estructura del proyecto

```text
InventoryApp/
│
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/jorge/inventoryapp/
│       │   │
│       │   ├── data/
│       │   │   ├── local/
│       │   │   │   └── SessionManager.kt
│       │   │   │
│       │   │   ├── remote/
│       │   │   │   ├── api/
│       │   │   │   │   ├── ApiService.kt
│       │   │   │   │   ├── RetrofitClient.kt
│       │   │   │   │   ├── AuthInterceptor.kt
│       │   │   │   │   └── UnauthorizedInterceptor.kt
│       │   │   │   │
│       │   │   │   └── dto/
│       │   │   │
│       │   │   └── repository/
│       │   │
│       │   └── ui/
│       │       ├── login/
│       │       ├── productos/
│       │       ├── inventario/
│       │       └── theme/
│       │
│       └── debug/
│           └── AndroidManifest.xml
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── setup-api.ps1
└── README.md
```

---

## Funcionalidades

### Autenticación

- Inicio de sesión
- Validación de credenciales mediante la API
- Recepción de JWT
- Almacenamiento de sesión en memoria
- Envío automático del JWT
- Detección automática de respuestas `401 Unauthorized`
- Cierre de sesión
- Limpieza de formularios al cerrar sesión

### Productos

- Listar productos
- Crear productos
- Editar productos
- Activar o desactivar productos
- Eliminar productos
- Confirmación antes de eliminar
- Protección cuando un producto posee inventario asociado

### Inventario

- Consultar inventario por producto
- Crear registros de inventario
- Editar inventario
- Eliminar inventario
- Seleccionar proveedor
- Gestionar número de lote
- Gestionar precio
- Gestionar stock

### Interfaz

- Estados de carga
- Estados vacíos
- Mensajes de error
- Mensajes de éxito
- Formularios validados
- Confirmaciones de eliminación
- Diseño adaptable
- Soporte para orientación vertical y horizontal

---

## Modelo de inventario

La aplicación representa un inventario donde un producto puede tener distintos registros dependiendo del proveedor y del lote.

```text
Producto
│
├── Proveedor A
│   └── Lote 1
│       ├── Precio
│       └── Stock
│
└── Proveedor B
    └── Lote 2
        ├── Precio
        └── Stock
```

Ejemplo:

```text
Monitor Samsung 50 pulgadas 4K
│
├── TecnoImport
│   └── LOT-001
│       Precio: $250.00
│       Stock: 10
│
└── ElectroDistribuciones
    └── LOT-002
        Precio: $300.00
        Stock: 20
```

Esto permite que el mismo producto tenga precios y cantidades diferentes según el proveedor o lote.

---

## Flujo de autenticación

El flujo de login es:

```text
LoginScreen
    ↓
LoginViewModel
    ↓
AuthRepository
    ↓
ApiService
    ↓
POST /api/Auth/login
    ↓
ASP.NET Core API
    ↓
JWT
    ↓
SessionManager
```

Después del login, el token se almacena en `SessionManager`.

---

## JWT

El token JWT se agrega automáticamente a las solicitudes protegidas.

`AuthInterceptor` obtiene el token desde `SessionManager` y agrega:

```http
Authorization: Bearer <TOKEN>
```

De esta forma no es necesario agregar manualmente el header en cada llamada de Retrofit.

---

## Manejo de sesión expirada

La aplicación utiliza `UnauthorizedInterceptor`.

Cuando la API responde:

```http
401 Unauthorized
```

se ejecuta:

```text
401
 ↓
UnauthorizedInterceptor
 ↓
SessionManager.clearToken()
 ↓
token = null
 ↓
MainActivity detecta el cambio
 ↓
LoginScreen
```

Esto permite regresar automáticamente al login cuando la sesión deja de ser válida.

---

## SessionManager

La sesión se mantiene en memoria mediante:

```text
StateFlow<String?>
```

El JWT es la fuente de verdad para determinar si el usuario se encuentra autenticado.

Actualmente la sesión no se persiste al cerrar completamente la aplicación.

Para una aplicación productiva podría utilizarse almacenamiento persistente y seguro según los requerimientos del proyecto.

---

## Retrofit

Retrofit se utiliza para consumir la API REST.

Los principales endpoints utilizados son:

### Autenticación

```http
POST /api/Auth/login
```

### Productos

```http
GET    /api/Productos
POST   /api/Productos
PUT    /api/Productos/{id}
DELETE /api/Productos/{id}
```

### Proveedores

```http
GET /api/Proveedores
```

### Inventario

```http
GET    /api/Inventarios/producto/{productoId}
POST   /api/Inventarios
PUT    /api/Inventarios/{id}
DELETE /api/Inventarios/{id}
```

---

## OkHttp

OkHttp utiliza tres componentes principales:

```text
Request
   ↓
AuthInterceptor
   ↓
Authorization: Bearer JWT
   ↓
API
   ↓
UnauthorizedInterceptor
   ↓
¿401?
   ├── No → continuar
   └── Sí → cerrar sesión
```

También se utiliza `HttpLoggingInterceptor`.

El nivel configurado es:

```text
BASIC
```

para evitar registrar cuerpos de solicitudes que puedan contener información sensible.

---

## Manejo de errores

Los ViewModels manejan distintos tipos de errores.

Por ejemplo:

```text
HttpException
IOException
Exception
```

Se muestran mensajes amigables al usuario como:

```text
No se pudo conectar con el servidor.
La sesión no es válida.
El producto no existe.
Los datos enviados no son válidos.
```

También se utiliza `Log.e()` para registrar detalles técnicos en Logcat.

No se registran:

- Contraseñas
- Tokens JWT

---

## Validaciones

La aplicación realiza validaciones antes de enviar datos al backend.

### Login

Se valida que:

- Correo no esté vacío
- Contraseña no esté vacía

### Producto

Se valida que:

- Nombre sea obligatorio

### Inventario

Se valida que:

- Exista un proveedor seleccionado
- Número de lote sea obligatorio
- Precio sea numérico
- Precio sea mayor a cero
- Stock sea numérico
- Stock no sea negativo

Además, el backend vuelve a validar los datos recibidos.

---

## Diseño adaptable

La interfaz utiliza componentes flexibles de Jetpack Compose.

Entre ellos:

```kotlin
fillMaxWidth()
widthIn()
LazyColumn
verticalScroll()
imePadding()
```

Los formularios tienen límites máximos de ancho para evitar que se expandan excesivamente en pantallas grandes.

Ejemplo:

```kotlin
widthIn(max = 480.dp)
```

para Login.

Y:

```kotlin
widthIn(max = 800.dp)
```

para las pantallas principales.

También se utiliza scroll en formularios para mejorar el funcionamiento en orientación horizontal y cuando aparece el teclado.

---

## Estado de la interfaz

Los ViewModels exponen estados mediante `StateFlow`.

Ejemplo conceptual:

```text
ViewModel
   ↓
StateFlow<UiState>
   ↓
Compose
   ↓
collectAsStateWithLifecycle()
   ↓
Recomposición automática
```

Esto permite actualizar automáticamente la pantalla cuando cambian:

- Productos
- Inventarios
- Loading
- Errores
- Mensajes de éxito
- Sesión

---

## Coroutines

Las llamadas a la API se ejecutan mediante Kotlin Coroutines.

Los ViewModels utilizan:

```kotlin
viewModelScope.launch
```

y los endpoints Retrofit utilizan funciones:

```kotlin
suspend
```

Esto permite realizar operaciones de red sin bloquear la interfaz principal.

---

## Requisitos

Para ejecutar el proyecto se necesita:

- Android Studio
- Android SDK
- JDK compatible con Android Studio
- Emulador Android o dispositivo físico
- InventoryApi ejecutándose localmente

---

## Backend

La aplicación consume el backend:

**InventoryApi**

Repositorio:

https://github.com/jorgesanaguaray/InventoryApi

El backend utiliza:

- ASP.NET Core 8
- C#
- Entity Framework Core
- SQL Server
- JWT
- BCrypt
- Swagger
- Serilog

---

## Configuración local

Actualmente Retrofit utiliza:

```text
http://127.0.0.1:5133/
```

Durante el desarrollo se utiliza `adb reverse` para conectar el emulador con el backend ejecutándose en el PC.

---

## Conexión con la API usando adb reverse

El emulador Android posee su propio `localhost`.

Para redirigir el puerto del emulador al PC se utiliza:

```bash
adb reverse tcp:5133 tcp:5133
```

El proyecto incluye:

```text
setup-api.ps1
```

que ejecuta automáticamente esta configuración.

El flujo es:

```text
InventoryApp
      ↓
127.0.0.1:5133
      ↓
adb reverse
      ↓
localhost:5133 del PC
      ↓
InventoryApi
```

---

## Ejecutar setup-api.ps1

Primero iniciar el emulador Android.

Luego, desde PowerShell en la raíz del proyecto:

```powershell
.\setup-api.ps1
```

El script ejecuta:

```powershell
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"

& $adb reverse tcp:5133 tcp:5133
```

Para comprobar los puertos:

```powershell
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" reverse --list
```

Debería aparecer algo similar a:

```text
tcp:5133 tcp:5133
```

---

## Permiso de Internet

La aplicación incluye:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

en:

```text
app/src/main/AndroidManifest.xml
```

---

## HTTP en desarrollo

El backend local utiliza HTTP durante el desarrollo.

Por seguridad, el tráfico HTTP se permite únicamente en:

```text
app/src/debug/AndroidManifest.xml
```

mediante:

```xml
<application
    android:usesCleartextTraffic="true" />
```

Esto significa que la configuración de HTTP inseguro está limitada a la variante Debug.

En producción debería utilizarse HTTPS.

---

## Instalación

Clonar el repositorio:

```bash
git clone https://github.com/jorgesanaguaray/InventoryApp.git
```

Entrar al proyecto:

```bash
cd InventoryApp
```

Abrir el proyecto en Android Studio.

Esperar a que Gradle sincronice las dependencias.

---

## Ejecutar la aplicación

### 1. Ejecutar el backend

Ejecutar primero `InventoryApi`.

La API debe estar disponible en:

```text
http://localhost:5133
```

### 2. Iniciar el emulador

Iniciar un dispositivo virtual desde Android Studio.

### 3. Configurar adb reverse

Desde la raíz de InventoryApp:

```powershell
.\setup-api.ps1
```

### 4. Ejecutar InventoryApp

Presionar **Run** desde Android Studio.

### 5. Iniciar sesión

Utilizar el usuario configurado mediante `SeedUser` en los User Secrets de InventoryApi.

---

## Dependencias principales

Retrofit:

```kotlin
implementation("com.squareup.retrofit2:retrofit:2.11.0")
```

Gson:

```kotlin
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
```

Logging Interceptor:

```kotlin
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
```

Jetpack Compose y Material 3 son administrados mediante el Compose BOM.

---

## Flujo general de la aplicación

```text
Usuario
   ↓
LoginScreen
   ↓
LoginViewModel
   ↓
AuthRepository
   ↓
Retrofit
   ↓
InventoryApi
   ↓
JWT
   ↓
SessionManager
   ↓
ProductosScreen
   ↓
ProductosViewModel
   ↓
ProductoRepository
   ↓
InventoryApi
```

Para inventario:

```text
ProductosScreen
   ↓
Seleccionar Inventario
   ↓
InventarioScreen
   ↓
InventarioViewModel
   ↓
InventarioRepository
   ↓
InventoryApi
   ↓
SQL Server
```

---

## Seguridad

La aplicación considera las siguientes medidas:

- JWT para endpoints protegidos
- Token agregado mediante interceptor
- Cierre automático de sesión ante `401`
- Contraseña no almacenada por la aplicación
- JWT no registrado en Logcat
- Logging HTTP en nivel `BASIC`
- HTTP habilitado únicamente en Debug
- HTTPS esperado para producción

---

## Mejoras futuras

Algunas mejoras posibles para una versión de producción serían:

- Persistencia segura de sesión
- Navigation Compose
- Dependency Injection con Hilt
- Tests unitarios
- Tests de UI
- Manejo centralizado de errores HTTP
- Refresh Tokens
- Mejorar selector de proveedores
- Búsqueda y filtrado de productos
- Paginación para grandes volúmenes de datos

Estas mejoras no forman parte del alcance actual.

---

## API relacionada

Repositorio backend:

**InventoryApi**

https://github.com/jorgesanaguaray/InventoryApi

---

## Autor

**Jorge Sanaguaray**

Proyecto Android desarrollado como parte de un ejercicio técnico Full Stack utilizando Kotlin, Jetpack Compose y una API REST desarrollada con ASP.NET Core.