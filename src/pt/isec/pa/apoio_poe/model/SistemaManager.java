package pt.isec.pa.apoio_poe.model;

import pt.isec.pa.apoio_poe.model.data.*;
import pt.isec.pa.apoio_poe.model.fsm.ISistemState;
import pt.isec.pa.apoio_poe.model.fsm.SistemContext;
import pt.isec.pa.apoio_poe.model.fsm.SistemState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SistemaManager {
    private SistemContext fsm;
    PropertyChangeSupport pcs;

    public SistemaManager() {
        fsm = new SistemContext();
        pcs = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void guarda(String nome){
        fsm.guarda(nome);
        pcs.firePropertyChange(null,null,null);
    }

    public void carrega(String nome) {
        fsm.carrega(nome);
        pcs.firePropertyChange(null,null,null);
    }

    public void mostrarPastaSaves(){ fsm.mostrarPastaSaves(); }

    public int verificarPasta(String nomef){ return fsm.verificarPasta(nomef); }

    public void undo() {
        fsm.undo();
        pcs.firePropertyChange(null,null,null);
    }

    public void redo() {
        fsm.redo();
        pcs.firePropertyChange(null,null,null);
    }

    public String setMapStudentProposal(long nrAluno, String codProposta) {
        var ret = setMapStudentProposal(nrAluno, codProposta);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String removeOneAssignment(long nrAluno) {
        var ret = fsm.removeOneAssignment(nrAluno);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String setMapProposalTeacher(String emailDocente, long nrAluno) {
        var ret = fsm.setMapProposalTeacher(emailDocente, nrAluno);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String editTeachersAssociation(String emailDocente, long nrEstudante) {
        var ret = fsm.editTeachersAssociation(emailDocente, nrEstudante);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String removeTeachersAssociation(String emailDocente) {
        var ret = fsm.removeTeachersAssociation(emailDocente);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    //métdos de acesso aos dados
    //normalmente para satisfazer necessidades da interface com o utilizador (não devem existir set's, alterações de dados)
    public int getCurrentFloor() { return fsm.getCurrentFloor(); }

    public List<Students> getStudentsDraw() { return fsm.getStudentsDraw(); }

    public List<Students> getStudents() { return fsm.getStudents(); }

    public List<Teachers> getTeachers() { return fsm.getTeachers(); }

    public List<Proposals> getProposals() {
        return fsm.getProposals();
    }

    public List<Proposals> getTipoPropostas(int op) { return fsm.getTipoPropostas(op); }

    public int getRamos(int op) { return fsm.getRamos(op); }

    public int getassignedOrNotProposals(int op) { return fsm.getassignedOrNotProposals(op); }

    public LinkedHashMap<String, Integer> companiesWithMoreInternships() { return fsm.companiesWithMoreInternships(); }

    public LinkedHashMap<String, Integer> teachersWithMoreAssignments() { return fsm.teachersWithMoreAssignments(); }

    public Map<Long, List<String>> getMapApplications() {
        return fsm.getMapApplications();
    }

    public Map<Students, Proposals> getMapStudentProposal() { return fsm.getMapStudentProposal(); }

    public Map<Proposals, Teachers> getMapProposalTeacher() { return fsm.getMapProposalTeacher(); }

    public  String getProposalTeacher() { return fsm.getProposalTeacher(); }

    public SistemState getState() { return fsm.getState(); }

    //package-private
    void changeState(ISistemState newState) {
        changeState(newState);
        pcs.firePropertyChange(null,null,null);
    }

    //métodos correspondentes às transições
    public boolean up() {
        var ret = fsm.up();
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public boolean down() {
        var ret = fsm.down();
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public boolean change(SistemState estado) {
        var ret = fsm.change(estado);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public void setStudents(String filename) {
        fsm.setStudents(filename);
        pcs.firePropertyChange(null,null,null);
    }

    public boolean addStudent(Students student) {
        var ret = fsm.addStudent(student);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String editaEstudante(long nrEstudante, int op, String campo) {
        var ret = fsm.editaEstudante(nrEstudante, op, campo);
        pcs.firePropertyChange(null, null, null);
        return ret;
    }

    public String removeStudent(long nrEstudante) {
        var ret = fsm.removeStudent(nrEstudante);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String consultStudents() { return fsm.consultStudents(); }

    public Students searchStudent(long nrEstudante) { return fsm.searchStudent(nrEstudante);}

    public void setTeachers(String filename) {
        fsm.setTeachers(filename);
        pcs.firePropertyChange(null,null,null);
    }

    public boolean addTeachers(Teachers teacher) {
        var ret = fsm.addTeachers(teacher);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String editaDocente(String emailDocente, String nome) {
        var ret = fsm.editaDocente(emailDocente, nome);
        pcs.firePropertyChange(null, null, null);
        return ret;
    }
    public String removeTeachers(String email) {
        var ret = fsm.removeTeachers(email);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String consultTeachers() {
        var ret = fsm.consultTeachers();
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public Teachers searchTeacher(String email) {
        var ret = fsm.searchTeacher(email);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public void setProposals(String filename) {
        fsm.setProposals(filename);
        pcs.firePropertyChange(null,null,null);
    }

    public boolean addProposals(Proposals proposal) {
        var ret = fsm.addProposals(proposal);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String editaProposta(String codigoIdentificacao, int op, String campo) {
        var ret = fsm.editaProposta(codigoIdentificacao, op, campo);
        pcs.firePropertyChange(null, null, null);
        return ret;
    }

    public String editProposalsAdd(long nrAluno, String codProposta) {
        var ret = fsm.editProposalsAdd(nrAluno,codProposta);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String editProposalsRemove(long nrAluno, String codProposta) {
        var ret = fsm.editProposalsRemove(nrAluno,codProposta);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String removeApplications(long nrEstudante) {
        var ret = fsm.removeApplications(nrEstudante);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String removeProposal(String codigoIdentificacao) {
        var ret = fsm.removeProposal(codigoIdentificacao);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String consultProposals() {
        var ret = fsm.consultProposals();
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public Proposals searchProposal(String codigoIdentificacao) {
        var ret = fsm.searchProposal(codigoIdentificacao);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public int typeProposal(Proposals proposal){
        var ret = fsm.typeProposal(proposal);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public void setApplications(String filename) {
        fsm.setApplications(filename);
        pcs.firePropertyChange(null,null,null);
    }

    public String addAplications(long nrAluno, List<String> codProposta) {
        var ret = fsm.addAplications(nrAluno,codProposta);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public boolean removeApplication(long nrAluno) {
        var ret = fsm.removeApplication(nrAluno);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public String consultApplications() { return fsm.consultApplications(); }

    public String consultSelfProposedStudents() { return fsm.consultSelfProposedStudents(); }

    public String CSWithAlreadyRegisteredApplication() { return fsm.CSWithAlreadyRegisteredApplication(); }

    public String CSwithoutRegisteredApplication() { return fsm.CSwithoutRegisteredApplication(); }

    public String studentsSelfProposals() { return fsm.studentsSelfProposals(); }

    public String teachersProposals() { return fsm.teachersProposals();}

    public String proposalsWithApplications() { return fsm.proposalsWithApplications(); }

    public String proposalsWithoutApplications() { return fsm.proposalsWithoutApplications(); }

    public void automaticSelfProprosals() {
        fsm.automaticSelfProprosals();
        pcs.firePropertyChange(null,null,null);
    }

    public void automaticteachersProprosals() {
        fsm.automaticteachersProprosals();
        pcs.firePropertyChange(null,null,null);
    }

    public boolean automaticStudentsWithoutAssignmentsProposals() {
        var ret = fsm.automaticStudentsWithoutAssignmentsProposals();
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public boolean Draw(int op) {
        var ret = fsm.Draw(op);
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public List<Students> studentsWithouAssignments() { return fsm.studentsWithouAssignments(); }

    public String consultApplicationsStudentsInvolved() { return fsm.consultApplicationsStudentsInvolved(); }

    public void removeAssignments() {
        fsm.removeAssignments();
        pcs.firePropertyChange(null,null,null);
    }

    public String CSwithProposalsAssigned() { return fsm.CSwithProposalsAssigned(); }

    public String CSwithoutProposalsAssigned() { return fsm.CSwithoutProposalsAssigned(); }

    public String availableProposals() { return fsm.availableProposals(); }

    public String assignedProposals() { return fsm.assignedProposals(); }

    public void automaticAssociationOfTeachersProposingProjects() {
        fsm.automaticAssociationOfTeachersProposingProjects();
        pcs.firePropertyChange(null,null,null);
    }

    public String studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor() { return fsm.studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor(); }

    public String studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor() { return fsm.studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor(); }

    public String inMedium() { return fsm.inMedium(); }

    public String inMax() { return fsm.inMax(); }

    public String inMin() { return fsm.inMin(); }

    public String inTeacher(String emailDocente) { return fsm.inTeacher(emailDocente); }

    public String studentsWithoutAssignedProposalsAndWithApplicationOptions() { return fsm.studentsWithoutAssignedProposalsAndWithApplicationOptions(); }

    public boolean verificaArea(Proposals proposal, Students student) { return fsm.verificaArea(proposal, student); }

    public List<String> searchApplication(long nrAluno) { return fsm.searchApplication(nrAluno); }

    public boolean fecharState() {
        var ret = fsm.fecharState();
        pcs.firePropertyChange(null,null,null);
        return ret;
    }

    public void exportToFile(String filename, int type) throws Exception {
        fsm.exportToFile(filename, type);
        pcs.firePropertyChange(null,null,null);
    }

    public String verificaNome(String titulo) { return fsm.verificaNome(titulo); }

    public String verificaEmail(String titulo) { return fsm.verificaEmail(titulo); }

    public String verificaCurso(String titulo) { return fsm.verificaCurso(titulo); }

    public String verificaRamo(String titulo) { return fsm.verificaRamo(titulo); }

    public double verificaClassificacao(String titulo) { return fsm.verificaClassificacao(titulo); }

}
