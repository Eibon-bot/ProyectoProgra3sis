package pos.logic;

import pos.data.*;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Service {
    private static Service theInstance;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private FarmaceuticoDao farmadao;
    private MedicamentoDao medicadao;
    private MedicoDao medicodao;
    private PacienteDao pacientedao;
    private PrescripcionDao prescripdao;
    private RecetaDao recetadao;
    private AdministradorDao admindao;

    Service() {
        try {
            farmadao = new FarmaceuticoDao();
            medicadao = new MedicamentoDao();
            medicodao = new MedicoDao();
            pacientedao = new PacienteDao();
            prescripdao = new PrescripcionDao();
            recetadao = new RecetaDao();
            admindao = new AdministradorDao();
            forzarEnlacePrescripciones();
            forzarReferencias();
            reconstruirReferencias();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stop(){
        try {
            Database.instance().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reconstruirReferencias() throws Exception {
        Map<String, Medicamento> mapa = new HashMap<>();
        for (Medicamento m : medicadao.findAll()) {
            mapa.put(m.getCodigo(), m);
        }

        for (Receta r : recetadao.findAllRecetas()) {
            for (Prescripcion p : r.getPrescripciones()) {
                if (p.getMedicamento() != null && p.getMedicamento().getCodigo() != null) {
                    String cod = p.getMedicamento().getCodigo();
                    Medicamento real = mapa.get(cod);
                    p.setMedicamento(real);
                }
            }
        }
    }


    public void createMedico(Medico e) throws Exception {
        String nuevoId = generarNuevoIdMedico();
        e.setId(nuevoId);
        e.setClave(nuevoId);
        try {
            medicodao.read(e.getId());
            throw new Exception("Médico ya existe");
        } catch (Exception ex) {
            if (ex.getMessage().equals("Médico no existe")) {
                medicodao.create(e);
            } else {
                throw ex;
            }
        }
    }

    public void deleteMedico(Medico e) throws Exception {
        medicodao.delete(e);
    }

    public void updateMedico(Medico m) throws Exception {
        medicodao.update(m);

    }

    public Medico readMedico(String nombre) throws Exception {
        return medicodao.read(nombre);
    }

    public List<Medico> findAllMedico() {
        try{
            return medicodao.findAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String generarNuevoIdMedico() {
        List<Medico> lista = findAllMedico();
        int max = 0;
        for (Medico m : lista) {
            String id = m.getId();
            if (id != null && id.startsWith("MED-")) {
                try {
                    int num = Integer.parseInt(id.substring(6));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("MED-%04d", max + 1);
    }

    public void createFarmaceutico(Farmaceutico e) throws Exception {
        String nuevoId = generarNuevoIdFarma();
        e.setId(nuevoId);
        e.setClave(nuevoId);
        try {
            farmadao.read(e.getId());
            throw new Exception("Farmacéutico ya existe");
        } catch (Exception ex) {
            if (ex.getMessage().equals("Farmacéutico no existe")) {
                farmadao.create(e);
            } else {
                throw ex;
            }
        }
    }


    public void deleteFarmaceutico(Farmaceutico e) throws Exception {
        farmadao.delete(e.getId());

    }

    public void updateFarmaceutico(Farmaceutico p) throws Exception {
        farmadao.update(p);

    }

    public Farmaceutico readFarmaceutico(String nombre) throws Exception {
        return farmadao.read(nombre);
    }

    public List<Farmaceutico> findAllFarmaceutico() {
        try{
            return farmadao.findAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String generarNuevoIdFarma() {
        List<Farmaceutico> lista = findAllFarmaceutico();
        int max = 0;
        for (Farmaceutico f : lista) {
            String id = f.getId();
            if (id != null && id.startsWith("FARMA-")) {
                try {
                    int num = Integer.parseInt(id.substring(6));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("FARMA-%04d", max + 1);
    }

    public void createMedicamento(Medicamento e) throws Exception {
        String cod = generarNuevoCodMedicamento(e.getNombre());
        e.setCodigo(cod);
        try {
            medicadao.create(e);
        } catch (Exception ex) {
            if (ex.getMessage().contains("duplicate") || ex.getMessage().contains("UNIQUE")) {
                throw new Exception("Medicamento ya existe: " + ex.getMessage());
            }
            throw ex;
        }
    }

    public void deleteMedicamento(Medicamento e) throws Exception {
        try {
            medicadao.delete(e);
        } catch (Exception ex) {
            if (ex.getMessage().contains("foreign key") || ex.getMessage().contains("FK")) {
                throw new Exception("El medicamento no puede ser eliminado porque está en uso.");
            }
            throw ex;
        }

    }

    public void updateMedicamento(Medicamento p) throws Exception {
        medicadao.update(p);
    }

    public Medicamento readMedicamento(String nombre) throws Exception {
        return medicadao.read(nombre);
    }

    public String generarNuevoCodMedicamento(String nombre) {
        String prefijo = nombre.trim().toUpperCase();
        prefijo = prefijo.length() >= 3 ? prefijo.substring(0, 3) : prefijo;
        int max = 0;
        for (Medicamento f : findAllMedicamento()) {
            String cod = f.getCodigo();
            if (cod != null && cod.startsWith(prefijo + "-")) {
                try {
                    int num = Integer.parseInt(cod.substring(prefijo.length() + 1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("%s-%03d", prefijo, max + 1);
    }

    public List<Medicamento> findAllMedicamento() {
        try{
            return medicadao.findAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void createPaciente(Paciente e) throws Exception {
        String nuevoId = generarNuevoIdPaciente();
        e.setId(nuevoId);
        try {
            pacientedao.read(e.getId());
            throw new Exception("Paciente ya existe");
        } catch (Exception ex) {
            if (ex.getMessage().equals("Paciente no existe")) {
                pacientedao.create(e);
            } else {
                throw ex;
            }
        }
    }


    public void deletePaciente(Paciente e) throws Exception {
        pacientedao.delete(e);

    }

    public void updatePaciente(Paciente p) throws Exception {
        pacientedao.update(p);

    }

    public Paciente readPaciente(String nombre) throws Exception {
        return pacientedao.read(nombre);
    }

    public String generarNuevoIdPaciente() {
        int max = 0;
        for (Paciente p : findAllPaciente()) {
            String id = p.getId();
            if (id != null && id.startsWith("PAC-")) {
                try {
                    int num = Integer.parseInt(id.substring(6));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("PAC-%04d", max + 1);
    }

    public List<Medico> findAllMedicos() throws Exception {
        return medicodao.findAll();
    }

    public List<Farmaceutico> findAllFarmaceuticos() throws Exception {
        return farmadao.findAll();
    }

    public List<Medicamento> findAllMedicamentos() throws Exception {

        return medicadao.findAll();
    }

    public List<Paciente> findAllPaciente() {
        try{
            return pacientedao.findAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Receta> findAllRecetas() {
        try{
            return recetadao.findAllRecetas();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public List<Paciente> searchPacienteNombre(Paciente m) {
        try {
            List<Paciente> lista = pacientedao.findAll();
            if (lista == null || lista.isEmpty()) {
                return new ArrayList<>();
            }

            return lista.stream()
                    .filter(i -> i.getNombre() != null && i.getNombre().toLowerCase().contains(m.getNombre().toLowerCase()))
                    .sorted(Comparator.comparing(Paciente::getNombre))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();

        }
    }

    public List<Paciente> searchPacienteId(Paciente p) {
        try {
            List<Paciente> lista = pacientedao.findAll();
            if (lista == null || lista.isEmpty()) {
                return new ArrayList<>();
            }

            return lista.stream()
                    .filter(i -> i.getId() != null && i.getId().toLowerCase().contains(p.getId().toLowerCase()))
                    .sorted(Comparator.comparing(Paciente::getId))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }
    public List<Medico> searchMedicoId(Medico m) {
        try {
            List<Medico> lista = medicodao.findAll();
            if (lista == null || lista.isEmpty()) {
                return new ArrayList<>();
            }

            return lista.stream()
                    .filter(i -> i.getId() != null && i.getId().toLowerCase().contains(m.getId().toLowerCase()))
                    .sorted(Comparator.comparing(Medico::getId))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();

        }
    }
    public List<Farmaceutico> searchFarmaceuticoId(Farmaceutico e) {
        try {
            List<Farmaceutico> lista = farmadao.findAll();
            if (lista == null || lista.isEmpty()) {
                return new ArrayList<>();
            }

            return lista.stream()
                    .filter(i -> i.getId() != null && i.getId().toLowerCase().contains(e.getId().toLowerCase()))
                    .sorted(Comparator.comparing(Farmaceutico::getId))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    public List<Farmaceutico> searchFarmaceuticoNombre(Farmaceutico e) {
        try {
            List<Farmaceutico> lista = farmadao.findAll();
            if (lista == null || lista.isEmpty()) {
                return new ArrayList<>();
            }

            return lista.stream()
                    .filter(i -> i.getNombre() != null && i.getNombre().toLowerCase().contains(e.getNombre().toLowerCase()))
                    .sorted(Comparator.comparing(Farmaceutico::getNombre))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }


    public List<Medico> searchMedicoNombre(Medico m) {
        try {
            List<Medico> lista = medicodao.findAll();
            if (lista == null || lista.isEmpty()) {
                return new ArrayList<>();
            }

            return lista.stream()
                    .filter(i -> i.getNombre() != null && i.getNombre().toLowerCase().contains(m.getNombre().toLowerCase()))
                    .sorted(Comparator.comparing(Medico::getNombre))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    public List<Medicamento> searchMedicamentoCod(Medicamento e) {
        try {
            List<Medicamento> lista = medicadao.findAll();
            if (lista == null || lista.isEmpty()) {
                return new ArrayList<>();
            }

            return lista.stream()
                    .filter(i -> i.getCodigo() != null && i.getCodigo().toLowerCase().contains(e.getCodigo().toLowerCase()))
                    .sorted(Comparator.comparing(Medicamento::getCodigo))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    public List<Medicamento> searchMedicamentoNombre(Medicamento m) {
        try {
            List<Medicamento> lista = medicadao.findAll();
            if (lista == null || lista.isEmpty()) {
                return new ArrayList<>();
            }

            return lista.stream()
                    .filter(i -> i.getNombre() != null && i.getNombre().toLowerCase().contains(m.getNombre().toLowerCase()))
                    .sorted(Comparator.comparing(Medicamento::getNombre))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }


    public void updateReceta(Receta r) throws Exception {
        recetadao.update(r);
    }


    public List<Receta> searchRecetaPorIdPaciente(Receta rfiltro) {
        try {
            List<Receta> todas = recetadao.findAllRecetas();
            return todas.stream()
                    .filter(r -> r.getPaciente() != null
                            && r.getPaciente().getId().equalsIgnoreCase(rfiltro.getPaciente().getId()))
                    .collect(Collectors.toList());
        } catch (SQLException ex) {
            return new ArrayList<>();
        }
    }


    public List<Receta> searchRecetaPorNombrePaciente(Receta filtro) throws Exception {
        String nombre = filtro.getPaciente().getNombre();
        String estado = filtro.getEstado();
        return recetadao.findByNombrePaciente(nombre, estado);
    }


    public List<Receta> filtrarRecetaPorEstado(Receta filtro) throws Exception {
        return recetadao.findByEstado(filtro.getEstado());
    }



    public Medicamento findMedicamentoByNombre(String nombre) throws Exception {
        return findAllMedicamentos().stream()
                .filter(m -> m.getNombre() != null && m.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public Medicamento buscarMedicamentoPorCodigo(String codigo) throws Exception {
        return findAllMedicamentos().stream()
                .filter(m -> m.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }


    public List<Receta> findAllRecetasConPrescripciones() {
        List<Receta> recetas = new ArrayList<>();
        try {
            recetas = recetadao.findAllRecetas();

            Map<Integer, List<Prescripcion>> prescripcionesPorReceta = new HashMap<>();
            for (Prescripcion p : prescripdao.findAll()) {
                prescripcionesPorReceta
                        .computeIfAbsent(p.getRecetaCodigo(), k -> new ArrayList<>())
                        .add(p);
            }

            for (Receta r : recetas) {
                List<Prescripcion> pres = prescripcionesPorReceta.getOrDefault(r.getCodigo(), new ArrayList<>());
                r.setPrescripciones(pres);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return recetas;
    }



    public void forzarReferencias() throws Exception {
        for (Receta receta : findAllRecetas()) {
            for (Prescripcion prescripcion : receta.getPrescripciones()) {
                Medicamento med = buscarMedicamentoPorCodigo(prescripcion.getCodigoMedicamento());
                if (med != null) {
                    prescripcion.setMedicamento(med);
                }
            }
        }
    }

    public Medicamento findMedicamentoByCodigo(String codigo) throws Exception {
        return findAllMedicamentos().stream()
                .filter(m -> m.getCodigo() != null && m.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    private void forzarEnlacePrescripciones() throws Exception {
        for (Receta receta : findAllRecetas()) {
            for (Prescripcion pres : receta.getPrescripciones()) {
                if (pres.getMedicamento() == null) {
                    String cod = pres.getCodigoMedicamento();
                    if (cod == null || cod.isEmpty()) {
                        cod = pres.getCodigoMedicamento();
                    }
                    if (cod != null && !cod.isEmpty()) {
                        Medicamento m = findMedicamentoByCodigo(cod.trim());
                        if (m != null) pres.setMedicamento(m);
                    }
                }
            }
        }
    }


    public void crearRecetaConPrescripciones(Receta r) throws Exception {
        recetadao.create(r);

        for (Prescripcion p : r.getPrescripciones()) {
            p.setRecetaCodigo(r.getCodigo());
            prescripdao.create(p);
        }
    }

    //SERVICE LOGIN Y CAMBIO DE CLAVE

    public Usuario login(String id, String clave) throws Exception {
        try {
            return medicodao.loginMedico(id, clave);
        } catch (Exception e) {
            if (!e.getMessage().contains("no encontrado") && !e.getMessage().contains("Credenciales inválidas")) throw e;
        }


        try {
            return farmadao.loginFarmaceutico(id, clave);
        } catch (Exception e) {
            if (!e.getMessage().contains("no encontrado") && !e.getMessage().contains("Credenciales inválidas")) throw e;
        }


        try {
            return admindao.loginAdministrador(id, clave);
        } catch (Exception e) {
            if (!e.getMessage().contains("no encontrado") && !e.getMessage().contains("Credenciales inválidas")) throw e;
        }

        throw new Exception("Usuario no encontrado");
    }

    public void cambiarClave(String id, String claveActual, String claveNueva) throws Exception {
        if (id == null || claveActual == null || claveNueva == null || claveNueva.isEmpty()) {
            throw new Exception("Datos incompletos para cambiar la clave");
        }


        try {
            Medico m = medicodao.readPorId(id);
            if (!m.getClave().equals(claveActual)) throw new Exception("Clave actual incorrecta");
            medicodao.cambiarClaveMedico(id, claveNueva);
            return;
        } catch (Exception ex) {
            if (!ex.getMessage().contains("no existe")) throw ex;
        }


        try {
            Farmaceutico f = farmadao.readPorId(id);
            if (!f.getClave().equals(claveActual)) throw new Exception("Clave actual incorrecta");
            farmadao.cambiarClaveFarmaceutico(id, claveNueva);
            return;
        } catch (Exception ex) {
            if (!ex.getMessage().contains("no existe")) throw ex;
        }

        try {
            Administrador a = admindao.readPorId(id);
            if (!a.getClave().equals(claveActual)) throw new Exception("Clave actual incorrecta");
            admindao.cambiarClaveAdministrador(id, claveNueva);
            return;
        } catch (Exception ex) {
            if (!ex.getMessage().contains("no existe")) throw ex;
        }

        throw new Exception("Usuario no encontrado");
    }


}

