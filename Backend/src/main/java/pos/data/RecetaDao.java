package pos.data;

import pos.logic.Medico;
import pos.logic.Paciente;
import pos.logic.Prescripcion;
import pos.logic.Receta;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecetaDao {
    Database db;
    public RecetaDao() {
        db = Database.instance();
    }
    public void create(Receta r) throws Exception {

        String sql = "INSERT INTO Receta (Paciente_id, Medico_id, fechaEmision, fechaRetiro, estado) VALUES (?,?,?,?,?)";

        PreparedStatement stm = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        stm.setString(1, r.getPaciente().getId());


        if (r.getMedico() != null) {
            stm.setString(2, r.getMedico().getId());
        } else {
            stm.setNull(2, Types.VARCHAR);
        }
        stm.setDate(3, Date.valueOf(r.getFechaEmision()));
        stm.setDate(4, r.getFechaRetiro() != null ? Date.valueOf(r.getFechaRetiro()) : null);
        stm.setString(5, r.getEstado());

        int count = stm.executeUpdate();

        ResultSet rs = stm.getGeneratedKeys();
        if (rs.next()) {
            r.setCodigo(rs.getInt(1));
        }

        rs.close();
        stm.close();

        if (count == 0) {
            throw new Exception("No se pudo crear la receta");
        }
    }

    public Receta read(String idPaciente, String idMedico, LocalDate fechaEmision) throws Exception {
        String sql = "select * from Receta r where r.idPaciente=? and r.idMedico=? and r.fechaEmision=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, idPaciente);
        stm.setString(2, idMedico);
        stm.setDate(3, Date.valueOf(fechaEmision));
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "r");
        } else {
            throw new Exception("Receta no existe");
        }
    }

    public void update(Receta r) throws Exception {
        String sql;
        PreparedStatement stm;

        if (r.getMedico() == null || r.getMedico().getId() == null) {
            sql = "UPDATE Receta SET fechaRetiro=?, estado=? WHERE Paciente_id=? AND Medico_id IS NULL AND fechaEmision=?";
            stm = db.prepareStatement(sql);
            stm.setDate(1, r.getFechaRetiro() != null ? Date.valueOf(r.getFechaRetiro()) : null);
            stm.setString(2, r.getEstado());
            stm.setString(3, r.getPaciente().getId());
            stm.setDate(4, Date.valueOf(r.getFechaEmision()));
        } else {
            sql = "UPDATE Receta SET fechaRetiro=?, estado=? WHERE Paciente_id=? AND Medico_id=? AND fechaEmision=?";
            stm = db.prepareStatement(sql);
            stm.setDate(1, r.getFechaRetiro() != null ? Date.valueOf(r.getFechaRetiro()) : null);
            stm.setString(2, r.getEstado());
            stm.setString(3, r.getPaciente().getId());
            stm.setString(4, r.getMedico().getId());
            stm.setDate(5, Date.valueOf(r.getFechaEmision()));
        }

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Receta no existe");
        }
    }

    public void delete(Receta r) throws Exception {
        String sql = "delete from Receta where idPaciente=? and idMedico=? and fechaEmision=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, r.getPaciente().getId());
        stm.setString(2, r.getMedico().getId());
        stm.setDate(3, Date.valueOf(r.getFechaEmision()));
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Receta no existe");
        }
    }

    public List<Receta> findByEstado(String estado) throws SQLException {
        List<Receta> recetas = new ArrayList<>();

        String sql;
        PreparedStatement stm;

        if (estado == null || estado.equalsIgnoreCase("Todos")) {
            sql = "SELECT r.codigo, r.fechaEmision, r.fechaRetiro, r.estado, " +
                    "p.id AS paciente_id, p.nombre AS paciente_nombre, " +
                    "m.id AS medico_id, m.nombre AS medico_nombre " +
                    "FROM Receta r " +
                    "LEFT JOIN Paciente p ON r.Paciente_id = p.id " +
                    "LEFT JOIN Medico m ON r.Medico_id = m.id";
            stm = db.prepareStatement(sql);
        } else {
            sql = "SELECT r.codigo, r.fechaEmision, r.fechaRetiro, r.estado, " +
                    "p.id AS paciente_id, p.nombre AS paciente_nombre, " +
                    "m.id AS medico_id, m.nombre AS medico_nombre " +
                    "FROM Receta r " +
                    "LEFT JOIN Paciente p ON r.Paciente_id = p.id " +
                    "LEFT JOIN Medico m ON r.Medico_id = m.id " +
                    "WHERE UPPER(r.estado) = UPPER(?)";
            stm = db.prepareStatement(sql);
            stm.setString(1, estado);
        }

        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            recetas.add(from(rs));
        }

        return recetas;
    }

    public List<Receta> findByNombrePaciente(String nombrePaciente, String estado) throws SQLException {
        List<Receta> recetas = new ArrayList<>();

        String sql = "SELECT r.codigo, r.fechaEmision, r.fechaRetiro, r.estado, " +
                "p.id AS paciente_id, p.nombre AS paciente_nombre, " +
                "m.id AS medico_id, m.nombre AS medico_nombre " +
                "FROM Receta r " +
                "LEFT JOIN Paciente p ON r.Paciente_id = p.id " +
                "LEFT JOIN Medico m ON r.Medico_id = m.id " +
                "WHERE p.nombre LIKE ?";

        if (estado != null && !estado.equalsIgnoreCase("Todos")) {
            sql += " AND r.estado = ?";
        }

        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + nombrePaciente + "%");

        if (estado != null && !estado.equalsIgnoreCase("Todos")) {
            stm.setString(2, estado);
        }

        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            recetas.add(from(rs));
        }

        return recetas;
    }



    private Receta from(ResultSet rs, String alias) {
        try {
            Receta r = new Receta();
            Paciente p = new Paciente();
            p.setId(rs.getString(alias + ".idPaciente"));
            Medico m = new Medico();
            m.setId(rs.getString(alias + ".idMedico"));
            r.setPaciente(p);
            r.setMedico(m);
            r.setFechaEmision(rs.getDate(alias + ".fechaEmision").toLocalDate());
            Date fr = rs.getDate(alias + ".fechaRetiro");
            if (fr != null) {
                r.setFechaRetiro(fr.toLocalDate());
            }
            r.setEstado(rs.getString(alias + ".estado"));
            r.setPrescripciones(new ArrayList<Prescripcion>());
            return r;
        } catch (SQLException ex) {
            return null;
        }
    }

    public List<Receta> findByPacienteId(String idPaciente) throws Exception {
        List<Receta> recetas = new ArrayList<>();
        String sql = "SELECT * FROM Receta r WHERE r.Paciente_id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, idPaciente);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Receta r = from(rs, "r");
            recetas.add(r);
        }
        return recetas;
    }

    private Receta from(ResultSet rs) throws SQLException {
        Receta r = new Receta();

        Paciente p = new Paciente();
        p.setId(rs.getString("paciente_id"));
        p.setNombre(rs.getString("paciente_nombre"));

        Medico m = new Medico();
        m.setId(rs.getString("medico_id"));
        m.setNombre(rs.getString("medico_nombre"));

        r.setPaciente(p);
        r.setMedico(m);

        r.setCodigo(rs.getInt("codigo"));
        r.setFechaEmision(rs.getDate("fechaEmision").toLocalDate());

        Date fr = rs.getDate("fechaRetiro");
        if (fr != null) {
            r.setFechaRetiro(fr.toLocalDate());
        }

        r.setEstado(rs.getString("estado"));
        r.setPrescripciones(new ArrayList<>());

        return r;
    }


    public List<Receta> findAllRecetas() throws SQLException {
        List<Receta> recetas = new ArrayList<>();
        String sql = "SELECT r.codigo, r.fechaEmision, r.fechaRetiro, r.estado, " +
                "p.id AS paciente_id, p.nombre AS paciente_nombre, " +
                "m.id AS medico_id, m.nombre AS medico_nombre " +
                "FROM Receta r " +
                "LEFT JOIN Paciente p ON r.Paciente_id = p.id " +
                "LEFT JOIN Medico m ON r.Medico_id = m.id";

        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            recetas.add(from(rs));
        }
        return recetas;
    }


}