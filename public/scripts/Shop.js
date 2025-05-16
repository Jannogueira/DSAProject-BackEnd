$(document).ready(function () {
    $.ajax({
        url: '/TocaBolas/Shop/items',
        method: 'GET',
        success: function (productos) {
            const contenedor = $('#productos');
            contenedor.empty();

            productos.forEach(p => {
                let imagenProducto = '';

                switch(p.name.toLowerCase()) {
                    case 'bomba':
                        imagenProducto = 'imagenes/bomba.jpg';
                        break;
                    case 'oro':
                        imagenProducto = 'imagenes/oro.jpg';
                        break;
                    case 'delete':
                        imagenProducto = 'imagenes/delete.jpg';
                        break;
                    default:
                        imagenProducto = 'imagenes/default.jpg';
                        break;
                }

                const card = `
                    <div class="col-md-4 mb-4">
                        <div class="card shadow rounded-4">
                            <img src="${imagenProducto}" class="card-img-top borderedondo" alt="${p.name}">
                            <div class="card-body">
                                <h5 class="card-title">${p.name}</h5>
                                <p class="card-text">${p.description}</p>
                                <a href="#" class="btn btn-primary w-100">Agregar al carrito</a>
                            </div>
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
