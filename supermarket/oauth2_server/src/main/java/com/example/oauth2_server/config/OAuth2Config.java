package com.example.oauth2_server.config;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
=======

import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
<<<<<<< HEAD
=======
import org.springframework.security.oauth2.provider.ClientDetailsService;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
<<<<<<< HEAD
=======

>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import java.util.Arrays;

@Configuration
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    TokenStore tokenStore;

<<<<<<< HEAD

=======
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
    @Autowired
    DefaultTokenServices tokenServices;

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
<<<<<<< HEAD

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
=======
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ClientDetailsService clientDetailsService;

>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
<<<<<<< HEAD
                .secret(bCryptPasswordEncoder().encode("secret"))
                .scopes("app")
                .authorizedGrantTypes("refresh_token","authorization_code")
                .redirectUris("http://localhost:8081/customer-server/getToken.html");
=======
                .secret(bCryptPasswordEncoder.encode("secret"))
                .scopes("app")
                .authorizedGrantTypes("refresh_token","authorization_code")
                .redirectUris("http://localhost:8090/find");
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
<<<<<<< HEAD
        security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
=======
        security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").checkTokenAccess("isAuthenticated()");
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
<<<<<<< HEAD
        endpoints.tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .userDetailsService(userDetailsService);
    }
=======
        endpoints
                .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .userDetailsService(userDetailsService);
    }


>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
}
