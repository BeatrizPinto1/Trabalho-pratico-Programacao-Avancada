package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Students;
import pt.isec.pa.apoio_poe.model.data.Teachers;

import java.util.List;
import java.util.Map;

public class ProposalsManagement extends SistemStateAdapter{
    public ProposalsManagement(SistemContext context, Sistema sistem) {
        super(context, sistem);
        sistem.setCurrentState(1);
    }

    @Override
    public boolean up() {
        changeState(SistemState.APPLICATION_OPTIONS);
        return true;
    }

    @Override
    public boolean change(SistemState estado) {
        changeState(estado);
        return true;
    }

    @Override
    public void setProposals(String filename) { sistem.setProposals(filename); }

    @Override
    public boolean addProposals(Proposals proposal) { return sistem.addProposals(proposal); }

    @Override
    public String editaProposta(String codigoIdentificacao, int op, String campo) { return sistem.editaProposta(codigoIdentificacao, op, campo); }

    @Override
    public String removeProposal(String codigoIdentificacao) { return sistem.removeProposal(codigoIdentificacao); }

    @Override
    public String consultProposals() { return sistem.consultProposals(); }

    @Override
    public Proposals searchProposal(String codigoIdentificacao) { return sistem.searchProposal(codigoIdentificacao); }

    @Override
    public int typeProposal(Proposals proposal) {return sistem.typeProposal(proposal); }

    @Override
    public Students searchStudent(long nrEstudante) { return sistem.searchStudent(nrEstudante); }

    @Override
    public Teachers searchTeacher(String email) { return sistem.searchTeacher(email); }

    @Override
    public Map<Long, List<String>> getMapApplications() {
        return sistem.getMapApplications();
    }

    @Override
    public String verificaEmail(String titulo) { return sistem.verificaEmail(titulo); }

    @Override
    public String verificaRamo(String titulo) { return sistem.verificaRamo(titulo); }

    @Override
    public void exportToFile(String filename, int type) throws Exception { sistem.exportToFile(filename, type); }

    @Override
    public List<Proposals> getProposals() {
        return sistem.getProposals();
    }

    @Override
    public List<Proposals> getTipoPropostas(int op) {return sistem.getTipoPropostas(op); }

    @Override
    public SistemState getState() {
        return SistemState.PROPOSALS_MANAGEMENT;
    }

}
