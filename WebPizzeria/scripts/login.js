document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const loginMessage = document.getElementById('loginMessage');

    try {
        const response = await fetch('http://localhost:8080/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
            credentials: "include"
        });

        const data = await response.json();

        if (response.ok) {
            localStorage.setItem("token", data.access_token);
            localStorage.setItem("refreshToken", data.refresh_token);
            console.log('Token guardado:', data.access_token);
            console.log('Refresh Token guardado:', data.refresh_token);
            window.location.href = "requests.html";
        } else {

            if (response.status === 401) { 
                loginMessage.textContent = 'Invalid username or password.';
            } else if (response.status === 400) {  
                loginMessage.textContent = data.message || 'Bad Request'; 
            }
            else {
                loginMessage.textContent = `Login failed: ${data.message || 'An error occurred.'}`;
                console.error('Server Error:', response.status, data);
            }
        }
    } catch (err) {
        loginMessage.textContent = 'A network error occurred.';
        console.error('Fetch Error:', err);
    }
});

document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("token");
    if (token) {
        window.location.href = "requests.html";
    }
});

const showHiddenPass = () => {
    const input = document.getElementById('password');
    const iconEye = document.getElementById('eyeIcon');

    iconEye.addEventListener('click', () => {
        input.type = input.type === 'password' ? 'text' : 'password';
        iconEye.classList.toggle('ri-eye-line');
        iconEye.classList.toggle('ri-eye-off-line');
    });
}

showHiddenPass();