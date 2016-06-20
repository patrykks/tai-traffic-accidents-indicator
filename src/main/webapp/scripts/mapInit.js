function init_map(admin) {
    var map = L.map('map').setView([50.0, 15.5], 8);

    var mapLayer = L.tileLayer("http://otile{s}.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png", {
        subdomains: "1234",
        attribution: "&copy; <a href='http://www.openstreetmap.org/'>OpenStreetMap</a> and contributors, under an <a href='http://www.openstreetmap.org/copyright' title='ODbL'>open license</a>. Tiles Courtesy of <a href='http://www.mapquest.com/'>MapQuest</a> <img src='http://developer.mapquest.com/content/osm/mq_logo.png'>"
    }).addTo(map);

    var clusterGroup = new L.markerClusterGroup({disableClusteringAtZoom: 11});
    clusterGroup._getExpandedVisibleBounds = function () {
        return map.getBounds();
    };
    map.addLayer(clusterGroup);
    map.on('contextmenu', function (e) {
        if (map.getZoom() > 10)
            addTAIMarker(clusterGroup, [e.latlng.lat, e.latlng.lng], null, severity, type, admin);
    });

    $('.filterButton').click(function () {
        updateMarkers($("#sev_filter").val(), $("#type_filter").val());
    });

    function createFilter(id, list, text) {
        var combo = $('#' + id);
        $.each(list, function (i, val) {
            combo.append($('<option value="' + (i + 1) + '" />').text(val));
        });
        combo.multiselect({
            selectedList: 2,
            noneSelectedText: text
        });
    }

    var severity;
    $.ajax({
        url: uiProperties.hostname + "/user/enums/severity",
        type: "GET",
        success: function (response) {
            severity = response;
            createFilter("sev_filter", severity, "Filter severity");
        }
    });

    var type;
    $.ajax({
        url: uiProperties.hostname + "/user/enums/type",
        type: "GET",
        success: function (response) {
            type = response;
            createFilter("type_filter", type, "Filter type");
        }
    });

    function updateMarkers(sevs, types) {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: uiProperties.hostname + "/user/map/accidents",
            data: $.param({
                    "lat": 50.0,
                    "lon": 20.0,
                    "radius": 0.0,
                    "severity": sevs,
                    "type": types
                },
                true),
            success: function (data) {
                clusterGroup.clearLayers();
                $.each(data.bing, function (k, v) {
                    addBINGMarker(clusterGroup, v.point.coordinates, v, severity, type, false);
                });
                $.each(data.tai, function (k, v) {
                    addTAIMarker(clusterGroup, [v.latitude, v.longitude], v, severity, type, admin);
                });
            }
        });
    }

    updateMarkers([], []);
}
