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
            libros.anadirLibro(new Libro(1, "1001 noches", "alibaba", "planeta", 200, 1000000,0f));
            libros.anadirLibro(new Libro(2, "2021 noches", "aliexpress", "luna", 400, 1000000,23.0f));
            libros.anadirLibro(new Libro(3, "10 dias", "amazon", "asteroide", 600, 1000000,24.55f));
            List lista = libros.verCatalogo();
            lista.forEach(System.out::println);

            System.out.println("////////////VER CATALOGO INVERSO");
            libros.verCatalogoInverso();
            int[] filas = {1, 3};
            for (Libro libro : libros.verCatalogo()) {
                System.out.println(libro);
            }
            System.out.println("//////////VER CATALOGO POR LISTA");
            libros.verCatalogo(filas);

			libros.obtenerLibro(1325);
			libros.librosporEditorial("planeta");
			for (String columna : libros.getCamposLibro()) {
                System.out.println(columna);
            }
            libros.borrar(new Libro(12345,"Sistemas Operativos","Tanembaun","Informatica",156,3,45.23));
			libros.actualizarPrecio(122);

			libros.actualizaPrecio(1, 233.2f, 34);
			for (Libro verCatalogo : libros.verCatalogo()) {
				System.out.println(verCatalogo.toString());
			}

        } catch (AccesoDatosException | SQLException e) {
            e.printStackTrace();
        }
    }
}
