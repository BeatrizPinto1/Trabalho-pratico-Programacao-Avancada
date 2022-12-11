package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Students;

import java.util.List;
import java.util.Map;

public class ApplicationOptionsLOCK extends SistemStateAdapter {
    public ApplicationOptionsLOCK(SistemContext context, Sistema sistem) {
        super(context, sistem);
        sistem.setCurrentState(2);
    }

    @Override
    public boolean up() { //e subir tambem
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
    public List<Students> getStudents() { return sistem.getStudents(); }

    @Override
    public List<Proposals> getProposals() {
        return sistem.getProposals();
    }

    @Override
    public Map<Long, List<String>> getMapApplications() {
        return sistem.getMapApplications();
    }

    @Override
    public SistemState getState() {
        return SistemState.APPLICATION_OPTIONS_LOCK;
    }
}

