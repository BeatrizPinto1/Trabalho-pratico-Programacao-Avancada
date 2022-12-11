package pt.isec.pa.apoio_poe.model.data;

import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.memento.IMementoOriginator;
import pt.isec.pa.apoio_poe.model.memento.Memento;
import pt.isec.pa.apoio_poe.ui.gui.resources.util.ToastMessage;
import pt.isec.pa.apoio_poe.ui.text.utils.PAInput;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sistema implements Serializable, IMementoOriginator {
    private static final long serialVersionUID = 1L;

    private List<Students> students;
    private List<Teachers> teachers;
    private List<Proposals> proposals;
    private List<Students> studentsDraw;
    private Map<Long, List<String>> mapApplications;
    private Map<Students, Proposals> mapStudentProposal;
    private Map<Proposals, Teachers> mapProposalTeacher;

    List<Boolean> closedPhases;

    private int currentState;

    public Sistema(int currentState) {
        this.currentState = currentState;
        students = new ArrayList<>();
        teachers = new ArrayList<>();
        proposals = new ArrayList<>();
        studentsDraw = new ArrayList<>();
        mapApplications = new HashMap<>();
        mapStudentProposal = new HashMap<>();
        mapProposalTeacher = new HashMap<>();
        closedPhases = new ArrayList<>(Collections.nCopies(4, false));
    }

    @Override
    public Memento getMemento() throws IOException {
        Memento m = new Memento(this);
        return m;
    }

    @Override
    public void setMemento(Memento m) throws IOException, ClassNotFoundException {
        Sistema sistema = (Sistema) m.getSnapshot();

        students = sistema.students;
        teachers = sistema.teachers;
        proposals = sistema.proposals;
        studentsDraw = sistema.studentsDraw;
        mapApplications = sistema.mapApplications;
        mapStudentProposal = sistema.mapStudentProposal;
        mapProposalTeacher = sistema.mapProposalTeacher;
        closedPhases = sistema.closedPhases;
        currentState = sistema.currentState;
    }

    public int verificarPasta(String nomef){
        File folder = new File("saves");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].getName().equals(nomef)){
                return 1;
            }
        }
        return 0;
    }

    public void mostrarPastaSaves(){
        StringBuilder texto = new StringBuilder();

        File folder = new File("saves");
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles.length >= 5){
            for (int i = listOfFiles.length-5; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println(listOfFiles[i].getName());
                    texto.append(listOfFiles[i].getName());
                }
            }
        }else{
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    System.out.println(listOfFiles[i].getName());
                }
            }
        }
    }

    public int getCurrentState() {
        return currentState;
    }

    public List<Students> getStudentsDraw() {
        return studentsDraw;
    }

    public void setCurrentState(int currentState) {
        if (currentState < 1 || currentState > 5)
            return;
        this.currentState = currentState;
    }

    private List<Students> readStudentsFromCSV(String fileName) {
        List<Students> listaAlunos = new ArrayList<>();

        try (FileInputStream fstream = new FileInputStream(fileName)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String line = br.readLine();

            while (line != null) {
                String[] attributes = line.split(",");

                Students aluno = createStudent(listaAlunos, attributes);
                if (aluno != null)
                    listaAlunos.add(aluno);

                line = br.readLine();
            }

        } catch (IOException ioe) {
            System.out.println("This file does not exist!");
        }

        return listaAlunos;
    }


    private Students createStudent(List<Students> listaAlunos, String[] metadata) {
        long nrEstudante = Long.parseLong(metadata[0]);
        String name = metadata[1];
        String email = metadata[2];
        String curso = metadata[3];
        String ramo = metadata[4];
        double classificacao = Double.parseDouble(metadata[5]);
        String estagios = metadata[6];
        if (!estagios.equalsIgnoreCase("true") && !estagios.equalsIgnoreCase("false"))
            return null;
        boolean acedeEstagios = Boolean.parseBoolean(metadata[6]);

        if (listaAlunos.contains(new Students(nrEstudante, name, email, curso, ramo, classificacao, acedeEstagios)))
            return null;
        else if (!curso.equalsIgnoreCase("LEI") && !curso.equalsIgnoreCase("LEI-PL"))
            return null;
        else if (!ramo.equalsIgnoreCase("DA") && !ramo.equalsIgnoreCase("RAS") && !ramo.equalsIgnoreCase("SI"))
            return null;
        else if (classificacao < 0 || classificacao > 1)
            return null;
        else if(searchTeacher(email) != null)
            return null;

        return new Students(nrEstudante, name, email, curso, ramo, classificacao, acedeEstagios);
    }

    private List<Teachers> readTeachersFromCSV(String fileName) {
        List<Teachers> listaDocentes = new ArrayList<>();

        try (FileInputStream fstream = new FileInputStream(fileName)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String line = br.readLine();

            while (line != null) {
                String[] attributes = line.split(",");

                Teachers docente = createTeacher(listaDocentes, attributes);

                if (docente != null)
                    listaDocentes.add(docente);

                line = br.readLine();
            }

        } catch (IOException ioe) {
            System.out.println("This file does not exist!");
        }

        return listaDocentes;
    }


    private Teachers createTeacher(List<Teachers> listaDocentes, String[] metadata) {
        String name = metadata[0];
        String email = metadata[1];


        if (listaDocentes.contains(new Teachers(name, email)))
            return null;
        List<String> emailAlunos = new ArrayList<>();
        for(Students student: students)
            emailAlunos.add(student.getEmail());
        if(emailAlunos.contains(email))
            return null;

        return new Teachers(name, email);
    }

    private List<Proposals> readProposalsFromCSV(String fileName) {
        List<Proposals> listaPropostas = new ArrayList<>();

        try (FileInputStream fstream = new FileInputStream(fileName)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String line = br.readLine();

            while (line != null) {
                String[] attributes = line.split(",");

                Proposals proposta = createProposal(listaPropostas, attributes);

                if (proposta != null)
                    listaPropostas.add(proposta);

                line = br.readLine();
            }

        } catch (IOException ioe) {
            System.out.println("This file does not exist!");
        }

        return listaPropostas;
    }


    private Proposals createProposal(List<Proposals> listaPropostas, String[] metadata) {
        String tipo = metadata[0];
        String codigoIdentificacao = metadata[1];
        switch (tipo) {
            case "T1" -> {
                String areaDestino = metadata[2];
                String[] attributes = areaDestino.split("\\|");
                for (String a : attributes) {
                    if (!a.equalsIgnoreCase("DA") && !a.equalsIgnoreCase("RAS") && !a.equalsIgnoreCase("SI"))
                        return null;
                }
                String titulo = metadata[3];
                String entidadeAcolhimento = metadata[4];
                if (metadata.length == 5) {
                    if (listaPropostas.contains(new Stages(codigoIdentificacao, areaDestino, titulo, entidadeAcolhimento)))
                        return null;
                    return new Stages(codigoIdentificacao, areaDestino, titulo, entidadeAcolhimento);
                } else {
                    long nrAluno = Long.parseLong(metadata[5]);

                    if (searchStudent(nrAluno) == null)
                        return null;
                    else if (listaPropostas.contains(new Stages(codigoIdentificacao, areaDestino, titulo, entidadeAcolhimento, nrAluno)))
                        return null;
                    for (Proposals proposal : listaPropostas) {
                        if (proposal.getNrAluno() == nrAluno)
                            return null;
                    }
                    return new Stages(codigoIdentificacao, areaDestino, titulo, entidadeAcolhimento, nrAluno);
                }

            }

            case "T2" -> {
                String areaDestino = metadata[2];
                String[] attributes = areaDestino.split("\\|");
                for (String a : attributes) {
                    if (!a.equalsIgnoreCase("DA") && !a.equalsIgnoreCase("RAS") && !a.equalsIgnoreCase("SI"))
                        return null;
                }
                String titulo = metadata[3];
                String emailDocente = metadata[4];

                if (searchTeacher(emailDocente) == null)
                    return null;

                if (metadata.length == 5) {
                    if (listaPropostas.contains(new Projects(codigoIdentificacao, areaDestino, titulo, emailDocente)))
                        return null;
                    return new Projects(codigoIdentificacao, areaDestino, titulo, emailDocente);
                } else {
                    long nrAluno = Long.parseLong(metadata[5]);

                    if (searchStudent(nrAluno) == null)
                        return null;
                    else if (listaPropostas.contains(new Projects(codigoIdentificacao, areaDestino, titulo, emailDocente, nrAluno)))
                        return null;
                    for (Proposals proposal : listaPropostas) {
                        if (proposal.getNrAluno() == nrAluno)
                            return null;
                    }
                    return new Projects(codigoIdentificacao, areaDestino, titulo, emailDocente, nrAluno);
                }

            }

            case "T3" -> {
                String titulo = metadata[2];
                long nrAluno = Long.parseLong(metadata[3]);

                if (searchStudent(nrAluno) == null)
                    return null;
                else if (listaPropostas.contains(new SelfProposedProject(codigoIdentificacao, titulo, nrAluno)))
                    return null;
                for (Proposals proposal : listaPropostas) {
                    if (proposal.getNrAluno() == nrAluno)
                        return null;
                }
                return new SelfProposedProject(codigoIdentificacao, titulo, nrAluno);
            }

        }

        return null;
    }

    private Map<Long, List<String>> readApplicationsFromCSV(String fileName) {
        Map<Long, List<String>> listaCandidaturas = new HashMap<>();

        try (FileInputStream fstream = new FileInputStream(fileName)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String line = br.readLine();

            while (line != null) {
                boolean continuar = true;
                String[] attributes = line.split(",");

                long nrAluno = Long.parseLong(attributes[0]);

                for (Proposals proposta : proposals)
                    if (proposta.getNrAluno() == nrAluno)
                        continuar = false;

                if (searchStudent(nrAluno) != null && continuar) {
                    List<String> propostas = createApplicaion(attributes);

                    if (propostas.size() != 0 && !listaCandidaturas.containsKey(nrAluno)) {
                        listaCandidaturas.put(nrAluno, propostas);
                    }
                }
                line = br.readLine();
            }

        } catch (IOException ioe) {
            System.out.println("This file does not exist!");
        }

        return listaCandidaturas;
    }


    private List<String> createApplicaion(String[] metadata) {
        List<String> nrProposta = new ArrayList<>(Arrays.asList(metadata).subList(1, metadata.length));
        List<String> propostas = new ArrayList<>();
        Proposals proposta;

        for (String a : nrProposta) {
            if ((proposta = searchProposal(a)) == null) ;
            else if (proposta.getNrAluno() != 0) ;
            else
                propostas.add(a);
        }

        return propostas;
    }

    public List<Students> getStudents() {
        return students;
    }

    public List<Teachers> getTeachers() {
        return teachers;
    }

    public List<Proposals> getProposals() {return proposals; }

    public List<Proposals> getTipoPropostas(int op) {
        List<Proposals> propostas = new ArrayList<>();

        switch (op) {
            case 1 -> {
                for(Proposals proposal : proposals) {
                    if(proposal instanceof Stages)
                        propostas.add(proposal);
                }
            }
            case 2 -> {
                for(Proposals proposal : proposals) {
                    if(proposal instanceof Projects)
                        propostas.add(proposal);
                }
            }
            default -> {
                for(Proposals proposal : proposals) {
                    if(proposal instanceof SelfProposedProject)
                        propostas.add(proposal);
                }
            }
        }


        return propostas;
    }

    public int getRamos(int op) {
        int count = 0;

        switch (op) {
            case 1 -> {

                for(Proposals proposal : proposals) {
                    if(proposal instanceof Stages) {
                        if( ((Stages) proposal).getAreaDestino().contains("DA") || ((Stages) proposal).getAreaDestino().contains("da") )
                            count++;
                    }
                    else if(proposal instanceof Projects) {
                        if( ((Projects) proposal).getAreaDestino().contains("DA") || ((Projects) proposal).getAreaDestino().contains("da") )
                            count++;
                    }
                }

            }
            case 2 -> {
                for(Proposals proposal : proposals) {
                    if(proposal instanceof Stages) {
                       // System.out.println(((Stages) proposal).getAreaDestino());
                        if( ((Stages) proposal).getAreaDestino().contains("RAS") || ((Stages) proposal).getAreaDestino().contains("ras") )
                            count++;
                    }
                    else if(proposal instanceof Projects) {
                        if( ((Projects) proposal).getAreaDestino().contains("RAS") || ((Projects) proposal).getAreaDestino().contains("ras") )
                            count++;
                    }
                }
            }
            default -> {
                for(Proposals proposal : proposals) {
                    if(proposal instanceof Stages) {
                        if( ((Stages) proposal).getAreaDestino().contains("SI") || ((Stages) proposal).getAreaDestino().contains("si") )
                            count++;
                    }
                    else if(proposal instanceof Projects) {
                        if( ((Projects) proposal).getAreaDestino().contains("SI") || ((Projects) proposal).getAreaDestino().contains("si") )
                            count++;
                    }
                }
            }
        }


        return count;
    }

    public Map<Long, List<String>> getMapApplications() {
        return mapApplications;
    }

    public Map<Students, Proposals> getMapStudentProposal() {
        return mapStudentProposal;
    }

    public  Map<Proposals, Teachers> getMapProposalTeacher() { return mapProposalTeacher; }

    public  String getProposalTeacher() {

        StringBuilder texto = new StringBuilder();

        for(Proposals proposta: mapProposalTeacher.keySet())
            texto.append(proposta).append(" -> ").append(mapProposalTeacher.get(proposta)).append("\n");

        if (texto.isEmpty())
            texto.append("There are no proposals with teacher associated!");

        return texto.toString();
    }

    public void setStudents(String filename) {
        List<Students> alunos = readStudentsFromCSV(filename);
        this.students = alunos;
    }

    public boolean addStudent(Students student) {
        if (students.contains(student))
            return false;
        for(Teachers teacher: teachers) {
            if (teacher.getEmail().equalsIgnoreCase(student.getEmail()))
                return false;
        }
        this.students.add(student);
        return true;
    }

    public void setTeachers(String filename) {
        List<Teachers> docentes = readTeachersFromCSV(filename);
        this.teachers = docentes;
    }

    public boolean addTeacher(Teachers teacher) {
        if (teachers.contains(teacher))
            return false;
        for(Students student: students) {
            if (student.getEmail().equalsIgnoreCase(teacher.getEmail()))
                return false;
        }
        this.teachers.add(teacher);
        return true;
    }

    public void setProposals(String filename) {
        List<Proposals> proposals = readProposalsFromCSV(filename);
        this.proposals = proposals;
    }

    public boolean addProposals(Proposals proposal) {
        if (proposals.contains(proposal))
            return false;
        this.proposals.add(proposal);
        return true;
    }

    public void setApplications(String filename) {
        Map<Long, List<String>> candidaturas = readApplicationsFromCSV(filename);
        this.mapApplications = candidaturas;
    }

    public String addAplications(long nrAluno, List<String> codProposta) {
        StringBuilder texto = new StringBuilder();

        if (codProposta == null || nrAluno == 0) {
            texto.append("Insert a stundet ID and a proposal code!");
            return texto.toString();
        }

        if(searchStudent(nrAluno) == null) {
            texto.append("This student does not exist!");
            return texto.toString();
        }

        for(String proposta: codProposta) {
            if(searchProposal(proposta) == null) {
                texto.append("This proposal does not exist!");
                return texto.toString();
            }
        }

        /*if(mapBooks.containsValue(book))
            return -1;*/
        if (mapApplications.containsKey(nrAluno)) {
            texto.append("This student already has applicaions!");
            return texto.toString();
        }

        mapApplications.put(nrAluno, codProposta);
        return texto.toString();
    }

    public String editProposalsAdd(long nrAluno, String codProposta) {
        Proposals proposal;
        StringBuilder texto = new StringBuilder();

        if (codProposta == null || nrAluno == 0) {
            texto.append("The proposal code or the student number is null!");
            return texto.toString();
        }

        if ((searchStudent(nrAluno)) == null) {
            texto.append("This student does not exist!");
            return texto.toString();
        }

        if ((proposal = searchProposal(codProposta)) == null) {
            texto.append("This proposal does not exist!");
            return texto.toString();
        }

        if (proposal.getNrAluno() != 0) {
            texto.append("This proposal already has a student associated!");
            return texto.toString();
        }

        if (!mapApplications.containsKey(nrAluno)) {
            texto.append("This student has no applications!");
            return texto.toString();
        }

        if(mapApplications.get(nrAluno).contains(codProposta)) {
            texto.append("This student already has this proposal in his application!");
            return texto.toString();
        }

        mapApplications.get(nrAluno).add(codProposta);

        return texto.toString();
    }

    public String editProposalsRemove(long nrAluno, String codProposta) {
        StringBuilder texto = new StringBuilder();

        if (codProposta == null || nrAluno == 0) {
            texto.append("The proposal code or the student number is null!");
            return texto.toString();
        }

        if ((searchStudent(nrAluno)) == null) {
            texto.append("This student does not exist!");
            return texto.toString();
        }

        if ((searchProposal(codProposta)) == null) {
            texto.append("This proposal does not exist!");
            return texto.toString();
        }

        if (!mapApplications.containsKey(nrAluno)) {
            texto.append("This student has no applications!");
            return texto.toString();
        }

        if(!mapApplications.get(nrAluno).contains(codProposta)) {
            texto.append("This student has not this proposal in his application!");
            return texto.toString();
        }

        mapApplications.get(nrAluno).remove(codProposta);

        return texto.toString();
    }

    public String editTeachersAssociation(String emailDocente, long nrEstudante) {
        StringBuilder texto = new StringBuilder();

        texto.append(removeTeachersAssociation(emailDocente));
        texto.append(setMapProposalTeacher(emailDocente, nrEstudante));

        return texto.toString();
    }

    public String removeApplications(long nrEstudante) {
        StringBuilder texto = new StringBuilder();

        if ((searchStudent(nrEstudante)) == null) {
            texto.append("This student does not exist!");
            return texto.toString();
        }

        if (!mapApplications.containsKey(nrEstudante)) {
            texto.append("This student has no applications!");
            return texto.toString();
        }

        mapApplications.remove(nrEstudante);
        return texto.toString();
    }


    public String removeTeachersAssociation(String emailDocente) {
        StringBuilder texto = new StringBuilder();

        if ((searchTeacher(emailDocente)) == null) {
            texto.append("This teacher does not exist!");
            return texto.toString();
        }

        boolean have = false;
        for (Proposals key : mapProposalTeacher.keySet()) { //corro a lista de propostas
            if (mapProposalTeacher.get(key).getEmail().equalsIgnoreCase(emailDocente)) {
                mapProposalTeacher.remove(key);
                have = true;
            }
        }

        if(!have) {
            texto.append("This teacher does not have a student associated!");
            return texto.toString();
        }

        return texto.toString();
    }

    public String removeStudent(long nrEstudante) {
        StringBuilder string = new StringBuilder();
        Students student;

        if ((student = searchStudent(nrEstudante)) == null) {
            string.append("This student does not exist!");
            return string.toString();
        }

        this.students.remove(student);
        return string.toString();
    }

    public String removeTeachers(String email) {
        StringBuilder string = new StringBuilder();
        Teachers teacher;

        if ((teacher = searchTeacher(email)) == null) {
            string.append("This teacher does not exist!");
            return string.toString();
        }

        this.teachers.remove(teacher);
        return string.toString();
    }

    public String removeProposal(String codigoIdentificacao) {
        StringBuilder string = new StringBuilder();
        Proposals proposal;

        if ((proposal = searchProposal(codigoIdentificacao)) == null) {
            string.append("This proposal does not exist!");
            return string.toString();
        }

        this.proposals.remove(proposal);
        return string.toString();
    }

    public boolean removeApplication(long nrAluno) {
        if (mapApplications == null)
            return false;

        return mapApplications.remove(nrAluno) != null;
    }

    public String consultStudents() {
        StringBuilder alunos = new StringBuilder();

        for (Students student : students) {
            alunos.append(student).append("\n");
        }

        if (alunos.isEmpty())
            alunos.append("There are no students!");

        return alunos.toString();
    }

    public String consultTeachers() {
        StringBuilder teachers = new StringBuilder();

        for (Teachers teacher : getTeachers()) {
            teachers.append(teacher).append("\n");
        }

        if (teachers.isEmpty())
            teachers.append("There are no teachers!");

        return teachers.toString();
    }

    public String consultProposals() {
        StringBuilder proposals = new StringBuilder();

        for (Proposals proposal : getProposals()) {
            proposals.append(proposal).append("\n");
        }

        if (proposals.isEmpty())
            proposals.append("There are no proposals!");

        return proposals.toString();
    }

    public String consultApplications() {
        StringBuilder applications = new StringBuilder();

        for(Long nrAluno: getMapApplications().keySet())
            applications.append(nrAluno).append(" = ").append(getMapApplications().get(nrAluno)).append("\n");
        //if(mapApplications.isEmpty())
        //  return null;

        if (applications.isEmpty())
            applications.append("There are no applications!");

        return applications.toString();
    }

    public String consultSelfProposedStudents() {
        StringBuilder students = new StringBuilder();
        Students aluno;
        for (Proposals proposal : getProposals()) {
            if (proposal instanceof SelfProposedProject && searchStudent(proposal.getNrAluno()) != null) { //ja temautomaticamente aluno mas fica a condicao
                aluno = searchStudent(proposal.getNrAluno());
                students.append(aluno).append("\n");
            }
        }

        if (students.isEmpty())
            students.append("There are no students with self-proposal!");

        return students.toString();
    }

    public String CSWithAlreadyRegisteredApplication() {
        StringBuilder students = new StringBuilder();

        for (Students student : getStudents()) {
            if (mapApplications.containsKey(student.getNrEstudante()))
                students.append(student).append("\n");
        }

        if (students.isEmpty())
            students.append("There are no students with an application already registered!");

        return students.toString();
    }

    public String CSwithoutRegisteredApplication() {
        StringBuilder students = new StringBuilder();

        for (Students student : getStudents()) {
            if (!mapApplications.containsKey(student.getNrEstudante()))
                students.append(student).append("\n");
        }

        if (students.isEmpty())
            students.append("There are no students without application!");

        return students.toString();
    }

    public String CSwithProposalsAssigned() {
        StringBuilder students = new StringBuilder();

        /*List<Proposals> proposalsTemp = new ArrayList<>(mapStudentProposal.values());
        proposalsTemp.sort(Comparator.comparingInt(this::typeProposal));*/

        for (Students student : getStudents()) {
            if (mapStudentProposal.containsKey(student)) {

                if (typeProposal(mapStudentProposal.get(student)) == 3)
                    students.append(student).append(" : 1\n");
                else if (typeProposal(mapStudentProposal.get(student)) == 2 && mapStudentProposal.get(student).getNrAluno() != 0)
                    students.append(student).append(" : 2\n");
                else {
                    List<String> values = mapApplications.get(student.getNrEstudante());
                    int pos = values.indexOf(mapStudentProposal.get(student).codIdentificacao);

                    students.append(student).append(" : ").append(pos+1).append("\n");
                }

            }
        }

        if (students.isEmpty())
            students.append("There are no students with proposals assigned!");

        return students.toString();
    }

    public String CSwithoutProposalsAssigned() {
        StringBuilder students = new StringBuilder();

        for (Students student : getStudents()) {
            if (!mapStudentProposal.containsKey(student))
                students.append(student).append("\n");
        }

        if (students.isEmpty())
            students.append("There are no students without proposals assigned!");

        return students.toString();
    }

    public String studentsSelfProposals() {
        StringBuilder proposals = new StringBuilder();

        for (Proposals proposal : getProposals()) {
            if (proposal instanceof SelfProposedProject && proposal.getNrAluno() != 0) //ja temautomaticamente aluno mas fica a condicao
                proposals.append(proposal).append("\n");
        }

        if (proposals.isEmpty())
            proposals.append("There are no student self-proposals!");

        return proposals.toString();
    }

    public String teachersProposals() {
        StringBuilder proposals = new StringBuilder();

        for (Proposals proposal : getProposals()) {
            if (proposal instanceof Projects)
                proposals.append(proposal).append("\n");
        }

        if (proposals.isEmpty())
            proposals.append("There are no teachers proposals!");

        return proposals.toString();
    }

    public String availableProposals() { //mas se tiver com o numero de um aluno? typeproposal == 1
        StringBuilder proposals = new StringBuilder();

        for (Proposals proposal : getProposals()) {
            if(!mapStudentProposal.containsValue(proposal))
                proposals.append(proposal).append("\n");
        }

        if (proposals.isEmpty())
            proposals.append("There are no proposals available!");

        return proposals.toString();
    }

    public String assignedProposals() {
        StringBuilder proposals = new StringBuilder();

        for (Proposals proposal : getProposals()) {
            if(mapStudentProposal.containsValue(proposal))
                proposals.append(proposal).append("\n");
        }

        if (proposals.isEmpty())
            proposals.append("There are no assigned proposals!");

        return proposals.toString();
    }

    public int getassignedOrNotProposals(int op) {
        int count = 0;

        switch (op) {
            case 1 -> {
                count = mapStudentProposal.values().size();
            }
            case 2 -> {
                for (Proposals proposal : getProposals()) {
                    if(!mapStudentProposal.containsValue(proposal))
                        count++;
                }
            }
        }

        return count;
    }

    public LinkedHashMap<String, Integer> companiesWithMoreInternships() {
        List<String> texto = new ArrayList<>();
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        int count = 0;

        for(Proposals proposal: mapStudentProposal.values()) {
            if(proposal instanceof Stages)
                texto.add(((Stages) proposal).getIdentidadeAcolhimento());
        }

        for(int i = 0; i < texto.size(); i++) {
            for(int j = 0; j < texto.size(); j++) {
                if(texto.get(i).equalsIgnoreCase(texto.get(j)))
                    count++;
            }
            if(!map.containsKey(texto.get(i)))
                map.put(texto.get(i), count);
            count = 0;
        }


        //LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();

        //Use Comparator.reverseOrder() for reverse ordering
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        /*List<Integer> numeros = new ArrayList<>(map.values());
        Collections.sort(numeros, Collections.reverseOrder());
        List<Integer> resposta = new ArrayList<>();
        for(int i = 0; i < numeros.size(); i++) {
            for(Integer j: map.values()) {
                if(numeros.get(i) == j) {
                    resposta.add(numeros.get(i));
                }
            }
        }
        Map<Integer, String> fim = new HashMap<>();*/

        return reverseSortedMap;
    }

    public LinkedHashMap<String, Integer> teachersWithMoreAssignments() {
        List<String> texto = new ArrayList<>();
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        int count = 0;

        for(Teachers teacher: mapProposalTeacher.values()) {
            texto.add(teacher.getNome());
        }

        for(int i = 0; i < texto.size(); i++) {
            for(int j = 0; j < texto.size(); j++) {
                if(texto.get(i).equalsIgnoreCase(texto.get(j)))
                    count++;
            }
            if(!map.containsKey(texto.get(i)))
                map.put(texto.get(i), count);
            count = 0;
        }

        //LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();

        //Use Comparator.reverseOrder() for reverse ordering
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        return reverseSortedMap;
    }

    public String studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor() {
        StringBuilder students = new StringBuilder();

        for (Students student : getStudents()) {
            boolean have = false;
            if (mapStudentProposal.containsKey(student)) { // estudante com proposta atribuida
                Proposals proposal = mapStudentProposal.get(student); //procuro a proposta desse estudante


                /*if(!mapProposalTeacher.containsKey(proposal))
                    students.append(student).append("\n");*/
                for (Proposals key : mapProposalTeacher.keySet()) { //corro a lista de propostas
                    if (proposal.codIdentificacao.equalsIgnoreCase(key.codIdentificacao)) {
                        have = true;//vejo se o docente e o estudante tem a mesma proposta
                        break;
                    }
                }

                if(have)
                    students.append(student).append("\n");
            }
        }

        if(students.isEmpty())
            students.append("There are no students with an assigned proposal and an associated advisor");

        return students.toString();
    }

    public String studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor() {
        StringBuilder students = new StringBuilder();

        for (Students student : getStudents()) {
            boolean have = false;
            if (mapStudentProposal.containsKey(student)) { // estudante com proposta atribuida
                Proposals proposal = mapStudentProposal.get(student); //procuro a proposta desse estudante

                /*if(!mapProposalTeacher.containsKey(proposal))
                    students.append(student).append("\n");*/
                for (Proposals key : mapProposalTeacher.keySet()) { //corro a lista de propostas
                    if (proposal.codIdentificacao.equalsIgnoreCase(key.codIdentificacao)) {
                        have = true;//vejo se o docente e o estudante tem a mesma proposta
                        break;
                    }
                }

                if(!have)
                    students.append(student).append("\n");

            }
        }

        if(students.isEmpty())
            students.append("There are no students with an assigned proposal but not an associated advisor");

        return students.toString();
    }

    public String inMedium() {
        StringBuilder string = new StringBuilder();
        List<Teachers> docentes = new ArrayList<>(mapProposalTeacher.values());
        List<Teachers> diffTeachers = new ArrayList<>();

        for (Teachers docente : docentes) {
            if (!diffTeachers.contains(docente)) {
                diffTeachers.add(docente);
            }
        }

        List<Proposals> propostas = new ArrayList<>(mapProposalTeacher.keySet());

        if(diffTeachers.size() == 0 || propostas.size() == 0) {
            string.append("There are no guidelines!");
            return string.toString();
        }

        string.append(Math.round((float)(propostas.size()/diffTeachers.size())));
        return string.toString();
    }

    public String inMax() {
        StringBuilder string = new StringBuilder();
        List<Teachers> docentes = new ArrayList<>(mapProposalTeacher.values());

        if(docentes.size() == 0) {
            string.append("There are no guidelines!");
            return string.toString();
        }

        int max = 0, count;

        for(int i = 0; i < docentes.size(); i++)   //For loop to hold each element
        {
            count = 1;
            for (int j = i + 1; j < docentes.size(); j++)  //For loop to check for duplicate elements
            {
                if (docentes.get(j).getEmail().equalsIgnoreCase(docentes.get(i).getEmail())) {
                    count++;
                    if (count > max) {
                        max = count;
                    }
                }
            }
        }

        string.append(max);
        return string.toString();
    }

    public String inMin() {
        StringBuilder string = new StringBuilder();
        List<Teachers> docentes = new ArrayList<>(mapProposalTeacher.values());

        if(docentes.size() == 0) {
            string.append("There are no guidelines!");
            return string.toString();
        }

        int min, count = 1;

        for (int i = 1; i < docentes.size(); i++)  {
            if (docentes.get(i).getEmail().equalsIgnoreCase(docentes.get(0).getEmail())) {
                count++;
            }
        }

        min = count;

        for(int i = 0; i < docentes.size(); i++)   //For loop to hold each element
        {
            count = 1;
            for (int j = i + 1; j < docentes.size(); j++)  //For loop to check for duplicate elements
            {
                if (docentes.get(j).getEmail().equalsIgnoreCase(docentes.get(i).getEmail())) {
                    count++;
                    if (count < min) {
                        min = count;
                    }
                }
            }
        }

        string.append(min);
        return string.toString();
    }

    public String inTeacher(String emailDocente) {
        Teachers teacher;
        StringBuilder string = new StringBuilder();
        List<Teachers> docentes = new ArrayList<>(mapProposalTeacher.values());

        if((teacher = (searchTeacher(emailDocente))) == null) {
            string.append("This teacher does not exist!");
            return string.toString();
        }
        if(docentes.size() == 0 || !docentes.contains(teacher)) {
            string.append("This teacher has no guidelines!");
            return string.toString();
        }

        int count = 0;

        for(Teachers docente: docentes) {
            if(teacher.getEmail().equalsIgnoreCase(docente.getEmail()))
                count++;
        }

        string.append(count);
        return string.toString();
    }

    public String studentsWithoutAssignedProposalsAndWithApplicationOptions() {
        StringBuilder students = new StringBuilder();

        for (Students student : getStudents()) {
            if (!mapStudentProposal.containsKey(student) && mapApplications.containsKey(student.getNrEstudante())) // estudante sem proposta atribuida
                students.append(student);                                                                      //mas com candidaturas
        }

        if (students.isEmpty())
            students.append("There are no students without assigned proposals and with application options!");

        return students.toString();
    }

    public static boolean flatContains(Iterable<? extends Collection<?>> collections, Object value) {
        for (Collection<?> collection : collections) {
            if (collection.contains(value)) {
                return true;
            }
        }
        return false;
    }

    public String proposalsWithApplications() {
        StringBuilder proposals = new StringBuilder();

        for (Proposals proposal : getProposals()) {
            if (flatContains(mapApplications.values(), proposal.getCodIdentificacao()))
                proposals.append(proposal).append("\n");
        }

        if (proposals.isEmpty())
            proposals.append("There are no proposals with applications!");

        return proposals.toString();
    }

    public String proposalsWithoutApplications() {
        StringBuilder proposals = new StringBuilder();

        for (Proposals proposal : getProposals()) {
            if (!flatContains(mapApplications.values(), proposal.getCodIdentificacao()))
                proposals.append(proposal).append("\n");
        }

        if (proposals.isEmpty())
            proposals.append("There are no proposals without applications!");

        return proposals.toString();
    }

    public void automaticSelfProprosals() {
        Students student;
        for (Proposals proposal : proposals) {
            if (proposal instanceof SelfProposedProject) {
                if ((student = searchStudent(proposal.getNrAluno())) != null) { //nao precisa pqe obrigatorio existir
                    if (!mapStudentProposal.containsKey(student)) {
                        mapStudentProposal.put(student, proposal);
                    }
                }
            }
        }
    }

    public void automaticteachersProprosals() {
        Students student;
        for (Proposals proposal : proposals) {
            if (proposal instanceof Projects) {
                if ((student = searchStudent(proposal.getNrAluno())) != null) { //para ver se tem aluno
                    if (!mapStudentProposal.containsKey(student)) {
                        mapStudentProposal.put(student, proposal);
                    }
                }
            }
        }
    }

    public boolean verificaArea(Proposals proposal, Students student) {
        Stages stage;
        Projects project;

        if (typeProposal(proposal) == 1) {
            stage = (Stages) proposal;
            List<String> attributes = List.of(stage.getAreaDestino().split("\\|"));
            if (attributes.contains(student.getRamo()))
                return true;
        } else if (typeProposal(proposal) == 2) {
            project = (Projects) proposal;
            List<String> attributes = List.of(project.getAreaDestino().split("\\|"));
            if (attributes.contains(student.getRamo()))
                return true;
        }

        return false;
    }

    public boolean solve(Students student1, String prop) { //proposal e pop

        List<String> values = mapApplications.get(student1.getNrEstudante());

        for (String value : values) { //correr as propostas que o estudante indicou por ordem de preferencia
            if (!value.equalsIgnoreCase(prop)) {
                Proposals proposal = searchProposal(value);

                if ((typeProposal(proposal) == 1 && !student1.isAcedeEstagios())) ;
                else if (!verificaArea(proposal, student1)) ;

                else if (mapStudentProposal.containsValue(proposal)) {
                    Students student2 = null;
                    for (Students key : mapStudentProposal.keySet()) {
                        if (proposal.equals(mapStudentProposal.get(key))) {
                            student2 = key;
                        }
                    }
                    assert student2 != null;
                    if (student2.getClassificacao() == student1.getClassificacao()) {
                        studentsDraw.clear();
                        studentsDraw.add(student2);
                        studentsDraw.add(student1);
                        return false;
                    }
                } else {
                    mapStudentProposal.put(student1, proposal);
                    break;
                }

            }

        }
        return true;
    }

    public boolean automaticStudentsWithoutAssignmentsProposals() { //voltar a meter studentsdraw a null

        List<Students> studentsTemp = new ArrayList<>(students);
        studentsTemp.sort((o1, o2) -> (int) Math.signum(o2.getClassificacao() - o1.getClassificacao()));

        for (Students student : studentsTemp) { //estudantes organizados por classficacao
            if (mapApplications.containsKey(student.getNrEstudante()) && !mapStudentProposal.containsKey(student)) { //ver se o estudante realizou candidatura

                if (!solve(student, null))
                    return false;

            }
        }
        return true;
    }

    public boolean Draw(int op) { //1 ja tem //2 nao tem
        Proposals proposal = mapStudentProposal.get(studentsDraw.get(0));
        String prop;

        switch (op) {
            case 1 -> {
                prop = mapStudentProposal.get(studentsDraw.get(0)).codIdentificacao;
                if (!solve(studentsDraw.get(1), prop))
                    return false;
            }
            case 2 -> {
                mapStudentProposal.remove(studentsDraw.get(0));
                mapStudentProposal.put(studentsDraw.get(1), proposal);

                prop = mapStudentProposal.get(studentsDraw.get(1)).codIdentificacao;
                if (!solve(studentsDraw.get(0), prop))
                    return false;
            }
        }
        return true;
    }

    public String removeOneAssignment(long nrAluno) {
        StringBuilder string = new StringBuilder();
        Students student;

        if ((student = searchStudent(nrAluno)) == null) {
            string.append("This student does not exist!");
            return string.toString();
        }

        if (!getMapStudentProposal().containsKey(student)) {
            string.append("This student does not have an application defined");
        }

        getMapStudentProposal().remove(student);
        return string.toString();
    }

    public void removeAssignments() {
        List<Proposals> proposals = new ArrayList<>(mapStudentProposal.values());
        Students aluno = null;

        for (Proposals proposta : proposals) {
            if (typeProposal(proposta) == 1) ;
            else if (typeProposal(proposta) == 2 && proposta.getNrAluno() != 0) ;
            else {
                for (Students key : mapStudentProposal.keySet()) {
                    if (proposta.equals(mapStudentProposal.get(key))) {
                        aluno = key;
                    }
                }
                mapStudentProposal.remove(aluno);
            }
        }
    }

    public String consultApplicationsStudentsInvolved() {
        StringBuilder texto = new StringBuilder();
        for (Students student : studentsDraw) {
            texto.append(student.getNrEstudante() + " -> " + mapApplications.get(student.getNrEstudante()) + "\n");
        }

        return texto.toString();
    }

    public List<Students> studentsWithouAssignments() {
        List<Students> alunos = new ArrayList<>();
        for (Students student : students) {
            if (!mapStudentProposal.containsKey(student))
                alunos.add(student);
        }
        return alunos;
    }

    public void automaticAssociationOfTeachersProposingProjects() {
        for (Proposals proposal : proposals) {

            if (typeProposal(proposal) == 2) {
                Teachers docente = searchTeacher(  ((Projects)proposal).getEmailDocente()  );
                if(!mapProposalTeacher.containsKey(proposal)) {
                    mapProposalTeacher.put(proposal, docente); //da print?
                }

            }
        }
    }

    public String setMapStudentProposal(long nrAluno, String codProposta) {
        Students student;
        Proposals proposal;
        StringBuilder string = new StringBuilder();

        if((student = searchStudent(nrAluno)) == null) {
            string.append("This student does not exist!");
            return string.toString();
        }

        if(getMapStudentProposal().containsKey(student)) {
            string.append("This student already has an application defined");
            return string.toString();
        }

        if((proposal = searchProposal(codProposta)) == null) {
            string.append("This proposal does not exist!");
            return string.toString();
        }

        if(getMapStudentProposal().containsValue(proposal)) {
            string.append("This proposal already has an student defined");
            return string.toString();
        }

        if((typeProposal(proposal) == 1 && !student.isAcedeEstagios()) || !verificaArea(proposal, student)) {
            string.append("The student cannot be assigned to this proposal!");
            return string.toString();
        }

        mapStudentProposal.put(student, proposal);
        return string.toString();
    }

    public String setMapProposalTeacher(String emailDocente, long nrAluno) {
        Teachers teacher;
        Students student;
        Proposals proposal;
        StringBuilder string = new StringBuilder();

        if((teacher = searchTeacher(emailDocente)) == null) {
            string.append("This teacher does not exist!");
            return string.toString();
        }

        if((student = searchStudent(nrAluno)) == null) {
            string.append("This student does not exist!");
            return string.toString();
        }

        if(!mapStudentProposal.containsKey(student)) {
            string.append("This student does not have an application defined");
            return string.toString();
        }

        proposal = searchProposal(mapStudentProposal.get(student).codIdentificacao);

        if(mapProposalTeacher.containsKey(proposal)) {
            string.append("This student already has an teacher defined");
            return string.toString();
        }

        if(typeProposal(proposal) == 2)
        {
            if(!((Projects)proposal).getEmailDocente().equalsIgnoreCase(teacher.getEmail())) {
                string.append("This proposal was proposed by another teacher!");
                return string.toString();
            }
        }

        mapProposalTeacher.put(proposal, teacher);
        return string.toString();
    }

    public Students searchStudent(long nrEstudante) {
        for (Students student : students) {
            if (student.getNrEstudante() == nrEstudante) {
                //return (Students) student.clone();
                return student;
            }
        }
        return null;
    }

    public Teachers searchTeacher(String email) {
        for (Teachers teacher : teachers) {
            if (Objects.equals(teacher.getEmail(), email))
                //return (Teachers) teacher.clone();
                return teacher;
        }
        return null;
    }

    public Proposals searchProposal(String codigoIdentificacao) {
        for (Proposals proposal : getProposals()) {
            if (Objects.equals(proposal.getCodIdentificacao(), codigoIdentificacao))
                //return (Proposals) proposal.clone();
                return proposal;
        }
        return null;
    }

    public List<String> searchApplication(long nrAluno) {
        if (mapApplications == null || mapApplications.size() == 0)
            return null;

        //for(Book b : mapBooks.values())
        //  if(b.getId() == bookId)
        //    return b;
        return mapApplications.get(nrAluno);
    }

    public int typeProposal(Proposals proposal) {
        if (proposal instanceof Stages)
            return 1;
        else if (proposal instanceof Projects)
            return 2;
        else
            return 3;
    }

    public void exportToFile(String filename, int type) throws Exception {
        FileWriter fw = null;
        try{
            File file = new File(filename);
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            switch (type) {
                case 1 -> {
                    for (Students student: getStudents())
                        pw.println(student);
                }
                case 2 -> {
                    for (Teachers teacher: getTeachers())
                        pw.println(teacher);
                }
                case 3 -> {
                    for (Proposals proposal: getProposals())
                        pw.println(proposal);
                }
                case 4 -> {
                    for(Long nrAluno: getMapApplications().keySet()) {
                        List<String> apps = getMapApplications().get(nrAluno);
                        pw.print(nrAluno);
                        for (String app: apps)
                            pw.print(","+ app);
                        pw.print("\n");
                    }
                }
                case 5 -> {
                    for(Students student: getStudents()) {
                        String prop = "";
                        int pos = 0;

                        List<String> values = getMapApplications().get(student.getNrEstudante());

                        if(getMapStudentProposal().containsKey(student))
                            prop = getMapStudentProposal().get(student).getCodIdentificacao();

                        if(values != null && prop != null)
                            pos = values.indexOf(prop);
                        pos++;


                        pw.print(student);
                        List<String> apps = getMapApplications().get(student.getNrEstudante());
                        if(apps != null) {
                            for (String app: apps)
                                pw.print(","+ app);
                        }
                        pw.print("," + getMapStudentProposal().get(student) + "," + pos + "\n");
                    }
                }
                case 6 -> {
                    for(Students student: getStudents()) {
                        String prop = "";
                        int pos = 0;

                        List<String> values = getMapApplications().get(student.getNrEstudante());

                        if(getMapStudentProposal().containsKey(student))
                            prop = getMapStudentProposal().get(student).getCodIdentificacao();

                        Teachers orientador = getMapProposalTeacher().get( getMapStudentProposal().get(student) );

                        if(values != null && prop != null)
                            pos = values.indexOf(prop);
                        pos++;


                        pw.print(student);
                        List<String> apps = getMapApplications().get(student.getNrEstudante());
                        if(apps != null) {
                            for (String app: apps)
                                pw.print(","+ app);
                        }
                        pw.print("," + getMapStudentProposal().get(student) + "," + pos + "," + orientador + "\n");
                    }
                }
            }
            pw.flush();
        }catch (Exception e) {
            System.err.println("Error!!!");
            throw new Exception("O meu texto");
        } finally {
            if(fw != null)
                fw.close();
        }
    }

    public String editaEstudante(long nrEstudante, int op, String campo) {
        StringBuilder texto = new StringBuilder();
        Students student;

        if ((student = searchStudent(nrEstudante)) == null) {
            texto.append("This student does not exist!");
            return texto.toString();
        }

            switch(op) {
            case 1 -> {
                if(!campo.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")) {
                    texto.append("Insert a correct name!");
                    return texto.toString();
                }
                student.setNome(campo);
            }
            case 2 -> {
                String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                Pattern pattern;
                Matcher matcher;

                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(campo);
                if(!matcher.matches()) {
                    texto.append("Insert a correct email!");
                    return texto.toString();
                }
                student.setEmail(campo);
            }
            case 3 -> {
                if(!campo.equalsIgnoreCase("LEI") && !campo.equalsIgnoreCase("LEI-PL")) {
                    texto.append("Insert a correct curse!");
                    return texto.toString();
                }
                student.setCurso(campo);
            }
            case 4 -> {
                if(!campo.equalsIgnoreCase("DA") && !campo.equalsIgnoreCase("RAS") && !campo.equalsIgnoreCase("SI")) {
                    texto.append("Insert a correct branch!");
                    return texto.toString();
                }
                student.setRamo(campo);
            }
            case 5 -> {
                double classificacao;
                Scanner scanner = new Scanner(campo);
                if (scanner.hasNextDouble()) {
                    classificacao = scanner.nextDouble();
                    if (classificacao < 0 || classificacao > 20) {
                        texto.append("Insert a correct classification!");
                        return texto.toString();
                    }
                }
                else {
                    texto.append("Insert a correct classification!");
                    return texto.toString();
                }
                student.setClassificacao(classificacao);
            }
            case 6 -> {
                boolean acedeEStagios;
                Scanner scanner2 = new Scanner(campo);
                if (scanner2.hasNextBoolean())
                    acedeEStagios = scanner2.nextBoolean();
                else {
                    texto.append("Insert a correct Possibility of internships!");
                    return texto.toString();
                }
                student.setAcedeEstagios(acedeEStagios);
            }
        }

        return texto.toString();
    }

    public String editaDocente(String emailDocente, String nome) {
        StringBuilder texto = new StringBuilder();
        Teachers teachers;

        if ((teachers = searchTeacher(emailDocente)) == null) {
            texto.append("This teacher does not exist!");
            return texto.toString();
        }


        if(!nome.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")) {
            texto.append("Insert a correct name!");
            return texto.toString();
        }
        teachers.setNome(nome);

        return texto.toString();
    }

    public String editaProposta(String codigoIdentificacao, int op, String campo) {
        StringBuilder texto = new StringBuilder();
        Proposals proposal;

        if ((proposal = searchProposal(codigoIdentificacao)) == null) {
            texto.append("This proposal does not exist!");
            return texto.toString();
        }

        if(typeProposal(proposal) == 1) {
            switch (op) {
                //case 1 -> proposal.setCodIdentificacao(campo);
                case 1 -> proposal.setTitulo(campo);
                case 2 -> {
                    if (!campo.equalsIgnoreCase("DA") && !campo.equalsIgnoreCase("RAS") && !campo.equalsIgnoreCase("SI")) {
                        texto.append("Insert a correct destination area!");
                        return texto.toString();
                    }
                    ((Stages) proposal).setAreaDestino(campo);
                }
                case 3 -> ((Stages) proposal).setIdentidadeAcolhimento(campo);
                case 4 -> {
                    Long nrAluno;
                    Scanner scanner = new Scanner(campo);
                    if (scanner.hasNextLong())
                        nrAluno = scanner.nextLong();
                    else {
                        texto.append("Insert a correct student ID!");
                        return texto.toString();
                    }

                    if (searchStudent(nrAluno) == null) {
                        texto.append("This student does not exist!");
                        return texto.toString();
                    }

                    ((Stages) proposal).setNrAluno(nrAluno);
                }
            }
        }
        else if(typeProposal(proposal) == 2) {
            switch (op) {
                //case 1 -> proposal.setCodIdentificacao(campo);
                case 1 -> proposal.setTitulo(campo);
                case 2 -> {
                    if (!campo.equalsIgnoreCase("DA") && !campo.equalsIgnoreCase("RAS") && !campo.equalsIgnoreCase("SI")) {
                        texto.append("Insert a correct destination area!");
                        return texto.toString();
                    }
                    ((Projects) proposal).setAreaDestino(campo);
                }
                case 3 -> {
                    String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                    Pattern pattern;
                    Matcher matcher;

                    pattern = Pattern.compile(regex);
                    matcher = pattern.matcher(campo);
                    if(!matcher.matches()) {
                        texto.append("Insert a correct email!");
                        return texto.toString();
                    }

                    if(searchTeacher(campo) == null) {
                        texto.append("This teacher does not exist!");
                        return texto.toString();
                    }

                    ((Projects) proposal).setEmailDocente(campo);
                }
                case 4 -> {
                    Long nrAluno;
                    Scanner scanner = new Scanner(campo);
                    if (scanner.hasNextLong())
                        nrAluno = scanner.nextLong();
                    else {
                        texto.append("Insert a correct student ID!");
                        return texto.toString();
                    }

                    if (searchStudent(nrAluno) == null) {
                        texto.append("This student does not exist!");
                        return texto.toString();
                    }

                    ((Projects) proposal).setNrAluno(nrAluno);
                }
            }
        }
         else if(typeProposal(proposal) == 3) {
            switch (op) {
                //case 1 -> proposal.setCodIdentificacao(campo);
                case 1 -> proposal.setTitulo(campo);
                case 2 -> {
                    Long nrAluno;
                    Scanner scanner = new Scanner(campo);
                    if (scanner.hasNextLong())
                        nrAluno = scanner.nextLong();
                    else {
                        texto.append("Insert a correct student ID!");
                        return texto.toString();
                    }

                    if (searchStudent(nrAluno) == null) {
                        texto.append("This student does not exist!");
                        return texto.toString();
                    }

                    ((SelfProposedProject) proposal).setNrAluno(nrAluno);
                }
            }
        }

        return texto.toString();
    }

    public String verificaNome(String titulo) {
        String nome;
        do {
            nome = PAInput.readString(titulo, false);
        } while (!nome.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$"));

        return nome;
    }

    public String verificaEmail(String titulo) {
        String email;
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern;
        Matcher matcher;

        do {
            email = PAInput.readString(titulo, false);
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(email);
        } while (!matcher.matches());

        return email;
    }

    public String verificaCurso(String titulo) {
        String curso;

        do {
            curso = PAInput.readString(titulo, false);
        } while (!curso.equalsIgnoreCase("LEI") && !curso.equalsIgnoreCase("LEI-PL"));

        return curso;
    }

    public String verificaRamo(String titulo) {
        String ramo;

        do {
            ramo = PAInput.readString(titulo, false);
        } while (!ramo.equalsIgnoreCase("DA") && !ramo.equalsIgnoreCase("RAS") && !ramo.equalsIgnoreCase("SI"));

        return ramo;
    }

    public double verificaClassificacao(String titulo) {
        double classificacao;

        do {
            classificacao = PAInput.readNumber(titulo, false);
        } while (classificacao > 20 || classificacao < 0);

        return classificacao;
    }

    public Boolean getClosedPhases(int i) {
        return closedPhases.get(i);
    }

    public boolean fecharConfigurationState() {
        ArrayList<Stages> stages = new ArrayList<>();
        ArrayList<Projects> projects = new ArrayList<>();
        int ras = 0, da = 0, si = 0;

        for (Proposals proposal : proposals) {
            if (proposal instanceof Stages)
                stages.add((Stages) proposal);
            else if (proposal instanceof Projects)
                projects.add((Projects) proposal);
        }

        for (Stages stage : stages) {
            if(stage.getAreaDestino().contains("DA") || stage.getAreaDestino().contains("da"))
                da++;
            else if(stage.getAreaDestino().contains("RAS") || stage.getAreaDestino().contains("ras"))
                ras++;
            else if(stage.getAreaDestino().contains("SI") || stage.getAreaDestino().contains("si"))
                si++;
        }

        for (Projects project : projects) {
            if(project.getAreaDestino().contains("DA") || project.getAreaDestino().contains("da"))
                da++;
            else if(project.getAreaDestino().contains("RAS") || project.getAreaDestino().contains("ras"))
                ras++;
            else if(project.getAreaDestino().contains("SI") || project.getAreaDestino().contains("si"))
                si++;
        }

        for (Students student : students) {
            if(student.getRamo().equalsIgnoreCase("DA"))
                da--;
            else if(student.getRamo().equalsIgnoreCase("RAS"))
                ras--;
            if(student.getRamo().equalsIgnoreCase("SI"))
                si--;
        }

        if (ras >= 0 && da >= 0 && si >= 0) {
            this.closedPhases.set(0, true);
            return true;
        }

        return false;
    }

    public boolean fecharApplicationOptionsState() {
        if(getClosedPhases(0)) {
            this.closedPhases.set(1, true);
            return true;
        }

        return false;
    }

    public boolean fecharAssignmentProposalsState() {

        for(Long nrAluno: mapApplications.keySet()) {
            if(!mapStudentProposal.containsKey(searchStudent(nrAluno)))
                return false;
        }

        this.closedPhases.set(2, true);
        return true;
    }

    @Override
    public String toString() {
        return "Sistem: currentState = " + currentState;
    }

}
