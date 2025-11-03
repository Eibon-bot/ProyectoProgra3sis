package pos.data;

import pos.logic.Administrador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdministradorDao {
    Database db;

    public AdministradorDao() {
        db = Database.instance();
    }
    public Administrador readPorId(String id) throws Exception {
        String sql = "SELECT id, nombre, clave FROM Administrador WHERE id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, id.trim());
            try (ResultSet rs = db.executeQuery(ps)) {
                if (rs.next()) {
                    return new Administrador(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            rs.getString("clave")
                    );
                } else {
                    throw new Exception("Administrador no encontrado");
                }
            }
        }
    }


    public Administrador loginAdministrador(String id, String clave) throws Exception {
        Administrador a = readPorId(id);
        if (!a.getClave().equals(clave)) throw new Exception("Credenciales inválidas");
        return a;
    }


    public void cambiarClaveAdministrador(String id, String claveNueva) throws Exception {
        String sql = "UPDATE Administrador SET clave = ? WHERE id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, claveNueva);
            ps.setString(2, id.trim());
            int count = db.executeUpdate(ps);
            if (count == 0) throw new Exception("No se pudo cambiar la clave del Administrador");
        }
    }
}
