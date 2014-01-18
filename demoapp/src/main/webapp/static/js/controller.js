var ToDoDemo = window.ToDoDemo || {}


ToDoDemo.Controller = {
    onPageLoad: function() {
        this.displayAll();
    },

    displayAll: function() {
        ToDoDemo.Model.getAll(
            function(oData) {
                ToDoDemo.View.getToDoView().displayData(oData);
            },
            this.displayError
        );
    },

    deleteItem: function(oItem) {
        var toDelete = confirm("Do you want to delete item '" + oItem.name + "'");
        if (toDelete) {
            ToDoDemo.Model.deleteItem(oItem.id,
                function() {
                    ToDoDemo.Controller.displayAll();
                },
                this.displayError
            );
        }
    },

    displayError: function(strError) {
        ToDoDemo.View.getErrorView().displayError(strError);
    }

}