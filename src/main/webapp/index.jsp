<!--
    @author: Mike Burgess (MetricalSky)
    @date: 2016 September 10
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>PAX Signature Generator</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet"/>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"/>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"/>
    <![endif]-->
</head>
<body>
<div class="container">
    <h1>PAX Signature Generator</h1>
    <c:forEach items="${applicationScope['paxen']}" var="pax">
    <div class="panel panel-default">
            <div class="panel-heading">${pax.displayName}</div>
            <div class="panel-body">
                <div class="btn-group" data-toggle="buttons">
                <c:forEach items="${pax.years}" var="year">
                    <label class="btn btn-default"><input type="checkbox" class="paxselect" autocomplete="off" data-pax="${pax.name}">${year}</label>
                </c:forEach>
                </div>
            </div>
    </div>
    </c:forEach>
    <form method="POST" action="signature.png">
        <div class="panel panel-primary">
            <div class="panel-heading">My PAX</div>
            <table class="table table-striped table-bordered table-responsive">
                <tbody id="my-pax">
                </tbody>
            </table>
            <div class="panel-footer clearfix">
                Badges will be displayed in the order listed here.<br /><label class="btn btn-default"><input type="checkbox" autocomplete="off" name="sortBadges"/> Sort by year instead.</label>
                <button type="submit" class="btn btn-primary pull-right">Generate!</button>
            </div>
        </div>
    </form>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>

<script>
    // add or remove My PAX rows as the above year checkboxes are toggled
    $('input:checkbox.paxselect').change(function() {
        var paxid = $(this).data('pax') + $(this).parent().text();

        if ($(this).is(':checked')) {
            $('#my-pax').append(
                '<tr id="' + paxid + '">' +
                    '<td>' + paxid + '</td>' +
                    '<td>' +
                        '<input type="hidden" name="Year[]" value="' + $(this).parent().text() + '"/>' +
                        '<input type="hidden" name="PAX[]" value="' + $(this).data('pax') + '"/>' +
                        '<select class="form-control" name="Badge[]">' +
                            '<option value="ATTENDEE">Attendee</option>' +
                            '<option value="OMEGANAUT">Omeganaut</option>' +
                            '<option value="MEDIA">Media</option>' +
                            '<option value="ENFORCER">Enforcer</option>' +
                        '</select>' +
                    '</td>' +
                    '<td>' +
                        '<div class="btn-group" data-toggle="buttons">' +
                            '<label class="btn btn-default"><input type="checkbox" name="future[]" class="future-pax"/>Future PAX?</label>' +
                        '</div>' +
                    '</td>' +
                '</tr>');
        } else {
            $('#' + paxid).remove();
        }

        // reset the values of the Future PAX checkboxes to match their index
        $('.future-pax').each(function(index) {
            $(this).attr('value', index);
        })
    })
</script>
</body>
</html>
