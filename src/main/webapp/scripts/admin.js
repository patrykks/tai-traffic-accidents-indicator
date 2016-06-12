/**
 * Created by afetrist on 07/06/16.
 */
jQuery(document).ready(function() {
    jQuery('.tabs .tab-links a').on('click', function(e)  {
        var currentAttrValue = jQuery(this).attr('href');

        // Show/Hide Tabs
        jQuery('.tabs ' + currentAttrValue).fadeIn(400).siblings().hide();

        // Change/remove current tab to active
        jQuery(this).parent('li').addClass('active').siblings().removeClass('active');

        e.preventDefault();
    });
});

function init() {
    init_map();
    init_table();
}

function init_map() {
    var map = L.map('map').setView([50.0, 15.5], 8);

    var mapLayer = L.tileLayer("http://otile{s}.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png", {
        subdomains: "1234",
        attribution: "&copy; <a href='http://www.openstreetmap.org/'>OpenStreetMap</a> and contributors, under an <a href='http://www.openstreetmap.org/copyright' title='ODbL'>open license</a>. Tiles Courtesy of <a href='http://www.mapquest.com/'>MapQuest</a> <img src='http://developer.mapquest.com/content/osm/mq_logo.png'>"
    }).addTo(map);

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
}

function init_table() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8080/tai/user/show",
        success: function (data) {
            var table = document.getElementById("usersview");

            while(table.rows.length > 0){
                table.deleteRow(0);
            }

            var row = table.insertRow(0);
            row.style.backgroundColor = "#52CAA4";
            var headers = ["Username", "First Name", "Last Name", "Role",
                "Non Expired", "Non Locked", "Credentials Non Expired", "Enabled", "Provider"];

            for (var i=0; i < headers.length; i++){
                var cell = row.insertCell(i);
                cell.innerHTML = headers[i];
            }

            var rowCounter = 1;


            $.each(data, function (k, v) {
                var cellCounter = 0;
                var newRow = table.insertRow(rowCounter);
                rowCounter++;

                var cell = newRow.insertCell(cellCounter++);
                cell.innerHTML = v.username;

                cell = newRow.insertCell(cellCounter++)
                cell.innerHTML = v.firstName;

                cell = newRow.insertCell(cellCounter++)
                cell.innerHTML = v.lastName;

                cell = newRow.insertCell(cellCounter++)
                cell.innerHTML = v.role;

                cell = newRow.insertCell(cellCounter++)
                cell.innerHTML = v.accountNonExpired;

                cell = newRow.insertCell(cellCounter++)
                cell.innerHTML = v.accountNonLocked;

                cell = newRow.insertCell(cellCounter++)
                cell.innerHTML = v.credentialsNonExpired;

                cell = newRow.insertCell(cellCounter++)
                cell.innerHTML = v.enabled;

                cell = newRow.insertCell(cellCounter)
                cell.innerHTML = v.signInProvider;


            })

        }
    });
}

jQuery(document).ready(function () {
    $("div.tabs").click(function (event) {
        var core = String(event.target);
        var path = core.charAt(core.length - 1);
        if(path == "1"){
            init_map();
        }else if(path == "2"){
            init_table();
        }
    })
});

function ban() {
    var nick = document.getElementById('ban_field').value;
    var found = false;

    var token = $('#_csrf').attr('content');
    var header = $('#_csrf_header').attr('content');

    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8080/tai/user/show",
        success: function (data) {
            $.each(data, function (k, v) {
                if(v.username == nick){

                    $.ajax({
                        url: 'http://localhost:8080/tai/user/ban',
                        type: 'POST',
                        contentType: "application/json",
                        data: '{' + v._id + '}',
                        beforeSend: function(xhr) {
                            xhr.setRequestHeader(header, token);
                        },
                        success: function() {
                            
                        },
                        error: function(xhr, status, error) {
                            alert("An AJAX error occured: " + status + "\nError: " + error);
                        }
                    });

                    found = true;
                }
            })

            if(!found){
                alert("No such user!")
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("Status: " + textStatus);
            alert("Error: " + errorThrown);
        }
    });
}