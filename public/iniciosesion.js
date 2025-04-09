$(document).ready(function () {
    $('#inicioBtn').click(function () {
        var usuario = $('#email').val().trim();
        var password = $('#password').val().trim();

        if (usuario === '') {
            alert('No has rellenado el campo de usuario');
        }else if(password == ''){
            alert('No has rellenado el campo de usuario');
        }else {
            var urlApi = '/TocaBolas/user/' + usuario + '/' + password;

            $.ajax({
                url: urlApi,
                method: 'GET',
                dataType: 'json',
                success: function (response) {
                    alert('Inicio de sesión completado.');
                    // Aquí puedes redirigir o hacer algo más con la respuesta
                },
                error: function () {
                    $('#email').val('');
                    $('#password').val('');
                    alert('Error en el inicio de sesión, comprueba tu usuario y contraseña');
                }
            });
        }
    });
});