package es.workast.core.security;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.User;

/**
 * @author Nicolás Cornaglia
 */
public class SecurityUser extends User {

    private static final long serialVersionUID = 1L;
    private Long id;

    public SecurityUser(Long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
            boolean accountNonLocked, GrantedAuthority[] authorities) throws IllegalArgumentException {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
