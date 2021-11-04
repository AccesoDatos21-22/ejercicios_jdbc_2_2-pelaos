package Main;

import dao.Cafes;
import dao.Libros;
import modelo.AccesoDatosException;

import java.io.File;

public class Main {


    public static void main(String[] args) throws AccesoDatosException {
			Cafes cafes = new Cafes();
			Libros libros = new Libros();
		try {
//			cafes.insertar("Cafetito", 150, 1.0f, 100, 1000);
//			cafes.insertar("Cafe tacilla", 150, 2.0f, 100, 1000);
//			cafes.verTabla();
//			cafes.buscar("Cafe tacilla");
//			cafes.cafesPorProveedor(150);
			cafes.borrar("Cafe tacilla");
			cafes.verTabla();
//
		} catch (AccesoDatosException e) {
			e.printStackTrace();
		}
    }
}
