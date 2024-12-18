package co.org.ccb.constituciones.transversal.transversal.util;

import co.org.ccb.constituciones.transversal.errores.entrada.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilidadesApiTest {

    @Mock
    private HttpServletRequest request;

    private String validToken;
    private String expiredToken;

    @BeforeEach
    void setUp() {
        // Crear un token válido (expira en 1 hora desde ahora)
        long validExpiration = ZonedDateTime.now().plusHours(1).toEpochSecond();
        validToken = createJwtToken("usuario_test", validExpiration);

        // Crear un token expirado (expiró hace 1 hora)
        long expiredExpiration = ZonedDateTime.now().minusHours(1).toEpochSecond();
        expiredToken = createJwtToken("usuario_test", expiredExpiration);
    }

    @Test
    void initSession_conTokenValido_deberiaIniciarSesion() {
        // Arrange
        String authHeader = "Bearer " + validToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(request.getRequestURI()).thenReturn("/api/test");

        // Act
        UtilidadesApi.initSession(request);

        // Assert
        assertNotNull(UtilidadesApi.session);
        assertEquals("/api/test", UtilidadesApi.session.getService());
        assertEquals("usuario_test", UtilidadesApi.session.getUsuario());
        assertEquals(validToken, UtilidadesApi.session.getToken());
    }

    @Test
    void initSession_sinHeader_deberiLanzarUnauthorizedException() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () ->
                UtilidadesApi.initSession(request)
        );
    }

    @Test
    void initSession_conTokenExpirado_deberiaLanzarUnauthorizedException() {
        // Arrange
        String authHeader = "Bearer " + expiredToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () ->
                UtilidadesApi.initSession(request)
        );
    }

    @Test
    void initSession_conTokenInvalido_deberiaLanzarUnauthorizedException() {
        // Arrange
        String authHeader = "Bearer invalid.token.format";
        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () ->
                UtilidadesApi.initSession(request)
        );
    }

    @Test
    void decodificarJwt_conTokenValido_deberiaRetornarPayload() {
        // Act
        Map<String, Object> payload = UtilidadesApi.decodificarJwt(validToken);

        // Assert
        assertNotNull(payload);
        assertEquals("usuario_test", payload.get("user_name"));
        assertNotNull(payload.get("exp"));
    }

    @Test
    void decodificarJwt_conTokenInvalido_deberiaLanzarUnauthorizedException() {
        // Act & Assert
        assertThrows(UnauthorizedException.class, () ->
                UtilidadesApi.decodificarJwt("invalid.token")
        );
    }

    // Método auxiliar para crear tokens JWT de prueba
    private String createJwtToken(String username, long expiration) {
        // Crear un token JWT simple para pruebas
        String header = Base64.getUrlEncoder().encodeToString(
                "{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes()
        );
        String payload = Base64.getUrlEncoder().encodeToString(
                String.format("{\"user_name\":\"%s\",\"exp\":%d}", username, expiration).getBytes()
        );
        String signature = Base64.getUrlEncoder().encodeToString("test-signature".getBytes());

        return String.format("%s.%s.%s", header, payload, signature);
    }
}