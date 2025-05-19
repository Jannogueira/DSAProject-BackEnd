$(document).ready(function () {
    $.ajax({
        url: '/TocaBolas/Shop/items',
        method: 'GET',
        success: function (productos) {
            const contenedor = $('#productos');
            contenedor.empty();
            productos.forEach(({id, nombre, descripcion, precio, url_icon}) => {
                const card = `
                    <div class="col-md-4 mb-4">
                        <div class="card shadow rounded-4" style="min-height: 400px">
                            <img src="${url_icon}" class="card-img-top borderedondo" alt="${nombre}">
                            <div class="card-body">
                                <h5 class="card-title">${nombre}</h5>
                                <p class="card-text">${descripcion}</p>
                                <p class="card-text fw-bold text-success">${precio} monedas</p>
                            </div>
                            <button class="btn btn-primary w-75 mt-auto mb-2             mx-auto" data-id="${id}">Agregar al carrito</button>
                        </div>
                    </div>
                `;
                contenedor.append(card);
            });
        },
        error: function () {
            alert('Error al cargar los productos.');
        }
    });
    $('#inicioBtn').click(function (event) {
        $.ajax({
            url: '/TocaBolas/Shop/comprar',
            method: 'POST',
            data: itemsString, // Envía el string directamente
            contentType: 'text/plain', // IMPORTANTE: Content-Type correcto
            headers: {
                'Authorization': 'Bearer ' + token // Header de autenticación
            },
            success: function (response) {
                console.log("Compra exitosa:", response);
                // Manejar respuesta exitosa
            },
            error: function (xhr, status, error) {
                console.error("Error en la compra:", xhr.responseJSON);
                // Manejar errores según el código de estado
                if (xhr.status === 401) {
                    mostrarError("Token inválido o expirado");
                } else if (xhr.status === 404) {
                    mostrarError("Usuario o ítem no encontrado");
                } else if (xhr.status === 400) {
                    mostrarError("Dinero insuficiente");
                }
            }
        });
    });
});
