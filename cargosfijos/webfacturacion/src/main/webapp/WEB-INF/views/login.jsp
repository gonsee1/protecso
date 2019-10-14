<%-- 
    Document   : login.jsp
    Created on : 26-ene-2015, 9:55:14
    Author     : crodas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<script src="resources/js/jquery-3.2.1.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="jumbotron d-flex align-items-center">
		<div class="container">
			<div class="row" >
				<div class="col-md-4"></div>
				<div class="col-md-4">
					<form method="POST" action=".">
						<br/>
						<br/>
						<img src="resources/sencha/facturacion/images/logoAmericatelLogin.jpg" class="img-fluid rounded mx-auto d-block" alt="" />
						<br/> 
						<input type="text" placeholder="Usuario" name="txt_usuario"
							value="${usuario}" class="form-control input-md" />
						<input type="password" placeholder="Clave" name="txt_clave" 
							value="${clave}" class="form-control input-md">
						<br/> 
						<button class="btn btn-md btn-primary btn-block"><span class="glyphicon glyphicon-log-in">Login</span></button>
						<br/>
						<br/>
					</form>
	    		</div>
				<div class="col-md-4"></div>
			</div>
		</div>
	</div>
</body>
</html>
