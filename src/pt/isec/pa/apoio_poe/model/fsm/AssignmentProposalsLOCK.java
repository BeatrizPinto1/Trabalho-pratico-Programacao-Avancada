package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Students;

import java.util.List;
import java.util.Map;

public class AssignmentProposalsLOCK extends SistemStateAdapter {
    public AssignmentProposalsLOCK(SistemContext context, Sistema sistem) {
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
    public List<Students> getStudents() { return sistem.getStudents(); }

    @Override
    public List<Proposals> getProposals() {
        return sistem.getProposals();
    }

    @Override
    public Map<Students, Proposals> getMapStudentProposal() { return sistem.getMapStudentProposal(); }

    @Override
    public String assignedProposals() { return sistem.assignedProposals(); }

    @Override
    public SistemState getState() {
        return SistemState.ASSIGNMENT_PROPOSALS_LOCK;
    }
}
