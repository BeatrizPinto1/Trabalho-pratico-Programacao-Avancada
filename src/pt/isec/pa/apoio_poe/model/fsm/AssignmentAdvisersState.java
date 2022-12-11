package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Teachers;

import java.util.List;
import java.util.Map;

public class AssignmentAdvisersState extends SistemStateAdapter {
    protected AssignmentAdvisersState(SistemContext context, Sistema sistem) {
        super(context, sistem);
        sistem.setCurrentState(4);
    }

    @Override
    public boolean up() {
        changeState(SistemState.CONSULT_STATE);
        return true;
    }

    @Override
    public boolean down() {
        if(sistem.getClosedPhases(2)) {
            changeState(SistemState.ASSIGNMENT_PROPOSALS_LOCK);
        }
        else
            changeState(SistemState.ASSIGNMENT_PROPOSALS);
        return true;
    }

    @Override
    public void automaticAssociationOfTeachersProposingProjects() { sistem.automaticAssociationOfTeachersProposingProjects(); }

    @Override
    public String studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor() { return sistem.studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor(); }

    @Override
    public String studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor() { return sistem.studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor(); }

    @Override
    public Map<Proposals, Teachers>getMapProposalTeacher() { return sistem.getMapProposalTeacher(); }

    @Override
    public  String getProposalTeacher() { return sistem.getProposalTeacher(); }

    @Override
    public List<Teachers> getTeachers() { return sistem.getTeachers(); }

    @Override
    public List<Proposals> getProposals() {
        return sistem.getProposals();
    }

    @Override
    public String inMedium() { return sistem.inMedium(); }

    @Override
    public String inMax() { return sistem.inMax(); }

    @Override
    public String inMin() { return sistem.inMin(); }

    @Override
    public String inTeacher(String emailDocente) { return sistem.inTeacher(emailDocente); }

    @Override
    public Teachers searchTeacher(String eamil) {
        return sistem.searchTeacher(eamil);
    }

    @Override
    public Proposals searchProposal(String codigoIdentificacao) {
        return sistem.searchProposal(codigoIdentificacao);
    }

    @Override
    public int typeProposal(Proposals proposal) {
        return sistem.typeProposal(proposal);
    }

    @Override
    public String setMapProposalTeacher(String emailDocente, long nrAluno) { return sistem.setMapProposalTeacher(emailDocente, nrAluno); }

    @Override
    public String editTeachersAssociation(String emailDocente, long nrEstudante) { return sistem.editTeachersAssociation(emailDocente, nrEstudante); }

    @Override
    public String removeTeachersAssociation(String emailDocente) { return sistem.removeTeachersAssociation(emailDocente); }

    @Override
    public void exportToFile(String filename, int type) throws Exception { sistem.exportToFile(filename, type); }

    @Override
    public SistemState getState() {
        return SistemState.ASSIGNMENT_ADVISERS;
    }
}
