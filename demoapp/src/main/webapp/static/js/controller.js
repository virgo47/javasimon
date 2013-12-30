var ToDoDemo = window.ToDoDemo || {}


ToDoDemo.Controller = {
    onPageLoad: function() {
        ToDoDemo.Model.getAll(
            function(oData) {
                ToDoDemo.View.getToDoView().displayData(oData);
            },
            function(strError) {
                ToDoDemo.View.getErrorView().displayError(strError);
            }
        );
    }
}