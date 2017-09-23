<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <!--[if lte IE 8]><script src="<c:url value="/resources/assets/js/ie/html5shiv.js" />"></script><![endif]-->
    <link href="<c:url value="/resources/assets/css/main.css" />" rel="stylesheet"/>
    <!--[if lte IE 9]><link href="<c:url value="/resources/assets/css/ie9.css" />" rel="stylesheet"/><![endif]-->
    <!--[if lte IE 9]><link href="<c:url value="/resources/assets/css/ie8.css" />" rel="stylesheet"/><![endif]-->
    <title>404 ERROR</title>
</head>
    <body class="landing">
        <div id="page-wrapper">
            <!-- Banner -->
                <section id="banner">
                    <div class="content">
                        <header>
                            <h2><a href="https://en.wikipedia.org/wiki/HTTP_404" style="color:#e44c65">404 ERROR!</a></h2>
                            <p>This Page Not Available<br />
                            ohh...Your Requested the page that is no longer There</p>
                        </header>
                        <span class="image"><img src="<c:url value="/resources/images/404.jpg"/>" alt=""  /></span>
                    </div>
                    
                </section>
        </div>
         <!-- Scripts -->
            <script src="<c:url value="/resources/assets/js/jquery.min.js" />"></script>
            <script src="<c:url value="/resources/assets/js/jquery.scrolly.min.js" />"></script>
            <script src="<c:url value="/resources/assets/js/jquery.dropotron.min.js" />"></script>
            <script src="<c:url value="/resources/assets/js/jquery.scrollex.min.js" />"></script>
            <script src="<c:url value="/resources/assets/js/skel.min.js" />"></script>
            <script src="<c:url value="/resources/assets/js/util.js" />"></script>
         
            <!--[if lte IE 8]><script src="<c:url value="/resources/assets/js/ie/respond.min.js" />"></script><![endif]-->
            <script src="<c:url value="/resources/assets/js/main.js" />"></script>

    </body>
</html>