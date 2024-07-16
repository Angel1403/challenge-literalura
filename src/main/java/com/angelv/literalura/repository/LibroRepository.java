package com.angelv.literalura.repository;

import com.angelv.literalura.model.Idioma;
import com.angelv.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitulo(String titulo);
    List<Libro> findByLenguaje(Idioma lenguaje);
    List<Libro> findTop10ByOrderByNumeroDeDescargasDesc();
}
