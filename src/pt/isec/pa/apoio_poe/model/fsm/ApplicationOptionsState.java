package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Students;

import java.util.List;
import java.util.Map;

public class ApplicationOptionsState extends SistemStateAdapter {
    protected ApplicationOptionsState(SistemContext context, Sistema sistem) {
        super(context, sistem);
        sistem.setCurrentState(2);
    }

    @Override
    public boolean up() {
        if(sistem.getClosedPhases(2))
            changeState(SistemState.ASSIGNMENT_PROPOSALS_LOCK);
        else
            changeState(SistemState.ASSIGNMENT_PROPOSALS);
        return true;
    }

    @Override
    public boolean down() {
        if(sistem.getClosedPhases(0))
            changeState(SistemState.CONFIGURATION_STATE_LOCK);
        else
            changeState(SistemState.CONFIGURATION_STATE);
        return true;
    }

    public boolean change(SistemState estado) {
        changeState(estado);
        return true;
    }

    @Override
    public void setApplications(String filename) { sistem.setApplications(filename); }

    @Override
    public String addAplications(long nrAluno, List<String> codProposta) { return sistem.addAplications(nrAluno, codProposta); }

    @Override
    public String editProposalsAdd(long nrAluno, String codProposta) { return sistem.editProposalsAdd(nrAluno, codProposta); }

    @Override
    public String editProposalsRemove(long nrAluno, String codProposta) { return sistem.editProposalsRemove(nrAluno, codProposta); }

    @Override
    public String removeApplications(long nrEstudante) { return sistem.removeApplications(nrEstudante); }

    @Override
    public boolean removeApplication(long nrAluno) { return sistem.removeApplication(nrAluno); }

    @Override
    public String consultApplications() {return sistem.consultApplications(); }

    @Override
    public String consultSelfProposedStudents() { return sistem.consultSelfProposedStudents(); }

    @Override
    public String CSWithAlreadyRegisteredApplication() { return sistem.CSWithAlreadyRegisteredApplication(); }

    @Override
    public String CSwithoutRegisteredApplication() { return sistem.CSwithoutRegisteredApplication(); }

    @Override
    public String studentsSelfProposals() { return sistem.studentsSelfProposals(); }

    @Override
    public String teachersProposals() { return sistem.teachersProposals() ;}

    @Override
    public String proposalsWithApplications() { return sistem.proposalsWithApplications(); }

    @Override
    public String proposalsWithoutApplications() { return sistem.proposalsWithoutApplications(); }

    @Override
    public List<String> searchApplication(long nrAluno) { return sistem.searchApplication(nrAluno); }

    @Override
    public Students searchStudent(long nrEstudante) { return sistem.searchStudent(nrEstudante); }

    @Override
    public Proposals searchProposal(String codigoIdentificacao) { return sistem.searchProposal(codigoIdentificacao); }

    @Override
    public Map<Long, List<String>> getMapApplications() {
        return sistem.getMapApplications();
    }

    @Override
    public List<Students> getStudents() { return sistem.getStudents(); }

    @Override
    public List<Proposals> getProposals() {
        return sistem.getProposals();
    }

    @Override
    public void exportToFile(String filename, int type) throws Exception { sistem.exportToFile(filename, type); }

    @Override
    public boolean fecharState() { return sistem.fecharApplicationOptionsState(); }

    @Override
    public SistemState getState() {
        return SistemState.APPLICATION_OPTIONS;
    }
}
