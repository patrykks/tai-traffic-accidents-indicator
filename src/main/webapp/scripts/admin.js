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
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8080/tai/user/show",
        success: function (data) {
            var table = document.getElementById("usersview");
            var row = table.insertRow(0);
            row.style.backgroundColor = "#52CAA4";
            var headers = ["ID", "Class", "Username", "First Name", "Last Name", "Role",
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
                cell.innerHTML = v._id;

                cell = newRow.insertCell(cellCounter++)
                cell.innerHTML = v._class;

                cell = newRow.insertCell(cellCounter++)
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
                        data: v,
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