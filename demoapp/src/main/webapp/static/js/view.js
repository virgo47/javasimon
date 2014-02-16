var ToDoDemo = window.ToDoDemo || {}

ToDoDemo.View = {

    init: function() {
        $("#itemForm").hide();
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
            $deleteButton.button({
                icons: {
                    primary: "ui-icon-trash"
                },
                text: false
            });

            $deleteButton.attr("title", "Delete this item");
            $deleteButton.tooltip();
            $deleteButton.click(function() {
                ToDoDemo.Controller.deleteItem(oItem);
            });

            return $deleteButton;
        }

        function createUpdateButton(oItem) {
            var $updateButton = $('<button/>');
            $updateButton.button({
                icons: {
                    primary: "ui-icon-pencil"
                },
                text: false
            });
            $updateButton.attr("title", "Update this item");
            $updateButton.tooltip();
            $updateButton.click(function() {
                ToDoDemo.Controller.updateItem(oItem);
            });

            return $updateButton;
        }

        function createCheckbox(oItem) {
            var $checkBox = $('<input type="checkbox"/>')
            $checkBox.change(function(){
                if(this.checked)
                    ToDoDemo.Controller.checkItem(oItem, true);
                else
                    ToDoDemo.Controller.checkItem(oItem, false);
            });

            return $checkBox;
        }

        function addItem(oItem) {
            var $row = $('<tr>');

            var $doneCb = createCheckbox(oItem);
            $doneCb.attr('checked', oItem.done);

            var $cbHolder = $('<td/>');
            $cbHolder.append($doneCb);

            var $nameField = $('<td/>');
            $nameField.text(oItem.name);

            var $descriptionField = $('<td/>');
            $descriptionField.text(oItem.description);

            var $deleteButton = $('<td/>');
            var $updateButton = $('<td/>');
            $deleteButton.append(createDeleteButton(oItem));
            $updateButton.append(createUpdateButton(oItem));

            $row.append($cbHolder);
            $row.append($nameField);
            $row.append($descriptionField);
            $row.append($deleteButton);
            $row.append($updateButton);

            $toDoList.append($row);

        }

        return view;
    },

    createItemForm: function(fnOnAccept, oItem) {
        function createItem() {
             var $itemNameInput = $("#itemNameField");
             var $itemDescriptionInput = $("#descriptionField");

             var itemName = $itemNameInput.val();
             var itemDescription = $itemDescriptionInput.val();
             var newItem = {
                 name: itemName,
                 done: false,
                 description: itemDescription
             };

             if (oItem) {
                newItem.id = oItem.id;
                newItem.done = oItem.done;
             }

             return newItem;
        }

        function clearItemForm() {
            $("#itemNameField").val("");
            $("#descriptionField").val("");
            $errorMessages = $("#itemForm label.error");
            $errorMessages.remove();
        }

        function fillItemForm() {
            if (oItem) {
                $("#itemNameField").val(oItem.name);
                $("#descriptionField").val(oItem.description);
            }
        }

        clearItemForm();
        var acceptButtonName = oItem ? "Update" : "Create";

        var formButtons = {};
        formButtons[acceptButtonName] = function() {
            if ($('#form').valid()) {
                fnOnAccept(createItem());
            }
        };
        formButtons["Cancel"] = function() {
            createForm.dialog("close");
        }

        var createForm = $("#itemForm").dialog({
             autoOpen: false,
             height: 300,
             width: 350,
             modal: true,
             buttons: formButtons
        });
        fillItemForm();

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

    createErrorView: function($errorDiv) {
        return {
            displayError: function(strError) {
                alert(strError);
            }
        }
    }
}