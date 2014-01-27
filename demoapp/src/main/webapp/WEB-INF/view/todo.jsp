<html>
<head>
    <title>
        Test demo application
    </title>

    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/start/jquery-ui.css">

    <script src="http://code.jquery.com/jquery-2.0.3.min.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/additional-methods.js"></script>

    <script src="/static/js/conf.js"/></script>
    <script src="/static/js/model.js"/></script>
    <script src="/static/js/view.js"/></script>
    <script src="/static/js/controller.js"/></script>

    <link rel="stylesheet" href="../../themes/base/jquery.ui.all.css">
   	<link rel="stylesheet" href="../demos.css">
    <style>
        body {
            font-size:10px;
        }

        .error {
            color: red;
        }
    </style>
</head>
<body>
	<h1>TODO list:</h1>
	<table id="toDoList">
	</table>

    <button id="create-item">Create new item</button>

    <div id="itemForm" title="Create new item">
    	<p class="validateTips">All form fields are required.</p>

    	<form id="form">
            <fieldset>
                <table>
                    <tr>
                        <td><label for="name">Name</label></td>
                        <td><input type="text" name="name" id="itemNameField" required="true" class="text ui-widget-content ui-corner-all" /></td>
                    </tr>

                    <tr>
                        <td><label for="name">Description</label></td>
                        <td><input type="text" name="description" id="descriptionField" required="true" class="text ui-widget-content ui-corner-all" /></td>
                    </tr>
                </table>
            </fieldset>
    	</form>
    </div>


	<script>
        $(function() {
            ToDoDemo.Controller.onPageLoad();

            $( "#create-item" )
                .button()
                .click(function() {
                    ToDoDemo.Controller.onCreateItem();
            });
        });
	</script>
</body>
</html>