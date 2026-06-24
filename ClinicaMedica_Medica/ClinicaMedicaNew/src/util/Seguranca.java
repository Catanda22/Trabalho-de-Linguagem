package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitários de segurança: hash de senha com SHA-256.
 */
public class Seguranca {

    private Seguranca() {}

    /**
     * Retorna o hash SHA-256 da senha em hexadecimal minúsculo.
     */
    public static String hashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 indisponível", e);
        }
    }

    /** Verifica se a senha corresponde ao hash armazenado. */
    public static boolean verificar(String senha, String hashArmazenado) {
        return hashSenha(senha).equals(hashArmazenado);
    }
}
