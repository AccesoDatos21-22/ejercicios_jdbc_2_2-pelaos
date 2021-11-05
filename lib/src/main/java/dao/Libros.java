package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.AccesoDatosException;
import modelo.Libro;
import utils.Utilidades;


/**
 * @author Carlos
 * @version 1.0
 * @descrition
 * @date 23/10/2021
 * @license GPLv3
 */

public class Libros {

    // Consultas a realizar en BD


    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement pstmt;

    /**
     * Constructor: inicializa conexión
     *
     * @throws AccesoDatosException
     */

    public Libros() throws AccesoDatosException {
        try {
            // Obtenemos la conexión
            this.con = new Utilidades().getConnection();
            this.stmt = null;
            this.rs = null;
            this.pstmt = null;
        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicación real, escribo en el log y delego
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            // System.err.println(sqle.getMessage());
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        }
    }


    /**
     * Método para cerrar la conexión
     *
     * @throws AccesoDatosException
     */
    public void cerrar() {

        if (con != null) {
            Utilidades.closeConnection(con);
        }

    }


    /**
     * Método para liberar recursos
     *
     * @throws AccesoDatosException
     */
    private void liberar() {
        try {
            // Liberamos todos los recursos pase lo que pase
            //Al cerrar un stmt se cierran los resultset asociados. Podíamos omitir el primer if. Lo dejamos por claridad.
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log, no delego porque
            // es error al liberar recursos
            Utilidades.printSQLException(sqle);
        }
    }

    /**
     * Metodo que muestra por pantalla los datos de la tabla cafes
     *
     * @param con
     * @throws SQLException
     */

    public List<Libro> verCatalogo() throws AccesoDatosException {

        return null;

    }

    /**
     * Actualiza el numero de copias para un libro
     *
     * @param isbn
     * @param copias
     * @throws AccesoDatosException
     */

    public void actualizarCopias(Libro libro) throws AccesoDatosException {

    }


    /**
     * Añade un nuevo libro a la BD
     *
     * @param isbn
     * @param titulo
     * @param autor
     * @param editorial
     * @param paginas
     * @param copias
     * @throws AccesoDatosException
     */
    public void anadirLibro(Libro libro) throws AccesoDatosException {


    }

    /**
     * Borra un libro por ISBN
     *
     * @param isbn
     * @throws AccesoDatosException
     */

    public void borrar(Libro libro) throws AccesoDatosException, SQLException {

        String searchBookString = "delete from libros where ISBN= ?";
        if (pstmt == null)
            pstmt = con.prepareStatement(searchBookString);
        pstmt.setInt(1, libro.getISBN());
        pstmt.executeQuery();
        System.out.println("Libro borrado.");


    }

    /**
     * Devulve los nombres de los campos de BD
     *
     * @return
     * @throws AccesoDatosException
     */

    public String[] getCamposLibro() throws AccesoDatosException, SQLException {
        String sqlSentece = "select * from libros;";
        if (pstmt == null)
            stmt = con.createStatement();

        ResultSetMetaData a= stmt.executeQuery(sqlSentece).getMetaData();
        int numeroColumnas = a.getColumnCount();
        String[] columnas = new String[numeroColumnas];
        for (int i = 0; i < numeroColumnas; i++)
            columnas[i] = a.getColumnName(i + 1);
        liberar();
        return columnas;
    }


    public void obtenerLibro(int ISBN) throws AccesoDatosException, SQLException {
        String searchBookString = "select * from libros where ISBN= ?";
        if (pstmt == null)
            pstmt = con.prepareStatement(searchBookString);
        pstmt.setInt(1, ISBN);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            int isbn = rs.getInt("isbn");
            String titulo = rs.getString("titulo");
            String autor = rs.getString("autor");
            String editorial = rs.getString("editorial");
            int paginas = rs.getInt("paginas");
            int copias = rs.getInt("copias");
            System.out.println("Libro> isbn:" + isbn + ", titulo:" + titulo + ", autor:" + autor + ", editorial:" + editorial + ", paginas: " + paginas + ", copias:" + copias);
            return;
        }
        System.out.println("Ningun libro encontrado con isbn: " + ISBN);
        liberar();

    }

    public boolean crearTablaLibro() throws SQLException {
        String sqlSentence = "create table libros2 (\n" +
                "   isbn integer not null,\n" +
                "   titulo varchar(50) not null,\n" +
                "   autor varchar(50) not null,\n" +
                "   editorial varchar(25) not null,\n" +
                "   paginas integer not null,\n" +
                "   copias integer not null,\n" +
                "   constraint isbn_pk primary key (isbn)\n" +
                ");\n";
        if (stmt == null)
            stmt = con.createStatement();
        stmt.execute(sqlSentence);
        liberar();
        return true;
    }


}

