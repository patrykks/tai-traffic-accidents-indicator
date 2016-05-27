function init() {
    var map = L.map('map').setView([50.0, 15.5], 8);

    var mapLayer = L.tileLayer("http://otile{s}.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png", {
        subdomains: "1234",
        attribution: "&copy; <a href='http://www.openstreetmap.org/'>OpenStreetMap</a> and contributors, under an <a href='http://www.openstreetmap.org/copyright' title='ODbL'>open license</a>. Tiles Courtesy of <a href='http://www.mapquest.com/'>MapQuest</a> <img src='http://developer.mapquest.com/content/osm/mq_logo.png'>"
    }).addTo(map);

    $.ajax({
        url: 'http://localhost:8080/tai/map/accidents',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify({"incidentId":10,
            "severity":2,
            "verified":true,
            "description": 
            "Przy Strzy?awa (DW 549) - Roboty drogowe. Ograniczenia.",
            "source":4,
            "congestion":null,
            "detour":null,
            "lane":null,
            "type":9,
            "roadClosed":false,
            "point":{"x":53.1385,"y":18.17752,"coordinates":[52.5243700,13.4105300],"type":"Point"},
            "toPoint":{"x":52.5243700 ,"y":13.4105300,"coordinates":[53.1385,18.17752],"type":"Point"},
            "start":"/Date(1464263340000)/",
            "end":"/Date(1464451200000)/",
            "lastModified":"/Date(1464335296977)/"}),
        success: function() { alert('POST completed'); },
        error: function(xhr, status, error) {
            alert("An AJAX error occured: " + status + "\nError: " + error);
        }
    });
    
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