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
        success: function (data) {
            var clusterGroup = new L.markerClusterGroup({disableClusteringAtZoom: 12});
            clusterGroup._getExpandedVisibleBounds = function() {return map.getBounds();};
            $.each(data, function (k, v) {
                clusterGroup.addLayer(L.marker(v.point.coordinates)
                    .bindPopup(L.popup().setContent(v.description)));
            });
            map.addLayer(clusterGroup);

            /*var pruneCluster = new PruneClusterForLeaflet();
            $.each(data, function (k, v) {
                var marker = new PruneCluster.Marker(v.point.coordinates[0], v.point.coordinates[1]);
                marker.data.icon = createIcon;
                marker.data.popup = v.description;
                pruneCluster.RegisterMarker(marker);
            });
            map.addLayer(pruneCluster);*/
        }
    });

    function createIcon(data, category) {
        return new L.Icon.Default();
    }
}