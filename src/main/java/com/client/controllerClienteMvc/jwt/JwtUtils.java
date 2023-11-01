package com.client.controllerClienteMvc.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtUtils {

    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    private static final String SECRET_KEY = "9a6d4f12b8e3567c45a3b0d87e1f9cda";
    private static final long EXPIRE_DAYS = 0;
    private static final long EXPIRE_HOURS = 0;
    private static final long EXPIRE_MINUTES = 30;

    private JwtUtils() {
    }

    private static Key generateKey() {
        // essa classes Keys faz parte da biblioteca do io.jwttoken - esse método é responsavel para preparar nossa chave para ser criptografada no momento que for gerado o token
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // processo para calcular a data expiração do token
    private static Date toExpiredDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Processo que gera o token
    public static JwtToken createToken(String username, String role) {
        Date issuedAt = new Date(); // gerando data de criação do token
        Date limit = toExpiredDate(issuedAt);

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT") // informa que é um token do tipo JWT
                .setSubject(username)
                .setIssuedAt(issuedAt) // método que recebe a data de geração do token
                .setExpiration(limit)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .claim("role", role) // O claim é usado quando queremos add alguma informação no token que nao exista métod pronto, como as permissões de usuário
                // agora nos precisamos transformar esse objeto em um token com formato string que tenha padrao com base 64 separado pelos pontos
                .compact();
        return new JwtToken(token);
    }

    // Pegando nosso token verificado
    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()// eu preciso passar a chave para que o metodo verifique a assinatura que ele recebeu do token que o cliente enviou confere com a nos geramos
                    .parseClaimsJws(refactorToken(token)).getBody(); // recuperando corpo do token
        } catch (JwtException ex) {
            System.out.println(String.format("Token inválido %s", ex.getMessage()));
        }
        return null; // caso gere a exceção retornamos null
    }

    // Removendo a incial BEARER do token
    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

    // método para recuperar UserName dentro do token
    public static String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject(); // nosso getClaimsFromToken com criamos antes retorna o corpo do nosso token
    }

    //Esse método vai ser usado para testar a válidade do token
    // se ocorrer alguma exeção no .parseClaimsJws(refactorToken(token)); significa que nosso token enviado pelo usuário contem algum erro logo retorna false  por ser invalido
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token));
            return true;
        } catch (JwtException ex) {
            System.out.println(String.format("Token inválido %s", ex.getMessage()));
        }
        return false;
    }
}

