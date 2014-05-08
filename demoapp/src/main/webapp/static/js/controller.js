var ToDoDemo = window.ToDoDemo || {}


ToDoDemo.Controller = {
    onPageLoad: function() {
        ToDoDemo.View.init();
        this.displayAll();
    },

    onCreateItem: function() {
         var itemForm = ToDoDemo.View.createItemForm(
             function(oNewItem) {
                ToDoDemo.Model.addItem(oNewItem,
                    function() {
                        ToDoDemo.Controller.displayAll();
                        itemForm.close();
                    },
                    function(sErrorText) {
                        itemForm.displayError(sErrorText);
                    }
                );
             }
         );

         itemForm.open();
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

    updateItem: function(oItem) {
        var itemForm = ToDoDemo.View.createItemForm(
             function(oUpdatedItem) {
                ToDoDemo.Model.updateItem(oUpdatedItem,
                    function() {
                        ToDoDemo.Controller.displayAll();
                        itemForm.close();
                    },
                    function(sErrorText) {
                        itemForm.displayError(sErrorText);
                    }
                );
             },
             oItem
         );

         itemForm.open();
    },

    checkItem: function(oItem, bIsChecked) {
        oItem.done = bIsChecked;
        ToDoDemo.Model.updateItem(oItem,
            function() {},
            this.displayError
        );
    },

    displayError: function(strError) {
        ToDoDemo.View.getErrorView().displayError(strError);
    }

}