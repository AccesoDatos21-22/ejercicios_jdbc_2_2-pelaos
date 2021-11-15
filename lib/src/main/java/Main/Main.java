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
			libros.actualizaPrecio(1,233.2f, 34);
			libros.anadirLibro(new Libro(1, "1001 noches", "alibaba", "planeta", 200, 1000000));
			List lista = libros.verCatalogo();
			lista.forEach(System.out::println);
			System.out.println("Libro por editorial");
			libros.librosporEditorial("planeta");

			for (Libro verCatalogo : libros.verCatalogo()) {
				System.out.println(verCatalogo.toString());
			}

//			System.out.println(libros.crearTablaLibro());

            libros.obtenerLibro(1325);
            for (String columna : libros.getCamposLibro()) {
                System.out.println(columna);
            }
            libros.borrar(new Libro(12345,"Sistemas Operativos","Tanembaun","Informatica",156,3));


		} catch (AccesoDatosException | SQLException e) {
			e.printStackTrace();
		}
	}
}
