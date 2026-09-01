\# InventoryApp



Aplicación Android nativa para la gestión de inventario desarrollada con Kotlin y Jetpack Compose.



La aplicación consume una API REST desarrollada con ASP.NET Core y permite autenticarse, administrar productos y gestionar inventarios por proveedor y lote.



\## Tecnologías



\- Kotlin

\- Jetpack Compose

\- Material 3

\- MVVM

\- StateFlow

\- Kotlin Coroutines

\- Retrofit

\- OkHttp

\- Gson

\- JWT Authentication



\## Arquitectura



La aplicación utiliza una arquitectura basada en MVVM:



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



\## Estructura



```text

data/

├── local/

├── remote/

│   ├── api/

│   └── dto/

└── repository/



ui/

├── login/

├── productos/

├── inventario/

└── theme/

