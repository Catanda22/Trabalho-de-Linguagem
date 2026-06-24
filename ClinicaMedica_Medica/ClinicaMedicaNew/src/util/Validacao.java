package util;

/**
 * Métodos estáticos de validação de entradas.
 */
public class Validacao {

    private Validacao() {}

    /** Valida CPF (11 dígitos, algoritmo dos dígitos verificadores). */
    public static boolean biValido(String bi) {
    if (bi == null || bi.trim().isEmpty()) {
        return false;
    }

    bi = bi.trim().toUpperCase();

    // Formato: 9 números + 2 letras + 3 números
    return bi.matches("\\d{9}[A-Z]{2}\\d{3}");
}

    /** Verifica se a string não está vazia/nula. */
    public static boolean preenchido(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /** Valida e-mail básico. */
    public static boolean emailValido(String email) {
        if (email == null || email.isBlank()) return true; // opcional
        return email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }
}
