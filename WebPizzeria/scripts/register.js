document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const registerMessage = document.getElementById('registerMessage');

    if (password !== confirmPassword) {
        registerMessage.textContent = "Passwords do not match.";
        return;
    }

    try {
        //envio datos al registro 
        const response = await fetch('http://localhost:8080/auth/register', { 
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password }) 
        });

        const data = await response.json();

        if (response.ok) {
            registerMessage.textContent = "Registration successful. Please login.";
            window.location.href = "index.html";
        } else {
        //errores en el registro    
            if (response.status === 400) { 
                registerMessage.textContent = data.message || "Invalid input data."; 
            } else if (response.status === 409) { 
                registerMessage.textContent = data.message || "Username or email already exists.";
            } else {
                registerMessage.textContent = "Registration failed. Please try again later.";
                console.error("Server Error:", response.status, data); 
            }
        }
    } catch (error) {
        registerMessage.textContent = "A network error occurred.";
        console.error("Fetch Error:", error);
    }
});
