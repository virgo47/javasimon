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

    create: function(oItem, fnSuccess, fnFail) {
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

    update: function(oItem, fnSuccess, fnFail) {
    }

}