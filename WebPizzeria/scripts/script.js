document.addEventListener("DOMContentLoaded", function () {
    // Manejamos el tamaño de la pizza
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

    // Manejamos los toppings y sus contadores
    const toppingCheckboxes = document.querySelectorAll(".topping");
    const counters = document.querySelectorAll(".counter");

    toppingCheckboxes.forEach(checkbox => {
        checkbox.addEventListener("change", function () {
            const counter = document.querySelector(`.counter[data-for="${this.dataset.id}"]`);
            counter.disabled = !this.checked; // Activa o desactiva el contador
        });
    });
});
