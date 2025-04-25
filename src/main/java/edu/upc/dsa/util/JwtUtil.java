package edu.upc.dsa.util;

import edu.upc.dsa.WebManagerImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "{abiN£QUzd%,D#-l31'1";
    private static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    // Crear token JWT
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    // Validar token JWT
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Muestra el error para facilitar debugging
            return false;
        }
    }

    // Obtener el nombre de usuario desde el token
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public static String getEmailFromToken(String token) {
        String username = getUsernameFromToken(token);
        WebManagerImpl wm = WebManagerImpl.getInstance();
        // Obtener el correo de usuario usando el nombre de usuario
        return wm.getCorreo(username); // Devuelve el correo del usuario
    }
}
