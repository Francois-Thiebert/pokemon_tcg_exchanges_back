package pokemonTcgExchanges.configuration;

import org.slf4j.MDC;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class UserIpFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Récupérer l'adresse IP de l'utilisateur
        String ipAddress = request.getRemoteAddr();

        // Ajouter l'adresse IP au MDC
        MDC.put("ipAddress", ipAddress);

        try {
            // Continuer avec la chaîne de filtres
            chain.doFilter(request, response);
        } finally {
            // Supprimer l'adresse IP du MDC après la requête pour éviter toute fuite de mémoire
            MDC.remove("ipAddress");
        }
    }

    @Override
    public void destroy() {
    }
}

