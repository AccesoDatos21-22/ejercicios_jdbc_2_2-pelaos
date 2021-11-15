package dao;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import java.sql.ResultSetMetaData;

import modelo.AccesoDatosException;
import modelo.Libro;
import utils.Utilidades;


/**
 * @author Carlos
 * @version 1.0
 * @license GPLv3
 */

public class Libros {
    private static final String CREATE_LIBROS = " create table libros (" +
            "   isbn integer not null," +
            "   titulo varchar(50) not null," +
            "   autor varchar(50) not null," +
            "   editorial varchar(25) not null," +
            "   paginas integer not null," +
            "   copias integer not null," +
            "   constraint isbn_pk primary key (isbn)" +
            ");";
    private static final String INSERT_LIBRO_QUERY = "insert into libros values (?,?,?,?,?,?)";
    private static final String SEARCH_LIBROS_EDITORIAL = "select * from libros WHERE libros.editorial= ?";

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
        String fileSQL = "lib/src/main/resources/libros.sql";
        try {
            // Obtenemos la conexión
            this.con = new Utilidades().getConnection();
            this.stmt = null;
            this.rs = null;
            this.pstmt = null;
            String sql = getSqlScript(fileSQL);
            loadDatabase(sql);
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
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
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
     * @return List<Libro>
     * @throws SQLException
     */

    public List<Libro> verCatalogo() throws AccesoDatosException {
        String sqlSentence = "SELECT * FROM libros;";
        ArrayList<Libro> list = new ArrayList<Libro>();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sqlSentence);
            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
                list.add(new Libro(isbn, titulo, autor, editorial, paginas, copias));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        }
        return list;

    }

    /**
     * Actualiza el numero de copias para un libro
     *
     * @param libro
     * @throws AccesoDatosException
     */

    public void actualizarCopias(Libro libro) throws AccesoDatosException {
        String updateSql = "UPDATE libros SET titulo=?, autor=?, editorial=?, paginas=?, copias=? where isbn = ?";
        try {
            if (pstmt == null)
                pstmt = con.prepareStatement(updateSql);
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setString(3, libro.getEditorial());
            pstmt.setInt(4, libro.getPaginas());
            pstmt.setInt(5, libro.getCopias());
            pstmt.setInt(6, libro.getISBN());
            rs = pstmt.executeQuery();
            System.out.println("Libro actualizado correctamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        liberar();
    }


    /**
     * Añade un nuevo libro a la BD
     *
     * @param libro
     * @throws AccesoDatosException
     */
    public void anadirLibro(Libro libro) throws AccesoDatosException {
        String insertString = "insert into libros (isbn,titulo,autor,editorial,paginas,copias) values(?,?,?,?,?,?);";
        try {
            if (pstmt == null)
                pstmt = con.prepareStatement(insertString);
            pstmt.setInt(1, libro.getISBN());
            pstmt.setString(2, libro.getTitulo());
            pstmt.setString(3, libro.getAutor());
            pstmt.setString(4, libro.getEditorial());
            pstmt.setInt(5, libro.getPaginas());
            pstmt.setInt(6, libro.getCopias());
            rs = pstmt.executeQuery();
            System.out.println("Libro added correctamente");
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
        liberar();


    }

    /**
     * Borra un libro por ISBN
     *
     * @param libro
     * @throws AccesoDatosException
     */

    public void borrar(Libro libro) throws AccesoDatosException, SQLException {

        String searchBookString = "delete from libros where ISBN= ?";
        if (pstmt == null)
            pstmt = con.prepareStatement(searchBookString);
        pstmt.setInt(1, libro.getISBN());
        pstmt.executeUpdate();
        System.out.println("Libro borrado.");
        liberar();

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

        ResultSetMetaData a = stmt.executeQuery(sqlSentece).getMetaData();
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

        stmt = con.createStatement();
        stmt.execute(sqlSentence);
        liberar();
        return true;
    }

    /**
     * @param file , donde se encuentra el archivo con la sentencia sql
     * @return Devuelve un String con la sentencia sql para crear la base de datos con inserts y valores por defecto
     * @author Oscar
     */
    public String getSqlScript(String file) {
        String finalSQL = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String a;
            while ((a = br.readLine()) != null) {
                finalSQL += a;

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(finalSQL);
        return finalSQL;
    }

    /**
     * @param sql , sentencia sql
     * @throws SQLException
     * @author Oscar
     * @returns Carga la base de datos ya creada.
     */
    public void loadDatabase(String sql) throws SQLException {
        if (stmt == null)
            stmt = con.createStatement();
        stmt.execute(sql);
    }

    public void librosporEditorial(String editorial) throws AccesoDatosException {
        //Sentencia SQL
        PreparedStatement stmnt = null;
        //Resultados a obtener de la sentencia SQL
        ResultSet rs = null;
        try {
            con = new Utilidades().getConnection();
            //Creacion de la sentencia
            stmnt = con.prepareStatement(SEARCH_LIBROS_EDITORIAL);
            stmnt.setString(1, editorial);
            //Ejecución de la consulta y obtencion de resultados en un ResultSet
            rs = stmnt.executeQuery();
            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editor = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
                System.out.println(isbn + ", " + titulo + ", " + autor + ", " + editor + ", " + paginas + ", " + copias);
            }
        } catch (IOException e) {
            // Error al leer propiedades
            // En una aplicación real, escribo en el log y delego
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } catch (SQLException sqle) {
            // En una aplicación real, escribo en el log y delego
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            try {
                // Liberamos todos los recursos pase lo que pase
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }

            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }

    }

    public void actualizaPrecio(int isbn, float precio, int paginas) {
        int paginasActuales = -1;
        float precioFinal;
        String sqlSelect = "select paginas from libros where ISBN =" + isbn;
        String sqlPreparedStmt = "update libros set paginas=?, precio=? where isbn = " + isbn;
        try {
            //Deshabilitamos el autocommit
            con.setAutoCommit(false);

            pstmt = con.prepareStatement(sqlPreparedStmt,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //Primero, buscaremos si el libro existe en nuestro catálogo y actulizamos los valores
            while (rs.next()) {
                //Si paginas es -1, significa que no ha habido ningún resultado.
                if ((paginasActuales = rs.getInt("paginas")) == -1)
                    throw new SQLException();

                //Cambiamos el número de páginas y calculamos el precio
                paginas += paginasActuales;
                precioFinal = paginasActuales * precio;
                //Actualizamos nuestros datos en la bdd
                rs.updateInt("paginas", paginas);
                rs.updateFloat("precio", precioFinal);
                rs.updateRow();
            }

            //Commit para guardar cambios.
            con.commit();
        } catch (SQLException e) {
            //Si se produce un fallo, hacemos un rollback
            try {
                System.err.println("Error al actualizar precio... Haciendo rollback");
                con.rollback();
                e.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    /**  Añade un nuevo método a la clase libros que reciba un isbn,
     *  un float que indica el precio por página y  un número de páginas.

     public void actualizaPrecio(int isbn, float precio, int paginas) throws AccesoDatosException;

     este método realizará lo siguiente:


     a.       Sumará el número de páginas a las páginas actuales que ya tiene el libro
     b.      Calcula el precio multiplicando el total de páginas por el precio por página
     c.       Actualiza el precio del libro
     d.      Las 2 operaciones de actualización ser una transacción
     e.       Resuélvelo utilizando ResultSet.CONCUR_UPDATABLE
     **/
}

