package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;

public class Projects extends Proposals implements Serializable {
    private static final long serialVersionUID = 1L;

    private String areaDestino;
    private String emailDocente;

    public Projects(String codIdentificacao, String areaDestino, String titulo, String emailDocente) {
        super(codIdentificacao, titulo);
        this.areaDestino = areaDestino;
        this.emailDocente = emailDocente;
    }

    public Projects(String codIdentificacao, String areaDestino, String titulo, String emailDocente, long nrAluno) {
        super(codIdentificacao, titulo, nrAluno);
        this.areaDestino = areaDestino;
        this.emailDocente = emailDocente;
    }

    public String getAreaDestino() {
        return areaDestino;
    }

    public void setAreaDestino(String areaDestino) {
        this.areaDestino = areaDestino;
    }

    public String getEmailDocente() {
        return emailDocente;
    }

    public void setEmailDocente(String emailDocente) {
        this.emailDocente = emailDocente;
    }

    @Override
    public String toString() {
        return "T2," + codIdentificacao + "," + areaDestino + "," + titulo + "," + emailDocente + "," + nrAluno;
    }

}
