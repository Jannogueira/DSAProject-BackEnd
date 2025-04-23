$(document).ready(function () { // Registro de usuario
    $('#uiverse-registro').hide();
    $('#btnRegistro').click(function (event) {
        event.preventDefault();

        const usuario = $('#rusuario').val().trim();
        const correo = $('#remail').val().trim();
        const password = $('#rpassword').val().trim();

        if (!usuario || !correo || !password) {
            alert('Por favor completa todos los campos.');
            return;
        }

        $.ajax({
            url: '/TocaBolas/users/register',
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
                        $('#error-usuario').text(response.message).show();
                    } else if (response.message.includes("correo")) {
                        $('#remail').addClass('is-invalid');
                        $('#error-correo').text(response.message).show();
                    }
                } else {
                    alert('Error en el registro, comprueba los datos.');
                }
            }
        });
    });
    $('#rusuario').on('input', function () {
        $('#error-usuario').hide();
        $('#rusuario').removeClass('is-invalid');
    });

    $('#remail').on('input', function () {
        $('#error-correo').hide();
        $('#remail').removeClass('is-invalid');
    });
});
