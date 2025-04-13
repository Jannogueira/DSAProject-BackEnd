$(document).ready(function () {
    localStorage.setItem("user", "JanNogueira");
    const token = localStorage.getItem("token");
    const user  = localStorage.getItem("user");


    if (user){
        $('#usuarioDropdown').show();            // 1. Mostrar el li primero
        $('#usuarioBoton').text(user);           // 2. Luego cambiar el texto
        $('#idBotonLogin').hide();               // 3. (opcional) Ocultar "Conectate"
    }

    $('#cerrarSesion').click(function() {
        localStorage.removeItem('user');
        alert("Has cerrado sesión");
        window.location.href = "index.html";
    });


    $('#inicioBtn').click(function (event) {
        event.preventDefault(); // 👈 Evita que el formulario se envíe automáticamente

        var usuario = $('#email').val().trim();
        var password = $('#password').val().trim();

        if (usuario === '') {
            alert('No has rellenado el campo de usuario');
        } else if (password === '') {
            alert('No has rellenado el campo de contraseña');
        } else {
            $.ajax({
                url: '/TocaBolas/users/login',
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
                    console.log(response);
                },
                error: function () {
                    alert('Error en el inicio de sesión, comprueba los datos.');
                }
            });
        }
    });

    $('#btnRegistro').click(function (event) {
        event.preventDefault(); // 👈 También aquí

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
                url: '/TocaBolas/users/register',
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
                    alert('Registro completado. Serás redirigido para iniciar sesión.');
                    window.location.href = "login.html";
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