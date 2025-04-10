$(document).ready(function () {
    $('#inicioBtn').click(function () {
        var usuario = $('#email').val().trim();
        var password = $('#password').val().trim();

        if (usuario === '') {
            alert('No has rellenado el campo de usuario');
        } else if (password === '') {
            alert('No has rellenado el campo de contraseña');
        } else {
            $.ajax({
                url: 'http://localhost:8080/TocaBolas/users/login',
                method: 'POST',
                data: {
                    correo: usuario,
                    password: password
                },
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                success: function (response) {
                    alert('Inicio de sesión completado.');
                    // Aquí puedes redirigir o guardar token si lo necesitas
                    console.log(response);
                },
                error: function () {
                    alert('Error en el inicio de sesión, comprueba los datos.');
                }
            });
        }
    });
    $('#btnRegistro').click(function () {
        var usuario = $('#rusuario').val().trim();
        var correo = $('#remail').val().trim();
        var password = $('#rpassword').val().trim();

        if (usuario === '') {
            alert('No has rellenado el campo de usuario');
        } else if (password === '') {
            alert('No has rellenado el campo de contraseña');
        } else if (correo === '') {
            alert('No has rellenado el campo de correo');
        } else {
            $.ajax({
                url: 'http://localhost:8080/TocaBolas/users/register',
                method: 'POST',
                data: {
                    username: usuario,
                    correo: correo,
                    password: password
                },
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                success: function (response) {
                    alert('Registro completado.');
                    console.log(response);
                },
                error: function (xhr) {
                    if (xhr.status === 409) {
                        alert('El usuario ya existe.');
                    } else {
                        alert('Error en el registro, comprueba los datos.');
                    }
                }
            });
        }
    });
});