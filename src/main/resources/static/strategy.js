


var module_list_strategy ="<span id=\"module-strategy\" class=\"module\">策略</span>";
$("#module-list").append(module_list_strategy);
var button_list_strategy =
    "        <div id=\"button-list-strategy\" class=\"button-list hidden\">\n" +
    "            <input id=\"button-strategy-add\" type=\"button\" value=\"新增\"/>\n" +
    "            <input id=\"button-strategy-update\" type=\"button\" value=\"修改\"/>\n" +
    "            <input id=\"button-strategy-delete\" type=\"button\" value=\"删除\"/>\n" +
    "            <input id=\"button-strategy-get\" type=\"button\" value=\"详情\"/>\n" +
    "            <input id=\"button-strategy-list\" type=\"button\" value=\"列表\"/>\n" +
    "        </div>";
$("#button-list").append(button_list_strategy);

var table_list_strategy ="        <div id=\"table-list-strategy\">\n" +
    "            <div id=\"table-list-strategy-add\" class=\"table-list hidden\">\n" +
    "                名称：<input id=\"table-list-strategy-add-name\" class=\"show-module-refresh\" type=\"text\"/>\n" +
    "                问题：<input id=\"table-list-strategy-add-question\" class=\"show-module-refresh\" type=\"text\"/>\n" +
    "                <input id=\"table-list-strategy-add-confirm\" type=\"button\" value=\"确定\"/>\n" +
    "                <input id=\"table-list-strategy-add-cancel\" type=\"button\" value=\"取消\"/>\n" +
    "            </div>\n" +
    "            <div id=\"table-list-strategy-update\" class=\"table-list hidden\">\n" +
    "                <input id=\"table-list-strategy-update-id\" type=\"hidden\"/>\n" +
    "                名称：<input id=\"table-list-strategy-update-name\" class=\"show-module-refresh\" type=\"text\"/>\n" +
    "                问题：<input id=\"table-list-strategy-update-question\" class=\"show-module-refresh\" type=\"text\"/>\n" +
    "                <input id=\"table-list-strategy-update-confirm\" type=\"button\" value=\"确定\"/>\n" +
    "                <input id=\"table-list-strategy-update-cancel\" type=\"button\" value=\"取消\"/>\n" +
    "            </div>\n" +
    "            <div id=\"table-list-strategy-get\" class=\"table-list hidden\">\n" +
    "                <input id=\"table-list-strategy-get-id\" type=\"hidden\"/>\n" +
    "                名称：<input id=\"table-list-strategy-get-name\" class=\"show-module-refresh\" type=\"text\" readonly/>\n" +
    "                问题：<input id=\"table-list-strategy-get-question\" class=\"show-module-refresh\" type=\"text\" readonly/>\n" +
    "            </div>\n" +
    "            <div id=\"table-list-strategy-list\" class=\"table-list hidden\">\n" +
    "                名称：<input id=\"table-list-strategy-list-name\" class=\"show-module-refresh\" type=\"text\"/>\n" +
    "                页数：<input id=\"table-list-strategy-list-pageNum\" class=\"show-module-refresh\" type=\"text\" value=\"1\"/>\n" +
    "                每页大小：<input id=\"table-list-strategy-list-pageSize\" class=\"show-module-refresh\" type=\"text\" value=\"50\"/>\n" +
    "                <input id=\"table-list-strategy-list-confirm\" type=\"button\" value=\"查询\"/>\n" +
    "                <div id=\"table-list-strategy-list-table\"></div>\n" +
    "            </div>\n" +
    "        </div>";
$("#table-list").append(table_list_strategy);





strategyModuleBindEvent();
showModule("strategy");


function strategyModuleBindEvent() {

    $("#button-strategy-add").click(function (event) {
        showModule(module, "add");
    });
    $("#table-list-strategy-add-confirm").click(function (event) {
        post("/trade/strategy/add", {
            name: $("#table-list-strategy-add-name").val(),
            question: $("#table-list-strategy-add-question").val()
        }, (r) => {
            $("#button-strategy-list").click();
        });
    });
    $("#table-list-strategy-add-cancel").click(function (event) {
        $("#button-strategy-list").click();
    });
    $("#button-strategy-update").click(function (event) {
        var data = getTableSelectedData();
        if (data == null) {
            alert("请选择数据");
            return;
        }
        var id = data.id;
        post("/trade/strategy/get", {
            id: id,
        }, (r) => {
            $("#table-list-strategy-update-id").val(id);
            $("#table-list-strategy-update-name").val(r.name);
            $("#table-list-strategy-update-question").val(r.question);
        });
        showModule(module, "update");
    });
    $("#table-list-strategy-update-confirm").click(function (event) {
        post("/trade/strategy/update", {
            id: $("#table-list-strategy-update-id").val(),
            name: $("#table-list-strategy-update-name").val(),
            question: $("#table-list-strategy-update-question").val()
        }, (r) => {
            $("#button-strategy-list").click();
        });
    });
    $("#table-list-strategy-update-cancel").click(function (event) {
        $("#button-strategy-list").click();
    });

    $("#button-strategy-delete").click(function (event) {
        var data = getTableSelectedData();
        if (data == null) {
            alert("请选择数据");
            return;
        }
        if (!confirm("您确定要删除该数据吗？")) {
            return;
        }
        var id = data.id;
        post("/trade/strategy/delete", {
            id: id,
        }, (r) => {
            $("#button-strategy-list").click();
        });
    });

    $("#button-strategy-get").click(function (event) {
        var data = getTableSelectedData();
        if (data == null) {
            alert("请选择数据");
            return;
        }
        var id = data.id;
        post("/trade/strategy/get", {
            id: id,
        }, (r) => {
            $("#table-list-strategy-get-name").val(r.name);
            $("#table-list-strategy-get-question").val(r.question);
        });
        showModule(module, "get");
    });

    $("#button-strategy-list").click(function (event) {
        showModule(module, "list");
        $("#table-list-strategy-list-confirm").click();
    });

    $("#table-list-strategy-list-confirm").click(function (event) {
        var data = {};
        addData(data, "name", "#table-list-strategy-list-name");
        addData(data, "pageNum", "#table-list-strategy-list-pageNum");
        addData(data, "pageSize", "#table-list-strategy-list-pageSize");
        post("/trade/strategy/list",
            data
            //     {
            //     name: getValue($("#table-list-strategy-list-name").val()),
            //     pageNum: getValue($("#table-list-strategy-list-pageNum").val()),
            //     pageSize: getValue($("#table-list-strategy-list-pageSize").val())
            // }
            , (r) => {
                renderTable("table-list-strategy-list-table", "table-list-strategy-list-table-body", [{
                    "name": "编号",
                    "key": "id"
                }, {
                    "name": "名称",
                    "key": "name"
                }], r.items);
            });
    });
}