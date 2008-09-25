package es.workast.utils;

import org.springframework.web.util.HtmlUtils;

/**
 * TODO Documentar
 * 
 * @author Nicolás Cornaglia
 */
public class StringUtils {

    /**
     * Hidden constructor
     */
    private StringUtils() {
    }

    public static String escape(String input) {

        //        HTML2WikiConverter conv = new HTML2WikiConverter();
        //        conv.setInputHTML(input);
        //        String result = conv.toWiki(new ToWikipedia());

        String result = HtmlUtils.htmlEscape(input);
        //result = result.replaceAll("(\r\n|\n\r|\n|\r)", "<br/>");

        return result;
    }

    public static String getParameterValue(String paramName, String headers) {
        String result = null;
        String[] params = headers.split(";");
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            if (param[0].trim().equals("filename")) {
                result = param[1].trim();
                if (result.startsWith("\"")) {
                    result = result.substring(1);
                }
                break;
            }
        }
        return result;
    }

}
