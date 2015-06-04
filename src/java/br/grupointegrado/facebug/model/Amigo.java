package br.grupointegrado.facebug.model;

/**
 * Classe que representa um relacionamento entre dois amigos.
 */
public class Amigo {

    private int id;
    private Usuario usuario;
    private Usuario amigo;
    private boolean aceitou;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getAmigo() {
        return amigo;
    }

    public void setAmigo(Usuario amigo) {
        this.amigo = amigo;
    }

    public boolean isAceitou() {
        return aceitou;
    }

    public void setAceitou(boolean aceitou) {
        this.aceitou = aceitou;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Amigo other = (Amigo) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
