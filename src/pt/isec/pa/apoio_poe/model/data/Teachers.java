package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.Objects;

public class Teachers implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    //boolean orientadorOUprojeto; //true - orientador //flase - proponente de projeto

    public Teachers(String nome, String email) {
        this.nome = nome;
        this.email = email;
        //this.orientadorOUprojeto = false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public boolean isOrientadorOUprojeto() {
        return orientadorOUprojeto;
    }

    public void setOrientadorOUprojeto(boolean orientadorOUprojeto) {
        this.orientadorOUprojeto = orientadorOUprojeto;
    }*/

    @Override
    public Object clone() {
        try {
            Teachers newTeacher = (Teachers) super.clone();
            return newTeacher;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() { return nome + "," + email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || !(o instanceof Teachers)) return false;

        Teachers teachers = (Teachers) o;

        return Objects.equals(email, teachers.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
