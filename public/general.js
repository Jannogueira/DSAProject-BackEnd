$(document).ready(function () {
    const token = localStorage.getItem("token");
    const user  = localStorage.getItem("user");
    const currentPage = window.location.pathname.split('/').pop();

    // Verificación del token en cada carga
    if (token) {
        $.ajax({
            url: '/TocaBolas/users/validate',
            method: 'GET',
            headers: {'Authorization': 'Bearer ' + token},
            success: function () {
                console.log("Token válido");
            },
            error: function () {
                console.warn("Token expirado. Cerrando sesión.");
                cerrarSesion();
            }
        });
    }
    // Redirección según estado de login
    const protectedUnlogedPages = ['store.html'];
    const protectedLogedPages = ['login.html', 'registro.html'];

    if (user && protectedLogedPages.includes(currentPage)) {
        window.location.href = "index.html";
    }
    if (!user && protectedUnlogedPages.includes(currentPage)) {
        window.location.href = "login.html";
    }

    // Mostrar/ocultar opciones según sesión
    if (user) {
        $('#usuarioDropdown').show();
        $('#usuarioBoton').text(user);
        $('#idBotonLogin, #testBoton').hide();
    } else {
        $('#idBotonTienda').hide();
        $('#idBotonLogin').show();
    }

    // Cierre de sesión
    $('#cerrarSesion').click(cerrarSesion);

    function cerrarSesion() {
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        window.location.href = "login.html";
    }

    // Modo test (para desarrollo)
    $('#testBoton').click(function () {
        localStorage.setItem("user", "JanNogueira");
        location.reload();
    });

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
