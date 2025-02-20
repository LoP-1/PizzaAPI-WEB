document.addEventListener("DOMContentLoaded", function () {
    //tamaño de la pizza
    const sizeRadios = document.querySelectorAll('input[name="size"]');
    sizeRadios.forEach(radio => {
        radio.addEventListener("change", () => {
            sizeRadios.forEach(otherRadio => {
                if (otherRadio !== radio) {
                    otherRadio.checked = false; // Solo uno puede estar seleccionado
                }
            });
        });
    });

//topings temporales
    const toppingCheckboxes = document.querySelectorAll(".topping");
    const counters = document.querySelectorAll(".counter");

    toppingCheckboxes.forEach(checkbox => {
        checkbox.addEventListener("change", function () {
            const counter = document.querySelector(`.counter[data-for="${this.dataset.id}"]`);
            counter.disabled = !this.checked; 
        });
    });
});

async function logout() {
    const token = localStorage.getItem("token"); // Obtener el token guardado localmente

    if (!token) {
        window.location.href = "index.html"; // Si no hay token, redirigir directamente
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/auth/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}` // Enviar el token en la cabecera
            },
            credentials: "include"
        });

        if (response.ok) {
            console.log("Sesión cerrada exitosamente.");
        } else {
            console.error("Error al cerrar sesión:", response.statusText);
        }
    } catch (error) {
        console.error("Error en la solicitud de logout:", error);
    }

    // Eliminar el token y redirigir al login logeo limpio
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    window.location.href = "index.html";
}
