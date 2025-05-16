$(document).ready(function () {
    $.ajax({
        url: '/TocaBolas/Shop/items',
        method: 'GET',
        success: function (productos) {
            const contenedor = $('#productos');
            contenedor.empty();

            productos.forEach(({ id, nombre, descripcion, precio, url_icon }) => {
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
});
