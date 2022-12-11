package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Students;

import java.util.List;
import java.util.Map;

public class AssignmentProposalsState extends SistemStateAdapter {
    protected AssignmentProposalsState(SistemContext context, Sistema sistem) {
        super(context, sistem);
        sistem.setCurrentState(3);
    }

    @Override
    public boolean up() {
        changeState(SistemState.ASSIGNMENT_ADVISERS);
        return true;
    }

    @Override
    public boolean down() {
        if(sistem.getClosedPhases(1))
            changeState(SistemState.APPLICATION_OPTIONS_LOCK);
        else
            changeState(SistemState.APPLICATION_OPTIONS);
        return true;
    }

    @Override
    public boolean change(SistemState estado) {
        changeState(estado);
        return true;
    }

    @Override
    public void automaticSelfProprosals() { sistem.automaticSelfProprosals(); }

    @Override
    public void automaticteachersProprosals() { sistem.automaticteachersProprosals(); }

    @Override
    public boolean automaticStudentsWithoutAssignmentsProposals() { return sistem.automaticStudentsWithoutAssignmentsProposals(); }

    @Override
    public boolean Draw(int op) { return sistem.Draw(op); }

    @Override
    public List<Students> getStudentsDraw() { return sistem.getStudentsDraw(); }

    @Override
    public Map<Students, Proposals> getMapStudentProposal() { return sistem.getMapStudentProposal(); }

    @Override
    public List<Students> studentsWithouAssignments() { return sistem.studentsWithouAssignments(); }

    @Override
    public String consultApplicationsStudentsInvolved() { return sistem.consultApplicationsStudentsInvolved(); }

    @Override
    public Students searchStudent(long nrEstudante) { return sistem.searchStudent(nrEstudante); }

    @Override
    public Proposals searchProposal(String codigoIdentificacao) { return sistem.searchProposal(codigoIdentificacao); }

    @Override
    public int typeProposal(Proposals proposal) {return sistem.typeProposal(proposal); }

    @Override
    public String setMapStudentProposal(long nrAluno, String codProposta) { return sistem.setMapStudentProposal(nrAluno, codProposta);}

    @Override
    public String removeOneAssignment(long nrAluno) { return sistem.removeOneAssignment(nrAluno); }

    @Override
    public void removeAssignments() { sistem.removeAssignments(); }

    @Override
    public String consultSelfProposedStudents() { return sistem.consultSelfProposedStudents(); }

    @Override
    public String CSWithAlreadyRegisteredApplication() { return sistem.CSWithAlreadyRegisteredApplication(); }

    @Override
    public String CSwithProposalsAssigned() { return sistem.CSwithProposalsAssigned(); }

    @Override
    public String CSwithoutProposalsAssigned() { return sistem.CSwithoutProposalsAssigned(); }

    @Override
    public String studentsSelfProposals(){ return sistem.studentsSelfProposals(); }

    @Override
    public String teachersProposals(){ return sistem.teachersProposals(); }

    @Override
    public String availableProposals() { return sistem.availableProposals(); }

    @Override
    public String assignedProposals() { return sistem.assignedProposals(); }

    @Override
    public List<Students> getStudents() { return sistem.getStudents(); }

    @Override
    public List<Proposals> getProposals() {
        return sistem.getProposals();
    }

    @Override
    public boolean verificaArea(Proposals proposal, Students student) { return sistem.verificaArea(proposal, student); }

    @Override
    public void exportToFile(String filename, int type) throws Exception { sistem.exportToFile(filename, type); }

    @Override
    public boolean fecharState() { return sistem.fecharAssignmentProposalsState(); }

    @Override
    public SistemState getState() {
        return SistemState.ASSIGNMENT_PROPOSALS;
    }
}
