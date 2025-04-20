$(document).ready(function () {
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
                alert('Inicio de sesión completado.');
                window.location.href = "index.html";
            },
            error: function () {
                alert('Error en el inicio de sesión, comprueba los datos.');
            }
        });
    });

});
