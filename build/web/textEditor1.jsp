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
  
        <script type="text/javascript">
            var serverLoadedContent =  '';
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
                        $("#serverContent").html(data.serverContent);
                        serverLoadedContent = data.serverContent;
                    }
                });
                
                $("#serverContent").keyup(function(){
                    if($(this).val() != serverLoadedContent){
                        $("#contentBtn").css("display", "block");
                        $("#compile").css("display", "none");
                        display = 'block';
                    }else{
                        $("#contentBtn").css("display", "none");
                        $("#compile").css("display", "block");
                        display = 'none';
                    }
                    saveBtnState();
                });
                
                $("#save").click(function(){
                    $.ajax({
                    type: "POST",
                    url: "DBConnectServlet",
                    data: {'projectID': $("#project").val(),
                           'serverContent': $("#serverContent").val(),
                            'method': 'saveServerContent'},
                    dataType: "json",
                    success: function(data){
                        console.log(data);  
                        if(data == 1){
                            alert('Server Code Updated');
                            serverLoadedContent = $("#serverContent").val();
                            $("#contentBtn").css("display", "none");
                            $("#compile").css("display", "block");
                            display = 'none';
                            saveBtnState();
                        }
                    }
                });
                    
                });
                
                $("#discard").click(function(){
                    $("#serverContent").val(serverLoadedContent);
                    $("#contentBtn").css("display", "none");
                    $("#compile").css("display", "block");
                    display = 'none';
                    saveBtnState();
                });
            });
            
            function saveBtnState(){
                $.ajax({
                        type: "POST",
                        url: "DBConnectServlet",
                        data: {'btnStatus': display,
                               'method': 'saveServerBtnState'},
                        dataType: "json",
                        success: function(data){}
                    });
            }
        </script>
</head>
<body>
<input type="hidden" id="project">
<div style="float:right">
<!--    <div id="compile">
        <button type="button" id="compileBtn" class="btn btn-secondary btn-success">Compile</button>
    </div>-->
    <div id="contentBtn" hidden="true" class="contentBtns" value='1'>
        <button type="button" id="save" class="btn btn-primary">Save</button>
        <button type="button" id="discard" class="btn btn-secondary btn-danger">Discard Changes</button>
    </div>  
</div><br>
<font face="Arial" size="2px;">
	<b><b id="projectName"></b>_server.mdl</b>
        
	<textarea class="lined" rows="30" cols="120" id="serverContent"></textarea><br>
	<div id="serverErrors" style="height: 130px; width: 770px; border: 1px solid silver; overflow-y: scroll;">
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