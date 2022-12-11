package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Teachers;

import java.util.LinkedHashMap;
import java.util.List;

public class ConsultState extends SistemStateAdapter {
    protected ConsultState(SistemContext context, Sistema sistem) {
        super(context, sistem);
        sistem.setCurrentState(5);
    }

    @Override
    public String studentsWithoutAssignedProposalsAndWithApplicationOptions() { return sistem.studentsWithoutAssignedProposalsAndWithApplicationOptions(); }

    @Override
    public String CSwithProposalsAssigned() { return sistem.CSwithProposalsAssigned(); }

    @Override
    public int getRamos(int op) { return sistem.getRamos(op); }

    @Override
    public int getassignedOrNotProposals(int op) { return sistem.getassignedOrNotProposals(op); }

    @Override
    public LinkedHashMap<String, Integer> companiesWithMoreInternships() { return sistem.companiesWithMoreInternships(); }

    @Override
    public LinkedHashMap<String, Integer> teachersWithMoreAssignments() { return sistem.teachersWithMoreAssignments(); }

    @Override
    public String availableProposals() { return sistem.availableProposals(); }

    @Override
    public String assignedProposals() { return sistem.assignedProposals(); }

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
    public void exportToFile(String filename, int type) throws Exception { sistem.exportToFile(filename, type); }

    @Override
    public SistemState getState() { return SistemState.CONSULT_STATE; }
}
