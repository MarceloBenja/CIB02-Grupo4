package edu.uni.datos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Deriva y comprueba contraseñas con PBKDF2 (HMAC-SHA256) y una sal aleatoria por usuario.
 *
 * <p>La base de datos nunca guarda la contraseña, solo el resultado de la derivación. Aunque
 * alguien copie la tabla Usuario, no puede leer las contraseñas, y la sal hace que dos usuarios
 * con la misma contraseña tengan valores distintos (RNF-02).
 */
public final class ClaveSegura {
    /** Iteraciones recomendadas por OWASP para PBKDF2-HMAC-SHA256. */
    public static final int ITERACIONES = 600_000;
    /** Tamaño de la sal en bytes. */
    public static final int BYTES_SAL = 16;
    /** Tamaño del resultado en bytes (256 bits). */
    public static final int BYTES_HASH = 32;

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int BITS_POR_BYTE = 8;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private ClaveSegura() {
    }

    /**
     * Deriva una contraseña.
     *
     * @param clave contraseña escrita por el usuario
     * @param sal sal del usuario
     * @param iteraciones número de iteraciones guardado junto a la sal
     * @return resultado de 32 bytes que se guarda en Usuario.claveHash
     */
    public static byte[] derivar(char[] clave, byte[] sal, int iteraciones) {
        PBEKeySpec especificacion = new PBEKeySpec(clave, sal, iteraciones, BYTES_HASH * BITS_POR_BYTE);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(especificacion).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("La JVM no ofrece " + ALGORITMO, e);
        } finally {
            especificacion.clearPassword();
        }
    }

    /**
     * Comprueba una contraseña contra el valor guardado, en tiempo constante.
     *
     * @param clave contraseña escrita por el usuario
     * @param sal sal guardada del usuario
     * @param iteraciones iteraciones guardadas del usuario
     * @param hashGuardado valor guardado en Usuario.claveHash
     * @return true si la contraseña es la correcta
     */
    public static boolean coincide(char[] clave, byte[] sal, int iteraciones, byte[] hashGuardado) {
        return MessageDigest.isEqual(derivar(clave, sal, iteraciones), hashGuardado);
    }

    /**
     * Genera una sal nueva para un usuario nuevo o para un cambio de contraseña.
     *
     * @return 16 bytes aleatorios
     */
    public static byte[] nuevaSal() {
        byte[] sal = new byte[BYTES_SAL];
        ALEATORIO.nextBytes(sal);
        return sal;
    }
}
