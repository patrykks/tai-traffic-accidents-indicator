<%@page session="false"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Traffic Congestion Indicator</title>
<spring:url value="/resources/core/js/leaflet.js"
                var="leafletJs" />
<spring:url value="/resources/core/css/leaflet.css" var="leafletCss" />
    <script src="${leafletJs}"></script>
    <link href="${leafletCss}" rel="stylesheet" />
<c:url var="home" value="/" scope="request" />

<spring:url value="/resources/core/css/hello.css" var="coreCss" />
<spring:url value="/resources/core/css/bootstrap.min.css"
	var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />
<spring:url value="/resources/core/js/jquery.1.10.2.min.js"
	var="jqueryJs" />
<script src="${jqueryJs}"></script>
</head>

<nav class="navbar navbar-inverse">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">Traffic Congestion Indicator</a>
		</div>
	</div>
</nav>

<div class ="container">
<div id="main">
    <div class="content" id="content">

        <h1>Traffic Congestion Indicator</h1>
        <h2>Information about traffic incidents on map</h2>

    <div id="map" style="height: 800px; position: relative; padding: 0px; margin: 0 auto 0 auto;"></div>


    <hr/>
    Not works properly in Eclipse internal browser, use Mozilla Firefox instead ! | <a href="../index.html" ><span>Index</span></a>
    <hr/>
    <script>

    var map = L.map('map').setView([50.0, 15.5], 8);

    var mapLayer = L.tileLayer("http://otile{s}.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png", {
    subdomains: "1234",
    attribution: "&copy; <a href='http://www.openstreetmap.org/'>OpenStreetMap</a> and contributors, under an <a href='http://www.openstreetmap.org/copyright' title='ODbL'>open license</a>. Tiles Courtesy of <a href='http://www.mapquest.com/'>MapQuest</a> <img src='http://developer.mapquest.com/content/osm/mq_logo.png'>"
    }).addTo(map);



	$.ajax({
		type: "GET",
		dataType: "json",
		url: "http://localhost:8080/tai/map/accidents",
		success: function(data){
            $.each(data.resourceSets, function(k,v) {
                $.each(v.resources, function(k1,v1) {
                    addMarkertoMap(v1.point.coordinates, v1.description)
                })
            });
		}
	});


    function addMarkertoMap(coordinates, description) {
        var marker = L.marker(coordinates).addTo(map);
        var popup = L.popup()
                .setLatLng(coordinates)
                .setContent(description)
                .openOn(map);
        marker.bindPopup(popup).openPopup();
    }

    </script>

    </div> <!-- content -->
    </div>
    </div>

<div class="container">
	<footer>
		<p>
			&copy; <a href="http://www.mkyong.com">Mkyong.com</a> 2015
		</p>
	</footer>
</div>

</body>
</html>