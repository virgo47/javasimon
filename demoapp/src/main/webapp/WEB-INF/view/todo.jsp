<html>
<head>
    <title>
        Test demo application
    </title>

    <script src="http://code.jquery.com/jquery-2.0.3.min.js"></script>

    <script src="/static/js/conf.js"/></script>
    <script src="/static/js/model.js"/></script>
    <script src="/static/js/view.js"/></script>
    <script src="/static/js/controller.js"/></script>
</head>
<body>
	<h1>TODO list:</h1>
	<table id="toDoList">
	</table>

	<script>
        $(function() {
            ToDoDemo.Controller.onPageLoad();
        });
	</script>
</body>
</html>