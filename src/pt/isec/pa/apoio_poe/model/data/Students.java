package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.Objects;

public class Students implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    //private static int contNr = 0;
    private long nrEstudante;
    private String nome;
    private String email;
    private String curso;
    private String ramo;
    private double classificacao;
    private boolean acedeEstagios;

    public Students(long nrEstudante, String nome, String email, String curso, String ramo, double classificacao, boolean acedeEstagios) {
        this.nrEstudante = nrEstudante;
        this.nome = nome;
        this.email = email;
        this.curso = curso;
        this.ramo = ramo;
        this.classificacao = classificacao;
        this.acedeEstagios = acedeEstagios;
    }

    public long getNrEstudante() {
        return nrEstudante;
    }

    public void setNrEstudante(long nrEstudante) {
        this.nrEstudante = nrEstudante;
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getRamo() {
        return ramo;
    }

    public void setRamo(String ramo) {
        this.ramo = ramo;
    }

    public double getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(double classificacao) {
        this.classificacao = classificacao;
    }

    public boolean isAcedeEstagios() {
        return acedeEstagios;
    }

    public void setAcedeEstagios(boolean acedeEstagios) {
        this.acedeEstagios = acedeEstagios;
    }

    @Override
    public Object clone() {
        try {
            Students newStudent = (Students) super.clone();
            return newStudent;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return nrEstudante + "," + nome + "," + email + ","  + curso + "," + ramo + "," + classificacao + "," + acedeEstagios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || !(o instanceof Students)) return false;

        Students students = (Students) o;

        return nrEstudante == students.getNrEstudante();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nrEstudante);
    }

}
