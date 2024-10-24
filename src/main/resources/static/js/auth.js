function logout() {
    const token = localStorage.getItem('token');
    
    fetch('/logout', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        localStorage.removeItem('token');
        sessionStorage.clear();
        window.history.forward();
        window.location.href = '/login';  // Cambiado de /login.html a /login
    })
    .catch(error => {
        console.error('Error durante el logout:', error);
        // Redirigir de todos modos
        localStorage.removeItem('token');
        sessionStorage.clear();
        window.location.href = '/login';  // Cambiado de /login.html a /login
    });
}

function checkAuth() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login';  // Cambiado de /login.html a /login
        return;
    }
    
    // Opcional: Verificar si el token es válido haciendo una petición al backend
    fetch('/api/verify-token', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .catch(() => {
        localStorage.removeItem('token');
        window.location.href = '/login';  // Cambiado de /login.html a /login
    });
}