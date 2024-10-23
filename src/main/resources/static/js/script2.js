document.addEventListener("DOMContentLoaded", () => {
    cargarBecados();
});

async function cargarBecados() {
    try {
        const response = await fetch('http://localhost:8081/api/solicitudes'); // URL de tu API
        if (!response.ok) {
            throw new Error(`Error en la respuesta: ${response.statusText}`);
        }
        becados = await response.json(); // Almacena los becados en la variable

        const tbody = document.querySelector("#solicitudes-table tbody");
        tbody.innerHTML = ""; // Limpiar la tabla antes de llenarla

        becados.forEach(becado => {
            const tr = document.createElement("tr");

            // Acceso a los campos según el JSON proporcionado
            const idBecado = becado.id; // El ID del becado
            const nombreEstudiante = becado.nombreEstudiante; // Nombre del estudiante
            const apellidoEstudiante = becado.apellidoEstudiante; // Apellido del estudiante
            const tipoBeca = becado.nombreBeca; // Nombre de la beca

            // Crear la fila en la tabla con los datos del becado
            tr.innerHTML = `
                <td>${nombreEstudiante} ${apellidoEstudiante}</td>
                <td>${tipoBeca}</td>
                <td>
                    <button class="ver-btn" onclick="verDetalles(${idBecado})">Ver</button>
                    <button class="aprobar-btn" onclick="aprobarBecado(${idBecado})">Aprobar</button>
                    <button class="rechazar-btn" onclick="rechazarBecado(${idBecado})">Rechazar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error("Error al cargar los datos:", error);
    }
}

function verDetalles(id) {
    const becado = becados.find(b => b.id === id); // Encuentra el becado por ID

    if (becado) {
        document.getElementById("ver-nombre").textContent = `Nombre del Estudiante: ${becado.nombreEstudiante} ${becado.apellidoEstudiante}`;
        document.getElementById("ver-tipo-beca").textContent = `Tipo de Beca: ${becado.nombreBeca}`;
        document.getElementById("ver-motivo").textContent = `Motivo de la Beca: ${becado.motivoBeca}`; // Agregar el motivo
        
        // Mostrar el modal
        document.getElementById("modal-ver").style.display = "block";
    }
}

async function aprobarBecado(id) {
    if (confirm("¿Estás seguro de que deseas aprobar esta beca?")) { // Confirmación antes de aprobar
        try {
            const response = await fetch(`http://localhost:8081/api/solicitudes/${id}/aprobar`, {
                method: 'PUT', // Método PUT para aprobar
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                alert("Beca aprobada correctamente.");
                cargarBecados(); // Recarga la lista de becados
            } else {
                alert("Error al aprobar la beca.");
            }
        } catch (error) {
            console.error("Error al aprobar:", error);
        }
    }
}

async function rechazarBecado(id) {
    if (confirm("¿Estás seguro de que deseas rechazar esta beca?")) { // Confirmación antes de rechazar
        try {
            const response = await fetch(`http://localhost:8081/api/solicitudes/${id}/rechazar`, {
                method: 'PUT', // Método PUT para rechazar
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                alert("Beca rechazada correctamente.");
                cargarBecados(); // Recarga la lista de becados
            } else {
                alert("Error al rechazar la beca.");
            }
        } catch (error) {
            console.error("Error al rechazar:", error);
        }
    }
}

// Cerrar el modal al hacer clic en la cruz
document.querySelectorAll('.close-ver').forEach(button => {
    button.addEventListener('click', () => cerrarModal('modal-ver'));
});

function cerrarModal(modalId) {
    document.getElementById(modalId).style.display = "none"; // Ocultar el modal
}
