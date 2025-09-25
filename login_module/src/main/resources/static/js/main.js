/**
 * Este script é executado após o carregamento completo da página.
 * Ele adiciona um listener de clique a todos os botões com a classe '.toggle-password'
 * para alternar a visibilidade da senha no campo de input associado.
 */
document.addEventListener("DOMContentLoaded", function() {

    const togglePasswordButtons = document.querySelectorAll(".toggle-password");

    togglePasswordButtons.forEach(button => {
        button.addEventListener("click", function () {
            // Encontra o input de senha dentro do mesmo 'input-group'.
            const passwordInput = this.closest(".input-group").querySelector("input");
            const icon = this.querySelector("i");

            // Verifica o tipo atual do input e o alterna.
            if (passwordInput.type === "password") {
                passwordInput.type = "text";
                // Troca o ícone para 'olho aberto'.
                icon.classList.remove("bi-eye-slash-fill");
                icon.classList.add("bi-eye-fill");
            } else {
                passwordInput.type = "password";
                // Troca o ícone de volta para 'olho fechado'.
                icon.classList.remove("bi-eye-fill");
                icon.classList.add("bi-eye-slash-fill");
            }
        });
    });
});