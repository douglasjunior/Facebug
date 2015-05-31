package br.grupointegrado.facebug.model;

import br.grupointegrado.facebug.util.ConversorUtil;

/**
 *
 * @author Rafael
 */
public class Foto {

    private int id;
    private byte[] foto;
    private int id_usuario;

    public Foto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

}
