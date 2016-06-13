function init_map(admin) {
    var map = L.map('map').setView([50.0, 15.5], 8);

    var mapLayer = L.tileLayer("http://otile{s}.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png", {
        subdomains: "1234",
        attribution: "&copy; <a href='http://www.openstreetmap.org/'>OpenStreetMap</a> and contributors, under an <a href='http://www.openstreetmap.org/copyright' title='ODbL'>open license</a>. Tiles Courtesy of <a href='http://www.mapquest.com/'>MapQuest</a> <img src='http://developer.mapquest.com/content/osm/mq_logo.png'>"
    }).addTo(map);

    var severity;
    $.ajax({
        url: "http://localhost:8080/tai/enums/severity",
        type: "GET",
        success: function (response) {
            severity=response;
        }
    });

    var type;
    $.ajax({
        url: "http://localhost:8080/tai/enums/type",
        type: "GET",
        success: function (response) {
            type=response;
        }
    });

    var clusterGroup = new L.markerClusterGroup({disableClusteringAtZoom: 12});
    clusterGroup._getExpandedVisibleBounds = function () {return map.getBounds();};
    map.addLayer(clusterGroup);
    map.on('contextmenu', function(e) {addMarker(clusterGroup, [e.latlng.lat, e.latlng.lng], null, severity, type, admin);});
    var redIcon = new (L.Icon.Default.extend({
        options: {
            iconUrl: 'http://localhost:8080/tai/scripts/images/marker-icon-2x-red.png'
        }
    }))();
    //https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png

    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8080/tai/map/accidents",
        success: function (data) {
            $.each(data.bing, function (k, v) {
                clusterGroup.addLayer(L.marker(v.point.coordinates, {icon: redIcon})
                    .bindPopup(L.popup().setContent(v.description)));
            });
            $.each(data.tai, function (k, v) {
                addMarker(clusterGroup, [v.latitude, v.longitude], v, severity, type, admin);
            });
        }
    });
}