package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.*;
import pt.isec.pa.apoio_poe.model.memento.CareTaker;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SistemContext {
    Sistema sistem;
    ISistemState state;
    CareTaker careTaker;

    public SistemContext() {
        sistem = new Sistema(1);
        state = SistemState.CONFIGURATION_STATE.createState(this, sistem);
        careTaker = new CareTaker(sistem);
    }

    public void guarda(String nome){
        GuardaCarrega.guardar(sistem, careTaker, state.getState(), nome);
    }

    public void carrega(String nome) {
        Object[] sist = GuardaCarrega.carrega(nome);
        this.sistem = (Sistema) sist[0];
        this.careTaker= (CareTaker) sist[1];
        this.state = new ConfigurationState(this, sistem);
        change((SistemState) sist[2]);
        //this.state = new  GuardaState(this, sistem, (SistemState) sist[2]);
    }

    public void mostrarPastaSaves(){ sistem.mostrarPastaSaves(); }

    public int verificarPasta(String nomef){ return sistem.verificarPasta(nomef); }

    public void undo() { careTaker.undo(); }

    public void redo() { careTaker.redo(); }

    public String setMapStudentProposal(long nrAluno, String codProposta) {
        careTaker.gravaMemento();
        return state.setMapStudentProposal(nrAluno, codProposta);
        //String.valueOf(cm.invokeCommand(new AddProposalToStudent(sistem, nrAluno, codProposta)));
    }

    public String removeOneAssignment(long nrAluno) {
        careTaker.gravaMemento();
        return state.removeOneAssignment(nrAluno);
        //String.valueOf(cm.invokeCommand(new RemoveProposalFromStudent(sistem, nrAluno, codProposta)));
    }

    public String setMapProposalTeacher(String emailDocente, long nrAluno) {
        careTaker.gravaMemento();
        return state.setMapProposalTeacher(emailDocente, nrAluno);
    }

    public String editTeachersAssociation(String emailDocente, long nrEstudante) {
        careTaker.gravaMemento();
        return state.editTeachersAssociation(emailDocente, nrEstudante);
    }

    public String removeTeachersAssociation(String emailDocente) {
        careTaker.gravaMemento();
        return state.removeTeachersAssociation(emailDocente);
    }

    //métdos de acesso aos dados
    //normalmente para satisfazer necessidades da interface com o utilizador (não devem existir set's, alterações de dados)
    public int getCurrentFloor() {
        return sistem.getCurrentState();
    }

    public List<Students> getStudentsDraw() { return state.getStudentsDraw(); }

    public List<Students> getStudents() { return state.getStudents(); }

    public List<Teachers> getTeachers() { return state.getTeachers(); }

    public List<Proposals> getProposals() {
        return state.getProposals();
    }

    public List<Proposals> getTipoPropostas(int op) { return state.getTipoPropostas(op); }

    public int getRamos(int op) { return state.getRamos(op); }

    public int getassignedOrNotProposals(int op) { return  state.getassignedOrNotProposals(op); }

    public LinkedHashMap<String, Integer> companiesWithMoreInternships() { return state.companiesWithMoreInternships(); }

    public LinkedHashMap<String, Integer> teachersWithMoreAssignments() { return state.teachersWithMoreAssignments(); }

    public Map<Long, List<String>> getMapApplications() {
        return state.getMapApplications();
    }

    public Map<Students, Proposals> getMapStudentProposal() { return state.getMapStudentProposal(); }

    public Map<Proposals, Teachers> getMapProposalTeacher() { return state.getMapProposalTeacher(); }

    public  String getProposalTeacher() { return state.getProposalTeacher(); }

    public SistemState getState() {
        if (state == null)
            return null;
        return state.getState();
    }

    //package-private
    void changeState(ISistemState newState) {
        state = newState;
    }

    //métodos correspondentes às transições
    public boolean up() {
        return state.up();
    }

    public boolean down() {
        return state.down();
    }

    public boolean change(SistemState estado) { return state.change(estado); }

    public void setStudents(String filename){ state.setStudents(filename); }

    public boolean addStudent(Students student) {
        return state.addStudent(student);
    }

    public String removeStudent(long nrEstudante) { return  state.removeStudent(nrEstudante); }

    public String consultStudents() { return state.consultStudents(); }

    public Students searchStudent(long nrEstudante) { return state.searchStudent(nrEstudante); }

    public void setTeachers(String filename) { state.setTeachers(filename); }

    public boolean addTeachers(Teachers teacher) {
        return state.addTeachers(teacher);
    }

    public String removeTeachers(String email) { return state.removeTeachers(email); }

    public String consultTeachers() {
        return state.consultTeachers();
    }

    public Teachers searchTeacher(String eamil) {
        return state.searchTeacher(eamil);
    }

    public void setProposals(String filename) { state.setProposals(filename); }

    public boolean addProposals(Proposals proposal) {
        return state.addProposals(proposal);
    }

    public String editaProposta(String codigoIdentificacao, int op, String campo) { return state.editaProposta(codigoIdentificacao, op, campo); }

    public String editProposalsAdd(long nrAluno, String codProposta) { return state.editProposalsAdd(nrAluno, codProposta); }

    public String editProposalsRemove(long nrAluno, String codProposta) { return state.editProposalsRemove(nrAluno, codProposta); }

    public String removeApplications(long nrEstudante) { return state.removeApplications(nrEstudante); }

    public String removeProposal(String codigoIdentificacao) { return state.removeProposal(codigoIdentificacao); }

    public String consultProposals() {
        return state.consultProposals();
    }

    public Proposals searchProposal(String codigoIdentificacao) {
        return state.searchProposal(codigoIdentificacao);
    }

    public int typeProposal(Proposals proposal) {
        return state.typeProposal(proposal);
    }

    public void setApplications(String filename) { state.setApplications(filename); }

    public String addAplications(long nrAluno, List<String> codProposta) { return state.addAplications(nrAluno, codProposta); }

    public boolean removeApplication(long nrAluno) { return state.removeApplication(nrAluno); }

    public String consultApplications() { return state.consultApplications(); }

    public String consultSelfProposedStudents() { return state.consultSelfProposedStudents(); }

    public String CSWithAlreadyRegisteredApplication() { return state.CSWithAlreadyRegisteredApplication(); }

    public String CSwithoutRegisteredApplication() { return state.CSwithoutRegisteredApplication(); }

    public String studentsSelfProposals() { return state.studentsSelfProposals(); }

    public String teachersProposals() { return state.teachersProposals() ;}

    public String proposalsWithApplications() { return state.proposalsWithApplications(); }

    public String proposalsWithoutApplications() { return state.proposalsWithoutApplications(); }

    public void automaticSelfProprosals() { state.automaticSelfProprosals(); }

    public void automaticteachersProprosals() { state.automaticteachersProprosals(); }

    public boolean automaticStudentsWithoutAssignmentsProposals() { return state.automaticStudentsWithoutAssignmentsProposals(); }

    public boolean Draw(int op) { return state.Draw(op); }

    public List<Students> studentsWithouAssignments() { return state.studentsWithouAssignments(); }

    public String consultApplicationsStudentsInvolved() { return state.consultApplicationsStudentsInvolved(); }

    public void removeAssignments() { state.removeAssignments(); }

    public String CSwithProposalsAssigned() { return state.CSwithProposalsAssigned(); }

    public String CSwithoutProposalsAssigned() { return state.CSwithoutProposalsAssigned(); }

    public String availableProposals() { return state.availableProposals(); }

    public String assignedProposals() { return state.assignedProposals(); }

    public void automaticAssociationOfTeachersProposingProjects() { state.automaticAssociationOfTeachersProposingProjects(); }

    public String studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor() { return state.studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor(); }

    public String studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor() { return state.studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor(); }

    public String inMedium() { return state.inMedium(); }

    public String inMax() { return state.inMax(); }

    public String inMin() { return state.inMin(); }

    public String inTeacher(String emailDocente) { return state.inTeacher(emailDocente); }

    public String studentsWithoutAssignedProposalsAndWithApplicationOptions() { return state.studentsWithoutAssignedProposalsAndWithApplicationOptions(); }

    public boolean verificaArea(Proposals proposal, Students student) { return state.verificaArea(proposal, student); }

    public List<String> searchApplication(long nrAluno) { return state.searchApplication(nrAluno); }

    public boolean fecharState() { return state.fecharState(); }

    public void exportToFile(String filename, int type) throws Exception { state.exportToFile(filename, type); }

    public String editaEstudante(long nrEstudante, int op, String campo) { return state.editaEstudante(nrEstudante, op, campo); }

    public String editaDocente(String emailDocente, String nome) { return state.editaDocente(emailDocente, nome); }

    public String verificaNome(String titulo) { return state.verificaNome(titulo); }

    public String verificaEmail(String titulo) { return state.verificaEmail(titulo); }

    public String verificaCurso(String titulo) { return state.verificaCurso(titulo); }

    public String verificaRamo(String titulo) { return state.verificaRamo(titulo); }

    public double verificaClassificacao(String titulo) { return state.verificaClassificacao(titulo); }

}
