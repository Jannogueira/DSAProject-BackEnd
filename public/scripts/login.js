$(document).ready(function () {
    $('#uiverse-login').hide();
    // Inicio de sesión
    $('#inicioBtn').click(function (event) {
        event.preventDefault();
        const correo = $('#email').val().trim();
        const password = $('#password').val().trim();
        if(!password && !correo){
            $('#error-incompleto-login').show();
            $('#email').addClass('is-invalid');
            $('#password').addClass('is-invalid');
            return;
        }
        if (!correo) {
            $('#error-cincompleto-login').show();
            $('#email').addClass('is-invalid');
            return;
        }
        if (!password) {
            $('#error-pincompleto-login').show();
            $('#password').addClass('is-invalid');
            return;
        }

        $.ajax({
            url: '/TocaBolas/users/login',
            method: 'POST',
            data: { correo, password },
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            success: function (response) {
                const data = typeof response === "string" ? JSON.parse(response) : response;
                localStorage.setItem("token", data.token);
                localStorage.setItem("user", data.user);
                $('#cuadro-login').hide();
                $('#uiverse-login').show();
                setTimeout(function() {
                    window.location.href = "index";
                }, 3000);
            },
            error: function () {
                $('#error-datos-login').show();
                $('#email').addClass('is-invalid');
                $('#password').addClass('is-invalid');
            }
        });
    });
    $('#email').on('input', function () {
        $('#error-datos-login').hide();
        $('#error-incompleto-login').hide();
        $('#error-rincompleto-login').hide();
        $('#error-pincompleto-login').hide();
        $('#email').removeClass('is-invalid');
        $('#password').removeClass('is-invalid');
    });

    $('#password').on('input', function () {
        $('#error-datos-login').hide();
        $('#error-incompleto-login').hide();
        $('#error-rincompleto-login').hide();
        $('#error-pincompleto-login').hide();
        $('#email').removeClass('is-invalid');
        $('#password').removeClass('is-invalid');
    });
});
