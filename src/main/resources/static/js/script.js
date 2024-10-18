document.addEventListener('DOMContentLoaded', function() {
    fetchEstudiosSocioeconomicos();
});

function fetchEstudiosSocioeconomicos() {
    fetch('http://localhost:8081/api/estudiosocioeconomico')
        .then(response => response.json())
        .then(data => {
            console.log(data); // Verificar la respuesta en la consola
            const tableBody = document.querySelector('#solicitudes-table tbody');
            tableBody.innerHTML = '';
            data.forEach(estudio => {
                // Cambiar aquí para que se ajuste a los datos que estás recibiendo
                const row = `
                    <tr>
                        <td>Estudiante ID: ${estudio.idEstudiante}</td> <!-- Esto debe cambiar para mostrar el nombre del estudiante -->
                        <td>${formatTipoBeca(estudio.tipoBeca)}</td>
                        <td>
                            <button class="ver-btn" onclick="verEstudioSocioeconomico(${estudio.idEstudio})">Ver Estudio</button>
                            <button class="eliminar-btn" onclick="eliminarEstudioSocioeconomico(${estudio.idEstudio})">Eliminar Estudio</button>
                        </td>
                    </tr>
                `;
                tableBody.innerHTML += row;
            });
        })
        .catch(error => console.error('Error:', error));
}

function formatTipoBeca(tipoBeca) {
    return tipoBeca.replace('_', ' ').toLowerCase()
        .replace(/\b\w/g, l => l.toUpperCase());
}

function verEstudioSocioeconomico(id) {
    // Implementar lógica para ver el estudio socioeconómico
    console.log('Ver estudio socioeconómico', id);
}

function eliminarEstudioSocioeconomico(id) {
    if (confirm('¿Está seguro de que desea eliminar este estudio socioeconómico?')) {
        fetch(`/api/estudiosocioeconomico/${id}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    fetchEstudiosSocioeconomicos(); // Recargar la lista
                } else {
                    console.error('Error al eliminar el estudio socioeconómico');
                }
            })
            .catch(error => console.error('Error:', error));
    }
}