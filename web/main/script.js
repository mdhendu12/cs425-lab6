var Lab6 = (function () {

    var that;

    var sessPageCallback = function(dd) {
 
        createSessionMenu(dd, "#sessionmenu");
        that.setAttMenuSessPg();

        $("#sessionid").change(function() {
            that.setAttMenuSessPg();
        });

    };

    var regPageCallback = function(dd) {
        createSessionMenu(dd, "#sessionmenu");
    }

    var createSessionMenu = function (data, appendSpot) {

        var select = document.createElement("select");
        select.name = "sessionid";
        select.id = "sessionid";

        for (var i = 0; i < data.sessions.length; ++i) {

            var session = data.sessions[i];
            var option = document.createElement("option");
            var id = session.id;
            var descript = session.description;
            option.value = id;
            option.innerHTML = descript;

            $(select).append(option);
        } 
        $(appendSpot).append(select);
    };

    var updtDisplyNameMenuSess = function (data) {
        
        data.registrations.sort((a, b) => (a.displayname > b.displayname) ? 1 : -1)

        var select = document.createElement("select");
        select.name = "attendeeid";
        select.id = "attendeeid";

        for (var i = 0; i < data.registrations.length; ++i) {
            var user = data.registrations[i];
            var option = document.createElement("option");
            var id = user.attendeeid;
            var dname = user.displayname;

            option.value = id;
            option.innerHTML = dname;

            select.append(option);
        }
        $("#attendeemenu").html(select);
    };

    var updtDisplyNameMenuAttendee = function (data) {
        
        data.attendees.sort((a, b) => (a.displayname > b.displayname) ? 1 : -1)

        var select = document.createElement("select");
        select.name = "attendeeid";
        select.id = "attendeeid";

        for (var i = 0; i < data.attendees.length; ++i) {
            var user = data.attendees[i];
            var option = document.createElement("option");
            var id = user.id;
            var dname = user.displayname;

            option.value = id;
            option.innerHTML = dname;

            select.append(option);
        }
        $("#attendeemenu").html(select);
    };

    var updateDisplayName = function() {
        var displayname = $("#displayname");
        displayname.val( $("#firstname").val() + " " + $("#lastname").val() );
    }; 

    var displayUserOnSess = function(data) {
        console.log(data);

        var attendeeid = data.attendeeid;
        var sessionid = String(data.sessionid);
        var session = data.session;

        delete data.attendeeid;
        delete data.sessionid;
        delete data.success;
        delete data.session;

        var form = document.createElement("form");
        var fieldset = document.createElement("fieldset");
        var legend = document.createElement("legend");
        var properties = ["firstname", "lastname", "displayname"];
       
        form.name = "attendeeform";
        form.id = "attendeeform";
        legend.innerHTML = data.displayname;

        fieldset.append(legend);       

        for (var i = 0; i < properties.length; i++) {
            var input = document.createElement("input");
            var label = document.createElement("label");
            var labelText;

            if (properties[i] == "firstname") {
                labelText = "First Name: ";
            } else if (properties[i] == "lastname") {
                labelText = "Last Name: ";
            } else if (properties[i] == "displayname"){
                labelText = "Display Name: ";
            }

            label.innerHTML = labelText;
            label.for = properties[i];
            fieldset.append(label);

            input.type = "text";
            input.name = properties[i];
            input.id = properties[i];
            input.value = data[properties[i]];
            fieldset.append(input);
        }


        var label = document.createElement("label");
        label.innerHTML = "Session: ";
        fieldset.append(label);

        $("#sessionid").clone().attr("id", "usersessionid").attr("name", "sessionid")
            .appendTo(fieldset);

        // Update registration form 
        var span = document.createElement("span");
        span.innerHTML = "<input type=\"submit\" value=\"Update\" onclick=\""
        + "return Lab6.updateRegistration(" + attendeeid +");\">";

        fieldset.append(span);

        // Delete registration form 
        var span = document.createElement("span");
        span.innerHTML = "<input type=\"submit\" value=\"Delete\" onclick=\""
        + "return Lab6.deleteRegistration(" + attendeeid + ");\">";

        fieldset.append(span);
        

        form.append(fieldset);
        
        $("#output").html(form);
    
    };


    var displayUserOnAtt = function(data) {
        console.log(data);

        var attendeeid = data.id;

        delete data.attendeeid;

        var form = document.createElement("form");
        var fieldset = document.createElement("fieldset");
        var legend = document.createElement("legend");
        var properties = ["firstname", "lastname", "displayname"];
       
        form.name = "attendeeform";
        form.id = "attendeeform";
        legend.innerHTML = data.displayname;

        fieldset.append(legend);       

        for (var i = 0; i < properties.length; i++) {
            var input = document.createElement("input");
            var label = document.createElement("label");
            var labelText;

            if (properties[i] == "firstname") {
                labelText = "First Name: ";
            } else if (properties[i] == "lastname") {
                labelText = "Last Name: ";
            } else if (properties[i] == "displayname"){
                labelText = "Display Name: ";
            }

            label.innerHTML = labelText;
            label.for = properties[i];
            fieldset.append(label);

            input.type = "text";
            input.name = properties[i];
            input.id = properties[i];
            input.value = data[properties[i]];
            fieldset.append(input);
        }

        // Update registration form 
        var span = document.createElement("span");
        span.innerHTML = "<input type=\"submit\" value=\"Update\" onclick=\""
        + "return Lab6.updateAttendee(" + attendeeid +");\">";

        fieldset.append(span);
        
        form.append(fieldset);
        
        $("#firstname").change(updateDisplayName);
        $("#lastname").change(updateDisplayName);
        $("#output").html(form);
    
    };

    return {

        getSessionList: function (callBack) {
            var that = this;
            $.ajax({
                url: 'trainingsessions',
                method: 'GET',
                dataType: 'json',
                success: function(data) {
                    callBack(data, that);
                },
            });
        },

        onClickRegPost: function () {
            $.ajax({
                url: 'attendee',
                method: 'POST',
                data: $('#regform').serialize(),
                dataType: 'json',
                success: function(data) {
                    $.ajax({
                        url: 'registrations',
                        method: 'POST',
                        data: $('#regform').serialize() + "&attendeeid=" + data.id,
                        dataType: 'json',
                        success: window.alert("Registration was successful!")
                    });

                }
            });

            return false;
        },

        setAttMenuSessPg: function () {
            $.ajax({
                url: 'registrations',
                method: 'GET',
                data: "sessionid=" + $("#sessionid option:selected").val(),
                dataType: 'json',
                success: function(data) {
                    updtDisplyNameMenuSess(data);
                },
            });

            return false;
        },

        setAttMenuAttPg: function () {
            $.ajax({
                url: 'attendee',
                method: 'GET',
                dataType: 'json',
                success: function(data) {
                    updtDisplyNameMenuAttendee(data);
                },
            });

            return false;
        },

        onClickSessPost: function () {

            $.ajax({
                url: 'registrations',
                method: 'GET',
                data: $("#sesform").serialize(),
                dataType: 'json',
                success: function(data) {
                    displayUserOnSess(data);
                },
            });

            return false;
        },

        onClickAttPost: function() {
            $.ajax({
                url: 'attendee',
                method: 'GET',
                data: $("#attform").serialize(),
                dataType: 'json',
                success: function(data) {
                    displayUserOnAtt(data);
                },
            });

            return false;
        },

        regInit: function() {

            this.getSessionList(regPageCallback);

            $(document).on('change', '#firstname', updateDisplayName);
            $(document).on('change', '#lastname', updateDisplayName);

        },

        attInit: function() {
            $(document).on('change', '#firstname', updateDisplayName);
            $(document).on('change', '#lastname', updateDisplayName);

            this.setAttMenuAttPg();
         
        },

        sessionInit: function() {          
            $(document).on('change', '#firstname', updateDisplayName);
            $(document).on('change', '#lastname', updateDisplayName);

            that = this;

            this.getSessionList(sessPageCallback);
                       
        },

        deleteRegistration: function(attendeeid) {

            var queryStr = $("#attendeeform").serialize() + "&attendeeid=" + attendeeid;

            $.ajax({
                url: 'registrations',
                method: 'DELETE',
                data: queryStr,
                dataType: 'json',
                success: function(data) {
                    console.log(data);
                    console.log("User registration deleted successfully");
                    $("#attendeeform").remove();
                },
            });

            return false;

        },

        updateRegistration: function(attendeeid) {
           var queryStr = $("#attendeeform").serialize() + "&attendeeid=" + attendeeid;

            $.ajax({
                url: 'attendee',
                method: 'PUT',
                data: queryStr,
                dataType: 'json',
                success: function(data) {
                    console.log(data);
                },
            });

            $.ajax({
                url:'registrations',
                method: 'PUT',
                data: queryStr,
                dataType: 'json',
                success: function(data) {
                    console.log(data);
      
                },
            });


            return false;
        },

        updateAttendee: function(attendeeid) {
            var queryStr = $("#attendeeform").serialize() + "&attendeeid=" + attendeeid;
 
             $.ajax({
                 url: 'attendee',
                 method: 'PUT',
                 data: queryStr,
                 dataType: 'json',
                 success: function(data) {
                     console.log(data);
                 },
             });
 
             return false;
         },

        deleteAttendee: function(attendeeid) {

            $.ajax({
                url: 'attendee',
                method: 'DELETE',
                data: "attendeeid=" + attendeeid,
                dataType: 'json',
                success: function(data) {
                    console.log(data);
                    console.log("User registration deleted successfully");
                    $("#attendeeform").remove();
                },
            });

            return false;

        }

    };

})();