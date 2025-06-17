
# Backend Java - TOCABOLAS API ⚪

Este es el backend RESTful desarrollado en Java con JAX-RS (Jersey) para servir como API del juego TOCABOLAS (inspirado en Suika Game). Gestiona usuarios, inventarios de objetos especiales, puntuaciones y dinero acumulado.

## Funcionalidad 🧠
- Registro y login con autenticación JWT.
- Gestión de inventario (bombas, oro, borrar bolas).
- Actualización de puntuación y dinero acumulado.
- Endpoints seguros protegidos por token.

## Endpoints principales
- `POST /login` - Autenticación de usuario.
- `GET /user/stats` - Devuelve dinero y récord.
- `GET /user/inventario` - Devuelve el inventario del usuario.
- `POST /user/actualizarPuntuacion` - Actualiza récord si es mayor.
- `POST /user/anadirDinero` - Suma dinero acumulado del juego.

## Tecnologías usadas
- Java 17+
- JAX-RS (Jersey)
- JWT para autenticación
- MariaDB / MySQL
- JDBC + ORM personalizado
- Maven


## Autor
Desarrollado por Laura, Victor, Jan y Omar. 

