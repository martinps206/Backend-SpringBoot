package com.proyectoDH.config;



import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.GET, "/consultaproductosNoReservados/{codiUsuario}", "/favoritos/{userId}", "/generos", "/images/{imageCode}", "/productosReservadosUsiario/{codigoProducto}", "/images/{imageName}", "/productosReservados", "/producto/{codigo}", "/categorias", "/productosNoReservados").permitAll()
                        .requestMatchers(HttpMethod.POST, "/productosNoReservados-alta", "/agregardata", "/agregarfavoritos", "/agregar-con-imagen", "/agregarimagen", "/buscarpor", "/login", "/{codigo}", "/register", "/productos", "/crear", "/{id}/login", "/postCategorias", "/agregar", "/agregarTalle", "/users", "/talles", "/colores", "/caracteristicas", "/agregarcaracteristica", "/categorias", "/register-user").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/modificarproducto/{codigo}", "/caracteristicas/{codigo}", "/{userId}/admin", "/categorias/{codigo}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/productosNoReservados-baja/{idReserva}", "/eliminarfavoritos", "/eliminar/{id}", "/caracteristicas/{codigo}", "/categoria/{codigo}").permitAll()
                        .anyRequest().authenticated());



        return http.build();
    }

}
