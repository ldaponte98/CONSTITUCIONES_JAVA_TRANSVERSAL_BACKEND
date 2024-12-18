package co.org.ccb.constituciones.transversal.transversal.util;

import co.org.ccb.constituciones.transversal.errores.entrada.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilidadesApiTest {
    @Mock
    private HttpServletRequest request;

    private String validJwtToken;
    private String expiredJwtToken;

    @BeforeEach
    void setUp() {
        // Crear un token JWT válido (exp: 1 hora en el futuro)
        long futureTime = System.currentTimeMillis() / 1000 + 3600; // Una hora en el futuro
        String validPayload = String.format("{\"sub\":\"testuser\",\"exp\":%d}", futureTime);
        validJwtToken = createJwtToken(validPayload);

        // Crear un token JWT expirado (exp: 1 hora en el pasado)
        long pastTime = System.currentTimeMillis() / 1000 - 3600; // Una hora en el pasado
        String expiredPayload = String.format("{\"sub\":\"testuser\",\"exp\":%d}", pastTime);
        expiredJwtToken = createJwtToken(expiredPayload);

        // Reset session antes de cada prueba
        UtilidadesApi.session = null;
        UtilidadesApi.claveRutaAnonima = "clave-secreta";
    }

    private String createJwtToken(String payload) {
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String header = encoder.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String encodedPayload = encoder.encodeToString(payload.getBytes());
        String signature = encoder.encodeToString("dummy-signature".getBytes());
        return String.format("%s.%s.%s", header, encodedPayload, signature);
    }

    @Test
    void initSession_DebeCrearSesion_CuandoTokenEsValido() {
        // Arrange
        when(request.getHeader("access-key")).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwtToken);
        when(request.getRequestURI()).thenReturn("/api/test");

        // Act
        UtilidadesApi.initSession(request);

        // Assert
        assertNotNull(UtilidadesApi.session);
        assertEquals("testuser", UtilidadesApi.session.getUsuario());
        assertEquals("/api/test", UtilidadesApi.session.getService());
        assertEquals(validJwtToken, UtilidadesApi.session.getToken());
    }

    @Test
    void initSession_DebeLanzarUnauthorizedException_CuandoTokenEstaExpirado() {
        // Arrange
        when(request.getHeader("access-key")).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredJwtToken);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> UtilidadesApi.initSession(request));
    }

    @Test
    void initSession_DebeCrearSesionAnonima_CuandoAccessKeyEsValida() {
        // Arrange
        when(request.getHeader("access-key")).thenReturn("clave-secreta");
        when(request.getRequestURI()).thenReturn("/api/public");

        // Act
        UtilidadesApi.initSession(request);

        // Assert
        assertNotNull(UtilidadesApi.session);
        assertEquals("anonimo", UtilidadesApi.session.getUsuario());
        assertEquals("/api/public", UtilidadesApi.session.getService());
        assertTrue(UtilidadesApi.session.getToken().isEmpty());
    }

    @Test
    void initSession_DebeLanzarUnauthorizedException_CuandoAccessKeyEsInvalida() {
        // Arrange
        when(request.getHeader("access-key")).thenReturn("clave-incorrecta");

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> UtilidadesApi.initSession(request));
    }

    @Test
    void initSession_DebeLanzarUnauthorizedException_CuandoNoHayTokenNiAccessKey() {
        // Arrange
        when(request.getHeader("access-key")).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> UtilidadesApi.initSession(request));
    }

    @Test
    void decodificarJwt_DebeDecodificarPayload_CuandoTokenEsValido() {
        // Act
        Map<String, Object> resultado = UtilidadesApi.decodificarJwt(validJwtToken);

        // Assert
        assertNotNull(resultado);
        assertEquals("testuser", resultado.get("sub"));
        assertNotNull(resultado.get("exp"));
    }

    @Test
    void decodificarJwt_DebeLanzarUnauthorizedException_CuandoTokenEsInvalido() {
        // Arrange
        String tokenInvalido = "invalid.token.format";

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> UtilidadesApi.decodificarJwt(tokenInvalido));
    }

    @Test
    void initSession_DebeLanzarUnauthorizedException_CuandoFormatoTokenEsInvalido() {
        // Arrange
        when(request.getHeader("access-key")).thenReturn(null);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> UtilidadesApi.initSession(request));
    }
}