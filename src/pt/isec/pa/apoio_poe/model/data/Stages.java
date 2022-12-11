package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;

public class Stages extends Proposals implements Serializable {
    private static final long serialVersionUID = 1L;

    private String areaDestino;
    private String identidadeAcolhimento;

    public Stages(String codIdentificacao, String areaDestino, String titulo, String identidadeAcolhimento) {
        super(codIdentificacao, titulo);
        this.areaDestino = areaDestino;
        this.identidadeAcolhimento = identidadeAcolhimento;
    }

    public Stages(String codIdentificacao, String areaDestino, String titulo, String identidadeAcolhimento, long nrAluno) {
        super(codIdentificacao, titulo, nrAluno);
        this.areaDestino = areaDestino;
        this.identidadeAcolhimento = identidadeAcolhimento;
    }

    public String getAreaDestino() {
        return areaDestino;
    }

    public void setAreaDestino(String areaDestino) {
        this.areaDestino = areaDestino;
    }

    public String getIdentidadeAcolhimento() {
        return identidadeAcolhimento;
    }

    public void setIdentidadeAcolhimento(String identidadeAcolhimento) {
        this.identidadeAcolhimento = identidadeAcolhimento;
    }

    @Override
    public String toString() {
        return "T1," + codIdentificacao + "," + areaDestino + "," + titulo + "," + identidadeAcolhimento + "," + nrAluno;
    }
}
