package com.angelv.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String nombre;

    private Integer anioNacimiento;
    private Integer anioFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;


    public Autor() {}

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.anioNacimiento = datosAutor.anioNacimiento();
        this.anioFallecimiento = datosAutor.anioFallecimiento();
    }


    @Override
    public String toString() {
        StringBuilder librosStr = new StringBuilder();
        librosStr.append("Libros: ");
        for (int i = 0; i < libros.size(); i++) {
            librosStr.append(libros.get(i).getTitulo());
            if (i < libros.size() - 1) {
                librosStr.append(", ");
            }
        }
        return String.format("---- Autor ----" +
                "%nNombre: %s" +
                "%n%s" +
                "%nAño de Nacimiento: %d" +
                "%nAño de Fallecimiento: %d" +
                "%n--------------------------", nombre, librosStr, anioNacimiento, anioFallecimiento == null ? "Desconocido" : anioFallecimiento);
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getAnioFallecimiento() {
        return anioFallecimiento;
    }

    public void setAnioFallecimiento(Integer anioFallecimiento) {
        this.anioFallecimiento = anioFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
