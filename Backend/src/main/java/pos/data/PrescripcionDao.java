package pos.data;

import pos.logic.Medicamento;
import pos.logic.Prescripcion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PrescripcionDao {
    Database db;

    public PrescripcionDao() {
        db = Database.instance();
    }

    public void create(Prescripcion p) throws Exception {
        String sql = "INSERT INTO Prescripcion (Receta_codigo, Medicamento_codigo, cantidad, indicaciones, duracion) "
                + "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            stm = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, p.getRecetaCodigo());
            stm.setString(2, p.getCodigoMedicamento());
            stm.setInt(3, p.getCantidad());
            stm.setString(4, p.getIndicaciones());
            stm.setInt(5, p.getDuracion());

            int count = stm.executeUpdate();
            if (count == 0) {
                throw new Exception("Error al crear la Prescripción: 0 filas afectadas.");
            }
            rs = stm.getGeneratedKeys();
            if (rs.next()) {
                p.setCodigo(rs.getInt(1));
            }
        } finally {
            if (rs != null) rs.close();
            if (stm != null) stm.close();
        }
    }

    public Prescripcion read(String codigoMedicamento) throws Exception {
        String sql = "select * from Prescripcion pr inner join Medicamento m on pr.codigoMedicamento=m.codigo where pr.codigoMedicamento=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigoMedicamento);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "pr", "m");
        } else {
            throw new Exception("Prescripción no existe");
        }
    }
    public void update(Prescripcion p) throws Exception {
        String sql = "update Prescripcion set cantidad=?, indicaciones=?, duracion=? where codigoMedicamento=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, p.getCantidad());
        stm.setString(2, p.getIndicaciones());
        stm.setInt(3, p.getDuracion());
        stm.setString(4, p.getCodigoMedicamento());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Prescripción no existe");
        }
    }
    public void delete(Prescripcion p) throws Exception {
        String sql = "delete from Prescripcion where codigoMedicamento=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, p.getCodigoMedicamento());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Prescripción no existe");
        }
    }


    private Prescripcion from(ResultSet rs, String aliasPr, String aliasM) {
        try {
            Prescripcion p = new Prescripcion();
            p.setCodigoMedicamento(rs.getString(aliasPr + ".codigoMedicamento"));
            p.setCantidad(rs.getInt(aliasPr + ".cantidad"));
            p.setIndicaciones(rs.getString(aliasPr + ".indicaciones"));
            p.setDuracion(rs.getInt(aliasPr + ".duracion"));

            Medicamento m = new Medicamento();
            m.setCodigo(rs.getString(aliasM + ".codigo"));
            m.setNombre(rs.getString(aliasM + ".nombre"));
            m.setPresentacion(rs.getString(aliasM + ".presentacion"));
            p.setMedicamento(m);

            return p;
        } catch (SQLException ex) {
            return null;
        }
    }


    public List<Prescripcion> findByRecetaCodigo(int recetaCodigo) throws SQLException {
        List<Prescripcion> lista = new ArrayList<>();
        String sql = "SELECT pr.codigo, pr.cantidad, pr.indicaciones, pr.duracion, " +
                "pr.Medicamento_codigo, m.nombre AS med_nombre, m.presentacion AS med_presentacion " +
                "FROM Prescripcion pr " +
                "LEFT JOIN Medicamento m ON pr.Medicamento_codigo = m.codigo " +
                "WHERE pr.Receta_codigo = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1, recetaCodigo);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Prescripcion p = new Prescripcion();
            p.setCodigo(rs.getInt("codigo"));
            p.setCantidad(rs.getInt("cantidad"));
            p.setIndicaciones(rs.getString("indicaciones"));
            p.setDuracion(rs.getInt("duracion"));

            Medicamento m = new Medicamento();
            m.setCodigo(rs.getString("Medicamento_codigo"));
            m.setNombre(rs.getString("med_nombre"));
            m.setPresentacion(rs.getString("med_presentacion"));
            p.setMedicamento(m);

            lista.add(p);
        }
        return lista;
    }

    public List<Prescripcion> findAll() throws SQLException {
        List<Prescripcion> lista = new ArrayList<>();
        String sql = "SELECT pr.codigo AS pr_codigo, pr.cantidad, pr.indicaciones, pr.duracion, " +
                "pr.Receta_codigo AS pr_recetaCodigo, pr.Medicamento_codigo AS pr_medicamentoCodigo, " +
                "m.nombre AS med_nombre, m.presentacion AS med_presentacion " +
                "FROM Prescripcion pr " +
                "LEFT JOIN Medicamento m ON pr.Medicamento_codigo = m.codigo";

        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Prescripcion p = new Prescripcion();
            p.setCodigo(rs.getInt("pr_codigo"));
            p.setCantidad(rs.getInt("cantidad"));
            p.setIndicaciones(rs.getString("indicaciones"));
            p.setDuracion(rs.getInt("duracion"));
            p.setRecetaCodigo(rs.getInt("pr_recetaCodigo"));

            Medicamento m = new Medicamento();
            m.setCodigo(rs.getString("pr_medicamentoCodigo"));
            m.setNombre(rs.getString("med_nombre"));
            m.setPresentacion(rs.getString("med_presentacion"));
            p.setMedicamento(m);

            lista.add(p);
        }
        return lista;
    }



}
