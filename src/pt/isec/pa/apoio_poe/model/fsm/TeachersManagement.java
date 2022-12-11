package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Students;
import pt.isec.pa.apoio_poe.model.data.Teachers;

import java.util.List;

public class TeachersManagement extends SistemStateAdapter {
    public TeachersManagement(SistemContext context, Sistema sistem) {
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
    public void setTeachers(String filename) { sistem.setTeachers(filename); }

    @Override
    public boolean addTeachers(Teachers teacher) { return sistem.addTeacher(teacher); }

    @Override
    public String editaDocente(String emailDocente, String nome) { return sistem.editaDocente(emailDocente, nome); }

    @Override
    public String removeTeachers(String email) { return sistem.removeTeachers(email); }

    @Override
    public String consultTeachers() { return sistem.consultTeachers(); }

    @Override
    public Teachers searchTeacher(String eamil) {
        return sistem.searchTeacher(eamil);
    }

    @Override
    public String verificaNome(String titulo) { return sistem.verificaNome(titulo); }

    @Override
    public String verificaEmail(String titulo) { return sistem.verificaEmail(titulo); }

    @Override
    public List<Teachers> getTeachers() { return sistem.getTeachers(); }

    @Override
    public void exportToFile(String filename, int type) throws Exception { sistem.exportToFile(filename, type); }

    @Override
    public SistemState getState() {
        return SistemState.TEACHERS_MANAGEMENT;
    }

}
