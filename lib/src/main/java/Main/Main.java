package Main;

import dao.Cafes;
import dao.Libros;
import modelo.AccesoDatosException;
import modelo.Cafe;
import modelo.Libro;
import java.util.List;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws AccesoDatosException {

		try {
			Libros libros = new Libros();
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

		Cafes cafes = new Cafes();
		cafes.transferencia("Colombian","Espresso");
		cafes.buscar("Colombian");
		cafes.buscar("Espresso");
	}
}
