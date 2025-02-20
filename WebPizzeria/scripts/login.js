document.getElementById('loginForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            //guardar los valores de los inputs
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
    
            try {
                const response = await fetch('http://localhost:8080/auth/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, password }),
                    credentials: "include"
                });
    
                const data = await response.json();
                if (response.ok) {

                    // Guardar los tokens en localStorage acceso y refresco
                    localStorage.setItem("token", data.access_token);
                    localStorage.setItem("refreshToken", data.refresh_token);
    
                    console.log('Token guardado:', data.access_token);
                    console.log('Refresh Token guardado:', data.refresh_token);
    
                    // Redirigir a pedidos.html para creacion de pizzas
                    window.location.href = "pedidos.html";
                } else {
                    document.getElementById('loginMessage').textContent = 'Error: ' + data.message;
                }
            } catch (err) {
                console.error('Error:', err);
            }
        });
    
        // Comprobar si hay un token al cargar la página y redirigir si es necesario
        document.addEventListener("DOMContentLoaded", function () {
            const token = localStorage.getItem("token");
            if (token) {
                window.location.href = "pedidos.html"; // Si ya hay sesión, redirigir
            }
        });