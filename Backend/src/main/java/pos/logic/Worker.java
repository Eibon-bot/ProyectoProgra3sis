package pos.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Worker {
    Server srv;
    Socket s;
    Service service;
    ObjectOutputStream os;
    ObjectInputStream is;
    boolean continuar;

    String sid; // Session Id
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;

    public Worker(Server srv, Socket s, ObjectOutputStream os, ObjectInputStream is, String sid, Service service) {
            this.srv = srv;
            this.s = s;
            this.os = os;
            this.is = is;
            this.service = service;
            this.sid = sid;
    }
    public void setAs(Socket as, ObjectOutputStream aos, ObjectInputStream ais) {
        this.as = as;
        this.aos = aos;
        this.ais = ais;
    }

    public void start() {
        try {
            System.out.println("WorkerDespacho atendiendo peticiones...");
            Thread t = new Thread(() -> listen());
            continuar = true;
            t.start();
        } catch (Exception ex) { }
    }

    public void stop() {
        continuar = false;
        System.out.println("Conexión cerrada...");
    }

    public void listen() {
        int method;
        while (continuar) {
            try {
                method = is.readInt();
                System.out.println("Operación: " + method);

                switch (method) {

                    // ================== MÉDICOS ==================
                    case Protocol.MEDICO_CREATE:
                        try {
                            service.createMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.MEDICO_READ:
                        try {
                            Medico m = service.readMedico((String) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(m);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.MEDICO_UPDATE:
                        try {
                            service.updateMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.MEDICO_DELETE:
                        try {
                            service.deleteMedico((Medico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.MEDICO_FINDALL:
                        try {
                            List<Medico> lm = service.findAllMedico();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(lm);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.MEDICO_SEARCH_ID:
                        try {
                            Medico criterio = (Medico) is.readObject();
                            Medico result = (Medico) service.searchMedicoId(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICO_SEARCH_NOMBRE:
                        try {
                            Medico criterio = (Medico) is.readObject();
                            List<Medico> result = service.searchMedicoNombre(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    // ================== FARMACÉUTICOS ==================
                    case Protocol.FARMA_CREATE:
                        try {
                            service.createFarmaceutico((Farmaceutico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.FARMA_READ:
                        try {
                            Farmaceutico f = service.readFarmaceutico((String) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(f);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.FARMA_UPDATE:
                        try {
                            service.updateFarmaceutico((Farmaceutico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.FARMA_DELETE:
                        try {
                            service.deleteFarmaceutico((Farmaceutico) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.FARMA_SEARCH_ID:
                        try {
                            Farmaceutico criterio = (Farmaceutico) is.readObject();
                            Farmaceutico result = (Farmaceutico) service.searchFarmaceuticoId(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.FARMA_SEARCH_NOMBRE:
                        try {
                            Farmaceutico criterio = (Farmaceutico) is.readObject();
                            List<Farmaceutico> result = service.searchFarmaceuticoNombre(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;


                    case Protocol.FARMA_FINDALL:
                        try {
                            List<Farmaceutico> lf = service.findAllFarmaceutico();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(lf);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    // ================== MEDICAMENTOS ==================
                    case Protocol.MEDICAMENTO_CREATE:
                        try {
                            service.createMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.MEDICO_GENERAR_NUEVO_ID:
                        try {
                            String nuevoId = service.generarNuevoIdMedico();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(nuevoId);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.FARMA_GENERAR_NUEVO_ID:
                        try {
                            String nuevoId = service.generarNuevoIdFarma();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(nuevoId);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICAMENTO_GENERAR_NUEVO_ID:
                        try {
                            String prefijo = (String) is.readObject();
                            String nuevoId = service.generarNuevoCodMedicamento(prefijo);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(nuevoId);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.PACIENTE_GENERAR_NUEVO_ID:
                        try {
                            String nuevoId = service.generarNuevoIdPaciente();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(nuevoId);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICAMENTO_READ:
                        try {
                            Medicamento med = service.readMedicamento((String) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(med);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.MEDICAMENTO_UPDATE:
                        try {
                            service.updateMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.MEDICAMENTO_DELETE:
                        try {
                            service.deleteMedicamento((Medicamento) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.MEDICAMENTO_SEARCH_COD:
                        try {
                            Medicamento criterio = (Medicamento) is.readObject();
                            Medicamento result = (Medicamento) service.searchMedicamentoCod(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICAMENTO_SEARCH_NOMBRE:
                        try {
                            Medicamento criterio = (Medicamento) is.readObject();
                            List<Medicamento> result = service.searchMedicamentoNombre(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;
                    case Protocol.MEDICAMENTO_FIND_BY_NOMBRE:
                        try {
                            String nombre = (String) is.readObject();
                            Medicamento result = service.findMedicamentoByNombre(nombre);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.MEDICAMENTO_FIND_BY_CODIGO:
                        try {
                            String codigo = (String) is.readObject();
                            Medicamento result = service.findMedicamentoByCodigo(codigo);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;


                    case Protocol.MEDICAMENTO_FINDALL:
                        try {
                            List<Medicamento> lm = service.findAllMedicamento();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(lm);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    // ================== PACIENTES ==================
                    case Protocol.PACIENTE_CREATE:
                        try {
                            service.createPaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.PACIENTE_READ:
                        try {
                            Paciente p = service.readPaciente((String) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(p);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.PACIENTE_UPDATE:
                        try {
                            service.updatePaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.PACIENTE_DELETE:
                        try {
                            service.deletePaciente((Paciente) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.PACIENTE_SEARCH_ID:
                        try {
                            Paciente criterio = (Paciente) is.readObject();
                            Paciente result = (Paciente) service.searchPacienteId(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.PACIENTE_SEARCH_NOMBRE:
                        try {
                            Paciente criterio = (Paciente) is.readObject();
                            List<Paciente> result = service.searchPacienteNombre(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;



                    case Protocol.PACIENTE_FINDALL:
                        try {
                            List<Paciente> lp = service.findAllPaciente();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(lp);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    // ================== RECETAS ==================
                    case Protocol.RECETA_CREATE_CON_PRESCRIPCIONES:
                        try {
                            service.crearRecetaConPrescripciones((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    case Protocol.RECETA_UPDATE:
                        try {
                            service.updateReceta((Receta) is.readObject());
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;
                    case Protocol.RECETA_SEARCH_IDPACIENTE:
                        try {
                            Receta criterio = (Receta) is.readObject();
                            List<Receta> result = service.searchRecetaPorIdPaciente(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_SEARCH_NOMBREPACIENTE:
                        try {
                            Receta criterio = (Receta) is.readObject();
                            List<Receta> result = service.searchRecetaPorNombrePaciente(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_FILTRAR_ESTADO:
                        try {
                            Receta criterio = (Receta) is.readObject();
                            List<Receta> result = service.filtrarRecetaPorEstado(criterio);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(result);
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;

                    case Protocol.RECETA_FINDALL_CON_PRESCRIPCIONES:
                        try {
                            List<Receta> lr = service.findAllRecetasConPrescripciones();
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(lr);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    // ================== LOGIN / SEGURIDAD ==================
                    case Protocol.LOGIN:
                        try {
                            String id = (String) is.readObject();
                            String clave = (String) is.readObject();
                            Usuario u = service.login(id, clave);

                            srv.sidToUser.put(sid, u.getId());
                            srv.byUser.put(u.getId(), this);

                            os.writeInt(Protocol.ERROR_NO_ERROR);
                            os.writeObject(u);
                            os.flush();

                            srv.broadcastUserJoined(u.getId());
                        } catch (Exception ex) {
                            os.writeInt(Protocol.ERROR_ERROR);
                        }
                        break;





                    case Protocol.CAMBIAR_CLAVE:
                        try {
                            String id = (String) is.readObject();
                            String claveActual = (String) is.readObject();
                            String claveNueva = (String) is.readObject();
                            service.cambiarClave(id, claveActual, claveNueva);
                            os.writeInt(Protocol.ERROR_NO_ERROR);
                        } catch (Exception ex) { os.writeInt(Protocol.ERROR_ERROR); }
                        break;

                    // ================== DESCONECTAR ==================
                    case Protocol.DISCONNECT:
                        stop();
                        srv.remove(this);
                        break;
                }

                os.flush();

            } catch (IOException e) {
                stop();
            }
        }
    }







    public synchronized void sendAsyncUserJoined(String userId){
        if (aos == null) return;
        try {
            aos.writeInt(Protocol.USER_JOINED);
            aos.writeObject(userId);
            aos.flush();
        } catch(Exception ignored){}
    }

    public synchronized void sendAsyncUserLeft(String userId){
        if (aos == null) return;
        try {
            aos.writeInt(Protocol.USER_LEFT);
            aos.writeObject(userId);
            aos.flush();
        } catch(Exception ignored){}
    }

    public synchronized void sendAsyncUserList(List<String> users){
        if (aos == null) return;
        try {
            aos.writeInt(Protocol.USER_LIST);
            aos.writeObject(users);
            aos.flush();
        } catch(Exception ignored){}
    }

    public synchronized void deliver_message(String fromId, String text){
        if (aos == null) return;
        try {
            aos.writeInt(Protocol.DELIVER_MESSAGE);
            aos.writeObject(fromId);
            aos.writeObject(text);
            aos.flush();
        } catch(Exception ignored){}
    }




//    public synchronized void deliver_message(String message) {
//        if (as != null) {
//            try {
//                aos.writeInt(Protocol.DELIVER_MESSAGE);
//                aos.writeObject(message);
//                aos.flush();
//            } catch (Exception e) {
//            }
//        }
//    }
}