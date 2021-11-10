# Ejercicios 2.1.

### Ejercicio 2 Apartado F
Llamamos a cerrar y liberar al terminar las funciones que realizan alguna acción con los recursos. Así no excedemos la memoria y la dejamos más libre

### Ejercicio 2 Apartado G

Al tener varios métodos para manejar recursos, estamos maximizando la eficiencia del programa y de la memoria interna. A su vez, al definir los objetos como atributos, nos olvidamos de crear un objeto cada vez que creas un método.

### Ejercicio 7
Este método tiene como objetivo conseguir los nombres de las columnas que contiene nuestra tabla. En primer lugar, necesitaremos una sentencia SQL para poder obetener las columnas. Utilizaremos la siguiente:
```javascript
private static final String SELECT_CAMPOS_QUERY = "SELECT * FROM LIBROS LIMIT 1";
```
Después, tendremos que crear el ResultSet y el PreparedStatement para ejecutar la Query y recibir los resultados de esta. Para poder acceder a los datos de nuestra tabla, utilizaremos:
```javascript 
ResultSetMetaData rsmd=rs.getMetaData();
```
Siendo **rs** un ResultSet.  
Con **rsmd** podremos conseguir los nombres y el numero de columnas con:
```javascript
rsmd.getColumnCount();
rsmd.getColumnLabel;
```
En este caso, esta guardando los nombres de las columnas en un array utilizando un bucle para recorrer las clumnas.
```javascript
int columns = rsmd.getColumnCount();
        	campos = new String[columns];
        	for (int i = 0; i < columns; i++) {
            	//Los indices de las columnas comienzan en 1
            	campos[i] = rsmd.getColumnLabel(i + 1);
        	}

```
<<<<<<< HEAD
Por último, devuelve el array.
=======
Por último, devuelve el array.
>>>>>>> 2eaeec0d9dd9ab9533e894216be61d93fce49a41
