document.addEventListener("DOMContentLoaded", () => {
    cargarBecados();
});

async function cargarBecados() {
    try {
        const response = await fetch('http://localhost:8081/api/becados'); // URL de tu API
        if (!response.ok) {
            throw new Error(`Error en la respuesta: ${response.statusText}`);
        }
        const becados = await response.json();
        
        const tbody = document.querySelector("#solicitudes-table tbody");
        tbody.innerHTML = ""; // Limpiar la tabla antes de llenarla

        becados.forEach(becado => {
            const tr = document.createElement("tr");

            // Acceso a los campos según el JSON proporcionado
            const nombreEstudiante = becado.nombreEstudiante; // Accede directamente al nombre
            const apellidoEstudiante = becado.apellidoEstudiante; // Accede directamente al apellido
            const tipoBeca = becado.nombreBeca; // Accede directamente al nombre de la beca

            tr.innerHTML = `
                <td>${nombreEstudiante} ${apellidoEstudiante}</td>
                <td>${tipoBeca}</td>
                <td>
                    <button class="ver-btn" onclick="verDetalles(${becado.id})">Ver</button>
                    <button class="editar-btn" onclick="editarEstudiante(${becado.id})">Editar</button>
                    <button class="eliminar-btn" onclick="eliminarEstudiante(${becado.id})">Eliminar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error("Error al cargar los datos:", error);
    }
}

function verDetalles(id) {
    // Implementa la lógica para mostrar los detalles del estudiante
    alert(`Mostrar detalles para el ID: ${id}`);
}

function editarEstudiante(id) {
    // Implementa la lógica para editar al estudiante
    alert(`Editar estudiante con ID: ${id}`);
}

function eliminarEstudiante(id) {
    // Implementa la lógica para eliminar al estudiante
    alert(`Eliminar estudiante con ID: ${id}`);
}
