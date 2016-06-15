function addMarker(cluster, coordinates, incident, sevs, types, admin) {
    var token = $('#_csrf').attr('content');
    var header = $('#_csrf_header').attr('content');
    var brandNew = false;
    var v = JSON.parse(JSON.stringify({
        "latitude": "",
        "longitude": "",
        "severity": "",
        "description": "",
        "type": "",
        "roadClosed": "",
        "start": "",
        "end": "",
        "creator": "",
        "votes": 0,
        "voters": []
    }));

    if(incident != null)
        v = $.extend(true, {}, incident);
    else {
        brandNew = true;
        v.latitude = coordinates[0];
        v.longitude = coordinates[1];
        v.start = Date.now();
        v.end = Date.now();
    }

    function createCombobox(name, list, selected) {
        var combo = $('<select name="' + name + '"/>');
        $.each(list, function (i, val) {
            if ((i + 1) == selected) {
                combo.append($('<option value="' + (i + 1) + '" selected="selected"/>').text(val));
            } else {
                combo.append($('<option value="' + (i + 1) + '"/>').text(val));
            }
        });
        return combo;
    }

    function rebind(target) {
        marker.closePopup();
        marker.unbindPopup();
        marker.bindPopup(target[0]);
        marker.openPopup();
    }

    function timestampToDate(timestamp) {
        if ($.type(timestamp) === "string")
            timestamp = parseInt(timestamp);
        var date = new Date(timestamp);
        var month = date.getMonth() + 1;
        month = month < 10 ?  "0" + month : "" + month;
        return date.getDate() + "-" + month + "-" + (1900 + date.getYear());
    }

    function dateToTimestamp(date) {
        return new Date(date.split("-").reverse().join("-")).getTime();
    }

    function refresh() {
        $('.likes').text(v.votes);
        $('.description_text').text(v.description);
        $('.description').val(v.description);
        $('.end').val(timestampToDate(v.end));
        $('.closed').attr('checked', v.roadClosed);
    }

    function submit() {
        var newV = $.extend(true, {}, v);
        newV.description = $('.myForm :input[name=description]').val();
        newV.end = dateToTimestamp($('.myForm :input[name=end]').val());
        newV.severity = parseInt($('.myForm :input[name=severity]').val());
        newV.type = parseInt($('.myForm :input[name=type]').val());
        newV.roadClosed = $('.closed').is(':checked');
        $.ajax({
            url: uiProperties.hostname + '/map/accidents/add',
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(newV),
            beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
            success: function(id) {
                alert('POST completed');
                if(brandNew && id != null) {
                    newV._id = JSON.parse('{"$oid" : "' + id + '"}');
                    marker.off('popupclose');
                    brandNew = false;
                }
                v=$.extend(true, {}, newV);
                rebind(container);
                refresh();
            },
            error: function(xhr, status, error) { alert("An AJAX error occurred: " + status + "\nError: " + error); }
        });
        return false;
    }

    function vote(votes) {
        if(brandNew)
            return;
        $.ajax({
            url: uiProperties.hostname + '/vote',
            type: 'GET',
            contentType: "application/json",
            data: $.param({"incident": v._id.$oid, "votes": votes}, true),
            success: function (data) {
                if (data) {
                    v.votes += votes;
                    $(".likes").text(v.votes);
                }
            }
        });
    }

    function cancel() {
        if(brandNew)
            cluster.removeLayer(marker);
        else
            rebind(container);
    }

    function remove() {
        if(brandNew)
            cluster.removeLayer(marker);

        $.ajax({
            url: uiProperties.hostname + '/map/accidents/remove',
            type: 'POST',
            contentType: "text/plain",
            data: v._id.$oid,
            beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
            success: function() { cluster.removeLayer(marker); },
            error: function(xhr, status, error) { alert("An AJAX error occured: " + status + "\nError: " + error); }
        });
    }
    var markerUrl = uiProperties.hostname + "/scripts/images/marker-icon-2x-green.png";
    var greenIcon = new (L.Icon.Default.extend({
        options: {
            iconUrl: markerUrl
        }
    }))();
    var marker = L.marker(coordinates, {icon: greenIcon});
    cluster.addLayer(marker);
    var container = $('<div />');
    var form = $('<form class="myForm" method="POST" style="display:block; width:250px;"/>');

    if (admin)
        container.append($('<button class="editButton" type="button" style="display:inline-block;">').text("Edit").click(function () {rebind(form);}));
    container.append($('<button class="upButton" type="button" style="display:inline-block; float: right;">').text("+").click(function () {vote(1);}));
    container.append($('<span class="likes" style="display:inline-block; float: right; margin-top:5px;">').text(v.votes));
    container.append($('<button class="downButton" type="button" style="display:inline-block; float: right;">').text("-").click(function () {vote(-1);}));
    container.append($('<br/><br/>'));
    container.append($('<span class="description_text" style="display:block; width:150px; word-wrap:break-word;">').text(v.description));

    form.append($('<span class="description_span" style="display:inline-block; width:30%">').text("description: "));
    form.append($('<input class="description"  type="text" name="description" style="width:65%">').val(v.description));
    form.append($('<span class="end_span" style="display:inline-block; width:30%">').text("end date: "));
    form.append($('<input class="end" type="text" name="end" placeholder="dd-mm-yyyy" style="width:65%">').val(timestampToDate(v.end)));
    form.append($('<br/>'));
    form.append($('<span class="type_span" style="display:inline-block; width:30%">').text("type: "));
    form.append(createCombobox("type", types, v.type));
    form.append($('<br/>'));
    form.append($('<span class="severity_span" style="display:inline-block; width:30%">').text("severity: "));
    form.append(createCombobox("severity", sevs, v.severity));
    form.append($('<br/>'));
    form.append($('<span class="closed_span" style="display:inline-block; width:30%">').text("road closed: "));
    form.append($('<input class="closed" type="checkbox" name="closed">'));
    form.append($('<br/>'));
    form.append($('<input type="submit" name="save" value="Save" style="width:30%; display:inline-block;">'));
    form.append($('<button class="cancelButton" type="button" style="display:inline-block; ">').text("Cancel").click(function () {cancel();}));
    form.append($('<button class="delButton" type="button" style="display:inline-block; float: right;">').text("Delete").click(function () {remove();}));
    form.bind('submit', submit);

    if(brandNew) {
        marker.bindPopup(form[0]);
        marker.openPopup();
        marker.on('popupclose', function() {cluster.removeLayer(marker);});
    } else
        marker.bindPopup(container[0]);
}
