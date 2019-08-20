<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="inc/meta.jsp" %>
</head>
<body ng-app="app">
<div class="app" ng-class="{'app-header-fixed':app.settings.headerFixed, 'app-aside-top':app.settings.asideTop}" ui-view ng-controller="MainController" id="MainController"></div>
<!--<div ui-view="header_menu"></div>-->
<%@ include file="inc/header.jsp" %>
<%@ include file="inc/common.jsp" %>
</body>
</html>