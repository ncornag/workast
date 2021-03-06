package es.workast.utils;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;

/** 
 * Clase que agrupa funcionalidad relativa a la seguridad
 * 
 * @author amarinso
 */
public class SecurityUtils {

    /**
     * Hidden constructor
     */
    private SecurityUtils() {
    }

    /** 
     * Obtiene el usuario logado
     * 
     * @return
     */
    public static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) (authentication == null ? null : authentication.getPrincipal());
    }

}
