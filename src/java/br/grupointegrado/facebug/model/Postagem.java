package br.grupointegrado.facebug.model;

import br.grupointegrado.facebug.util.ConversorUtil;
import java.util.Date;

public class Postagem {

    private int id;
    private String texto;
    private Date data;
    private Usuario usuario;
    private boolean publica;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public String getDataToString(){
        return ConversorUtil.dateTimeParaString(getData());
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isPublica() {
        return publica;
    }

    public void setPublica(boolean publica) {
        this.publica = publica;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
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
        final Postagem other = (Postagem) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
