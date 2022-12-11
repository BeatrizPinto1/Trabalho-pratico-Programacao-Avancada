package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.Objects;

public class Proposals implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    protected String codIdentificacao;
    protected String titulo;
    protected long nrAluno;

    public Proposals(String codIdentificacao, String titulo) {
        this.codIdentificacao = codIdentificacao;
        this.titulo = titulo;
    }

    public Proposals(String codIdentificacao, String titulo, long nrAluno) { //NAO PRECISA
        this.codIdentificacao = codIdentificacao;
        this.titulo = titulo;
        this.nrAluno = nrAluno;
    }

    public String getCodIdentificacao() {
        return codIdentificacao;
    }

    public void setCodIdentificacao(String codIdentificacao) {
        this.codIdentificacao = codIdentificacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public long getNrAluno() {
        return nrAluno;
    }

    public void setNrAluno(long nrAluno) {
        this.nrAluno = nrAluno;
    }

    @Override
    public Object clone() {
        try {
            Proposals newProposal = (Proposals) super.clone();
            return newProposal;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return codIdentificacao + "," + titulo + "," + nrAluno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || (!(o instanceof Stages) && !(o instanceof Projects) && !(o instanceof SelfProposedProject))) {
            return false;
        }

        Proposals proposals = (Proposals) o;

        return Objects.equals(codIdentificacao, proposals.getCodIdentificacao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(codIdentificacao);
    }
}
