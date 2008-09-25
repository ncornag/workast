package es.workast.utils;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/**
 * Filtro para forzar el Locale de las llamadar REST a lo que haya guardado en una cookie
 * 
 * @author Nicolás Cornaglia
 */
public class LocaleFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Locale actualLocale = LocaleContextHolder.getLocale();
        Cookie cookie = WebUtils.getCookie(request, LocaleUtils.DEFAULT_COOKIE_NAME);
        if (cookie != null) {
            Locale locale = StringUtils.parseLocaleString(cookie.getValue());
            if (locale != null && !locale.equals(actualLocale)) {
                LocaleContextHolder.setLocale(locale, true);
            }
        }

        filterChain.doFilter(request, response);
    }

}
