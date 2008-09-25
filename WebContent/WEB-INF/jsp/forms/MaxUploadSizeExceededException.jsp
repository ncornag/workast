<%
Exception ex = (Exception) request.getAttribute("exception");
String error = "{response:{status:'ERROR',errors:'" + ex.getCause().getMessage() + "'}}";
out.print(error);
%>
