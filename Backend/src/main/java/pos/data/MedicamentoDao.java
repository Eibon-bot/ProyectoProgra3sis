package pos.data;

import pos.logic.Medicamento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDao {
    Database db;

    public MedicamentoDao() {
        db = Database.instance();
    }

    public void create(Medicamento m) throws Exception {
        String sql = "insert into Medicamento (codigo, nombre, presentacion) values (?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getCodigo());
        stm.setString(2, m.getNombre());
        stm.setString(3, m.getPresentacion());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Medicamento ya existe");
        }
    }
    public Medicamento read(String codigo) throws Exception {
        String sql = "SELECT * FROM Medicamento WHERE codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs);
        } else {
            throw new Exception("Medicamento no existe");
        }
    }

    public void update(Medicamento f) throws Exception {
        if (f.getCodigo() == null || f.getCodigo().isEmpty()) {
            throw new Exception("Código de Medicamento no puede estar vacío");
        }
        Medicamento actual = read(f.getCodigo());
        if (f.getNombre() == null || f.getNombre().isEmpty()) {
            f.setNombre(actual.getNombre());
        }
        if (f.getPresentacion() == null || f.getPresentacion().isEmpty()) {
            f.setPresentacion(actual.getPresentacion());
        }
        String sql = "UPDATE Medicamento SET nombre=?, presentacion=? WHERE codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, f.getNombre());
        stm.setString(2, f.getPresentacion());
        stm.setString(3, f.getCodigo());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("No se pudo actualizar el Medicamento");
        }
    }



    public void delete(Medicamento m) throws Exception {
        if (m.getCodigo() == null || m.getCodigo().isEmpty()) {
            throw new Exception("Código de Medicamento no puede estar vacío");
        }

        String sql = "DELETE FROM Medicamento WHERE codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getCodigo());

        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Medicamento no existe");
        }
    }



    private Medicamento from(ResultSet rs) throws SQLException {
        Medicamento m = new Medicamento();
        m.setCodigo(rs.getString("codigo"));
        m.setNombre(rs.getString("nombre"));
        m.setPresentacion(rs.getString("presentacion"));
        return m;
    }


    public List<Medicamento> findAll() throws Exception {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Medicamento ORDER BY codigo";
        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Medicamento f = new Medicamento();
            f.setCodigo(rs.getString("codigo"));
            f.setNombre(rs.getString("nombre"));
            f.setPresentacion(rs.getString("presentacion"));
            lista.add(f);
        }

        return lista;
    }
}
