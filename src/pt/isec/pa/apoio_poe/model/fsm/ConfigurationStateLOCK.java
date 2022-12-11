package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Students;
import pt.isec.pa.apoio_poe.model.data.Teachers;

import java.util.List;

public class ConfigurationStateLOCK extends SistemStateAdapter {
    public ConfigurationStateLOCK(SistemContext context, Sistema sistem) {
        super(context, sistem);
        sistem.setCurrentState(1);
    }

    @Override
    public boolean up() {
        if(sistem.getClosedPhases(1))
            changeState(SistemState.APPLICATION_OPTIONS_LOCK);
        else
            changeState(SistemState.APPLICATION_OPTIONS);
        return true;
    }

    @Override
    public String consultStudents() { return sistem.consultStudents(); }

    @Override
    public String consultTeachers() { return sistem.consultTeachers(); }

    @Override
    public String consultProposals() { return sistem.consultProposals(); }

    @Override
    public List<Students> getStudents() { return sistem.getStudents(); }

    @Override
    public List<Teachers> getTeachers() { return sistem.getTeachers(); }

    @Override
    public List<Proposals> getProposals() {
        return sistem.getProposals();
    }

    @Override
    public List<Proposals> getTipoPropostas(int op) {return sistem.getTipoPropostas(op); }

    @Override
    public SistemState getState() {
        return SistemState.CONFIGURATION_STATE_LOCK;
    }

}
