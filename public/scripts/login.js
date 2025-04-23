$(document).ready(function () {
    $('#uiverse-login').hide();
    // Inicio de sesión
    $('#inicioBtn').click(function (event) {
        event.preventDefault();
        const correo = $('#email').val().trim();
        const password = $('#password').val().trim();

        if (!correo || !password) {
            alert('Por favor completa todos los campos.');
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
                alert('Error en el inicio de sesión, comprueba los datos.');
            }
        });
    });

});
