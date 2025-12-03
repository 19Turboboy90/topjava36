const mealAjaxUrl = "ui/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl,
    datatableApi: null,

    updateTable: function () {
        const filterData = $("#filterForm").serialize();
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: filterData
        }).done(function (data) {
            renderTable(data);
            successNoty("Filter is applied");
        });
    }
};

function renderTable(data) {
    ctx.datatableApi.clear().rows.add(data).draw();
}

function clearFilter() {
    $("#filterForm")[0].reset();
    ctx.updateTable();
    successNoty("Filter is reset");
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});