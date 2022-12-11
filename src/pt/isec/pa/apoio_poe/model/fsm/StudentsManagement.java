package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Students;

import java.util.List;

public class StudentsManagement extends SistemStateAdapter {
    protected StudentsManagement(SistemContext context, Sistema sistem) {
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
    public void setStudents(String filename) { sistem.setStudents(filename); }

    @Override
    public boolean addStudent(Students student) { return sistem.addStudent(student); } //changeState(SistemState.CONFIGURATION_STATE);

    @Override
    public String removeStudent(long nrEstudante) { return sistem.removeStudent(nrEstudante); }

    @Override
    public String consultStudents() { return sistem.consultStudents(); }

    @Override
    public Students searchStudent(long nrEstudante) { return sistem.searchStudent(nrEstudante); }

    @Override
    public String editaEstudante(long nrEstudante, int op, String campo) { return sistem.editaEstudante(nrEstudante, op, campo); }

    @Override
    public String verificaNome(String titulo) { return sistem.verificaNome(titulo); }

    @Override
    public String verificaEmail(String titulo) { return sistem.verificaEmail(titulo); }

    @Override
    public String verificaCurso(String titulo) { return sistem.verificaCurso(titulo); }

    @Override
    public String verificaRamo(String titulo) { return sistem.verificaRamo(titulo); }

    @Override
    public double verificaClassificacao(String titulo) { return sistem.verificaClassificacao(titulo); }

    @Override
    public void exportToFile(String filename, int type) throws Exception { sistem.exportToFile(filename, type); }

    @Override
    public List<Students> getStudents() { return sistem.getStudents(); }

    @Override
    public SistemState getState() { return SistemState.STUDENTS_MANAGEMENT; }

}
