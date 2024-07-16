package com.angelv.literalura.principal;

import com.angelv.literalura.model.*;
import com.angelv.literalura.repository.AutorRepository;
import com.angelv.literalura.repository.LibroRepository;
import com.angelv.literalura.service.ConsumoAPI;
import com.angelv.literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private final LibroRepository repositorioLibro;
    private final AutorRepository repositorioAutor;


    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.repositorioLibro = libroRepository;
        this.repositorioAutor = autorRepository;
    }


    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    **** Indique una opción ****
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 libros más descargados
                    
                    0 - Salir
                    **********************************
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresVivosEnAnio();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 6:
                    mostrarTop10Libros();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private DatosBusqueda getDatosLibro() {
        System.out.println("Escribe el título del libro que deseas buscar:");
        String tituloLibro = teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL_BASE + tituloLibro.replace(" ", "+"));
        return conversor.obtenerDatos(json, DatosBusqueda.class);
    }

    private void buscarLibroPorTitulo() {
        DatosBusqueda datosBusqueda = getDatosLibro();
        if (datosBusqueda == null || datosBusqueda.resultado().isEmpty()) {
            System.out.println("Libro no encontrado");
            return;
        }

        DatosLibro primerLibro = datosBusqueda.resultado().get(0);
        Libro libro = new Libro(primerLibro);
        System.out.println("---- Libro ----");
        System.out.println(libro);
        System.out.println("---------------");

        Optional<Libro> libroExistenteOptional = repositorioLibro.findByTitulo(libro.getTitulo());
        if (libroExistenteOptional.isPresent()) {
            System.out.println("\nEl libro ya está registrado\n");
            return;
        }

        if (primerLibro.autor().isEmpty()) {
            System.out.println("Sin autor");
            return;
        }

        DatosAutor datosAutor = primerLibro.autor().get(0);
        Autor autor = new Autor(datosAutor);
        Optional<Autor> autorOptional = repositorioAutor.findByNombre(autor.getNombre());

        Autor autorExistente = autorOptional.orElseGet(() -> repositorioAutor.save(autor));
        libro.setAutor(autorExistente);
        repositorioLibro.save(libro);

        System.out.printf("""
                ---- Libro ----
                Título: %s
                Autor: %s
                Idioma: %s
                Descargas: %d
                ---------------
                """, libro.getTitulo(), autor.getNombre(), libro.getLenguaje(), libro.getNumeroDeDescargas());
    }

    private void mostrarLibrosRegistrados() {
        List<Libro> libros = repositorioLibro.findAll();

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros registrados");
        }

        System.out.println("---- Libros Registrados ----");
        libros.forEach(System.out::println);
        System.out.println("----------------------------");
    }

    private void mostrarAutoresRegistrados() {
        List<Autor> autores = repositorioAutor.findAll();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores registrados");
            return;
        }

        System.out.println("---- Autores Registrados ----");
        autores.forEach(System.out::println);
        System.out.println("-----------------------------");
    }

    private void mostrarAutoresVivosEnAnio() {
        System.out.println("Introduce el año para mostrar los autores vivos:");
        int anio = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = repositorioAutor.findAutoresVivosEnAnio(anio);

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año: " + anio);
        } else {
            System.out.println("---- Autores vivos en el año: " + anio + " ----");
            autores.forEach(System.out::println);
            System.out.println("-----------------------------------------------");
        }
    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("Indica con el número de opción el idioma del cual deseas buscar libros:");
        String opciones = """
                1- es - Español
                2- en - Inglés
                3- pt - Portugués
                4- fr - Francés
                """;
        System.out.println(opciones);
        int opcion = teclado.nextInt();
        teclado.nextLine();

        switch (opcion) {
            case 1:
                listarLibrosPorIdioma(Idioma.ESPANOL);
                break;
            case 2:
                listarLibrosPorIdioma(Idioma.INGLES);
                break;
            case 3:
                listarLibrosPorIdioma(Idioma.PORTUGUES);
                break;
            case 4:
                listarLibrosPorIdioma(Idioma.FRANCES);
                break;
            default:
                System.out.println("Opción no válida");
        }
    }

    private void listarLibrosPorIdioma(Idioma idioma) {
        List<Libro> librosPorIdioma = repositorioLibro.findByLenguaje(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en: " + idioma.getIdiomaEspanol());
        } else {
            System.out.printf("---- Libros en %s ---- %n", idioma.getIdiomaEspanol());
            librosPorIdioma.forEach(System.out::println);
            System.out.println("------------------------");
        }
    }

    private void mostrarTop10Libros() {
        List<Libro> topLibros = repositorioLibro.findTop10ByOrderByNumeroDeDescargasDesc();
        System.out.println("---- Top 10 libros más descargados ----");
        topLibros.forEach(l ->
                System.out.println("Libro: " + l.getTitulo() + " | Descargas: " + l.getNumeroDeDescargas()));
        System.out.println("---------------------------------------");
    }

}
