function mostrarContenido(opcion) {
    const contenido = document.getElementById("contenido");
    if (opcion === "estadisticas") {
        // Aquí podrías hacer una llamada fetch para cargar datos reales
        contenido.innerHTML = "<h2>Estadísticas del usuario</h2><p>Datos cargados del backend...</p>";
    } else if (opcion === "cuenta") {
        contenido.innerHTML = "<h2>Información de la cuenta</h2><p>Nombre de usuario, correo, etc...</p>";
    }
}