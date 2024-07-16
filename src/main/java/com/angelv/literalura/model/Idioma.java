package com.angelv.literalura.model;

import java.util.HashMap;
import java.util.Map;

public enum Idioma {
    ESPANOL("[es]", "Español"),
    INGLES("[en]", "Ingles"),
    PORTUGUES("[pt]", "Portugues"),
    FRANCES("[fr]", "Frances");

    private final String idiomaGutendex;
    private final String idiomaEspanol;

    private static final Map<String, Idioma> gutendexMap = new HashMap<>();
    private static final Map<String, Idioma> espanolMap = new HashMap<>();

    static {
        for (Idioma idioma : Idioma.values()) {
            gutendexMap.put(idioma.idiomaGutendex.toLowerCase(), idioma);
            espanolMap.put(idioma.idiomaEspanol.toLowerCase(), idioma);
        }
    }

    Idioma(String idiomaGutendex, String idiomaEspanol) {
        this.idiomaGutendex = idiomaGutendex;
        this.idiomaEspanol = idiomaEspanol;
    }

    public static Idioma fromString(String text) {
        Idioma idioma = gutendexMap.get(text.toLowerCase());
        if (idioma != null) {
            return idioma;
        }
        throw new IllegalArgumentException("Ningún idioma encontrado: " + text);
    }

    public static Idioma fromEspanol(String text) {
        Idioma idioma = espanolMap.get(text.toLowerCase());
        if (idioma != null) {
            return idioma;
        }
        throw new IllegalArgumentException("Ningún idioma encontrado: " + text);
    }

    public String getIdiomaGutendex() {
        return idiomaGutendex;
    }

    public String getIdiomaEspanol() {
        return idiomaEspanol;
    }
}
