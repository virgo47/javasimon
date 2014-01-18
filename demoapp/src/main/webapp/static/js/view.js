var ToDoDemo = window.ToDoDemo || {}

ToDoDemo.View = {
    getToDoView: function() {
        var toDoTableId = "#toDoList";
        var $toDoList = $(toDoTableId);

        return this.createToDoView($toDoList);
    },

    getErrorView: function() {
        var errorId = "#error";
        var $errorDiv = $(errorId);

        return this.createErrorView($errorDiv);
    },

    createToDoView: function($toDoList) {
        var view = {
            displayData: function(oData) {
                $toDoList.empty();

                addItems(oData);
            }
        }

        function addItems(oData) {
            for (var idx in oData) {
                addItem(oData[idx]);
            }
        }

        function createDeleteButton(oItem) {
            var $deleteButton = $('<button/>');
            $deleteButton.text('Delete');
            $deleteButton.click(function() {
                ToDoDemo.Controller.deleteItem(oItem);
            });

            return $deleteButton;
        }

        function addItem(oItem) {
            var $row = $('<tr>');

            var $doneCb = $('<input type="checkbox" />');
            $doneCb.attr('checked', oItem.isDone);

            var $cbHolder = $('<td/>')
            $cbHolder.append($doneCb);

            var $textField = $('<td/>');
            $textField.text(oItem.name);

            var $deleteButton = $('<td/>');
            $deleteButton.append(createDeleteButton(oItem));

            $row.append($cbHolder);
            $row.append($textField);
            $row.append($deleteButton);

            $toDoList.append($row);

        }

        return view;
    },



    createErrorView: function($errorDiv) {
        return {
            displayError: function(strError) {
                alert(strError);
            }
        }
    }
}