package pos.data;


import pos.logic.Farmaceutico;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FarmaceuticoDao {
    Database db;

    public FarmaceuticoDao() {
        db = Database.instance();
    }


    public void create(Farmaceutico f) throws Exception {
        String sql = "INSERT INTO Farmaceutico (id, nombre, clave) VALUES (?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, f.getId());
        stm.setString(2, f.getNombre());
        stm.setString(3, f.getClave());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Error al crear farmacéutico");
        }
    }


    public List<Farmaceutico> findAll() throws Exception {
        List<Farmaceutico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Farmaceutico ORDER BY id";
        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Farmaceutico f = new Farmaceutico();
            f.setId(rs.getString("id"));
            f.setNombre(rs.getString("nombre"));
            f.setClave(rs.getString("clave"));
            lista.add(f);
        }

        return lista;
    }


    public Farmaceutico read(String id) throws Exception {
        String sql = "SELECT * FROM Farmaceutico WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            Farmaceutico f = new Farmaceutico();
            f.setId(rs.getString("id"));
            f.setNombre(rs.getString("nombre"));
            f.setClave(rs.getString("clave"));
            return f;
        } else {
            throw new Exception("Farmacéutico no existe");
        }
    }

    public void update(Farmaceutico f) throws Exception {
        Farmaceutico actual = read(f.getId());
        if (actual == null) {
            throw new Exception("Farmacéutico no existe");
        }
        if (f.getNombre() == null || f.getNombre().isEmpty()) {
            f.setNombre(actual.getNombre());
        }
        if (f.getClave() == null || f.getClave().isEmpty()) {
            f.setClave(actual.getClave());
        }
        String sql = "UPDATE Farmaceutico SET nombre=?, clave=? WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, f.getNombre());
        stm.setString(2, f.getClave());
        stm.setString(3, f.getId());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo actualizar el farmacéutico");
        }
    }



    public void delete(String id) throws Exception {
        String sql = "DELETE FROM Farmaceutico WHERE id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Farmaceutico no existe");
        }
    }


    public Farmaceutico loginFarmaceutico(String id, String clave) throws Exception {
        String sql = "SELECT id, nombre, clave FROM Farmaceutico WHERE id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, id.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbClave = rs.getString("clave");
                    if (!dbClave.equals(clave)) {
                        throw new Exception("Credenciales inválidas");
                    }
                    return new Farmaceutico(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            dbClave
                    );
                } else {
                    throw new Exception("Farmacéutico no encontrado");
                }
            }
        }
    }
    public void cambiarClaveFarmaceutico(String id, String claveNueva) throws Exception {
        String sql = "UPDATE Farmaceutico SET clave = ? WHERE id = ?";
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            ps.setString(1, claveNueva);
            ps.setString(2, id);
            ps.executeUpdate();
        }
    }
    public Farmaceutico readPorId(String id) throws Exception {
        List<Farmaceutico> todos = findAll();
        for (Farmaceutico f : todos) {
            if (f.getId().equalsIgnoreCase(id)) {
                return f;
            }
        }
        throw new Exception("Farmacéutico no existe");
    }




}