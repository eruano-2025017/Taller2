package com.estebanruano.kinalapp.config;

import com.estebanruano.kinalapp.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Recursos públicos
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/vista/login", "/vista/register").permitAll()

                        // CREAR – para ADMIN y USER (excepto usuarios, que queda solo ADMIN)
                        .requestMatchers(
                                "/vista/clientes/nuevo",
                                "/vista/productos/nuevo",
                                "/vista/ventas/nuevo",
                                "/vista/detalleVentas/nuevo"
                        ).hasAnyRole("ADMIN", "USER")

                        // Solo ADMIN puede crear usuarios
                        .requestMatchers("/vista/usuarios/nuevo").hasRole("ADMIN")

                        // Solo ADMIN puede editar o eliminar
                        .requestMatchers(
                                "/vista/clientes/editar/**",
                                "/vista/clientes/eliminar/**",
                                "/vista/productos/editar/**",
                                "/vista/productos/eliminar/**",
                                "/vista/usuarios/editar/**",
                                "/vista/usuarios/eliminar/**",
                                "/vista/ventas/editar/**",
                                "/vista/ventas/eliminar/**",
                                "/vista/detalleVentas/editar/**",
                                "/vista/detalleVentas/eliminar/**"
                        ).hasRole("ADMIN")

                        // Ver y listar para todos los autenticados
                        .requestMatchers(
                                "/vista/clientes",
                                "/vista/clientes/ver/**",
                                "/vista/productos",
                                "/vista/productos/ver/**",
                                "/vista/usuarios",
                                "/vista/usuarios/ver/**",
                                "/vista/ventas",
                                "/vista/ventas/ver/**",
                                "/vista/detalleVentas",
                                "/vista/detalleVentas/ver/**"
                        ).hasAnyRole("ADMIN", "USER")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/vista/login")
                        .loginProcessingUrl("/vista/login")
                        .defaultSuccessUrl("/vista/", true)
                        .failureUrl("/vista/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/vista/logout")
                        .logoutSuccessUrl("/vista/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}