$(document).ready(function () {
    const token = localStorage.getItem("token");
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
                            <button class="btn btn-primary w-75 mt-auto mb-2 mx-auto" data-id="${id}">Agregar al carrito</button>
                        </div>
                    </div>
                `;
                contenedor.append(card);
            });
            const card = `
                    <div class="col-md-4 mb-4">
                        <div class="card shadow rounded-4" style="min-height: 400px">
                        <img src="./imagenes/testing.jpg" class="card-img-top borderedondo">
                            <div class="card-body">
                                <h5 class="card-title">TEST COMPRA</h5>
                                <p class="card-text">Escribe la lista de objetos que quieres comprar!</p>
                                <input type="text" class="form-control" id="tstTxt" placeholder="1:2, 2:5">   
                            </div>
                            <button id="testBTN" class="btn btn-primary w-75 mt-auto mb-2 mx-auto">TEST</button>
                        </div>
                    </div>
            `;
            contenedor.append(card);
        },
        error: function () {
            alert('Error al cargar los productos.');
        }
    });

    // Delegación de eventos para el botón testBTN
    $('#productos').on('click', '#testBTN', function (event) {
        const itemsString = $('#tstTxt').val();
        // Aquí falta definir 'token' - asegúrate de que esté definido
        $.ajax({
            url: '/TocaBolas/Shop/comprar',
            method: 'POST',
            data: itemsString,
            contentType: 'text/plain',
            headers: {
                'Authorization': 'Bearer ' + token // Asegúrate de que 'token' esté definido
            },
            success: function (response) {
                console.log("Compra exitosa:", response);
            },
            error: function (xhr, status, error) {
                console.error("Error en la compra:", xhr.responseJSON);
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