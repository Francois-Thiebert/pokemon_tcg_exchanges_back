package pokemonTcgExchanges.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UserIpFilter> loggingFilter() {
        FilterRegistrationBean<UserIpFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserIpFilter());
        registrationBean.addUrlPatterns("/api/*");  // Appliquer le filtre uniquement aux URL commençant par /api/ (tu peux ajuster cette URL)
        return registrationBean;
    }
}

