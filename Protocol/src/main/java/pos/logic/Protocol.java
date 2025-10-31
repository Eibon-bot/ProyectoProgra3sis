package pos.logic;

public class Protocol {
    public static final String SERVER = "localhost";
    public static final int PORT = 1234;

    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_ERROR    = 1;


    public static final int MEDICO_CREATE = 101;
    public static final int MEDICO_READ   = 102;
    public static final int MEDICO_UPDATE = 103;
    public static final int MEDICO_DELETE = 104;
    public static final int MEDICO_FINDALL = 105;
    public static final int MEDICO_SEARCH_ID = 106;
    public static final int MEDICO_SEARCH_NOMBRE = 107;

    public static final int FARMA_CREATE = 201;
    public static final int FARMA_READ   = 202;
    public static final int FARMA_UPDATE = 203;
    public static final int FARMA_DELETE = 204;
    public static final int FARMA_FINDALL = 205;
    public static final int FARMA_SEARCH_ID = 206;
    public static final int FARMA_SEARCH_NOMBRE = 207;

    public static final int MEDICAMENTO_CREATE = 301;
    public static final int MEDICAMENTO_READ   = 302;
    public static final int MEDICAMENTO_UPDATE = 303;
    public static final int MEDICAMENTO_DELETE = 304;
    public static final int MEDICAMENTO_FINDALL = 305;
    public static final int MEDICAMENTO_SEARCH_COD = 306;
    public static final int MEDICAMENTO_SEARCH_NOMBRE = 307;
    public static final int MEDICAMENTO_FIND_BY_NOMBRE = 308;
    public static final int MEDICAMENTO_FIND_BY_CODIGO = 309;

    public static final int PACIENTE_CREATE = 401;
    public static final int PACIENTE_READ   = 402;
    public static final int PACIENTE_UPDATE = 403;
    public static final int PACIENTE_DELETE = 404;
    public static final int PACIENTE_FINDALL = 405;
    public static final int PACIENTE_SEARCH_NOMBRE = 406;
    public static final int PACIENTE_SEARCH_ID = 407;

    public static final int RECETA_UPDATE = 601;
    public static final int RECETA_CREATE_CON_PRESCRIPCIONES = 602;
    public static final int RECETA_SEARCH_IDPACIENTE = 603;
    public static final int RECETA_SEARCH_NOMBREPACIENTE = 604;
    public static final int RECETA_FILTRAR_ESTADO = 605;
    public static final int RECETA_FINDALL_CON_PRESCRIPCIONES = 606;

    public static final int MEDICO_GENERAR_NUEVO_ID = 108;

    public static final int FARMA_GENERAR_NUEVO_ID = 208;

    public static final int MEDICAMENTO_GENERAR_NUEVO_ID = 310;

    public static final int PACIENTE_GENERAR_NUEVO_ID = 408;

    public static final int RECETA_GENERAR_NUEVO_COD = 607;

    public static final int SYNC=10;
    public static final int ASYNC=11;
    public static final int LOGIN = 801;
    public static final int CAMBIAR_CLAVE = 802;
    public static final int DELIVER_MESSAGE = 901;
    public static final int DISCONNECT=99;

}
