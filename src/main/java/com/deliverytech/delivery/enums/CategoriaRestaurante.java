package com.deliverytech.delivery.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categorias predefinidas de restaurantes")
public enum CategoriaRestaurante {

    @Schema(description = "Pizzaria")
    PIZZARIA("Pizzaria", "🍕"),

    @Schema(description = "Hamburgueria")
    HAMBURGUERIA("Hamburgueria", "🍔"),

    @Schema(description = "Japonesa")
    JAPONESA("Japonesa", "🍣"),

    @Schema(description = "Italiana")
    ITALIANA("Italiana", "🍝"),

    @Schema(description = "Brasileira")
    BRASILEIRA("Brasileira", "🇧🇷"),

    @Schema(description = "Mexicana")
    MEXICANA("Mexicana", "🌮"),

    @Schema(description = "Chinesa")
    CHINESA("Chinesa", "🥢"),

    @Schema(description = "Árabe")
    ARABE("Árabe", "🥙"),

    @Schema(description = "Vegetariana")
    VEGETARIANA("Vegetariana", "🥗"),

    @Schema(description = "Doces & Sobremesas")
    DOCES_SOBREMESAS("Doces & Sobremesas", "🍰"),

    @Schema(description = "Lanches")
    LANCHES("Lanches", "🥪"),

    @Schema(description = "Saudável")
    SAUDAVEL("Saudável", "🥑");

    private final String nome;
    private final String emoji;

    CategoriaRestaurante(String nome, String emoji) {
        this.nome = nome;
        this.emoji = emoji;
    }

    @JsonValue
    public String getNome() {
        return nome;
    }

    public String getEmoji() {
        return emoji;
    }

    /**
     * Retorna o nome da categoria com emoji
     */
    public String getNomeComEmoji() {
        return emoji + " " + nome;
    }

    /**
     * Retorna a descrição completa da categoria
     */
    public String getDescricao() {
        return switch (this) {
            case PIZZARIA -> "Pizzas tradicionais e especiais";
            case HAMBURGUERIA -> "Hambúrgueres artesanais e lanches";
            case JAPONESA -> "Sushi, sashimi e pratos orientais";
            case ITALIANA -> "Massas, risotos e pratos italianos";
            case BRASILEIRA -> "Comida caseira e pratos típicos brasileiros";
            case MEXICANA -> "Tacos, burritos e comida mexicana";
            case CHINESA -> "Pratos da culinária chinesa";
            case ARABE -> "Esfihas, quibes e comida árabe";
            case VEGETARIANA -> "Pratos vegetarianos e veganos";
            case DOCES_SOBREMESAS -> "Bolos, tortas e sobremesas";
            case LANCHES -> "Sanduíches e lanches rápidos";
            case SAUDAVEL -> "Pratos saudáveis e funcionais";
        };
    }

    /**
     * Retorna as palavras-chave associadas à categoria para busca
     */
    public String[] getPalavrasChave() {
        return switch (this) {
            case PIZZARIA -> new String[]{"pizza", "pizzaria", "italiana", "margherita", "calabresa"};
            case HAMBURGUERIA -> new String[]{"hamburguer", "burger", "lanche", "batata", "milkshake"};
            case JAPONESA -> new String[]{"sushi", "sashimi", "temaki", "yakisoba", "oriental"};
            case ITALIANA -> new String[]{"massa", "macarrão", "risoto", "lasanha", "italiana"};
            case BRASILEIRA -> new String[]{"feijoada", "caseira", "brasileira", "tradicional", "regional"};
            case MEXICANA -> new String[]{"taco", "burrito", "mexicana", "nachos", "guacamole"};
            case CHINESA -> new String[]{"chinesa", "oriental", "yakisoba", "rolinho", "agridoce"};
            case ARABE -> new String[]{"esfiha", "kibe", "árabe", "shawarma", "homus"};
            case VEGETARIANA -> new String[]{"vegetariano", "vegano", "salada", "natural", "orgânico"};
            case DOCES_SOBREMESAS -> new String[]{"doce", "sobremesa", "bolo", "torta", "açaí"};
            case LANCHES -> new String[]{"lanche", "sanduíche", "wrap", "rápido", "prático"};
            case SAUDAVEL -> new String[]{"saudável", "fit", "light", "natural", "funcional"};
        };
    }

    /**
     * Retorna a cor associada à categoria (para UI)
     */
    public String getCor() {
        return switch (this) {
            case PIZZARIA -> "#FF6B35";
            case HAMBURGUERIA -> "#8B4513";
            case JAPONESA -> "#FF69B4";
            case ITALIANA -> "#228B22";
            case BRASILEIRA -> "#FFD700";
            case MEXICANA -> "#FF4500";
            case CHINESA -> "#DC143C";
            case ARABE -> "#DDA0DD";
            case VEGETARIANA -> "#32CD32";
            case DOCES_SOBREMESAS -> "#FF1493";
            case LANCHES -> "#FFA500";
            case SAUDAVEL -> "#00CED1";
        };
    }

    /**
     * Verifica se a categoria é considerada saudável
     */
    public boolean isSaudavel() {
        return this == VEGETARIANA || this == SAUDAVEL || this == JAPONESA;
    }

    /**
     * Verifica se a categoria é de comida rápida
     */
    public boolean isComidaRapida() {
        return this == HAMBURGUERIA || this == LANCHES || this == PIZZARIA;
    }

    /**
     * Verifica se a categoria é internacional
     */
    public boolean isInternacional() {
        return this != BRASILEIRA;
    }

    /**
     * Retorna o tempo médio de preparo em minutos
     */
    public int getTempoMedioPreparo() {
        return switch (this) {
            case LANCHES -> 15;
            case HAMBURGUERIA -> 20;
            case PIZZARIA -> 25;
            case DOCES_SOBREMESAS -> 20;
            case JAPONESA -> 30;
            case CHINESA -> 25;
            case MEXICANA -> 25;
            case ARABE -> 20;
            case VEGETARIANA -> 20;
            case SAUDAVEL -> 15;
            case ITALIANA -> 35;
            case BRASILEIRA -> 40;
        };
    }

    /**
     * Converte string para enum (case insensitive)
     */
    @JsonCreator
    public static CategoriaRestaurante fromString(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return null;
        }

        String categoriaLimpa = categoria.trim().toLowerCase();

        // Busca exata pelo nome
        for (CategoriaRestaurante cat : values()) {
            if (cat.nome.toLowerCase().equals(categoriaLimpa)) {
                return cat;
            }
        }

        // Busca por palavras-chave
        for (CategoriaRestaurante cat : values()) {
            for (String palavra : cat.getPalavrasChave()) {
                if (palavra.toLowerCase().equals(categoriaLimpa)) {
                    return cat;
                }
            }
        }

        // Busca parcial no nome
        for (CategoriaRestaurante cat : values()) {
            if (cat.nome.toLowerCase().contains(categoriaLimpa) || 
                categoriaLimpa.contains(cat.nome.toLowerCase())) {
                return cat;
            }
        }

        return null;
    }

    /**
     * Verifica se uma string é uma categoria válida
     */
    public static boolean isValida(String categoria) {
        return fromString(categoria) != null;
    }

    /**
     * Retorna todas as categorias como array de strings
     */
    public static String[] toStringArray() {
        CategoriaRestaurante[] categorias = values();
        String[] nomes = new String[categorias.length];
        for (int i = 0; i < categorias.length; i++) {
            nomes[i] = categorias[i].getNome();
        }
        return nomes;
    }

    /**
     * Retorna categorias filtradas por tipo
     */
    public static CategoriaRestaurante[] getCategoriasSaudaveis() {
        return java.util.Arrays.stream(values())
                .filter(CategoriaRestaurante::isSaudavel)
                .toArray(CategoriaRestaurante[]::new);
    }

    /**
     * Retorna categorias de comida rápida
     */
    public static CategoriaRestaurante[] getCategoriasRapidas() {
        return java.util.Arrays.stream(values())
                .filter(CategoriaRestaurante::isComidaRapida)
                .toArray(CategoriaRestaurante[]::new);
    }

    /**
     * Retorna categorias internacionais
     */
    public static CategoriaRestaurante[] getCategoriasInternacionais() {
        return java.util.Arrays.stream(values())
                .filter(CategoriaRestaurante::isInternacional)
                .toArray(CategoriaRestaurante[]::new);
    }

    /**
     * Busca categorias por palavra-chave
     */
    public static CategoriaRestaurante[] buscarPorPalavraChave(String palavra) {
        if (palavra == null || palavra.trim().isEmpty()) {
            return new CategoriaRestaurante[0];
        }

        String palavraLimpa = palavra.trim().toLowerCase();
        
        return java.util.Arrays.stream(values())
                .filter(cat -> {
                    // Busca no nome
                    if (cat.nome.toLowerCase().contains(palavraLimpa)) {
                        return true;
                    }
                    // Busca nas palavras-chave
                    for (String chave : cat.getPalavrasChave()) {
                        if (chave.toLowerCase().contains(palavraLimpa)) {
                            return true;
                        }
                    }
                    return false;
                })
                .toArray(CategoriaRestaurante[]::new);
    }

    @Override
    public String toString() {
        return nome;
    }
}