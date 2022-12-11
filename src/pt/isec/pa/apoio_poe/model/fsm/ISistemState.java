package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Proposals;
import pt.isec.pa.apoio_poe.model.data.Students;
import pt.isec.pa.apoio_poe.model.data.Teachers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ISistemState {
    boolean up();
    boolean down();
    boolean change(SistemState estado);
    boolean fecharState();

    //STUDENTS
    void setStudents(String filename);
    boolean addStudent(Students student);
    String editaEstudante(long nrEstudante, int op, String campo);
    String removeStudent(long nrEstudante);
    String consultStudents();
    Students searchStudent(long nrEstudante);

    //TEACHERS
    void setTeachers(String filename);
    boolean addTeachers(Teachers teacher);
    String editaDocente(String emailDocente, String nome);
    String removeTeachers(String email);
    String consultTeachers();
    Teachers searchTeacher(String eamil);

    //PROPOSALS
    void setProposals(String filename);
    boolean addProposals(Proposals proposal);
    String editaProposta(String codigoIdentificacao, int op, String campo);
    String editProposalsAdd(long nrAluno, String codProposta);
    String editProposalsRemove(long nrAluno, String codProposta);
    String removeApplications(long nrEstudante);
    String removeProposal(String codigoIdentificacao);
    String consultProposals();
    Proposals searchProposal(String codigoIdentificacao);
    int typeProposal(Proposals proposal);

    //CANDIDATIRAS
    void setApplications(String filename);
    String addAplications(long nrAluno, List<String> codProposta);
    boolean removeApplication(long nrAluno);
    String consultApplications();
    List<String> searchApplication(long nrAluno);
    String consultSelfProposedStudents();
    String CSWithAlreadyRegisteredApplication();
    String CSwithoutRegisteredApplication();
    String studentsSelfProposals();
    String teachersProposals();
    String proposalsWithApplications();
    String proposalsWithoutApplications();

    //ATRIBUICOES
    void automaticSelfProprosals();
    void automaticteachersProprosals();
    boolean automaticStudentsWithoutAssignmentsProposals();
    boolean Draw(int op);
    List<Students> getStudentsDraw();
    List<Students> getStudents();
    List<Teachers> getTeachers();
    List<Proposals> getProposals();
    List<Proposals> getTipoPropostas(int op);
    int getRamos(int op);
    int getassignedOrNotProposals(int op);
    LinkedHashMap<String, Integer> companiesWithMoreInternships();
    LinkedHashMap<String, Integer> teachersWithMoreAssignments();
    Map<Long, List<String>> getMapApplications();
    Map<Students, Proposals> getMapStudentProposal();
    List<Students> studentsWithouAssignments();
    Map<Proposals, Teachers>  getMapProposalTeacher();
    String getProposalTeacher();
    String consultApplicationsStudentsInvolved();
    String setMapStudentProposal(long nrAluno, String codProposta);
    String removeOneAssignment(long nrAluno);
    void removeAssignments();
    String CSwithProposalsAssigned();
    String CSwithoutProposalsAssigned();
    String availableProposals();
    String assignedProposals();
    void automaticAssociationOfTeachersProposingProjects();
    String studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor();
    String studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor();
    String inMedium();
    String inMax();
    String inMin();
    String inTeacher(String emailDocente);
    String studentsWithoutAssignedProposalsAndWithApplicationOptions();
    String setMapProposalTeacher(String emailDocente, long nrAluno);
    String editTeachersAssociation(String emailDocente, long nrEstudante);
    String removeTeachersAssociation(String emailDocente);
    boolean verificaArea(Proposals proposal, Students student);

    void exportToFile(String filename, int type) throws Exception;

    //VERIFICAÇÕES
    String verificaNome(String titulo);
    String verificaEmail(String titulo);
    String verificaCurso(String titulo);
    String verificaRamo(String titulo);
    double verificaClassificacao(String titulo);

    SistemState getState();

}
