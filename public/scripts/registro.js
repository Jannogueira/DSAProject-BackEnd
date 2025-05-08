$(document).ready(function () { // Registro de usuario
    $('#uiverse-registro').hide();
    $('#btnRegistro').click(function (event) {
        event.preventDefault();

        const usuario = $('#rusuario').val().trim();
        const correo = $('#remail').val().trim();
        const password = $('#rpassword').val().trim();

        if (!usuario || !correo || !password) {
            if (!usuario) {
                $('#error-uincompleto-reg').show();
                $('#rusuario').addClass('is-invalid');
            }
            if (!correo) {
                $('#error-cincompleto-reg').show();
                $('#remail').addClass('is-invalid');
            }
            if (!password) {
                $('#error-pincompleto-reg').show();
                $('#rpassword').addClass('is-invalid');
            }
            return;
        }



        $.ajax({
            url: '/TocaBolas/register',
            method: 'POST',
            data: { username: usuario, correo, password },
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            success: function () {
                $('#cuadro-registro').hide();
                $('#uiverse-registro').show();
                setTimeout(function() {
                    window.location.href = "login";
                }, 3500);
            },
            error: function (xhr) {
                if (xhr.status === 409) {
                    const response = JSON.parse(xhr.responseText);
                    if (response.message.includes("usuario")) {
                        $('#rusuario').addClass('is-invalid');
                        $('#error-usuario-reg').text(response.message).show();
                    } else if (response.message.includes("correo")) {
                        $('#remail').addClass('is-invalid');
                        $('#error-correo-reg').text(response.message).show();
                    }
                } else {
                    alert('Error en el registro, comprueba los datos.');
                }
            }
        });
    });
    $('#rusuario').on('input', function () {
        $('#error-usuario-reg').hide();
        $('#error-uincompleto-reg').hide();
        $('#rusuario').removeClass('is-invalid');
    });

    $('#remail').on('input', function () {
        $('#error-correo-reg').hide();
        $('#error-cincompleto-reg').hide();
        $('#remail').removeClass('is-invalid');
    });
    $('#rpassword').on('input', function () {
        $('#error-pincompleto-reg').hide();
        $('#error-pincompleto-reg').hide();
        $('#rpassword').removeClass('is-invalid');
    });
});
