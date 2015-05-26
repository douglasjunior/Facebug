function validaTexto(texto) {
    if (texto.length < 1) {
        return false;
    }
    return true;
}

function validaEmail(email) {
    var filtro = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;

    if (filtro.test(email)) {
        return true;
    }
    return false;
}

function validaSenha(senha) {
    if (senha.length < 8) {
        return false;
    }
    return true;
}


function validarCadastro() {
    var nome = document.cadastro.nome.value;
    var sobrenome = document.cadastro.sobrenome.value;
    var email = document.cadastro.email.value;
    var senha = document.cadastro.senha.value;

    if (!validaTexto(nome)) {
        alert("O campo Nome deve ter no mínimo 1 caractere.");
        document.cadastro.nome.focus();
        return false;
    }

    if (!validaTexto(sobrenome)) {
        alert("O campo Sobrenome deve ter no mínimo 1 caractere.");
        document.cadastro.sobrenome.focus();
        return false;
    }

    if (!validaEmail(email)) {
        alert("Digite um e-mail válido!");
        document.cadastro.email.focus();
        return false;
    }

    if (!validaSenha(senha)) {
        alert("O campo Senha deve ter no mínimo 8 caracteres.");
        document.cadastro.senha.focus();
        return false;
    }

    document.cadastro.submit();
}

