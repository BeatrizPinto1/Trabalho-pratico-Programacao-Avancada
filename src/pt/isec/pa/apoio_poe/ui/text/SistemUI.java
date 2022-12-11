package pt.isec.pa.apoio_poe.ui.text;

import pt.isec.pa.apoio_poe.model.data.*;
import pt.isec.pa.apoio_poe.model.fsm.SistemContext;
import pt.isec.pa.apoio_poe.model.fsm.SistemState;
import pt.isec.pa.apoio_poe.ui.text.utils.PAInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemUI {
    SistemContext fsm;
    Scanner sc;

    public SistemUI(SistemContext fsm) {
        this.fsm = fsm;
        this.sc = new Scanner(System.in);
    }

    boolean finish = false;

    public void start() {
        //ModelLog log = ModelLog.getInstance();
        while (!finish) {

            //for (String str : ModelLog.getInstance().getLog())
            //  System.out.println("Log: " + str);
            //log.reset();

            switch (fsm.getState()) {
                case CONFIGURATION_STATE -> ConfigurationStateUI();
                case CONFIGURATION_STATE_LOCK -> ConfigurationStateLOCKUI();

                case STUDENTS_MANAGEMENT -> StudentsManagementUI();
                case TEACHERS_MANAGEMENT -> TeachersManagementUI();
                case PROPOSALS_MANAGEMENT -> ProposalsManagementUI();

                case APPLICATION_OPTIONS -> ApplicationOptionsUI();
                case APPLICATION_OPTIONS_LOCK -> ApplicationOptionsLOCKUI();

                case ASSIGNMENT_PROPOSALS -> AssignmentProposalsUI();
                case ASSIGNMENT_PROPOSALS_LOCK -> AssignmentProposalsLOCKUI();

                //case DRAW_STATE -> DrawStateUI();
                case ASSIGNMENT_ADVISERS -> AssignmentAdvisersUI();
                //case ASSIGNMENT_ADVISERS_LOCK -> AssignmentAdvisersLOCKUI();

                case CONSULT_STATE -> ConsultStateUI();
            }

        }
    }

    private void ConfigurationStateUI() {
        switch (PAInput.chooseOption("*** Configuration State ***", "Students Management", "Teachers Management", "Proposals Management", "Save", "Load", "Close Phase", "Next Phase", "Quit")) {
            case 1 -> fsm.change(SistemState.STUDENTS_MANAGEMENT);
            case 2 -> fsm.change(SistemState.TEACHERS_MANAGEMENT);
            case 3 -> fsm.change(SistemState.PROPOSALS_MANAGEMENT);
            case 4 -> {
                String nome = PAInput.readString("Insira um nome para salvar o seu jogo: ", false);
                fsm.guarda(nome);
                //fsm.save();
            }
            case 5 -> {
                String carreg;
                fsm.mostrarPastaSaves();
                do {
                    carreg = PAInput.readString("Insira o nome do save que pretende carregar: ", false);
                } while (fsm.verificarPasta(carreg) != 1);
                fsm.carrega(carreg);
                //fsm.load();
            }
            case 6 -> {
                if (!fsm.fecharState())
                    System.out.println("This phase cannot be closed yet!");
                else
                    fsm.change(SistemState.CONFIGURATION_STATE_LOCK);
            }
            case 7 -> fsm.up();
            default -> finish = true;
        }
        //System.out.println("Fase fechada: " + fsm.isFecharFase());
        //fsm.change(SistemState.CONFIGURATION_STATE); -- SO NO CONFIGURATION --- MUDAR DE ESTADO NO PROPRIOCONFIG A CHMAAR AS FUNCOES
    }

    private void ConfigurationStateLOCKUI() {
        switch (PAInput.chooseOption("*** Configuration State ***", "Consut Students", "Consult Teachers", "Consult Proposals", "Next Phase", "Quit")) {
            case 1 -> ConsultStudentUI();
            case 2 -> ConsultTeacherUI();
            case 3 -> ConsultProposalsUI();
            case 4 -> fsm.up();
            default -> finish = true;
        }
    }

    private void ApplicationOptionsUI() {
        switch (PAInput.chooseOption("*** Application Options ***", "Applications Management", "Student lists", "Proposal lists", "Save", "Load", "Close Phase", "Next Phase", "Previous Phase", "Quit")) {
            case 1 -> ApplicationsManagementUI();
            case 2 -> StudentListsUI();
            case 3 -> ProposalListsUI();
            case 4 -> {
                String nome = PAInput.readString("Insira um nome para salvar o seu jogo: ", false);
                fsm.guarda(nome);
                //fsm.save();
            }
            case 5 -> {
                String carreg;
                fsm.mostrarPastaSaves();
                do {
                    carreg = PAInput.readString("Insira o nome do save que pretende carregar: ", false);
                } while (fsm.verificarPasta(carreg) != 1);
                fsm.carrega(carreg);
                //fsm.load();
            }
            case 6 -> {
                if (!fsm.fecharState())
                    System.out.println("This phase cannot be closed yet!");
                else
                    fsm.change(SistemState.APPLICATION_OPTIONS_LOCK);
            }
            case 7 -> fsm.up();
            case 8 -> fsm.down();
            default -> finish = true;
        }
    }

    private void ApplicationOptionsLOCKUI() {
        switch (PAInput.chooseOption("*** Application Options ***", "Consult Applications", "Student lists", "Proposal lists", "Next Phase", "Previous Phase", "Quit")) {
            case 1 -> consultApplicationUI();
            case 2 -> StudentListsUI();
            case 3 -> ProposalListsUI();
            case 4 -> fsm.up();
            case 5 -> fsm.down();
            default -> finish = true;
        }
    }

    private void AssignmentProposalsUI() {
        switch (PAInput.chooseOption("*** Assignment Proposals ***", "Automatic assignment of self-proposals or teachers proposals with associated student",
                "Automatic assignment of proposals to students without assignments",
                "Manual assignment of proposals to students without assignments",
                "Manually remove of assignments", "Undo", "Redo", "Export data",
                "Students list", "Proposals list",
                "Save", "Load","Close Phase", "Next Phase", "Previous Phase", "Quit")) {
            case 1 -> {
                switch (PAInput.chooseOption("** Automatic assignment **", "Automatic assignment of self-proposals", "Automatic assignment of teachers proposals with associated student")) {
                    case 1 -> fsm.automaticSelfProprosals();
                    case 2 -> fsm.automaticteachersProprosals();
                }
            }
            case 2 -> {
                if(!fsm.automaticStudentsWithoutAssignmentsProposals())
                    DrawStateUI(); //RETORNAR PROPOSTA
            }
            case 3 -> manualStudentsWithoutAssignmentsProposals();
            case 4 -> manualRemoveAssignments();
            case 5 -> fsm.undo();
            case 6 -> fsm.redo();
            case 7 -> {

                String filename = PAInput.readString("Filename: ", false);
                try {
                    fsm.exportToFile(filename, 5);
                } catch (Exception e) {
                    e.getMessage();
                }

            }
            case 8 -> studentsList();
            case 9 -> proposalsList();
            case 10 -> {
                String nome = PAInput.readString("Insira um nome para salvar o seu jogo: ", false);
                fsm.guarda(nome);
                //fsm.save();
            }
            case 11 -> {
                String carreg;
                fsm.mostrarPastaSaves();
                do {
                    carreg = PAInput.readString("Insira o nome do save que pretende carregar: ", false);
                } while (fsm.verificarPasta(carreg) != 1);
                fsm.carrega(carreg);
                //fsm.load();
            }
            case 12 -> {
                if (!fsm.fecharState())
                    System.out.println("This phase cannot be closed yet!");
                else
                    fsm.change(SistemState.ASSIGNMENT_PROPOSALS_LOCK);
            }
            case 13 -> fsm.up();
            case 14 -> fsm.down();
            default -> finish = true;
        }
    }

    private void AssignmentProposalsLOCKUI() {
        switch (PAInput.chooseOption("*** Assignment Proposals ***", "Students list", "Proposals list", "Next Phase", "Previous Phase", "Quit")) {
            case 1 -> studentsList();
            case 2 -> proposalsList();
            case 3 -> fsm.up();
            case 4 -> fsm.down();
            default -> finish = true;
        }
    }

    private void AssignmentAdvisersUI() {
        switch (PAInput.chooseOption("*** Assignment Advisers ***", "Automatic association of teachers proposing projects",
                "Manual association of teachers", "Undo", "Redo",
                "Export data",
                "Data about assignment of advisors",
                "Save", "Load", "Close Phase", "Previous Phase", "Quit")) {
            case 1 -> fsm.automaticAssociationOfTeachersProposingProjects();
            case 2 -> associationOfTeachers();//manualAssociationOfTeachers();
            case 3 -> fsm.undo();
            case 4 -> fsm.redo();
            case 5 -> {
                String filename = PAInput.readString("Filename: ", false);
                try {
                    fsm.exportToFile(filename, 6);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            case 6 -> dataAboutAssignmentOfAdvisors();
            case 7 -> {
                String nome = PAInput.readString("Insira um nome para salvar o seu jogo: ", false);
                fsm.guarda(nome);
                //fsm.save();
            }
            case 8 -> {
                String carreg;
                fsm.mostrarPastaSaves();
                do {
                    carreg = PAInput.readString("Insira o nome do save que pretende carregar: ", false);
                } while (fsm.verificarPasta(carreg) != 1);
                fsm.carrega(carreg);
                //fsm.load();
            }
            case 9 -> fsm.up();
            case 10 -> fsm.down();
            default -> finish = true;
        }
    }

    private void ConsultStateUI() {
        switch (PAInput.chooseOption("*** Consult State ***", "Export data", "List of students with assigned proposals",
                "List of students without assigned proposals and with application options",
                "Available proposals", "Assigned proposals", "Guidance by teacher", "Quit")) {
            case 1 -> {
                String filename = PAInput.readString("Filename: ", false);
                try {
                    fsm.exportToFile(filename, 6);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            case 2 -> System.out.println(fsm.CSwithProposalsAssigned());
            case 3 -> System.out.println(fsm.studentsWithoutAssignedProposalsAndWithApplicationOptions());
            case 4 -> System.out.println(fsm.availableProposals());
            case 5 -> System.out.println(fsm.assignedProposals());
            case 6 -> guidanceByTeacher();
            default -> finish = true;
        }
    }

    private void ApplicationsManagementUI() {
        switch (PAInput.chooseOption("** Applications Management **", "Import applications", "Export applications", "Insert", "Consult", "Edit", "Remove")) {
            case 1 ->  {
                String filename = PAInput.readString("Filename: ", false);
                fsm.setApplications(filename);
            }
            case 2 -> {

                String filename = PAInput.readString("Filename: ", false);
                try {
                    fsm.exportToFile(filename, 4);
                } catch (Exception e) {
                    e.getMessage();
                }

            }
            case 3 -> insertApplicationUI();
            case 4 -> consultApplicationUI();
            case 5 -> editApplicationUI();
            case 6 -> removeApplicationUI();
        }
    }

    private void insertApplicationUI() {
        int i = 0;
        Proposals proposal;
        List<String> propostas = new ArrayList<>();

        System.out.println(" --> Insert your proposals <--");
        long nrEstudante = 0;
        do {
            String nr = PAInput.readString("Student ID: ", false);
            Scanner scanner = new Scanner(nr);

            if (scanner.hasNextLong())
                nrEstudante = scanner.nextLong();
        }while(nrEstudante == 0);

        if ((fsm.searchStudent(nrEstudante)) == null) {
            System.out.println("This student does not exist!");
            return;
        }

        boolean continuar = false;
        do {
            String codProposta = PAInput.readString(++i + " - Proposal code: ", false);
            if ((proposal = fsm.searchProposal(codProposta)) == null) {
                System.out.println("This proposal does not exist!");
                return;
            }

            if (proposal.getNrAluno() != 0)
                System.out.println("This proposal already has a student associated!");
            else
                propostas.add(codProposta);

            switch (PAInput.chooseOption("Insert more?", "Yes", "No")) {
                case 1 -> continuar = true;
                case 2 -> continuar = false;
            }

        } while (continuar && i <= 6);

        if (propostas.size() != 0)
            System.out.println(fsm.addAplications(nrEstudante, propostas).isEmpty());
    }


    private void consultApplicationUI() {
        System.out.println("\n-> Applications <-");
        System.out.println(fsm.consultApplications());
    }

    private void editApplicationUI() {
        long nrEstudante = 0;

        do {
            String nr = PAInput.readString("Student ID: ", false);
            Scanner scanner = new Scanner(nr);

            if (scanner.hasNextLong())
                nrEstudante = scanner.nextLong();
        }while(nrEstudante == 0);

        switch (PAInput.chooseOption("Edit", "Add applications", "Remove application")) {
            case 1 -> {
                String codProposta = PAInput.readString("Proposal code: ", false);
                System.out.println(fsm.editProposalsAdd(nrEstudante, codProposta));
            }
            case 2 -> {
                String codProposta = PAInput.readString("Proposal code: ", false);
                System.out.println(fsm.editProposalsRemove(nrEstudante, codProposta));
            }
        }
    }


    private void removeApplicationUI() {
        long nrEstudante = 0;
        do {
            String nr = PAInput.readString("Student ID: ", false);
            Scanner scanner = new Scanner(nr);

            if (scanner.hasNextLong())
                nrEstudante = scanner.nextLong();
        }while(nrEstudante == 0);

        System.out.println(fsm.removeApplications(nrEstudante));
    }

    private void StudentListsUI() {
        switch (PAInput.chooseOption("** Student Lists **", "With self-proposal", "With application already registered", "No registered application")) {
            case 1 -> System.out.println(fsm.consultSelfProposedStudents());
            case 2 -> System.out.println(fsm.CSWithAlreadyRegisteredApplication());
            case 3 -> System.out.println(fsm.CSwithoutRegisteredApplication());
        }
    }

    private void ProposalListsUI() {
        boolean firstChoise = false, secondChoise = false, thirdChoise = false, fourthChoise = false;
        int op;

        do {
            switch (PAInput.chooseOption("** Proposal Lists **", "Student self-proposals", "Teachers proposals", "Proposals with applications", "Proposals without applications")) {
                case 1 -> firstChoise = true;
                case 2 -> secondChoise = true;
                case 3 -> thirdChoise = true;
                case 4 -> fourthChoise = true;
            }

            op = switch (PAInput.chooseOption("Insert more filters? ", "Yes", "NO")) {
                case 1 -> 1;
                case 2 -> 2;
                default -> throw new IllegalStateException("Unexpected value: " + PAInput.chooseOption("Insert more filters? ", "Yes", "NO"));
            };
        } while (op != 2);

        if (firstChoise)
            System.out.println(fsm.studentsSelfProposals());

        if (secondChoise)
            System.out.println(fsm.teachersProposals());

        if (thirdChoise)
            System.out.println(fsm.proposalsWithApplications());

        if (fourthChoise)
            System.out.println(fsm.proposalsWithoutApplications());

    }

    public void manualStudentsWithoutAssignmentsProposals() {
        long nrEstudante = 0;
        do {
            String nr = PAInput.readString("Student ID: ", false);
            Scanner scanner = new Scanner(nr);

            if (scanner.hasNextLong())
                nrEstudante = scanner.nextLong();
        }while(nrEstudante == 0);
        String codProposta = PAInput.readString("Proposal identification: ", false);

        System.out.println(fsm.setMapStudentProposal(nrEstudante, codProposta));
    }

    public void manualRemoveAssignments() {
        switch (PAInput.chooseOption("** Remove Assignments **", "Remove one assignment", "Remove all assignments")) {
            case 1 -> {
                long nrEstudante = 0;
                do {
                    String nr = PAInput.readString("Student ID: ", false);
                    Scanner scanner = new Scanner(nr);

                    if (scanner.hasNextLong())
                        nrEstudante = scanner.nextLong();
                }while(nrEstudante == 0);

                System.out.println(fsm.removeOneAssignment(nrEstudante));
            }
            case 2 -> fsm.removeAssignments();
        }
    }

    private void studentsList() {

        switch (PAInput.chooseOption("** Student Lists **", "With self-proposal", "With application already registered", "With proposal assigned", "Without proposal assigned")) {
            case 1 -> System.out.println(fsm.consultSelfProposedStudents());
            case 2 -> System.out.println(fsm.CSWithAlreadyRegisteredApplication());
            case 3 -> System.out.println(fsm.CSwithProposalsAssigned());
            case 4 -> System.out.println(fsm.CSwithoutProposalsAssigned());
        }

    }

    private void proposalsList() {

        boolean firstChoise = false, secondChoise = false, thirdChoise = false, fourthChoise = false;
        int op;

        do {
            switch (PAInput.chooseOption("** Proposal Lists **", "Student self-proposals", "Teachers proposals", "Available proposals", "Assigned proposals")) {
                case 1 -> firstChoise = true;
                case 2 -> secondChoise = true;
                case 3 -> thirdChoise = true;
                case 4 -> fourthChoise = true;
            }

            op = switch (PAInput.chooseOption("Insert more filters? ", "Yes", "NO")) {
                case 1 -> 1;
                case 2 -> 2;
                default -> throw new IllegalStateException("Unexpected value: " + PAInput.chooseOption("Insert more filters? ", "Yes", "NO"));
            };
        } while (op != 2);

        if (firstChoise)
            System.out.println(fsm.studentsSelfProposals());

        if (secondChoise)
            System.out.println(fsm.teachersProposals());

        if (thirdChoise)
            System.out.println(fsm.availableProposals());

        if (fourthChoise)
            System.out.println(fsm.assignedProposals());

    }

    private void dataAboutAssignmentOfAdvisors() {
        switch (PAInput.chooseOption("** Data about assignment of advisors **",
                "List of students with an assigned proposal and with an associated advisor"
                , "List of students with an assigned proposal but without an associate supervisor"
                , "Guidance by teacher")) {
            case 1 -> System.out.println(fsm.studentsWithAnAssignedProposalAndWithAnAssociatedAdvisor());
            case 2 -> System.out.println(fsm.studentsWithAnAssignedProposalButWithoutAnAssociatedAdvisor());
            case 3 -> guidanceByTeacher();
        }
    }

    private void guidanceByTeacher() {
        switch (PAInput.chooseOption("** Number of guidance per teacher **",
                "In media", "Minimum", "Maximum", "By teacher specified")) {
            case 1 -> System.out.println(fsm.inMedium());
            case 2 -> System.out.println(fsm.inMax());
            case 3 -> System.out.println(fsm.inMin());
            case 4 -> {
                String emailDocente = PAInput.readString("Teacher email: ", false);
                System.out.println(fsm.inTeacher(emailDocente));
            }
        }
    }

    private void associationOfTeachers() {
        switch (PAInput.chooseOption("** Association of Teachers **", "Insert", "Consult", "Edit", "Remove")) {
            case 1 -> manualAssociationOfTeachers();
            case 2 -> System.out.println(fsm.getMapProposalTeacher());
            case 3 -> editAssociationOfTeachers();
            case 4 -> removeAssociationOfTeachers();
        }
    }

    private void StudentsManagementUI() {
        switch (PAInput.chooseOption("** Students Management **", "Import students", "Export students", "Insert", "Consult", "Edit", "Remove", "Back to Configuration State")) {
            case 1 -> {
                String filename = PAInput.readString("Filename: ", false);
                fsm.setStudents(filename);
            }
            case 2 -> {

                String filename = PAInput.readString("Filename: ", false);
                try {
                    fsm.exportToFile(filename, 1);
                } catch (Exception e) {
                    e.getMessage();
                }

            }
            case 3 -> InsertStudentUI();
            case 4 -> ConsultStudentUI();
            case 5 -> {
                if (!EditStudentUI())
                    System.out.println("This student does not exist!");
            }
            case 6 -> {
                System.out.println("\nStudent number: ");
                long nrEstudante = sc.nextLong();

                System.out.println(fsm.removeStudent(nrEstudante));
            }
            case 7 -> fsm.change(SistemState.CONFIGURATION_STATE);
        }
    }

    private void InsertStudentUI() {
        long nrEstudante = 0;
        boolean acedeEstagios = false;

        do {
            String nr = PAInput.readString("Student ID: ", false);
            Scanner scanner = new Scanner(nr);

            if (scanner.hasNextLong())
                nrEstudante = scanner.nextLong();
        }while(nrEstudante == 0);

        String nome = fsm.verificaNome("Name: ");
        String email = fsm.verificaEmail("Email: ");
        String curso = fsm.verificaCurso("Curse: ");
        String ramo = fsm.verificaRamo("Branch: ");
        double classificacao = fsm.verificaClassificacao("Classification: ");

        boolean repeat;
        do {
            repeat = false;
            String estagios = PAInput.readString("Possibility of internships: ", false);
            Scanner scanner = new Scanner(estagios);

            if (scanner.hasNextBoolean())
                acedeEstagios = scanner.nextBoolean();
            else
                repeat = true;
        }while(repeat);

        if (!fsm.addStudent(new Students(nrEstudante, nome, email, curso, ramo, classificacao, acedeEstagios)))
            System.out.println("This student already exists!");
    }

    private void ConsultStudentUI() {
        System.out.println("\n-> Students <-");
        System.out.println(fsm.consultStudents());
    }

    private boolean EditStudentUI() {
        long nrEstudante = 0;

        do {
            String nr = PAInput.readString("Student ID: ", false);
            Scanner scanner = new Scanner(nr);

            if (scanner.hasNextLong())
                nrEstudante = scanner.nextLong();
        }while(nrEstudante == 0);

        Students student;

        if ((student = fsm.searchStudent(nrEstudante)) == null)
            return false;
        else {
            switch (PAInput.chooseOption("Edit", "Name", "Email", "Curse", "Branch", "Classification", "Stages")) {
                case 1 -> {
                    String nome = fsm.verificaNome("What's the new name? ");
                    fsm.editaEstudante(nrEstudante, 1, nome);
                    //student.setNome(nome);
                }
                case 2 -> {
                    String email = fsm.verificaEmail("What's the new email? ");
                    fsm.editaEstudante(nrEstudante, 2, email);
                    //student.setEmail(email);
                }
                case 3 -> {
                    String curso = fsm.verificaCurso("What's the new curse? ");
                    fsm.editaEstudante(nrEstudante, 3, curso);
                    //student.setCurso(curso);
                }
                case 4 -> {
                    String ramo = fsm.verificaRamo("What's de new branch: ");
                    fsm.editaEstudante(nrEstudante, 4, ramo);
                    //student.setRamo(ramo);
                }
                case 5 -> {
                    double classificacao = fsm.verificaClassificacao("What's the new classification? ");
                    fsm.editaEstudante(nrEstudante, 5, String.valueOf(classificacao));
                    //student.setClassificacao(classificacao);
                }
                case 6 -> fsm.editaEstudante(nrEstudante, 6, String.valueOf(!student.isAcedeEstagios()));
                        //student.setAcedeEstagios(!student.isAcedeEstagios());
            }
            return true;
        }
    }

    private void TeachersManagementUI() {
        switch (PAInput.chooseOption("** Teachers Management **", "Import teachers", "Export teachers", "Insert", "Consult", "Edit", "Remove", "Back to Configuration State")) {
            case 1 -> {
                String filename = PAInput.readString("Filename: ", false);
                fsm.setTeachers(filename);
            }
            case 2 -> {
                String filename = PAInput.readString("Filename: ", false);
                try {
                    fsm.exportToFile(filename, 2);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            case 3 -> InsertTeacherUI();
            case 4 -> ConsultTeacherUI();
            case 5 -> {
                if (!EditTeacherUI())
                    System.out.println("This teacher does not exist!");
            }
            case 6 -> {
                String email = PAInput.readString("Teacher email:", false);
                System.out.println(fsm.removeTeachers(email));
            }
            case 7 -> fsm.change(SistemState.CONFIGURATION_STATE);
        }
    }

    private void InsertTeacherUI() {
        String nome = fsm.verificaNome("Name: ");
        String email = fsm.verificaEmail("Email: ");

        if (!fsm.addTeachers(new Teachers(nome, email)))
            System.out.println("This teacher already exists!");
    }

    private void ConsultTeacherUI() {
        System.out.println("\n-> Teachers <-");
        System.out.println(fsm.consultTeachers());
    }

    private boolean EditTeacherUI() {
        String email = PAInput.readString("Teacher email:", false);

        if (fsm.searchTeacher(email) == null)
            return false;
        else {
            String nome = fsm.verificaNome("What's the new name? ");
            fsm.editaDocente(email,  nome);
            return true;
        }
    }

    private void ProposalsManagementUI() {
        switch (PAInput.chooseOption("** Proposals **", "Import Proposals", "Export Proposals", "Insert", "Consult", "Edit", "Remove", "Back to Configuration State")) {
            case 1 -> {
                String filename = PAInput.readString("Filename: ", false);
                fsm.setProposals(filename);
            }
            case 2 -> {
                String filename = PAInput.readString("Filename: ", false);
                try {
                    fsm.exportToFile(filename, 3);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            case 3 -> InsertProposalUI();
            case 4 -> ConsultProposalsUI();
            case 5 -> {
                if (!EditProposalUI())
                    System.out.println("This proposal does not exist!");
            }
            case 6 -> {
                String codigoIdentificacao = PAInput.readString("Identification code:", false);
                System.out.println(fsm.removeProposal(codigoIdentificacao));
            }
            case 7 -> fsm.change(SistemState.CONFIGURATION_STATE);
        }
    }

    private void InsertProposalUI() {
        switch (PAInput.chooseOption("Type", "Stage", "Project", "Internship/self-proposed project")) {
            case 1 -> InsertStageUI();
            case 2 -> InsertProjectUI();
            case 3 -> InsertInternshipORselfProposedProjectUI();
        }
    }

    private void InsertStageUI() {

        String codigoIdentificacao = PAInput.readString("Identification code: ", false);
        String titulo = PAInput.readString("Title: ", false);
        String areaDestino = fsm.verificaRamo("Destination area: ");
        String identidadeAcolhimento = PAInput.readString("Host identity: ", false);

        switch (PAInput.chooseOption("Put the student's nomination? ", "YES", "NO")) {
            case 1 -> {
                long nrEstudante = 0;
                do {
                    String nr = PAInput.readString("Student ID: ", false);
                    Scanner scanner = new Scanner(nr);

                    if (scanner.hasNextLong())
                        nrEstudante = scanner.nextLong();
                }while(nrEstudante == 0);

                if (fsm.searchStudent(nrEstudante) == null)
                    System.out.println("This student does not exist!");
                else {
                    if (!fsm.addProposals(new Stages(codigoIdentificacao, areaDestino, titulo, identidadeAcolhimento, nrEstudante)))
                        System.out.println("This proposal already exists!");
                }
            }

            case 2 -> {
                if (!fsm.addProposals(new Stages(codigoIdentificacao, areaDestino, titulo, identidadeAcolhimento)))
                    System.out.println("This proposal already exists!");
            }
        }

    }

    private void InsertProjectUI() {

        if (fsm.consultTeachers() == null) {
            System.out.println("There are no teachers!");
            return;
        }

        String codigoIdentificacao = PAInput.readString("Identification code: ", false);
        String titulo = PAInput.readString("Title: ", false);
        String areaDestino = fsm.verificaRamo("Destination area: ");
        String emailDocente = fsm.verificaEmail("Teacher's email:");
        if (fsm.searchTeacher(emailDocente) == null) {
            System.out.println("This teacher does not exist!");
            return;
        }

        switch (PAInput.chooseOption("Put the student's nomination? ", "YES", "NO")) {
            case 1 -> {
                long nrEstudante = 0;
                do {
                    String nr = PAInput.readString("Student ID: ", false);
                    Scanner scanner = new Scanner(nr);

                    if (scanner.hasNextLong())
                        nrEstudante = scanner.nextLong();
                }while(nrEstudante == 0);

                if (fsm.searchStudent(nrEstudante) == null)
                    System.out.println("This student does not exist!");
                else {
                    if (!fsm.addProposals(new Projects(codigoIdentificacao, areaDestino, titulo, emailDocente, nrEstudante)))
                        System.out.println("This proposal already exists!");
                }
            }

            case 2 -> {
                if (!fsm.addProposals(new Projects(codigoIdentificacao, areaDestino, titulo, emailDocente)))
                    System.out.println("This proposal already exists!");
            }
        }

    }

    private void InsertInternshipORselfProposedProjectUI() {
        String codigoIdentificacao = PAInput.readString("Identification code: ", false);
        String titulo = PAInput.readString("Title: ", false);
        long nrEstudante = 0;
        do {
            String nr = PAInput.readString("Student ID: ", false);
            Scanner scanner = new Scanner(nr);

            if (scanner.hasNextLong())
                nrEstudante = scanner.nextLong();
        }while(nrEstudante == 0);

        if (fsm.searchStudent(nrEstudante) == null) {
            System.out.println("This student does not exist!");
            return;
        }

        if (!fsm.addProposals(new SelfProposedProject(codigoIdentificacao, titulo, nrEstudante)))
            System.out.println("This proposal already exists!");
    }


    private void ConsultProposalsUI() {
        System.out.println("\n-> Proposals <-");
        System.out.println(fsm.consultProposals());
    }

    private boolean EditProposalUI() {
        String codigoIdentificacao = PAInput.readString("Identification code:", false);
        Proposals proposal;

        if ((proposal = fsm.searchProposal(codigoIdentificacao)) == null)
            return false;
        else {
            switch (fsm.typeProposal(proposal)) {
                case 1 -> editStages(codigoIdentificacao);
                case 2 -> editProjects(codigoIdentificacao);
                case 3 -> editSelfProposedProjects(codigoIdentificacao);
            }
            return true;
        }
    }

    private void editStages(String codigoIdentificacao) {
        switch (PAInput.chooseOption("Edit", "Title", "Destination area", "Host identity", "Student number")) {
            case 1 -> {
                String title = PAInput.readString("What's the new title? ", false);
                if(!fsm.editaProposta(codigoIdentificacao, 1, title).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 1, title));
            }
            case 2 -> {
                String areaDestino = fsm.verificaRamo("What's the new destination area? ");
                if(!fsm.editaProposta(codigoIdentificacao, 2, areaDestino).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 2, areaDestino));
            }
            case 3 -> {
                String identidadeAcolhimento = PAInput.readString("What's the new host identity? ", false);
                if(!fsm.editaProposta(codigoIdentificacao, 3, identidadeAcolhimento).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 3, identidadeAcolhimento));
            }
            case 4 -> {
                String nr = PAInput.readString("Student ID: ", false);
                if(!fsm.editaProposta(codigoIdentificacao, 4, nr).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 4, nr));
            }
        }
    }

    private void editProjects(String codigoIdentificacao) {
        switch (PAInput.chooseOption("Edit", "Title", "Destination area", "Teacher email", "Student Number")) {
            case 1 -> {
                String title = PAInput.readString("What's the new title? ", false);
                if(!fsm.editaProposta(codigoIdentificacao, 1, title).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 1, title));
            }
            case 2 -> {
                String areaDestino = fsm.verificaRamo("What's the new destination area? ");
                if(!fsm.editaProposta(codigoIdentificacao, 2, areaDestino).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 2, areaDestino));
            }
            case 3 -> {
                String emailDocente = fsm.verificaEmail("What's the new email? ");
                if(!fsm.editaProposta(codigoIdentificacao, 3, emailDocente).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 3, emailDocente));
            }
            case 4 -> {
                String nr = PAInput.readString("Student ID: ", false);
                if(!fsm.editaProposta(codigoIdentificacao, 4, nr).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 4, nr));
            }
        }
    }

    private void editSelfProposedProjects(String codigoIdentificacao) {
        switch (PAInput.chooseOption("Edit", "Title", "Student Number")) {
            case 1 -> {
                String title = PAInput.readString("What's the new title? ", false);
                if(!fsm.editaProposta(codigoIdentificacao, 1, title).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 1, title));
            }
            case 2 -> {
                String nr = PAInput.readString("Student ID: ", false);
                if(!fsm.editaProposta(codigoIdentificacao, 2, nr).isEmpty())
                    System.out.println(fsm.editaProposta(codigoIdentificacao, 2, nr));
            }
        }
    }

    public void DrawStateUI() {
        boolean continuar = true;
        do{
            switch (PAInput.chooseOption("** There was a tie, resolve it manually! **", "Assign proposal to the student",
                    "Consult the students involved", "Consult the applications of the students involved")) {
                case 1 -> {
                    solveDraw();
                    continuar = false;
                }
                case 2 -> System.out.println(fsm.getStudentsDraw());
                case 3 -> System.out.println(fsm.consultApplicationsStudentsInvolved());
            }
        }while (continuar);
    }

    private void solveDraw() {
        String nrAluno1 = String.valueOf(fsm.getStudentsDraw().get(0).getNrEstudante());
        String nrAluno2 = String.valueOf(fsm.getStudentsDraw().get(1).getNrEstudante());
        switch (PAInput.chooseOption("The same proposal came to both students, which one should take it", nrAluno1, nrAluno2)) {
            case 1 -> {
                if(!fsm.Draw(1))
                    DrawStateUI();
            }
            case 2 -> {
                if(!fsm.Draw(2))
                    DrawStateUI();
            }
        }
    }

    public void manualAssociationOfTeachers() {
        String emailDocente = PAInput.readString("Teacher email: ", false);
        long nrEstudante = 0;
        do {
            String nr = PAInput.readString("Student ID: ", false);
            Scanner scanner = new Scanner(nr);

            if (scanner.hasNextLong())
                nrEstudante = scanner.nextLong();
        }while(nrEstudante == 0);

        System.out.println(fsm.setMapProposalTeacher(emailDocente, nrEstudante));
    }

    private void editAssociationOfTeachers() {
        String emailDocente = PAInput.readString("Teacher email: ", false);

        System.out.println("New student ID to associate with the teacher");
        long nrEstudante = 0;
        do {
            String nr = PAInput.readString("Student ID: ", false);
            Scanner scanner = new Scanner(nr);

            if (scanner.hasNextLong())
                nrEstudante = scanner.nextLong();
        }while(nrEstudante == 0);

        System.out.println(fsm.editTeachersAssociation(emailDocente, nrEstudante));
    }


    private void removeAssociationOfTeachers() {
        String emailDocente = PAInput.readString("Teacher email: ", false);
        System.out.println(fsm.removeTeachersAssociation(emailDocente));
    }

}
