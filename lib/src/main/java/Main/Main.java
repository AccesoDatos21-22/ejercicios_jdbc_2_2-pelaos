package Main;

import dao.Cafes;
import dao.Libros;
import modelo.AccesoDatosException;

import java.io.File;
import java.sql.SQLException;

public class Main {


    public static void main(String[] args) {
		try {
			Libros libros = new Libros();
			System.out.println(libros.crearTablaLibro());


		} catch (AccesoDatosException | SQLException e) {
			e.printStackTrace();
		}
    }
}
