package pos.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Service {
    private static Service theInstance;
    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private Socket s;
    private ObjectOutputStream os;
    private ObjectInputStream is;




    private Socket as;
    private ObjectOutputStream aos;
    private ObjectInputStream ais;


    public interface ServiceListener{
        void onUserList(List<Usuario> users);
        void onUserJoined(Usuario user);
        void onUserLeft(Usuario user);
        void onMessage(Usuario from, String text);
    }





    private final List<ServiceListener> listeners = new CopyOnWriteArrayList<>();
    public void addListener(ServiceListener l){ listeners.add(l); }
    public void removeListener(ServiceListener l){ listeners.remove(l); }

    String sid;

    private Service() {
        try {
            s = new Socket(Protocol.SERVER, Protocol.PORT);
            os = new ObjectOutputStream(s.getOutputStream());
            is = new ObjectInputStream(s.getInputStream());

            os.writeInt(Protocol.SYNC);
            os.flush();
            sid=(String)is.readObject();


        } catch (Exception e) {
            System.err.println("No se pudo conectar al servidor");
            System.exit(-1);
        }
    }

    private void openAsyncChannel() throws Exception {
        as = new Socket(Protocol.SERVER, Protocol.PORT);
        aos = new ObjectOutputStream(as.getOutputStream());
        ais = new ObjectInputStream(as.getInputStream());
        aos.writeInt(Protocol.ASYNC);
        aos.writeObject(sid);
        aos.flush();

        Thread t = new Thread(this::asyncLoop, "async-listener");
        t.setDaemon(true);
        t.start();
    }

    private void asyncLoop(){
        try {
            while (true){
                int ev = ais.readInt();
                switch (ev){
                    case Protocol.USER_LIST -> {
                        @SuppressWarnings("unchecked")
                        List<Usuario> users = (List<Usuario>) ais.readObject();
                        for (var l: listeners) l.onUserList(users);
                    }
                    case Protocol.USER_JOINED -> {
                        Usuario u = (Usuario) ais.readObject();
                        for (var l: listeners) l.onUserJoined(u);
                    }
                    case Protocol.USER_LEFT -> {
                        Object payload = ais.readObject();

                        Usuario u = (payload instanceof Usuario)
                                ? (Usuario) payload
                                : fallbackUsuario((String) payload);
                        for (var l: listeners) l.onUserLeft(u);
                    }
                    case Protocol.DELIVER_MESSAGE -> {
                        Object fromObj = ais.readObject();
                        String text = (String) ais.readObject();

                        Usuario from = (fromObj instanceof Usuario)
                                ? (Usuario) fromObj
                                : fallbackUsuario((String) fromObj);
                        for (var l: listeners) l.onMessage(from, text);
                    }
                }
            }
        } catch (Exception ignored) {}
    }


    private static Usuario fallbackUsuario(String id) {
        return new Usuario(id, null, null,null) {};
    }



    public void sendMessage(Usuario to, String text) throws Exception {
        os.writeInt(Protocol.SEND_MESSAGE);
        os.writeObject(to);
        os.writeObject(text);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("No se pudo enviar el mensaje");
    }




    public String getSid() {
        return sid;
    }

    public String generarNuevoIdMedico() throws Exception {
        os.writeInt(Protocol.MEDICO_GENERAR_NUEVO_ID);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (String) is.readObject();
        else throw new Exception("Error al generar nuevo ID de médico");
    }

    public String generarNuevoIdFarma() throws Exception {
        os.writeInt(Protocol.FARMA_GENERAR_NUEVO_ID);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (String) is.readObject();
        else throw new Exception("Error al generar nuevo ID de farmacéutico");
    }

    public String generarNuevoCodMedicamento(String prefijo) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_GENERAR_NUEVO_ID);
        os.writeObject(prefijo);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (String) is.readObject();
        else throw new Exception("Error al generar nuevo código de medicamento");
    }

    public String generarNuevoIdPaciente() throws Exception {
        os.writeInt(Protocol.PACIENTE_GENERAR_NUEVO_ID);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (String) is.readObject();
        else throw new Exception("Error al generar nuevo ID de paciente");
    }

    // ================== MÉDICOS ==================
    public void createMedico(Medico m) throws Exception {
        os.writeInt(Protocol.MEDICO_CREATE);
        os.writeObject(m);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al crear médico");
    }

    public Medico readMedico(String id) throws Exception {
        os.writeInt(Protocol.MEDICO_READ);
        os.writeObject(id);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (Medico) is.readObject();
        else throw new Exception("Médico no existe");
    }

    public void updateMedico(Medico m) throws Exception {
        os.writeInt(Protocol.MEDICO_UPDATE);
        os.writeObject(m);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al actualizar médico");
    }

    public void deleteMedico(Medico m) throws Exception {
        os.writeInt(Protocol.MEDICO_DELETE);
        os.writeObject(m);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al eliminar médico");
    }

    public List<Medico> findAllMedico() throws Exception {
        os.writeInt(Protocol.MEDICO_FINDALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Medico>) is.readObject();
        else throw new Exception("Error al listar médicos");
    }

    public List<Medico> searchMedicoId(Medico filtro) throws Exception {
        os.writeInt(Protocol.MEDICO_SEARCH_ID);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Medico>) is.readObject();
        else throw new Exception("Error al buscar médicos por ID");
    }

    public List<Medico> searchMedicoNombre(Medico filtro) throws Exception {
        os.writeInt(Protocol.MEDICO_SEARCH_NOMBRE);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Medico>) is.readObject();
        else throw new Exception("Error al buscar médicos por nombre");
    }

    // ================== FARMACÉUTICOS ==================
    public void createFarmaceutico(Farmaceutico f) throws Exception {
        os.writeInt(Protocol.FARMA_CREATE);
        os.writeObject(f);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al crear farmacéutico");
    }

    public Farmaceutico readFarmaceutico(String id) throws Exception {
        os.writeInt(Protocol.FARMA_READ);
        os.writeObject(id);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (Farmaceutico) is.readObject();
        else throw new Exception("Farmacéutico no existe");
    }

    public void updateFarmaceutico(Farmaceutico f) throws Exception {
        os.writeInt(Protocol.FARMA_UPDATE);
        os.writeObject(f);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al actualizar farmacéutico");
    }

    public void deleteFarmaceutico(Farmaceutico f) throws Exception {
        os.writeInt(Protocol.FARMA_DELETE);
        os.writeObject(f);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al eliminar farmacéutico");
    }

    public List<Farmaceutico> findAllFarmaceutico() throws Exception {
        os.writeInt(Protocol.FARMA_FINDALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Farmaceutico>) is.readObject();
        else throw new Exception("Error al listar farmacéuticos");
    }

    public List<Farmaceutico> searchFarmaceuticoId(Farmaceutico filtro) throws Exception {
        os.writeInt(Protocol.FARMA_SEARCH_ID);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Farmaceutico>) is.readObject();
        else throw new Exception("Error al buscar farmacéuticos por ID");
    }

    public List<Farmaceutico> searchFarmaceuticoNombre(Farmaceutico filtro) throws Exception {
        os.writeInt(Protocol.FARMA_SEARCH_NOMBRE);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Farmaceutico>) is.readObject();
        else throw new Exception("Error al buscar farmacéuticos por nombre");
    }

    // ================== MEDICAMENTOS ==================
    public void createMedicamento(Medicamento m) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_CREATE);
        os.writeObject(m);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al crear medicamento");
    }

    public Medicamento readMedicamento(String nombre) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_READ);
        os.writeObject(nombre);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (Medicamento) is.readObject();
        else throw new Exception("Medicamento no existe");
    }

    public void updateMedicamento(Medicamento m) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_UPDATE);
        os.writeObject(m);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al actualizar medicamento");
    }

    public void deleteMedicamento(Medicamento m) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_DELETE);
        os.writeObject(m);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al eliminar medicamento");
    }

    public List<Medicamento> findAllMedicamento() throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_FINDALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Medicamento>) is.readObject();
        else throw new Exception("Error al listar medicamentos");
    }

    public List<Medicamento> searchMedicamentoCod(Medicamento filtro) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_SEARCH_COD);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Medicamento>) is.readObject();
        else throw new Exception("Error al buscar medicamentos por código");
    }

    public List<Medicamento> searchMedicamentoNombre(Medicamento filtro) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_SEARCH_NOMBRE);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Medicamento>) is.readObject();
        else throw new Exception("Error al buscar medicamentos por nombre");
    }

    public Medicamento findMedicamentoByNombre(String nombre) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_FIND_BY_NOMBRE);
        os.writeObject(nombre);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (Medicamento) is.readObject();
        else throw new Exception("Medicamento no encontrado por nombre");
    }

    public Medicamento findMedicamentoByCodigo(String codigo) throws Exception {
        os.writeInt(Protocol.MEDICAMENTO_FIND_BY_CODIGO);
        os.writeObject(codigo);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (Medicamento) is.readObject();
        else throw new Exception("Medicamento no encontrado por código");
    }

    // ================== PACIENTES ==================
    public void createPaciente(Paciente p) throws Exception {
        os.writeInt(Protocol.PACIENTE_CREATE);
        os.writeObject(p);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al crear paciente");
    }

    // ================== PACIENTES ==================
    public Paciente readPaciente(String id) throws Exception {
        os.writeInt(Protocol.PACIENTE_READ);
        os.writeObject(id);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (Paciente) is.readObject();
        else throw new Exception("Paciente no existe");
    }

    public void updatePaciente(Paciente p) throws Exception {
        os.writeInt(Protocol.PACIENTE_UPDATE);
        os.writeObject(p);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al actualizar paciente");
    }

    public void deletePaciente(Paciente p) throws Exception {
        os.writeInt(Protocol.PACIENTE_DELETE);
        os.writeObject(p);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al eliminar paciente");
    }

    public List<Paciente> findAllPaciente() throws Exception {
        os.writeInt(Protocol.PACIENTE_FINDALL);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Paciente>) is.readObject();
        else throw new Exception("Error al listar pacientes");
    }

    public List<Paciente> searchPacienteNombre(Paciente filtro) throws Exception {
        os.writeInt(Protocol.PACIENTE_SEARCH_NOMBRE);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Paciente>) is.readObject();
        else throw new Exception("Error al buscar pacientes por nombre");
    }

    public List<Paciente> searchPacienteId(Paciente filtro) throws Exception {
        os.writeInt(Protocol.PACIENTE_SEARCH_ID);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Paciente>) is.readObject();
        else throw new Exception("Error al buscar pacientes por ID");
    }

    // ================== RECETAS ==================
    public void updateReceta(Receta r) throws Exception {
        os.writeInt(Protocol.RECETA_UPDATE);
        os.writeObject(r);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al actualizar receta");
    }

    public void crearRecetaConPrescripciones(Receta r) throws Exception {
        os.writeInt(Protocol.RECETA_CREATE_CON_PRESCRIPCIONES);
        os.writeObject(r);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al crear receta con prescripciones");
    }

    public List<Receta> searchRecetaPorIdPaciente(Receta filtro) throws Exception {
        os.writeInt(Protocol.RECETA_SEARCH_IDPACIENTE);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Receta>) is.readObject();
        else throw new Exception("Error al buscar recetas por ID de paciente");
    }

    public List<Receta> searchRecetaPorNombrePaciente(Receta filtro) throws Exception {
        os.writeInt(Protocol.RECETA_SEARCH_NOMBREPACIENTE);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Receta>) is.readObject();
        else throw new Exception("Error al buscar recetas por nombre de paciente");
    }

    public List<Receta> filtrarRecetaPorEstado(Receta filtro) throws Exception {
        os.writeInt(Protocol.RECETA_FILTRAR_ESTADO);
        os.writeObject(filtro);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Receta>) is.readObject();
        else throw new Exception("Error al filtrar recetas por estado");
    }

    public List<Receta> findAllRecetasConPrescripciones() throws Exception {
        os.writeInt(Protocol.RECETA_FINDALL_CON_PRESCRIPCIONES);
        os.flush();
        if (is.readInt() == Protocol.ERROR_NO_ERROR)
            return (List<Receta>) is.readObject();
        else throw new Exception("Error al listar recetas con prescripciones");
    }

    // ================== LOGIN / SEGURIDAD ==================
    // Service.java
    public Usuario login(String id, String clave) throws Exception {
        os.writeInt(Protocol.LOGIN);
        os.writeObject(id);
        os.writeObject(clave);
        os.flush();

        int code = is.readInt();
        if (code == Protocol.ERROR_NO_ERROR) {
            Usuario u = (Usuario) is.readObject();

//

            return u;
        } else {
            throw new Exception("Credenciales inválidas");
        }
    }



    public void cambiarClave(String id, String actual, String nueva) throws Exception {
        os.writeInt(Protocol.CAMBIAR_CLAVE);
        os.writeObject(id);
        os.writeObject(actual);
        os.writeObject(nueva);
        os.flush();
        if (is.readInt() != Protocol.ERROR_NO_ERROR)
            throw new Exception("Error al cambiar clave");
    }

    // ================== Desconexión ==================
    private void disconnect() throws Exception {
        os.writeInt(Protocol.DISCONNECT);
        os.flush();
        s.shutdownOutput();
        s.close();
    }

    public void stop() {
        try {
            disconnect();
        } catch (Exception e) {
            System.exit(-1);
        }
    }
}