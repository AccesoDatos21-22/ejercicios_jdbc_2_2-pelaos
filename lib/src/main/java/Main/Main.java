package Main;

import dao.Libros;
import modelo.AccesoDatosException;
import modelo.Libro;
import java.util.List;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {

		try {
			Libros libros = new Libros();
			libros.actualizaPrecio(12345,1325,0.25f);

		} catch (AccesoDatosException e) {
			e.printStackTrace();
		}
	}
}
