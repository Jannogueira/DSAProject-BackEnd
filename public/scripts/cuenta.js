$(document).ready(function () {
    const token = localStorage.getItem("token");
    const user = localStorage.getItem("user");

    // Mostrar el nombre de usuario y correo en la sección de ajustes
    $(document).on('click', '#accBtn', function () {
        contenido.innerHTML = "<h2 class='text-center fuentetitulos'>Ajustes de cuenta</h2><br>" +
            "<p> ❑ <b>Nombre de usuario:</b> <strong id='changeUsernameFnc' title='Haz clic para editar' style='color: #0d5aa7'>" + user + "</strong></p>";

        // Obtener el correo asociado al token
        $.ajax({
            url: '/TocaBolas/correoPorToken', // Aquí debe estar la ruta para obtener el correo
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (response) {
                const data = typeof response === "string" ? JSON.parse(response) : response;
                if (data.status) {
                    contenido.innerHTML += "<p> ❑ <b>Correo: </b><b id='changeEmailFnc' title='Haz clic para editar' style='color: #0d5aa7'>" + data.email + "</b></p>" +
                        "<p> ❑ <a id='changePasswordFnc' title='Haz clic para editar' style='color: #0d5aa7; font-weight: 600'>Cambiar Contraseña</a></p>" +
                        "<p> ❑ <b id='deleteFnc' title='Haz clic para eliminar' style='color: #aa0303; font-weight: 600'>Eliminar Cuenta</b></p>";
                }
            }
        });
    });

    // Eliminar cuenta
    $(document).on('click', '#deleteFnc', function () {
        const password = prompt("Ingresa tu contraseña para confirmar la eliminación de la cuenta");

        if (!password) return;

        const token = localStorage.getItem("token");

        $.ajax({
            url: '/TocaBolas/eliminarUsuario',
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: { contrasena: password },
            success: function (response) {
                alert(response.message || "Cuenta eliminada exitosamente.");

                localStorage.removeItem('user');
                localStorage.removeItem('token');
                window.location.href = '/login'; // O a donde quieras llevarlo
            },
            error: function (xhr) {
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    alert("❌ " + xhr.responseJSON.message);
                } else {
                    alert("❌ Hubo un error al eliminar la cuenta.");
                }
            }
        });
    });

    // Cambiar nombre de usuario
    $(document).on('click', '#changeUsernameFnc', function () {
        const nuevoNombre = prompt("Ingresa el nuevo nombre de usuario");

        if (!nuevoNombre) return;

        $.ajax({
            url: '/TocaBolas/actualizarUsuario',
            method: 'PUT',
            data: {
                nuevoUsuario: nuevoNombre
            },
            headers: {
                Authorization: "Bearer " + localStorage.getItem("token")
            },
            success: function (response) {
                alert("Usuario actualizado correctamente");
                if (response.newToken) {
                    localStorage.setItem("token", response.newToken); // Guardamos el nuevo token
                }
                localStorage.setItem("user", nuevoNombre);
                window.location.reload();
            },
            error: function (xhr) {
                if (xhr.status === 409) {
                    alert("⚠️ Este nombre de usuario ya está en uso.");
                } else if (xhr.status === 401) {
                    alert("⚠️ Sesión expirada o token inválido.");
                } else {
                    alert("❌ Error al actualizar usuario.");
                }
            }
        });
    });

    // Cambiar correo
    $(document).on('click', '#changeEmailFnc', function () {
        const nuevoCorreo = prompt("Ingresa el nuevo correo");

        if (!nuevoCorreo) return;

        $.ajax({
            url: '/TocaBolas/actualizarCorreo',
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem("token")
            },
            data: {
                nuevoCorreo: nuevoCorreo // no usar JSON.stringify
            },
            success: function (response) {
                alert("Correo actualizado correctamente.");
                window.location.reload();
            },
            error: function (xhr) {
                if (xhr.status === 409) {
                    alert("⚠️ Este correo ya está en uso.");
                } else if (xhr.status === 401) {
                    alert("⚠️ Sesión expirada o token inválido.");
                } else {
                    alert("❌ Error al actualizar correo.");
                }
            }
        });

    });

    // Cambiar contraseña
    $(document).on('click', '#changePasswordFnc', function () {
        contenido.innerHTML = `
    <h2 class="text-center fuentetitulos">Cambiar Contraseña</h2><br>
    <div style="display: flex; flex-direction: column; align-items: center; gap: 10px; width: 100%;">
        
        <div class="mb-3" style="width: 80%; max-width: 300px;">
            <input type="password" id="currentPassword" class="form-control" placeholder="Contraseña actual">
            <div id="error-cincompleto-cpass" class="invalid-feedback">Campo incompleto!</div>
        </div>

        <div class="mb-3" style="width: 80%; max-width: 300px;">
            <input type="password" id="newPassword" class="form-control" placeholder="Nueva contraseña">
            <div id="error-cincompleto2-cpass" class="invalid-feedback">Campo incompleto!</div>
        </div>

        <div class="mb-3" style="width: 80%; max-width: 300px;">
            <input type="password" id="repeatNewPassword" class="form-control" placeholder="Repetir nueva contraseña">
            <div id="error-cincompleto3-cpass" class="invalid-feedback">Campo incompleto!</div>
            <div id="error-contraseña-cambio" class="text-danger" style="display:none;">Las contraseñas no coinciden</div>
        </div>

        <div style="margin-top: 20px; display: flex; gap: 15px;">
            <button id="savePasswordBtn" style="padding: 10px 20px; background-color: #0d5aa7; color: white; border: none; border-radius: 5px; cursor: pointer;">Guardar</button>
            <button id="backToSettingsBtn" style="padding: 10px 20px; background-color: #555; color: white; border: none; border-radius: 5px; cursor: pointer;">Atrás</button>
        </div>
    </div>
`;

    });

    // Guardar nueva contraseña
    $(document).on('click', '#savePasswordBtn', function () {
        const currentPassword = $('#currentPassword').val();
        const newPassword = $('#newPassword').val();
        const repeatNewPassword = $('#repeatNewPassword').val();
        const token = localStorage.getItem('token');

        if (!currentPassword || !newPassword || !repeatNewPassword) {

            if(!currentPassword){
                $('#currentPassword').addClass('is-invalid');
                $('#error-cincompleto-cpass').show();
            }
            if(!newPassword){
                $('#newPassword').addClass('is-invalid');
                $('#error-cincompleto2-cpass').show();
            }
            if(!repeatNewPassword){
                $('#repeatNewPassword').addClass('is-invalid');
                $('#error-cincompleto3-cpass').show();
            }
            return;
        }

        if (newPassword !== repeatNewPassword) {
            $('#repeatNewPassword').addClass('is-invalid');
            $('#error-contraseña-cambio').show();
            return;
        }

        $.ajax({
            url: '/TocaBolas/actualizarContrasena',
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: {
                contrasenaActual: currentPassword,
                nuevaContrasena: newPassword
            },
            success: function (response) {
                alert(response.message || "Contraseña cambiada exitosamente.");
                localStorage.removeItem("user");
                localStorage.removeItem("token");
                window.location.href = "login";
            },
            error: function () {
                alert("❌ Error al cambiar la contraseña. Revisa que la contraseña actual sea correcta.");
            }
        });
    });

    // Botón Atrás
    $(document).on('click', '#backToSettingsBtn', function () {
        $('#accBtn').click();
    });


    $(document).on('input', '#currentPassword', function () {
        $(this).removeClass('is-invalid');
        $('#error-cincompleto-cpass').hide();
    });
    $(document).on('input', '#newPassword', function () {
        $(this).removeClass('is-invalid');
        $('#error-cincompleto2-cpass').hide();
    });

    $(document).on('input', '#repeatNewPassword', function () {
        $(this).removeClass('is-invalid');
        $('#error-cincompleto3-cpass').hide();
        $('#error-contraseña-cambio').hide();
    });
});
