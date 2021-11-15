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
			int[] filas={1,3};
			for (Libro libro : libros.verCatalogo()) {
				System.out.println(libro);
			}
			System.out.println("//////////VER CATALOGO POR LISTA");
			libros.verCatalogo(filas);
			} catch (AccesoDatosException e) {
			e.printStackTrace();
		}
	}
}
