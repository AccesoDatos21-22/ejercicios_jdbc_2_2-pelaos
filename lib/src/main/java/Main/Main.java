package Main;

import dao.Cafes;
import dao.Libros;
import modelo.AccesoDatosException;
import modelo.Libro;

import java.io.File;
import java.sql.SQLException;

public class Main {


    public static void main(String[] args) {
        try {
            Libros libros = new Libros();
//			System.out.println(libros.crearTablaLibro());
//            libros.obtenerLibro(1325);
//            for (String columna : libros.getCamposLibro()) {
//                System.out.println(columna);
//            }
//            libros.borrar(new Libro(12345,"Sistemas Operativos","Tanembaun","Informatica",156,3));
//            libros.actualizarCopias(new Libro(12345,"Sase","asd","Informatica",156,3));
            for (Libro verCatalogo : libros.verCatalogo()) {
                System.out.println(verCatalogo.toString());
            }
        } catch (AccesoDatosException  e) {
            e.printStackTrace();
        }
    }
}
