package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import modelo.AccesoDatosException;
import utils.Utilidades;

/**
 * @descrition
 * @author Oscar
 * @date 23/10/2121
 * @version 1.0
 * @license GPLv3
 */

public class Cafes {

	private static Connection con;
	private static ResultSet resultSet;
	private static PreparedStatement preparedStatement;
	private static Statement statement;
	// Consultas a realizar en BD
	private static final String SELECT_CAFES_QUERY = "select CAF_NOMBRE, PROV_ID, PRECIO, VENTAS, TOTAL from CAFES";
	private static final String SEARCH_CAFE_QUERY = "select * from CAFES WHERE CAF_NOMBRE= ?";
	private static final String INSERT_CAFE_QUERY = "insert into CAFES values (?,?,?,?,?)";
	private static final String DELETE_CAFE_QUERY = "delete from CAFES WHERE CAF_NOMBRE = ?";
	private static final String SEARCH_CAFES_PROVEEDOR = "select * from CAFES,PROVEEDORES WHERE CAFES.PROV_ID= ? AND CAFES.PROV_ID=PROVEEDORES.PROV_ID";

	private static final String CREATE_TABLE_PROVEEDORES = "create table if not exists proveedores (PROV_ID integer NOT NULL, PROV_NOMBRE varchar(40) NOT NULL, CALLE varchar(40) NOT NULL, CIUDAD varchar(20) NOT NULL, PAIS varchar(2) NOT NULL, CP varchar(5), PRIMARY KEY (PROV_ID));";

	private static final String CREATE_TABLE_CAFES = "create table if not exists CAFES (CAF_NOMBRE varchar(32) NOT NULL, PROV_ID int NOT NULL, PRECIO numeric(10,2) NOT NULL, VENTAS integer NOT NULL, TOTAL integer NOT NULL, PRIMARY KEY (CAF_NOMBRE), FOREIGN KEY (PROV_ID) REFERENCES PROVEEDORES(PROV_ID));";

	/**
	 * Constructor: inicializa conexión
	 * 
	 * @throws AccesoDatosException
	 */

	public Cafes() {

		statement = null;
		preparedStatement = null;
		resultSet = null;

		try {
			con = new Utilidades().getConnection();

		} catch (IOException e) {

			System.err.println(e.getMessage());

		} catch (SQLException sqle) {
			Utilidades.printSQLException(sqle);

		} finally {
			try {
				// Liberamos todos los recursos pase lo que pase
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException sqle) {
				// En una aplicación real, escribo en el log, no delego porque
				// es error al liberar recursos
				Utilidades.printSQLException(sqle);
			}
		}
	}

	/**
	 * Metodo que muestra por pantalla los datos de la tabla cafes
	 *
	 * @param con
	 * @throws SQLException
	 */
	public void verTabla() throws AccesoDatosException {
		try {
			// Creación de la sentencia
			statement = con.createStatement();
			// Ejecución de la consulta y obtención de resultados en un
			// ResultSet
			resultSet = statement.executeQuery(SELECT_CAFES_QUERY);

			// Recuperación de los datos del ResultSet
			while (resultSet.next()) {
				String coffeeName = resultSet.getString("CAF_NOMBRE");
				int supplierID = resultSet.getInt("PROV_ID");
				float PRECIO = resultSet.getFloat("PRECIO");
				int VENTAS = resultSet.getInt("VENTAS");
				int total = resultSet.getInt("TOTAL");
				System.out.println(coffeeName + ", " + supplierID + ", " + PRECIO + ", " + VENTAS + ", " + total);
			}

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			// System.err.println(sqle.getMessage());
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {
			try {
				// Liberamos todos los recursos pase lo que pase
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException sqle) {
				// En una aplicación real, escribo en el log, no delego porque
				// es error al liberar recursos
				Utilidades.printSQLException(sqle);
			}
		}

	}

	/**
	 * Mótodo que busca un cafe por nombre y muestra sus datos
	 *
	 * @param nombre
	 */
	public void buscar(String nombre) throws AccesoDatosException {

		try {
			// Creación de la sentencia
			preparedStatement = con.prepareStatement(SEARCH_CAFE_QUERY);
			preparedStatement.setString(1, nombre);
			// Ejecución de la consulta y obtención de resultados en un
			// ResultSet
			resultSet = preparedStatement.executeQuery();

			// Recuperación de los datos del ResultSet
			if (resultSet.next()) {
				String coffeeName = resultSet.getString("CAF_NOMBRE");
				int supplierID = resultSet.getInt("PROV_ID");
				float PRECIO = resultSet.getFloat("PRECIO");
				int VENTAS = resultSet.getInt("VENTAS");
				int total = resultSet.getInt("TOTAL");
				System.out.println(coffeeName + ", " + supplierID + ", " + PRECIO + ", " + VENTAS + ", " + total);
			}

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {
			try {
				liberar();
				cerrar();
			} catch (SQLException e) {
				System.err.println("Error: " + e.getLocalizedMessage());
			}
		}

	}

	/**
	 * Mótodo para insertar una fila
	 *
	 * @param nombre
	 * @param provid
	 * @param precio
	 * @param ventas
	 * @param total
	 * @return
	 */
	public void insertar(String nombre, int provid, float precio, int ventas, int total) throws AccesoDatosException {

		try {

			preparedStatement = con.prepareStatement(INSERT_CAFE_QUERY);
			preparedStatement.setString(1, nombre);
			preparedStatement.setInt(2, provid);
			preparedStatement.setFloat(3, precio);
			preparedStatement.setInt(4, ventas);
			preparedStatement.setInt(5, total);
			// Ejecución de la inserción
			preparedStatement.executeUpdate();

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");

		} finally {
			try {
				liberar();
				cerrar();
			} catch (SQLException e) {
				System.err.println("Error: " + e.getLocalizedMessage());
			}
		}

	}

	/**
	 * Mótodo para borrar una fila dado un nombre de cafó
	 *
	 * @param nombre
	 * @return
	 */
	public void borrar(String nombre) throws AccesoDatosException {

		try {
			// Creación de la sentencia
			preparedStatement = con.prepareStatement(DELETE_CAFE_QUERY);
			preparedStatement.setString(1, nombre);
			// Ejecución del borrado
			preparedStatement.executeUpdate();
			System.out.println("café " + nombre + " ha sido borrado.");

		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");

		} finally {
			try {
				liberar();
				cerrar();
			} catch (SQLException e) {
				System.err.println("Error: " + e.getLocalizedMessage());
			}

		}

	}

	/**
	 * Mótodo que busca un cafe por nombre y muestra sus datos
	 *
	 * @param nombre
	 */
	public void cafesPorProveedor(int provid) throws AccesoDatosException {

		try {
			con = new Utilidades().getConnection();
			// Creación de la sentencia
			preparedStatement = con.prepareStatement(SEARCH_CAFES_PROVEEDOR);
			preparedStatement.setInt(1, provid);
			// Ejecución de la consulta y obtención de resultados en un
			// ResultSet
			resultSet = preparedStatement.executeQuery();

			// Recuperación de los datos del ResultSet
			while (resultSet.next()) {
				String coffeeName = resultSet.getString("CAF_NOMBRE");
				int supplierID = resultSet.getInt("PROV_ID");
				float PRECIO = resultSet.getFloat("PRECIO");
				int VENTAS = resultSet.getInt("VENTAS");
				int total = resultSet.getInt("TOTAL");
				String provName = resultSet.getString("PROV_NOMBRE");
				String calle = resultSet.getString("CALLE");
				String ciudad = resultSet.getString("CIUDAD");
				String pais = resultSet.getString("PAIS");
				int cp = resultSet.getInt("CP");
				System.out.println(coffeeName + ", " + supplierID + ", " + PRECIO + ", " + VENTAS + ", " + total
						+ ",Y el proveedor es:" + provName + "," + calle + "," + ciudad + "," + pais + "," + cp);
			}

		} catch (IOException e) {
			// Error al leer propiedades
			// En una aplicación real, escribo en el log y delego
			System.err.println(e.getMessage());
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} catch (SQLException sqle) {
			// En una aplicación real, escribo en el log y delego
			Utilidades.printSQLException(sqle);
			throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
		} finally {

			try {
				liberar();
				cerrar();
			} catch (SQLException e) {
				System.err.println("Error: " + e.getLocalizedMessage());
			}

		}

	}

	public static void cerrar() {
		if (con != null)
			Utilidades.closeConnection(con);
	}

	public static void liberar() throws SQLException {

		if (statement != null)
			statement.close();

		if (resultSet != null)
			resultSet.close();

		if (preparedStatement != null)
			preparedStatement.close();

	}
}
