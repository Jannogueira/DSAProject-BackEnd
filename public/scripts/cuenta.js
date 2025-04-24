$(document).ready(function () {
    $('#statBtn').click(function () {
        contenido.innerHTML = "<h2>Estadísticas del usuario</h2><p>Datos cargados del backend...</p>";
    });
    $('#accBtn').click(function () {
        contenido.innerHTML = "<h2>Información de la cuenta</h2><p>Nombre de usuario, correo, etc...</p>"
    });
});