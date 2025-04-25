$(document).ready(function () {
    const token = localStorage.getItem("token");
    const user = localStorage.getItem("user");

    // Mostrar el nombre de usuario y correo en la sección de ajustes
    $('#accBtn').click(function () {
        contenido.innerHTML = "<h2 class='text-center fuentetitulos'>Ajustes de cuenta</h2><br>" +
            "<p> ❑ <b>Nombre de usuario:</b> <strong title='Haz clic para editar' style='color: #0d5aa7'>" + user + "</strong></p>";

        // Obtener el correo asociado al token
        $.ajax({
            url: '/TocaBolas/users/correoPorToken', // Aquí debe estar la ruta para obtener el correo
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (response) {
                const data = typeof response === "string" ? JSON.parse(response) : response;
                if (data.status) {
                    contenido.innerHTML += "<p> ❑ <b>Correo: </b><b id='changeEmailFnc' title='Haz clic para editar' style='color: #0d5aa7'>" + data.email + "</b></p><p> ❑ <b title='Haz clic para editar' style='color: #0d5aa7; font-weight: 600'>Cambiar Contraseña</p><p> ❑ <b title='Haz clic para eliminar' style='color: #aa0303; font-weight: 600'>Eliminar Cuenta</p>";
                }
            }
        });
    });

    // Eliminar cuenta
    $('#deleteFnc').click(function () {
        const password = prompt("Ingresa tu contraseña para confirmar la eliminación de la cuenta");

        if (!password) return;

        $.ajax({
            url: '/TocaBolas/users/eliminarCuenta', // Aquí debe estar la ruta para eliminar cuenta
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: { password },
            success: function (response) {
                alert(response.message);
                // Redirigir o hacer lo necesario después de la eliminación
            },
            error: function () {
                alert("Hubo un error al eliminar la cuenta.");
            }
        });
    });

    // Cambiar nombre de usuario
    $('#changeUsernameFnc').click(function () {
        const nuevoNombre = prompt("Ingresa el nuevo nombre de usuario");

        if (!nuevoNombre) return;

        $.ajax({
            url: '/TocaBolas/users/cambiarNombreUsuario', // Aquí debe estar la ruta para cambiar el nombre
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: { nuevoNombre },
            success: function (response) {
                alert(response.message);
                // Actualizar la UI si es necesario
            },
            error: function () {
                alert("Hubo un error al cambiar el nombre.");
            }
        });
    });

    // Cambiar correo
    $('#changeEmailFnc').click(function () {
        const nuevoCorreo = prompt("Ingresa el nuevo correo");

        if (!nuevoCorreo) return;

        $.ajax({
            url: '/TocaBolas/users/cambiarCorreo', // Aquí debe estar la ruta para cambiar el correo
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: { nuevoCorreo },
            success: function (response) {
                alert(response.message);
                // Actualizar la UI si es necesario
            },
            error: function () {
                alert("Hubo un error al cambiar el correo.");
            }
        });
    });

    // Cambiar contraseña
    $('#changePasswordFnc').click(function () {
        const nuevaContrasena = prompt("Ingresa la nueva contraseña");

        if (!nuevaContrasena) return;

        $.ajax({
            url: '/TocaBolas/users/cambiarContrasena', // Aquí debe estar la ruta para cambiar la contraseña
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: { nuevaContrasena },
            success: function (response) {
                alert(response.message);
                // Actualizar la UI si es necesario
            },
            error: function () {
                alert("Hubo un error al cambiar la contraseña.");
            }
        });
    });
});
