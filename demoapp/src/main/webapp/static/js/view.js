var ToDoDemo = window.ToDoDemo || {}

ToDoDemo.View = {

    init: function() {
        $("#dialog-form").hide();
    },

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

        function createUpdateButton(oItem) {
            var $updateButton = $('<button/>');
            $updateButton.text('Update');
            $updateButton.click(function() {
                ToDoDemo.Controller.updateItem(oItem);
            });

            return $updateButton;
        }

        function addItem(oItem) {
            var $row = $('<tr>');

            var $doneCb = $('<input type="checkbox" />');
            $doneCb.attr('checked', oItem.isDone);

            var $cbHolder = $('<td/>');
            $cbHolder.append($doneCb);

            var $textField = $('<td/>');
            $textField.text(oItem.name);

            var $deleteButton = $('<td/>');
            var $updateButton = $('<td/>');
            $deleteButton.append(createDeleteButton(oItem));
            $updateButton.append(createUpdateButton(oItem));

            $row.append($cbHolder);
            $row.append($textField);
            $row.append($deleteButton);
            $row.append($updateButton);

            $toDoList.append($row);

        }

        return view;
    },

    createItemForm: function(fnOnCreate) {
        var createForm = $("#dialog-form").dialog({
            autoOpen: false,
            height: 300,
            width: 350,
            modal: true,
            buttons: {
                "Create": function() {
                    fnOnCreate(ToDoDemo.View.createItem());
                },
                "Cancel": function() {
                    $(this).dialog("close");
                }
            }
        });

        this.clearItemForm();
        return {
            displayError: function(sErrorText) {
                alert(sErrorText);
            },

            close: function() {
                createForm.dialog("close");
                createForm.dialog("destroy");
            },

            open: function() {
                createForm.dialog("open");
            }
        };
    },

    clearItemForm: function() {
        $("#itemNameField").val("");
        $("#descriptionField").val("");
    },

     createItem: function(oPrevItem) {
         var $itemNameInput = $("#itemNameField");
         var $itemDescriptionInput = $("#descriptionField");

         var itemName = $itemNameInput.val();
         var itemDescription = $itemDescriptionInput.val();
         var newItem = {
             name: itemName,
             done: false,
             description: itemDescription
         };

         if (oPrevItem) {
            newItem.id = oPrevItem.id;
         }

         return newItem;
    },

    updateItemForm: function(fnOnUpdate, oItem) {

         var createForm = $("#dialog-form").dialog({
             autoOpen: false,
             height: 300,
             width: 350,
             modal: true,
             buttons: {
                 "Update": function() {
                     fnOnUpdate(ToDoDemo.View.createItem(oItem));
                 },
                 "Cancel": function() {
                     $(this).dialog("close");
                 }
             }
         });

         this.fillItemForm(oItem);
         return {
             displayError: function(sErrorText) {
                 alert(sErrorText);
             },

             close: function() {
                 createForm.dialog("close");
                 createForm.dialog("destroy");
             },

             open: function() {
                 createForm.dialog("open");
             }
         };
    },

    fillItemForm: function(oItem) {
        $("#itemNameField").val(oItem.name);
        $("#descriptionField").val(oItem.description);
    },

    createErrorView: function($errorDiv) {
        return {
            displayError: function(strError) {
                alert(strError);
            }
        }
    }
}