package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Sistema;

public enum SistemState {
    CONFIGURATION_STATE, APPLICATION_OPTIONS, ASSIGNMENT_PROPOSALS, ASSIGNMENT_ADVISERS, CONSULT_STATE,
    STUDENTS_MANAGEMENT, TEACHERS_MANAGEMENT , PROPOSALS_MANAGEMENT,
    CONFIGURATION_STATE_LOCK, APPLICATION_OPTIONS_LOCK, ASSIGNMENT_PROPOSALS_LOCK;
    //DRAW_STATE;

    ISistemState createState(SistemContext context, Sistema elevator) {
        return switch(this) {
            case CONFIGURATION_STATE -> new ConfigurationState(context, elevator);
            case CONFIGURATION_STATE_LOCK -> new ConfigurationStateLOCK(context, elevator);

            case APPLICATION_OPTIONS -> new ApplicationOptionsState(context, elevator);
            case APPLICATION_OPTIONS_LOCK -> new ApplicationOptionsLOCK(context, elevator);

            case ASSIGNMENT_PROPOSALS -> new AssignmentProposalsState(context, elevator);
            case ASSIGNMENT_PROPOSALS_LOCK -> new AssignmentProposalsLOCK(context, elevator);

            case ASSIGNMENT_ADVISERS -> new AssignmentAdvisersState(context, elevator);

            case CONSULT_STATE -> new ConsultState(context, elevator);

            //ESTADOS DA FASE 1
            case STUDENTS_MANAGEMENT -> new StudentsManagement(context, elevator);
            case TEACHERS_MANAGEMENT -> new TeachersManagement(context, elevator);
            case PROPOSALS_MANAGEMENT -> new ProposalsManagement(context, elevator);
        };
    }

}
