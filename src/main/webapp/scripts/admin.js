jQuery(document).ready(function () {
    jQuery('.tabs .tab-links a').on('click', function (e) {
        var currentAttrValue = jQuery(this).attr('href');

        // Show/Hide Tabs
        jQuery('.tabs ' + currentAttrValue).fadeIn(400).siblings().hide();

        // Change/remove current tab to active
        jQuery(this).parent('li').addClass('active').siblings().removeClass('active');

        e.preventDefault();
    });
});

function init() {
    init_map(true);
    init_table();
}

function init_table() {
    var userShowPath = uiProperties.hostname + "/admin/user/show";
    $.ajax({
        type: "GET",
        dataType: "json",
        url: userShowPath,
        success: function (data) {
            var table = document.getElementById("usersview");

            while (table.rows.length > 0) {
                table.deleteRow(0);
            }

            var row = table.insertRow(0);
            row.style.backgroundColor = "#52CAA4";
            var headers = ["Username", "First Name", "Last Name", "Role",
                "Non Expired", "Non Locked", "Credentials Non Expired", "Enabled", "Provider", "Lock / Unlock"];
            for (var i = 0; i < headers.length; i++) {
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

                cell = newRow.insertCell(cellCounter++)
                cell.innerHTML = v.signInProvider;

                btnText = v.accountNonLocked == true ? 'Lock' : 'Unlock';
                cell = newRow.insertCell(cellCounter);
                cell.innerHTML = '<button id=' + rowCounter + ' class="btn btn-primary btn-xs my-xs-btn" type="button">'
                    + '<span class="glyphicon glyphicon-pencil"></span> ' + btnText + ' </button>';

                var actualRawCounter = new String(rowCounter);
                var actualId = v._id;
                var actualRow = row;
                document.getElementById(actualRawCounter).addEventListener("click", function (e) {
                    text = v.accountNonLocked == true ? 'Unlock' : 'Lock';
                    document.getElementById(actualRawCounter).innerHTML = '<button id=' + rowCounter + ' class="btn btn-primary btn-xs my-xs-btn" type="button">'
                        + '<span class="glyphicon glyphicon-pencil"></span> ' + text + ' </button>';
                    ban(actualId, !v.accountNonLocked == true);
                    table.rows[actualRawCounter - 1].cells[5].innerHTML = !v.accountNonLocked;
                    v.accountNonLocked = !v.accountNonLocked;
                });


            })

        }
    });
}

jQuery(document).ready(function () {
    $("div.tabs").click(function (event) {
        var core = String(event.target);
        var path = core.charAt(core.length - 1);
        if (path == "2") {
            init_table();
        }
    })
});


function ban(id, value) {
    var found = false;

    var token = $('#_csrf').attr('content');
    var header = $('#_csrf_header').attr('content');

    var userShowPath = uiProperties.hostname + "/admin/user/show";
    var userBanPath = uiProperties.hostname + "/admin/user/ban";

    $.ajax({
        type: "GET",
        dataType: "json",
        url: userShowPath,
        success: function (data) {
            $.each(data, function (k, v) {
                if (v._id == id) {

                    $.ajax({
                        url: userBanPath,
                        type: 'PUT',
                        contentType: "application/json",
                        data: JSON.stringify({
                            "user": v,
                            "value": value
                        }),
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader(header, token);
                        },
                        success: function () {

                        },
                        error: function (xhr, status, error) {
                            alert("An AJAX error occured: " + status + "\nError: " + error);
                        }
                    });

                    found = true;
                }
            })

            if (!found) {
                alert("No such user!" + id)
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("Status: " + textStatus);
            alert("Error: " + errorThrown);
        }
    });
}