package Main;

import dao.Cafes;
import dao.Libros;
import modelo.AccesoDatosException;
import modelo.Libro;
import java.util.List;

public class Main {
	public static void main(String[] args) {

		try {
			Libros libros=new  Libros();
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
