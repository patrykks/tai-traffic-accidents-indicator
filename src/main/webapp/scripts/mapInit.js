function init() {
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
            $.each(data, function(k,v) {
                addMarkertoMap(v.point.coordinates, v.description)
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
}