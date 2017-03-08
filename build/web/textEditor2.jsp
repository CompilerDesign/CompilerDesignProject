<%-- 
    Document   : textEditor1
    Created on : Jan 9, 2017, 10:22:09 AM
    Author     : Sha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
	<script src="design/bootstrap/js/jquery-1.3.2.min.js"></script>
	<script src="design/bootstrap/js/jquery-linedtextarea.js"></script>
	<link href="design/bootstrap/css/jquery-linedtextarea.css" type="text/css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="design/bootstrap/css/bootstrap.min.css">
        <!--<script src="js/bootstrap.min.js"></script>-->
    </head>
     <script type="text/javascript">
         var volunteerLoadedContent = '';
         var display = '';
            $(document).ready(function(){
                $.ajax({
                    type: "GET",
                    url: "DBConnectServlet",
                    data: {'method': 'loadProject'},
                    dataType: "json",
                    success: function(data){
                        console.log(data);
                        $("#project").val(data.projectID);
                        $("#projectName").html(data.projectName);
                        $("#volunteerContent").html(data.volunteerContent);
                        volunteerLoadedContent = data.volunteerContent;
                    }
                });
                $("#volunteerContent").keyup(function(){
                   if($(this).val() != volunteerLoadedContent){
                        $("#contentBtn").css("display", "block");
                        display = 'block';
                    }else{
                        $("#contentBtn").css("display", "none");
                        display = 'none';
                    }
                    saveBtnState();
                });
                
                $("#save").click(function(){
                    $.ajax({
                    type: "POST",
                    url: "DBConnectServlet",
                    data: {'projectID': $("#project").val(),
                           'volunteerContent': $("#volunteerContent").val(),
                            'method': 'saveVolunteerContent'},
                    dataType: "json",
                    success: function(data){
                        console.log(data);  
                        if(data == 1){
                            alert('Volunteer Code Updated');
                            volunteerLoadedContent = $("#volunteerContent").val();
                            $("#contentBtn").css("display", "none");
                            display = 'none';
                            saveBtnState();
                        }
                    }
                });
                    
                });
                
                $("#discard").click(function(){
                    $("#volunteerContent").val(volunteerLoadedContent);
                    $("#contentBtn").css("display", "none");
                    display = 'none';
                    saveBtnState();
                })
            });
            
            function saveBtnState(){
                $.ajax({
                        type: "POST",
                        url: "DBConnectServlet",
                        data: {'btnStatus': display,
                               'method': 'saveVolunteerBtnState'},
                        dataType: "json",
                        success: function(data){}
                    });
            }
        </script>
<body>
<input type="hidden" id="project">
<div style="float:right">
    <div id="contentBtn" hidden="true">
        <button type="button" id="save" class="btn btn-primary">Save</button>
        <button type="button" id="discard" class="btn btn-secondary btn-danger">Discard Changes</button>
    </div>  
</div><br>
<font face="Arial" size="2px;">       
	<b><b id="projectName"></b>_volunteer.mdl</b>

        <textarea class="lined" rows="30" cols="120" id="volunteerContent"></textarea><br>
	<div id="volunteerErrors" style="height: 130px; width: 765px; border: 1px solid silver; overflow-y: scroll;">
	  Errors here...
	</div>
</font>
<script>
$(function() {
	$(".lined").linedtextarea(
		{selectedLine: 0}
	);
});
</script>

</body>
</html>