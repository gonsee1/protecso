<%-- 
    Document   : home
    Created on : 23-ene-2015, 17:24:04
    Author     : crodas
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="NO_CACHE" value="<%=new java.util.Date().getTime()%>"/>
<c:set var="NO_CACHE" value="?t=${NO_CACHE}"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="UTF-8"/>
        <title>JSP Page</title>
        <script src="<c:url value="/resources/sencha/framework/ext-all.js${NO_CACHE}"/>" type="text/javascript"></script> 
        <script type="text/javascript">
            Ext.Loader.setPath('Ext.ux', 'resources/sencha/framework/Ext/ux');
        </script>
        <link type="text/css" href="<c:url value="resources/sencha/framework/Ext/ux/grid/css/GridFilters.css${NO_CACHE}"/>" rel="stylesheet"/>
        <link type="text/css" href="<c:url value="resources/sencha/framework/Ext/ux/grid/css/RangeMenu.css${NO_CACHE}"/>" rel="stylesheet"/>
            
        <script src="<c:url value="/resources/jss/sprintf.js${NO_CACHE}"/>" type="text/javascript"></script>
            
        <!-- <script src="<c:url value="/resources/sencha/framework/ux/form/MultiSelect.js${NO_CACHE}"/>" type="text/javascript"></script>-->          
        <script src="<c:url value="/resources/sencha/framework/themes/ext-theme-neptune.js${NO_CACHE}"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/sencha/framework/src/ux/Desktop.js${NO_CACHE}"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/sencha/framework/ext-charts.js${NO_CACHE}"/>" type="text/javascript"></script>        
        <script src="<c:url value="/resources/sencha/framework/ext-locale/build/ext-locale-es.js${NO_CACHE}"/>" type="text/javascript"></script> 
        
        <!--config 1-->        
        <script src="<c:url value="/resources/sencha/facturacion/config/app.js${NO_CACHE}"/>" type="text/javascript"></script> 
         
        <!--config 2-->
        <script src="<c:url value="/resources/sencha/facturacion/config/ux.desktop.App.js${NO_CACHE}"/>" type="text/javascript"></script>      
        <script src="<c:url value="/resources/sencha/facturacion/config/Desktop.App.js${NO_CACHE}"/>" type="text/javascript"></script> 
        <script src="<c:url value="/resources/sencha/facturacion/config/StartMenu.js${NO_CACHE}"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/sencha/facturacion/config/TaskBar.js${NO_CACHE}"/>" type="text/javascript"></script> 
        <script src="<c:url value="/resources/sencha/facturacion/config/Wallpaper.js${NO_CACHE}"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/sencha/facturacion/config/Settings.js${NO_CACHE}"/>" type="text/javascript"></script> 
        <!--widgets--> 
        <!--fncs--> 
        <script src="<c:url value="/resources/sencha/facturacion/fnc.js${NO_CACHE}"/>" type="text/javascript"></script>  
        <script src="<c:url value="/resources/sencha/facturacion/toExcel.js${NO_CACHE}"/>" type="text/javascript"></script>  
        
        
        <link type="text/css" href="<c:url value="/resources/sencha/framework/src/ux/Desktop-all.css${NO_CACHE}"/>" rel="stylesheet"/>
        <link type="text/css" href="<c:url value="/resources/sencha/framework/themes/KitchenSink-all.css${NO_CACHE}"/>" rel="stylesheet"/> 
        <!--<link type="text/css" href="<c:url value="/resources/sencha/framework/themes/ext-theme-neptune-all.css${NO_CACHE}"/>" rel="stylesheet"/>-->
        <link type="text/css" href="<c:url value="/resources/css/style.css${NO_CACHE}"/>" rel="stylesheet"/>         
        <link type="text/css" href="<c:url value="/resources/css/my-icons.css${NO_CACHE}"/>" rel="stylesheet"/>
        <style type="text/css">
            <c:forEach var="icons" items="${settings__var__list_icons}">
                .css_icon_sys__${icons[0]}{background-image:url(${icons[1]}) !important;}
            </c:forEach>            
        </style>
        <script type="text/javascript">
            var settings= {
                AJAX_URI:'./ajax/',
                ICONS_SYS:{<c:forEach var="icons" items="${settings__var__list_icons}">'${icons[0]}':{uri:'${icons[1]}',name:'${icons[2]}',cssName:'css_icon_sys__${icons[0]}'},</c:forEach>},                
                formats:{
                    date:"${settings__format__date}",
                    date_time:"${settings__format__date_time}",
                },
                online:{
                    nombre:'${settings__user_online.getNO_USUA_ForJSON()}',
                    apellido:'${settings__user_online.getAP_USUA_ForJSON()}',
                    correo:'${settings__user_online.getDE_CORR_ForJSON()}',
                    perfil:'${settings__user_online.getPERF_ForJSON().getNO_PERF_ForJSON()}',
                }                
            };
            
            var eventoControlado = false;
            window.onload = function() {
            document.onkeypress = mostrarInformacionCaracter;
            document.onkeyup = mostrarInformacionCaracter; }
            function mostrarInformacionCaracter(evObject) {
            var id = document.activeElement.id;
            document.getElementById("" + id).value = document.getElementById("" + id).value.toUpperCase();
            };            
            
        </script>
        
        <!--
        <c:forEach var="group" items="${settings__access__groups}">
            <script src="<c:url value="/resources/sencha/facturacion/models/${group.key.DE_PACK_ForJSON}.js${NO_CACHE}"/>" type="text/javascript"></script>
        </c:forEach>
        <c:forEach var="group" items="${settings__access__groups}">
            <script src="<c:url value="/resources/sencha/facturacion/stores/${group.key.DE_PACK_ForJSON}.js${NO_CACHE}"/>" type="text/javascript"></script>
        </c:forEach>        
        -->        
        <!--panels-->
        <c:forEach var="panel" items="${settings__access__panels}">   
            <c:if test="${panel.CO_ITEM_MODU_ForJSON >= 0}">
            <c:set var="path_uri_panel" value="${panel.DE_PACK_ForJSON}"/>
            <c:set value="/resources/sencha/facturacion/panels/${fn:replace(path_uri_panel,'.','/')}.js${NO_CACHE}" var="path_uri"/>
            <c:if test="${panel.CO_ITEM_MODU_ForJSON > 0 }">
                <script src="<c:url value="${path_uri}"/>" type="text/javascript" panel="${panel.CO_ITEM_MODU_ForJSON}"></script> 
            </c:if>
            </c:if>
        </c:forEach>        
        <script type="text/javascript">            
            settings.Permissions={
                varSendPackage:'${settings__var__send__permission__package}',
            <c:forEach var="group" items="${settings__access__groups}">
                'Desktop.Facturacion.Windows.${group.key.DE_PACK_ForJSON}':{
                    id:"idWin_${group.key.CO_MODU_ForJSON}",
                    title:"${group.key.NO_MODU_ForJSON}",
                    package:"Desktop.Facturacion.Windows.${group.key.DE_PACK_ForJSON}",
                    iconCls:'${group.key.DE_ICON_CLSS_ForJSON}',
                    items:[
                        <c:forEach var="item" items="${group.value}">
                            {
                                iconCls:'fac_icon_add',
                                package:"Desktop.Facturacion.Panels.${item.DE_PACK_ForJSON}",
                                title:"${item.NO_ITEM_MODU_ForJSON}",
                                items:[{xtype:'Desktop.Facturacion.Panels.${item.DE_PACK_ForJSON}'}],
                            },
                        </c:forEach>
                    ]
                },
            </c:forEach>
            };
        </script>    
        
        <!--windows-->
        <c:forEach var="window" items="${settings__access__windows}">
            <c:set value="/resources/sencha/facturacion/windows/${window.DE_PACK_ForJSON}.js${NO_CACHE}" var="path_uri"/>
            <script src="<c:url value="${path_uri}"/>" type="text/javascript"></script> 
        </c:forEach>
        
        <!--<script src="<c:url value="/resources/sencha/facturacion/config/NotePad.js${NO_CACHE}"/>" type="text/javascript"></script>-->
        <!-- run -->
        <script src="<c:url value="/resources/sencha/facturacion/config/run.js${NO_CACHE}"/>" type="text/javascript"></script>
        <script src="<c:url value="/resources/jss/settings.js${NO_CACHE}"/>" type="text/javascript"></script>
    </head>
    <body>
        
    </body>
</html>
