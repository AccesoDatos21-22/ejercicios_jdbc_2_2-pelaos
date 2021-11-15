package Main;

import dao.Libros;
import modelo.AccesoDatosException;
import modelo.Libro;

import java.util.HashMap;
import java.util.List;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {

		try {
			Libros libros = new Libros();
//			libros.anadirLibro(new Libro(1, "1001 noches", "alibaba", "planeta", 200, 1000000,3.56));
			System.out.println("Libro por editorial");

			for (Libro verCatalogo : libros.verCatalogo()) {
				System.out.println(verCatalogo.toString());
			}

//			System.out.println(libros.crearTablaLibro());

			libros.obtenerLibro(1325);
			libros.librosporEditorial("planeta");
			for (String columna : libros.getCamposLibro()) {
                System.out.println(columna);
            }
            libros.borrar(new Libro(12345,"Sistemas Operativos","Tanembaun","Informatica",156,3,45.23));
			libros.actualizarPrecio(122);

			for (Libro verCatalogo : libros.verCatalogo()) {
				System.out.println(verCatalogo.toString());
			}

		} catch (AccesoDatosException | SQLException e) {
			e.printStackTrace();
		}
	}
}
