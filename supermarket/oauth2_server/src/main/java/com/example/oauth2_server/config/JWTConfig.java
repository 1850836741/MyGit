package com.example.oauth2_server.config;

<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
<<<<<<< HEAD
=======
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

<<<<<<< HEAD
@Configuration
public class JWTConfig {

=======
import javax.servlet.http.HttpServletRequest;

@Configuration
public class JWTConfig {


>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("QAQ");
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices(){
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}
