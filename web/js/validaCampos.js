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

/**
 * Valida se a data (dd/MM/yyyy) está entre o ano inicial e final.
 * @param {String} data 
 * @param {Integer} anoInicial
 * @param {Integer} anoFinal
 * @returns {Boolean}
 */
function validaData(data, anoInicial, anoFinal){
    if (data != null && data.length == 10) {
         var dataAno = data.substr(6, 9); //recupera só a parte do ano da String
         if (dataAno > anoInicial && dataAno < anoFinal) {
             return true;
         }
    }
    return false;
}