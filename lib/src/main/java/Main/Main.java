package Main;

import dao.Libros;
import modelo.AccesoDatosException;
import modelo.Libro;
import java.util.List;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {

		Libros libros = null;
		try {
			libros = new Libros();
			libros.anadirLibro(new Libro(1, "1001 noches", "alibaba", "planeta", 200, 1000000));
			libros.anadirLibro(new Libro(2, "2021 noches", "aliexpress", "luna", 400, 1000000));
			libros.anadirLibro(new Libro(3, "10 dias", "amazon", "asteroide", 600, 1000000));
			List lista = libros.verCatalogo();
			lista.forEach(System.out::println);
			System.out.println("//////////////////////////////VER CATALOGO INVERSO");
			libros.verCatalogoInverso();
		} catch (AccesoDatosException e) {
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
