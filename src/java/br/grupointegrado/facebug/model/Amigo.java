package br.grupointegrado.facebug.model;

/**
 * Classe que representa um relacionamento entre dois amigos.
 */
public class Amigo {

    private Usuario usuario;
    private Usuario amigo;
    private boolean aceitou;

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
        int hash = 7;
        hash = 23 * hash + (this.usuario != null ? this.usuario.hashCode() : 0);
        hash = 23 * hash + (this.amigo != null ? this.amigo.hashCode() : 0);
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
        if (this.usuario != other.usuario && (this.usuario == null || !this.usuario.equals(other.usuario))) {
            return false;
        }
        if (this.amigo != other.amigo && (this.amigo == null || !this.amigo.equals(other.amigo))) {
            return false;
        }
        return true;
    }
    
}
