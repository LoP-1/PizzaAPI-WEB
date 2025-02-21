document.addEventListener("DOMContentLoaded", async function () {
    const token = localStorage.getItem("token");
    console.log("Token:", token);
    if (!token) {
        window.location.href = "index.html";
        return;
    }

    await fetchIngredients();

    // Manejo de selección de tamaño de pizza
    const sizeRadios = document.querySelectorAll('input[name="size"]');
    sizeRadios.forEach(radio => {
        radio.addEventListener("change", () => {
            sizeRadios.forEach(otherRadio => {
                if (otherRadio !== radio) {
                    otherRadio.checked = false;
                }
            });
        });
    });

    // Evento para enviar la pizza
    document.getElementById("pizza-form").addEventListener("submit", async function (event) {
        event.preventDefault();
        await createPizza();
    });
});

async function fetchIngredients() {
    try {
        const response = await fetch("http://localhost:8080/ingredients", {
            headers: { "Authorization": `Bearer ${localStorage.getItem("token")}` }
        });
        if (!response.ok) throw new Error("Error al obtener ingredientes");
        const ingredients = await response.json();
        renderIngredients(ingredients);
    } catch (error) {
        console.error("Error:", error);
    }
}

function renderIngredients(ingredients) {
    const container = document.querySelector("#pizza-form .statement:last-of-type");
    ingredients.forEach(ingredient => {
        const label = document.createElement("label");
        label.innerHTML = `
            <input type="checkbox" class="topping" data-id="${ingredient.id}"> ${ingredient.name}
            <input type="number" class="counter" data-for="${ingredient.id}" min="1" max="10" value="1" disabled>
        `;
        container.insertAdjacentElement("afterend", label);
    });

    document.querySelectorAll(".topping").forEach(checkbox => {
        checkbox.addEventListener("change", function () {
            document.querySelector(`.counter[data-for="${this.dataset.id}"]`).disabled = !this.checked;
        });
    });
}

async function getUserByUsername() {
    const username = localStorage.getItem("username"); // Obtener el username de localStorage
    if (!username) {
        throw new Error("No se encontró el username en localStorage");
    }

    const response = await fetch(`http://localhost:8080/users/${username}`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json",
        },
    });

    if (!response.ok) {
        throw new Error(`Error al obtener usuario: ${response.status}`);
    }

    return await response.json();
}

async function createPizza() {
    const selectedSize = document.querySelector('input[name="size"]:checked');
    if (!selectedSize) {
        alert("Selecciona un tamaño de pizza");
        return;
    }

    const ingredients = Array.from(document.querySelectorAll(".topping:checked")).map(checkbox => {
        return {
            id: checkbox.dataset.id,
            quantity: document.querySelector(`.counter[data-for="${checkbox.dataset.id}"]`).value
        };
    });

    try {
        const user = await getUserByUsername();
        if (!user) {
            alert("No se pudo obtener el usuario, intenta iniciar sesión nuevamente.");
            return;
        }

        const pizzaData = {
            size: selectedSize.value,
            userId: user.id, // Ahora obtenemos el ID del usuario por username
            ingredients: ingredients
        };

        const response = await fetch("http://localhost:8080/pizzas", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify(pizzaData)
        });

        if (!response.ok) throw new Error("Error al crear la pizza");
        alert("Pizza creada con éxito");
    } catch (error) {
        console.error("Error:", error);
    }
}

async function logout() {
    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "index.html";
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/auth/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
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

    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("username");
    window.location.href = "index.html";
}
