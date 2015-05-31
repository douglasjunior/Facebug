/*
 * Arquivo criado para agrupar métodos genéricos de validação dos formulários
 * Autor: Rebeca
 */

/**
 * Valida o tamanho mínimo de uma String
 * @param {String} texto
 * @param {Integer} tamanhoMinimo
 * @returns {Boolean}
 */
function validaTexto(texto, tamanhoMinimo) {
    // se o tamanho não for informado, então considera como 1
    tamanhoMinimo = tamanhoMinimo == null ? 1 : tamanhoMinimo;
    if (texto.length < tamanhoMinimo) {
        return false;
    }
    return true;
}

/**
 * Valida o endereço de e-mail com base em uma expressão regular.
 * @param {String} email
 * @returns {Boolean}
 */
function validaEmail(email) {
    var filtro = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;

    if (filtro.test(email)) {
        return true;
    }
    return false;
}

/**
 * Valida o tamanho mínimo da senha de 8 caracteres
 * @param {String} senha
 * @returns {Boolean}
 */
function validaSenha(senha) {
    return validaTexto(senha, 8);
}
