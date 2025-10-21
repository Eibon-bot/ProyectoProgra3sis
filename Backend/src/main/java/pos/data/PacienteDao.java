package pos.data;

import pos.logic.Paciente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {
    Database db;

    public PacienteDao() {
        db = Database.instance();
    }

    public void create(Paciente p) throws Exception {
        String sql = "INSERT INTO Paciente (id, nombre, fechaNacimiento, telefono) VALUES (?, ?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getId());
        stm.setString(2, p.getNombre());

        stm.setDate(3, java.sql.Date.valueOf(p.getFechaNacimiento()));

        stm.setString(4, p.getTelefono());
        stm.executeUpdate();
        stm.close();
    }


    public void update(Paciente p) throws Exception {
        String sql = "update Paciente set nombre=?, fechaNacimiento=?, telefono=? where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getNombre());
        stm.setDate(2, java.sql.Date.valueOf(p.getFechaNacimiento()));
        stm.setString(3, p.getTelefono());
        stm.setString(4, p.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Paciente no existe");
        }
    }

    public void delete(Paciente p) throws Exception {
        String id = p.getId().trim();

        String sqlCheck = "SELECT id FROM Paciente WHERE id = ?";
        PreparedStatement stmCheck = db.prepareStatement(sqlCheck);
        stmCheck.setString(1, id);
        ResultSet rsCheck = db.executeQuery(stmCheck);

        String sqlDelete = "DELETE FROM Paciente WHERE id = ?";
        PreparedStatement stmDelete = db.prepareStatement(sqlDelete);
        stmDelete.setString(1, id);
        int filas = db.executeUpdate(stmDelete);

        if (filas == 0) {
            throw new Exception("No se pudo borrar el Paciente");
        }
    }




    public Paciente read(String id) throws Exception {
        String sql = "SELECT * FROM Paciente WHERE id=?";
        try (PreparedStatement stm = db.getConnection().prepareStatement(sql)) {
            stm.setString(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return from(rs);
                } else {
                    throw new Exception("Paciente no existe");
                }
            }
        }
    }

    public List<Paciente> findByNombre(String nombre) throws Exception {
        List<Paciente> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Paciente WHERE nombre LIKE ? ORDER BY nombre";

        try (PreparedStatement stm = db.getConnection().prepareStatement(sql)) {
            stm.setString(1, "%" + nombre + "%");
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    resultado.add(from(rs));
                }
            }
        }
        return resultado;
    }

    private Paciente from(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getString("id"));
        p.setNombre(rs.getString("nombre"));
        java.sql.Date fecha = rs.getDate("fechaNacimiento");
        if (fecha != null) {
            p.setFechaNacimiento(fecha.toLocalDate());
        }
        p.setTelefono(rs.getString("telefono"));
        return p;
    }




    public List<Paciente> findAll() throws Exception {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paciente ORDER BY id";
        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Paciente f = new Paciente();
            f.setId(rs.getString("id"));
            f.setNombre(rs.getString("nombre"));
            java.sql.Date fecha = rs.getDate("fechaNacimiento");
            if (fecha != null) {
                f.setFechaNacimiento(fecha.toLocalDate());
            }
            f.setTelefono(rs.getString("telefono"));
            lista.add(f);
        }

        return lista;
    }
}
