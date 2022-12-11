package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;

public class SelfProposedProject extends Proposals implements Serializable {
    private static final long serialVersionUID = 1L;

    public SelfProposedProject(String codIdentificacao, String titulo, long nrAluno) {
        super(codIdentificacao, titulo, nrAluno);
    }

    @Override
    public String toString() {
        return "T3," + codIdentificacao + "," + titulo + "," + nrAluno;
    }

    //cada aluno -> 1 proposta
}
