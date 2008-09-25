package es.workast.core.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.SpringSecurityMessageSource;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * @author Nicolás Cornaglia
 */
public class UserDetailsServiceImpl extends JdbcDaoSupport implements UserDetailsService {

    //~ Static fields/initializers =====================================================================================

    public static final String DEF_USERS_BY_USERNAME_QUERY = "SELECT id, email, password, true FROM Person WHERE email=?";
    public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "SELECT p.email, a.authority FROM Person p, Authority a WHERE p.id = a.personId AND p.email=?";

    //~ Instance fields ================================================================================================

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private MappingSqlQuery authoritiesByUsernameMapping;
    private MappingSqlQuery usersByUsernameMapping;

    private String rolePrefix = "";

    //~ Methods ========================================================================================================

    @Override
    protected void initDao() throws ApplicationContextException {
        initMappingSqlQueries();
    }

    /**
     * Extension point to allow other MappingSqlQuery objects to be substituted in a subclass
     */
    private void initMappingSqlQueries() {
        this.usersByUsernameMapping = new UsersByUsernameMapping(getDataSource());
        this.authoritiesByUsernameMapping = new AuthoritiesByUsernameMapping(getDataSource());
    }

    @SuppressWarnings("unchecked")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        List users = usersByUsernameMapping.execute(username);

        if (users.size() == 0) {
            throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.notFound", new Object[] { username }, "Username {0} not found"), username);
        }

        SecurityUser user = (SecurityUser) users.get(0); // contains no GrantedAuthority[]

        Set dbAuthsSet = new HashSet();
        dbAuthsSet.addAll(authoritiesByUsernameMapping.execute(user.getUsername()));

        dbAuthsSet.add(new GrantedAuthorityImpl("ROLE_ANONYMOUS"));
        dbAuthsSet.add(new GrantedAuthorityImpl("ROLE_USER"));

        List dbAuths = new ArrayList(dbAuthsSet);
        if (dbAuths.size() == 0) {
            throw new UsernameNotFoundException(messages.getMessage("JdbcDaoImpl.noAuthority", new Object[] { username }, "User {0} has no GrantedAuthority"), username);
        }
        GrantedAuthority[] arrayAuths = (GrantedAuthority[]) dbAuths.toArray(new GrantedAuthority[dbAuths.size()]);

        return new SecurityUser(user.getId(), user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, arrayAuths);
    }

    /**
     * Allows a default role prefix to be specified. If this is set to a non-empty value, then it is
     * automatically prepended to any roles read in from the db. This may for example be used to add the
     * <tt>ROLE_</tt> prefix expected to exist in role names (by default) by some other Spring Security
     * classes, in the case that the prefix is not already present in the db.
     *
     * @param rolePrefix the new prefix
     */
    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    protected String getRolePrefix() {
        return rolePrefix;
    }

    //~ Inner Classes ==================================================================================================

    /**
     * Query object to look up a user's authorities.
     */
    private class AuthoritiesByUsernameMapping extends MappingSqlQuery {

        protected AuthoritiesByUsernameMapping(DataSource ds) {
            super(ds, DEF_AUTHORITIES_BY_USERNAME_QUERY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        @Override
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            String roleName = rolePrefix + rs.getString(2);
            GrantedAuthorityImpl authority = new GrantedAuthorityImpl(roleName);

            return authority;
        }
    }

    /**
     * Query object to look up a user.
     */
    private class UsersByUsernameMapping extends MappingSqlQuery {

        protected UsersByUsernameMapping(DataSource ds) {
            super(ds, DEF_USERS_BY_USERNAME_QUERY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        @Override
        protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
            Long id = rs.getLong(1);
            String username = rs.getString(2);
            String password = rs.getString(3);
            boolean enabled = rs.getBoolean(4);
            SecurityUser user = new SecurityUser(id, username, password, enabled, true, true, true, new GrantedAuthority[] { new GrantedAuthorityImpl("HOLDER") });

            return user;
        }
    }
}
