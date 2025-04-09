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
});