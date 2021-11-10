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
 * @descrition
 * @author Carlos
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class Libros {
	private static final String CREATE_LIBROS=" create table libros (" +
			"   isbn integer not null," +
			"   titulo varchar(50) not null," +
			"   autor varchar(50) not null," +
			"   editorial varchar(25) not null," +
			"   paginas integer not null," +
			"   copias integer not null," +
			"   constraint isbn_pk primary key (isbn)" +
			");";
	private static final String INSERT_LIBRO_QUERY="insert into libros values (?,?,?,?,?,?)";
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
		try {
			// Obtenemos la conexión
			this.con = new Utilidades().getConnection();
			this.stmt = con.createStatement();
			this.rs = null;
			this.pstmt = null;
			stmt.executeUpdate(CREATE_LIBROS);
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
		String sqlSentence="SELECT * FROM libros;";
		ArrayList<Libro> list = new ArrayList<Libro>();
		try {
			if (stmt == null)
				stmt = con.createStatement();

			rs = stmt.executeQuery(sqlSentence);
			while(rs.next()){
				int isbn= rs.getInt("isbn");
				String titulo = rs.getString("titulo");
				String autor = rs.getString("autor");
				String editorial = rs.getString("editorial");
				int paginas = rs.getInt("paginas");
				int copias = rs.getInt("copias");
				list.add(new Libro(isbn,titulo,autor,editorial,paginas,copias));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

    /**
     * Actualiza el numero de copias para un libro
     * @param isbn
     * @param copias
     * @throws AccesoDatosException
     */
	
	public void actualizarCopias(Libro libro) throws AccesoDatosException {
		
	}

	
    /**
     * Añade un nuevo libro a la BD
     * @param isbn
     * @param titulo
     * @param autor
     * @param editorial
     * @param paginas
     * @param copias
     * @throws AccesoDatosException
     */
	public void anadirLibro(Libro libro) throws AccesoDatosException {
		PreparedStatement stmt=null;
		try {
			stmt= con.prepareStatement(INSERT_LIBRO_QUERY);
			stmt.setInt(1,libro.getISBN());
			stmt.setString(2,libro.getTitulo());
			stmt.setString(3,libro.getAutor());
			stmt.setString(4,libro.getEditorial());
			stmt.setInt(5,libro.getPaginas());
			stmt.setInt(6,libro.getCopias());
			stmt.executeUpdate();
		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException(
					"Ocurrió un error al acceder a los datos");

		} finally {
			try {
				// Liberamos todos los recursos pase lo que pase
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

	/**
	 * Borra un libro por ISBN
	 * @param isbn
	 * @throws AccesoDatosException
	 */

	public void borrar(Libro libro) throws AccesoDatosException {
	}
	
	/**
	 * Devulve los nombres de los campos de BD
	 * @return
	 * @throws AccesoDatosException
	 */

	public String[] getCamposLibro() throws AccesoDatosException {
       
    return null;
	}


	public void obtenerLibro(int ISBN) throws AccesoDatosException {
		
	}

	public void librosporEditorial(String editorial) throws AccesoDatosException{
		//Sentencia SQL
		PreparedStatement stmnt=null;
		//Resultados a obtener de la sentencia SQL
		ResultSet rs=null;
		try {
			con=new Utilidades().getConnection();
			//Creacion de la sentencia
			stmnt=con.prepareStatement(SEARCH_LIBROS_EDITORIAL);
			stmnt.setString(1,editorial);
			//Ejecución de la consulta y obtencion de resultados en un ResultSet
			rs=stmnt.executeQuery();
			while (rs.next()){
				int isbn=rs.getInt("isbn");
				String titulo=rs.getString("titulo");
				String autor=rs.getString("autor");
				String editor=rs.getString("editorial");
				int paginas=rs.getInt("paginas");
				int copias=rs.getInt("copias");
				System.out.println(isbn+", "+titulo+", "+autor+", "+editor+", "+paginas+", "+copias);
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

}

