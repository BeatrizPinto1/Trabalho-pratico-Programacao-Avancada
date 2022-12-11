package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Sistema;
import pt.isec.pa.apoio_poe.model.data.Students;
import pt.isec.pa.apoio_poe.model.data.Teachers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

abstract class SistemStateAdapter implements ISistemState{
    protected SistemContext context;
    protected Sistema sistem;

    protected SistemStateAdapter(SistemContext context, Sistema sistem) {
        this.context = context;
        this.sistem = sistem;
    }

    protected void changeState (SistemState newState){
        context.changeState(newState.createState(context, sistem));
    }

    @Override
    public boolean up() {
        return false;
    }

    @Override
    public boolean down() {
        return false;
    }

    @Override
    public boolean change(SistemState estado) { return false; }

    //STUDENTS
    @Override
    public void setStudents(String filename) {  }
    @Override
    public boolean addStudent(Students student) { return false; }
    @Override
    public String editaEstudante(long nrEstudante, int op, String campo) { return null; }
    @Override
    public String removeStudent(long nrEstudante) { return null; }
    @Override
    public String consultStudents() { return null; }
    @Override
    public Students searchStudent(long nrEstudante) { return null; }

    //TEACHERS
    @Override
    public void setTeachers(String filename) { }
    @Override
    public boolean addTeachers(Teachers teacher) { return false; }
    @Override
    public String editaDocente(String emailDocente, String nome) { return null; }
    @Override
    public String removeTeachers(String email) { return null; }
    @Override
    public String consultTeachers() { return null; }
    @Override
    public Teachers searchTeacher(String eamil) { return null; }

    //PROPOSALS
    @Override
    public void setProposals(String filename) { }
    @Override
    public boolean addProposals(Proposals proposal) { return false; }
    @Override
    public String editaProposta(String codigoIdentificacao, int op, String campo) { return null; }
    @Override
    public String editProposalsAdd(long nrAluno, String codProposta) { return null; }
    @Override
    public String editProposalsRemove(long nrAluno, String codProposta) { return null; }
    @Override
    public String removeApplications(long nrEstudante) { return null; }
    @Override
    public String removeProposal(String codigoIdentificacao) { return null; }
    @Override
    public String consultProposals() { return null; }
    @Override
    public Proposals searchProposal(String codigoIdentificacao) { return null; }
    @Override
    public int typeProposal(Proposals proposal) { return 0; }

    //CANDIDATURAS
    @Override
    public void setApplications(String filename) { }
    @Override
    public String addAplications(long nrAluno, List<String> codProposta) { return null; }
    @Override
    public boolean removeApplication(long nrAluno) { return false; }
    @Override
    public String consultApplications() { return null; }
    @Override
    public List<String> searchApplication(long nrAluno) { return null; }
    @Override
    public String consultSelfProposedStudents() { return null; }
    @Override
    public String CSWithAlreadyRegisteredApplication() { return null; }
    @Override
    public String CSwithoutRegisteredApplication() { return null; }
    @Override
    public String studentsSelfProposals() { return null; }
    @Override
    public String teachersProposals() { return null;}
    @Override
    public String proposalsWithApplications() { return null; }
    @Override
    public String proposalsWithoutApplications() { return null; }

    //ATRIBUICOES
    @Override
    public void automaticSelfProprosals() { }
    @Override
    public void automaticteachersProprosals() { }
    @Override
    public boolean automaticStudentsWithoutAssignmentsProposals() { return true; }
    @Override
    public boolean Draw(int op) { return true; }
    @Override
    public List<Students> getStudentsDraw() { return null; }
    @Override
    public List<Students> getStudents() { return null; }
    @Override
    public List<Teachers> getTeachers() { return null; }
    @Override
    public List<Proposals> getProposals() {
        return null;
    }
    @Override
    public List<Proposals> getTipoPropostas(int op) { return null; }
    @Override
    public int getRamos(int op) { return 0; }
    @Override
    public int getassignedOrNotProposals(int op) { return 0; }
    @Override
    public LinkedHashMap<String, Integer> companiesWithMoreInternships() { return null; }
    @Override
    public LinkedHashMap<String, Integer> teachersWithMoreAssignments() { return null; }
    @Override
    public Map<Long, List<String>> getMapApplications() {
        return null;
    }
    @Override
    public Map<Students, Proposals> getMapStudentProposal() { return null; }
    @Override
    public Map<Proposals, Teachers> getMapProposalTeacher() { return null; }
    @Override
    public  String getProposalTeacher() { return null; }
    @Override
    public List<Students> studentsWithouAssignments() { return null; }
    @Override
    public String consultApplicationsStudentsInvolved() { return null; }
    @Override
    public String setMapStudentProposal(long nrAluno, String codProposta) { return null; }
    @Override
    public String removeOneAssignment(long nrAluno) { return null; }
    @Override
    public void removeAssignments() { }
    @Override
    public String CSwithProposalsAssigned() { return null; }
    @Override
    public String CSwithoutProposalsAssigned() { return null; }
    @Override
    public String availableProposals() { return null; }
    @Override
    public String assignedProposals() { return null; }
    @Override
    public void automaticAssociationOfTeachersProposingProjects() { }
    @Override
    public String studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor() { return null; }
    @Override
    public String studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor() { return null; }
    @Override
    public String inMedium() { return null; }
    @Override
    public String inMax() { return null; }
    @Override
    public String inMin() { return null; }
    @Override
    public String inTeacher(String emailDocente) { return null; }
    @Override
    public String studentsWithoutAssignedProposalsAndWithApplicationOptions() { return null; }
    @Override
    public String setMapProposalTeacher(String emailDocente, long nrAluno) { return null; }
    @Override
    public String removeTeachersAssociation(String emailDocente) { return null; }
    @Override
    public String editTeachersAssociation(String emailDocente, long nrEstudante) { return null; }
    @Override
    public boolean verificaArea(Proposals proposal, Students student) { return false; }

    //FASES
    @Override
    public boolean fecharState() { return false; }
    @Override
    public void exportToFile(String filename, int type) throws Exception { }

    //VERIFICAÇÕES
    @Override
    public String verificaNome(String titulo) { return null; }
    @Override
    public String verificaEmail(String titulo) { return null; }
    @Override
    public String verificaCurso(String titulo) { return null; }
    @Override
    public String verificaRamo(String titulo) { return null; }
    @Override
    public double verificaClassificacao(String titulo) { return 0; }

}
