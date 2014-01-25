var ToDoDemo = window.ToDoDemo || {}

ToDoDemo.Model = {
    getAll: function(fnSuccess, fnFail) {
        $.ajax({
            url: ToDoDemo.serverRoot + "/items",
            type:'GET',
            success: function(oData, strStatus, jqXHR) {
                var oParsed = JSON.parse(oData);
                fnSuccess(oParsed);
            },
            error: function(jqXHR, strStatus, strErrorThrown) {
                fnFail(strErrorThrown);
            }
        });
    },

    addItem: function(oItem, fnSuccess, fnFail) {
         $.ajax({
             url: ToDoDemo.serverRoot + "/addItem",
             type:'POST',
             contentType: 'application/json',
             dataType:"json",
             data: JSON.stringify(oItem),
             success: function() {
                 fnSuccess();
             },
             error: function(jqXHR, strStatus, strErrorThrown) {
                 fnFail(strErrorThrown);
             }
         });
    },

    deleteItem: function(numId, fnSuccess, fnFail) {
        $.ajax({
            url: ToDoDemo.serverRoot + "/deleteItem/" + numId,
            type:'POST',
            success: function(oData, strStatus, jqXHR) {
                fnSuccess();
            },
            error: function(jqXHR, strStatus, strErrorThrown) {
                fnFail(strErrorThrown);
            }
        });
    },

    updateItem: function(oItem, fnSuccess, fnFail) {
        $.ajax({
             url: ToDoDemo.serverRoot + "/updateItem",
             type:'POST',
             contentType: 'application/json',
             dataType:"json",
             data: JSON.stringify(oItem),
             success: function() {
                 fnSuccess();
             },
             error: function(jqXHR, strStatus, strErrorThrown) {
                 fnFail(strErrorThrown);
             }
         });
    }

}