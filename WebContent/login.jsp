<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="org.springframework.security.ui.AbstractProcessingFilter"%>
<%@ page import="org.springframework.security.ui.webapp.AuthenticationProcessingFilter"%>
<%@ page import="org.springframework.security.AuthenticationException"%>
<html>
	<head>
	    <link rel="stylesheet" type="text/css" href="js/extjs/resources/css/ext-all.css" />
	    <link rel="stylesheet" type="text/css" href="js/extjs/resources/css/xtheme-gray.css" />
	    <script type="text/javascript" src="js/extjs/adapter/ext/ext-base.js"></script>
	    <script type="text/javascript" src="js/extjs/ext-all.js"></script>
	</head>
	<style>
		body {
			background: white url("img/loginBackground.jpg");
	        margin : 0px;
	        padding : 0px;
	        font-family : arial;
	
		}
	    h1 {
	        background : white;
	        border-bottom : 1px solid #ccc;
	        padding : 5px;
			color : #666;
	    }
	</style>
	<body>
		<h1><img src="img/loginTitle.png" />beta</h1>
		
		<script>
		
		//fwk_login_page_token
		
		Ext.onReady(function(){
			
			var error = new Ext.form.Label({
				text : 'error'
			});
			
			
			//vamos a crear una clase bot�n con la funci�n de env�o que queremos. Y de
			// aqu� crearemos el text de usuario y el de password.
			//lo hacemos as� tan s�lo para probar esta forma de crear controles
			var loginField = Ext.extend(Ext.form.TextField, {
				initComponent : function(){
					Ext.apply(this,{
						allowBlank : false
						,enableKeyEvents : true
						,listeners : {keypress : this.onEnter, focus:function(){alert(22);} }
					});
					
					loginField.superclass.initComponent.apply(this, arguments);
				}
				,onKeyPress : function(e){
					if(e.getKey() == e.ENTER) this.ownerCt.getForm().submit();
				}
			});
			
			//ahora los 2 botones heredados de loginField
			var usuario = new loginField({ 
					fieldLabel : 'Nombre'
					,name : 'j_username'
					,value : 'ncornag'
			});
			
			var password = new loginField({ 
				fieldLabel : 'Contrase&ntilde;a'
				,name : 'j_password'
				,inputType: 'password'
				,value : 'ncornag'
			});
			
				
			var validar = new Ext.Button({
				text : 'Validar'
				,handler : function(target, e){
					this.ownerCt.getForm().submit();
				}
			});
			
			var loginForm = new Ext.form.FormPanel({
				url : '<c:url value='j_spring_security_check' />'
				,defaults : { }
				,items : [
					<c:if test="${not empty param.login_error}">
					error,
					</c:if>
					usuario
					,password
					,validar
				]
				,bodyStyle : "padding:10px"
				,standardSubmit : true
			});
			
			var window = new Ext.Window({
				title : 'Control de acceso a la aplicaci&oacute;n'
				,width : 350
				,autoHeight : true
				,items : [
					loginForm
				]
				//,modal : true
			});
			
			//window.on('afterlayout', function(){alert(1); usuario.focus(); });
			
			window.show();
			
			usuario.focus(false,10); //esperamos 10ms a que se pinte la pantalla
			
			
			//corrige el error de ext-js que al usar standardSubmit
			loginForm.getForm().getEl().dom.action=loginForm.getForm().url;
			
		});
		</script>
		
		
	</body>
</html>
