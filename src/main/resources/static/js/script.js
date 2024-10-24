document.addEventListener("DOMContentLoaded", () => {
    cargarBecados();
});

let becados = []; // Variable global para almacenar los becados

async function cargarBecados() {
    try {
        const response = await fetch('http://localhost:8081/api/becados');
        if (!response.ok) {
            throw new Error(`Error en la respuesta: ${response.statusText}`);
        }
        becados = await response.json(); // Almacena los becados en la variable global
        
        const tbody = document.querySelector("#solicitudes-table tbody");
        tbody.innerHTML = ""; // Limpiar la tabla antes de llenarla

        becados.forEach(becado => {
            const tr = document.createElement("tr");
            const idBecado = becado.id;
            const nombreEstudiante = becado.nombreEstudiante;
            const apellidoEstudiante = becado.apellidoEstudiante;
            const tipoBeca = becado.nombreBeca;

            tr.innerHTML = `
                <td>${nombreEstudiante} ${apellidoEstudiante}</td>
                <td>${tipoBeca}</td>
                <td>
                    <button class="ver-btn" onclick="verDetalles(${idBecado})">Ver</button>
                    <button class="editar-btn" onclick="abrirModalEditar(${idBecado}, '${nombreEstudiante}', '${apellidoEstudiante}', '${tipoBeca}')">Editar</button>
                    <button class="eliminar-btn" onclick="revocarBecado(${idBecado})">Revocar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error("Error al cargar los datos:", error);
    }
}

function verDetalles(id) {
    const becado = becados.find(b => b.id === id);

    if (becado) {
        document.getElementById("ver-nombre").textContent = `Nombre del Estudiante: ${becado.nombreEstudiante} ${becado.apellidoEstudiante}`;
        document.getElementById("ver-tipo-beca").textContent = `Tipo de Beca: ${becado.nombreBeca}`;
        
        document.getElementById("modal-ver").style.display = "block";
    }
}

function abrirModalEditar(id, nombre, apellido, tipoBeca) {
    document.getElementById("editar-id").value = id;
    document.getElementById("editar-nombre").value = `${nombre} ${apellido}`;
    document.getElementById("editar-tipo-beca").value = tipoBeca;

    document.getElementById("modal-editar").style.display = "flex";
}

function cerrarModal(modalId) {
    document.getElementById(modalId).style.display = "none";
}

async function guardarCambios(event) {
    event.preventDefault();

    const id = document.getElementById("editar-id").value;
    const nuevoTipoBeca = document.getElementById("editar-tipo-beca").value;

    if (nuevoTipoBeca) {
        try {
            const response = await fetch(`http://localhost:8081/api/becados/${id}/tipo-beca`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({nombre: nuevoTipoBeca})
            });

            if (response.ok) {
                alert("Tipo de beca actualizado.");
                cargarBecados();
                cerrarModal('modal-editar');
            } else {
                alert("Error al actualizar el tipo de beca.");
            }
        } catch (error) {
            console.error("Error al actualizar:", error);
        }
    }
}

async function revocarBecado(id) {
    if (confirm("¿Estás seguro de que deseas revocar esta beca?")) {
        try {
            const response = await fetch(`http://localhost:8081/api/becados/${id}/revocar`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                alert("Beca revocada correctamente.");
                cargarBecados();
            } else {
                alert("Error al revocar la beca.");
            }
        } catch (error) {
            console.error("Error al revocar:", error);
        }
    }
}

// Event Listeners para cerrar modales
document.querySelectorAll('.close-editar').forEach(button => {
    button.addEventListener('click', () => cerrarModal('modal-editar'));
});

document.querySelectorAll('.close-ver').forEach(button => {
    button.addEventListener('click', () => cerrarModal('modal-ver'));
});