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
import java.util.HashMap;
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
            "   precio real not null," +
            "   constraint isbn_pk primary key (isbn)" +
            ");";
    private static final String INSERT_LIBRO_QUERY = "insert into libros values (?,?,?,?,?,?)";
    private static final String SELECT_LIBROS_QUERY = "SELECT * FROM libros;";
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
                double precio = rs.getDouble("precio");
                list.add(new Libro(isbn, titulo, autor, editorial, paginas, copias, precio));
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
        String updateSql = "UPDATE libros SET titulo=?, autor=?, editorial=?, paginas=?, copias=?, precio=? where isbn = ?";
        try {

            pstmt = con.prepareStatement(updateSql);
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setString(3, libro.getEditorial());
            pstmt.setInt(4, libro.getPaginas());
            pstmt.setInt(5, libro.getCopias());
            pstmt.setDouble(6, libro.getPrecio());
            pstmt.setInt(7, libro.getISBN());
            pstmt.executeUpdate();
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
            pstmt.executeQuery();
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
            String titulo = rs.getString("titulo");
            String autor = rs.getString("autor");
            String editorial = rs.getString("editorial");
            int paginas = rs.getInt("paginas");
            int copias = rs.getInt("copias");
            Double precio = rs.getDouble("precio");
            System.out.println("Libro> isbn:" + ISBN + ", titulo:" + titulo + ", autor:" + autor + ", editorial:" + editorial + ", paginas: " + paginas + ", copias:" + copias + ",precio: " + precio);
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
                "   precio real not null,\n" +
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
        pstmt = null;
        //Resultados a obtener de la sentencia SQL
        rs = null;
        try {
            //Creacion de la sentencia
            pstmt = con.prepareStatement(SEARCH_LIBROS_EDITORIAL);
            pstmt.setString(1, editorial);
            //Ejecución de la consulta y obtencion de resultados en un ResultSet
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
                Double precio = rs.getDouble("precio");
                System.out.println(isbn + ", " + titulo + ", " + autor + ", " + editorial + ", " + paginas + ", " + copias + ", " + precio);
            }

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
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }

            } catch (SQLException sqle) {
                // En una aplicación real, escribo en el log, no delego porque
                // es error al liberar recursos
                Utilidades.printSQLException(sqle);
            }
        }
        //        Añade un nuevo campo precio a la tabla Libros.Añade un nuevo método a la clase Libros
//        que reciba un float indicando el precio por página de cada libro.Este método
//        debe consultar las páginas de cada libro,multiplicar por el precio por página y
//        rellenar la columna precio de cada libro.Resuélvelo con la consulta SELECT_LIBROS_QUERY
//        utilizando un ResultSet adecuado y sus métodos.Desarrolla una clase java
//        con método main para probar el nuevo método.


    }

    public void actualizarPrecio(double precio) {
        Libro libro = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editor = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
                precio = precio * copias;
                libro = new Libro(isbn, titulo, autor, editor, paginas, copias, precio);

                String updateSql = "UPDATE libros SET titulo=?, autor=?, editorial=?, paginas=?, copias=?, precio=? where isbn = ?";
                pstmt = con.prepareStatement(updateSql);
                pstmt.setString(1, libro.getTitulo());
                pstmt.setString(2, libro.getAutor());
                pstmt.setString(3, libro.getEditorial());
                pstmt.setInt(4, libro.getPaginas());
                pstmt.setInt(5, libro.getCopias());
                pstmt.setDouble(6, libro.getPrecio());
                pstmt.setInt(7, libro.getISBN());
                pstmt.executeUpdate();
                System.out.println("Libro actualizado correctamente");


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        liberar();

    }

    private void actualizarPrecioUpdateSQL(Libro libro) {
        String updateSql = "UPDATE libros SET titulo=?, autor=?, editorial=?, paginas=?, copias=?, precio=? where isbn = ?";
        try {

            pstmt = con.prepareStatement(updateSql);
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setString(3, libro.getEditorial());
            pstmt.setInt(4, libro.getPaginas());
            pstmt.setInt(5, libro.getCopias());
            pstmt.setDouble(6, libro.getPrecio());
            pstmt.setInt(7, libro.getISBN());
            pstmt.executeUpdate();
            System.out.println("Libro actualizado correctamente");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        liberar();

    }


}

