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
			libros.copiaLibro(12345,67891);
			List lista=libros.verCatalogo();
			lista.forEach(System.out::println);
		} catch (AccesoDatosException e) {
			e.printStackTrace();
		}
	}
}
