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
                        <div class="card shadow rounded-4 marco-negro2" style="min-height: 400px">
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

            // CARD para comprar por IDs
            const card = `
                <div class="col-md-4 mb-4">
                    <div class="card shadow rounded-4 marco-negro2" style="min-height: 400px">
                        <img src="./imagenes/carrito.png" class="card-img-top borderedondo">
                        <div class="card-body">
                            <h5 class="card-title">COMPRA POR ID's</h5>
                            <p class="card-text">Escribe la lista de objetos que quieres comprar!</p>
                            <input type="text" class="form-control" id="tstTxt" placeholder="1:2, 2:5">   
                        </div>
                        <button id="testBTN" class="btn btn-primary w-75 mt-auto mb-2 mx-auto">TEST</button>
                    </div>
                </div>
            `;

            // CARD para probar userStats
            const card2 = `
                <div class="col-md-4 mb-4">
                    <div class="card shadow rounded-4 marco-negro2" style="min-height: 400px">
                        <img src="./imagenes/carrito.png" class="card-img-top borderedondo">
                        <div class="card-body">
                            <h5 class="card-title">PRUEBA USERSTATS</h5>
                            <p class="card-text" id="stats">Stats</p>
                        </div>
                        <button id="statsBTN" class="btn btn-primary w-75 mt-auto mb-2 mx-auto">TEST</button>
                    </div>
                </div>
            `;

            contenedor.append(card);
            contenedor.append(card2);
        },
        error: function () {
            alert('Error al cargar los productos.');
        }
    });

    // Delegación evento para comprar items por ID
    $('#productos').on('click', '#testBTN', function () {
        const itemsString = $('#tstTxt').val();
        $.ajax({
            url: '/TocaBolas/Shop/comprar',
            method: 'POST',
            data: itemsString,
            contentType: 'text/plain',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (response) {
                console.log("Compra exitosa:", response);
            },
            error: function (xhr) {
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

    // 🆕 Delegación evento para obtener los stats del usuario
    $('#productos').on('click', '#statsBTN', function () {
        $.ajax({
            url: '/TocaBolas/userStats',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function (response) {
                console.log("Stats recibidos:", response);
                $('#stats').text(`Score: ${response.score}, Dinero: ${response.money}`);
            },
            error: function (xhr) {
                console.error("Error al obtener stats:", xhr.responseText);
                $('#stats').text("Error al obtener los stats.");
            }
        });
    });
});
