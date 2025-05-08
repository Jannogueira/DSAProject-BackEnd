$(document).ready(function () {
    const token = localStorage.getItem("token");
    const user  = localStorage.getItem("user");
    const currentPage = window.location.pathname.split('/').pop();

    // Verificación del token en cada carga
    if (token) {
        $.ajax({
            url: '/TocaBolas/validate',
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
    const protectedUnlogedPages = ['store.html', 'store'];
    const protectedLogedPages = ['login.html', 'registro.html', 'registro', 'login'];

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
        window.location.href = "login";
    }

    // Modo test (para desarrollo)
    $('#testBoton').click(function () {
        localStorage.setItem("user", "JanNogueira");
        location.reload();
    });
    $('#cuentaBtn').click(function () {
        window.location.href = 'cuenta?' + user;
    });
});
