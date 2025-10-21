package pos.data;

import pos.logic.Medico;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao {
    Database db;

    public MedicoDao() {
        db = Database.instance();
    }

    public void create(Medico m) throws Exception {
        String sql = "INSERT INTO Medico (id, nombre, clave, especialidad) VALUES (?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getId());
        stm.setString(2, m.getNombre());
        stm.setString(3, m.getClave());
        stm.setString(4, m.getEspecialidad());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Error al crear Médico");
        }
    }
    public Medico read(String id) throws Exception {
        String sql = "SELECT * FROM Medico WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, null); // ✅ sin alias
        } else {
            throw new Exception("Médico no existe");
        }
    }


    public void update(Medico m) throws Exception {
        Medico actual = read(m.getId());
        if (actual == null) {
            throw new Exception("Medico no existe");
        }

        if (m.getNombre() == null || m.getNombre().isEmpty()) {
            m.setNombre(actual.getNombre());
        }
        if (m.getClave() == null || m.getClave().isEmpty()) {
            m.setClave(actual.getClave());
        }
        if (m.getEspecialidad() == null || m.getEspecialidad().isEmpty()) {
            m.setEspecialidad(actual.getEspecialidad());
        }

        String sql = "UPDATE Medico SET nombre=?, clave=?, especialidad=? WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getNombre());
        stm.setString(2, m.getClave());
        stm.setString(3, m.getEspecialidad());
        stm.setString(4, m.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo actualizar el Medico");
        }
    }



    public void delete(Medico m) throws Exception {
        String sql = "delete from Medico where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Médico no existe");
        }
    }


    private Medico from(ResultSet rs, String alias) throws SQLException {
        Medico m = new Medico();
        if (alias != null && !alias.isEmpty()) {
            m.setId(rs.getString(alias + ".id"));
            m.setNombre(rs.getString(alias + ".nombre"));
            m.setClave(rs.getString(alias + ".clave"));
            m.setEspecialidad(rs.getString(alias + ".especialidad"));
        } else {
            m.setId(rs.getString("id"));
            m.setNombre(rs.getString("nombre"));
            m.setClave(rs.getString("clave"));
            m.setEspecialidad(rs.getString("especialidad"));
        }
        return m;
    }


    public List<Medico> findAll() throws Exception {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Medico ORDER BY id";
        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Medico f = new Medico();
            f.setId(rs.getString("id"));
            f.setNombre(rs.getString("nombre"));
            f.setClave(rs.getString("clave"));
            f.setEspecialidad(rs.getString("especialidad"));
            lista.add(f);
        }

        return lista;
    }


    public Medico loginMedico(String id, String clave) throws Exception {
        String sql = "SELECT id, nombre, clave, especialidad FROM Medico WHERE id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, id.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbClave = rs.getString("clave");
                    if (!dbClave.equals(clave)) {
                        throw new Exception("Credenciales inválidas");
                    }
                    return new Medico(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            dbClave,
                            rs.getString("especialidad")
                    );
                } else {
                    throw new Exception("Médico no encontrado");
                }
            }
        }
    }
    public void cambiarClaveMedico(String id, String claveNueva) throws Exception {
        String sql = "UPDATE Medico SET clave = ? WHERE id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, claveNueva);
            ps.setString(2, id);
            ps.executeUpdate();
        }
    }
    public Medico readPorId(String id) throws Exception {
        List<Medico> todos = findAll();
        for (Medico m : todos) {
            if (m.getId().equalsIgnoreCase(id)) {
                return m;

            }
        }
        throw new Exception("Médico no existe");
    }



}
