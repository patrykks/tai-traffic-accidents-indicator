function init() {
    var map = L.map('map').setView([50.0, 15.5], 8);

    var mapLayer = L.tileLayer("http://otile{s}.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png", {
        subdomains: "1234",
        attribution: "&copy; <a href='http://www.openstreetmap.org/'>OpenStreetMap</a> and contributors, under an <a href='http://www.openstreetmap.org/copyright' title='ODbL'>open license</a>. Tiles Courtesy of <a href='http://www.mapquest.com/'>MapQuest</a> <img src='http://developer.mapquest.com/content/osm/mq_logo.png'>"
    }).addTo(map);


    /*
    var token = $('#_csrf').attr('content');
    var header = $('#_csrf_header').attr('content');

    $.ajax({
        url: 'http://localhost:8080/tai/map/accidents',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify({
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
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function() { alert('POST completed'); },
        error: function(xhr, status, error) {
            alert("An AJAX error occured: " + status + "\nError: " + error);
        }
    });

    $.ajax({
        url: 'http://localhost:8080/tai/vote/upvote',
        type: 'POST',
        contentType: "text/plain",
        data: "5752159aa063331cd1fe1111",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function() { alert('POST completed'); },
        error: function(xhr, status, error) {
            alert("An AJAX error occured: " + status + "\nError: " + error);
        }
    });
    */

    var redIcon = new (L.Icon.Default.extend({
        options: {
            iconUrl: 'http://localhost:8080/tai/scripts/images/marker-icon-2x-red.png'
        }
    }))();

    var greenIcon = new (L.Icon.Default.extend({
        options: {
            iconUrl: 'http://localhost:8080/tai/scripts/images/marker-icon-2x-green.png'
        }
    }))();

    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8080/tai/map/accidents",
        success: function (data) {
            var clusterGroup = new L.markerClusterGroup({disableClusteringAtZoom: 12});
            clusterGroup._getExpandedVisibleBounds = function() {return map.getBounds();};
            $.each(data.bing, function (k, v) {
                clusterGroup.addLayer(L.marker(v.point.coordinates, {icon: redIcon})
                    .bindPopup(L.popup().setContent(v.description)));
            });
            $.each(data.tai, function (k, v) {
                clusterGroup.addLayer(L.marker(v.point.coordinates, {icon: greenIcon})
                    .bindPopup(L.popup().setContent(v.description)));
            });
            map.addLayer(clusterGroup);
        }
    });
    //https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png
}