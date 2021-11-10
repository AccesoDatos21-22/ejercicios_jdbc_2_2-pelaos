package Main;

import dao.Cafes;
import dao.Libros;
import modelo.AccesoDatosException;
import modelo.Libro;

import java.util.List;

public class Main {
	public static void main(String[] args) {

		try {
//			Cafes cafes = new Cafes();
			Libros libros=new  Libros();
//			cafes.insertar("Cafetito", 150, 1.0f, 100, 1000);
//			cafes.insertar("Cafe tacilla", 150, 2.0f, 100, 1000);
//			cafes.verTabla();
//			cafes.buscar("tacilla");
//			cafes.cafesPorProveedor(150);
//			cafes.borrar("Cafe tacilla");
//			cafes.verTabla();
			libros.anadirLibro(new Libro(1,"1001 noches","alibaba","planeta",200,1000000));
			List lista=libros.verCatalogo();
			lista.forEach(System.out::println);
			System.out.println("Libro por editorial");
			libros.librosporEditorial("planeta");
		} catch (AccesoDatosException e) {
			e.printStackTrace();
		}
	}
}
