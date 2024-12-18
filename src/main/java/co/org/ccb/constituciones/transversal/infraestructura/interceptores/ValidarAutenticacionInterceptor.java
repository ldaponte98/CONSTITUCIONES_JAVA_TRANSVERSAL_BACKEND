package co.org.ccb.constituciones.transversal.infraestructura.interceptores;

import co.org.ccb.constituciones.transversal.transversal.util.UtilidadesApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ValidarAutenticacionInterceptor implements HandlerInterceptor {
    @Value("${authorization.key-anonymous}")
    private String claveRutaAnonima;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UtilidadesApi.claveRutaAnonima = claveRutaAnonima;
        UtilidadesApi.initSession(request);
        return true; // true permite que la solicitud continúe, false la detiene
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // Se ejecuta después de que la respuesta ha sido enviada al cliente
        System.out.println("Respuesta enviada con estado: " + response.getStatus());
    }
}
