
var root = /*[[@{/}]]*/"/";
var obj = {};

function executedButton() {


    document.getElementById("resultShow").value = "脚本执行中....." ;
    document.getElementById("executedTime").innerHTML = "运行时间 : --" ;

    obj.sql = document.getElementById("sql").value;
    obj.jobType = document.getElementById("jobType").value;
    obj.executeMode = document.getElementById("executeMode").value;
    obj.jobName = document.getElementById("jobName").value;
    obj.timeout = document.getElementById("timeout").value;
    obj.silence = document.getElementById("silence").value;
    // obj.tags = document.getElementById("tags").value;
    obj.sessionPerUser = document.getElementById("sessionPerUser").value;
    // obj.sessionPerRequest = document.getElementById("sessionPerRequest").value;
    obj.async = document.getElementById("async").value;
    obj.callback = document.getElementById("callback").value;
    obj.skipInclude = document.getElementById("skipInclude").value;
    obj.skipAuth = document.getElementById("skipAuth").value;
    obj.skipGrammarValidate = document.getElementById("skipGrammarValidate").value;




    $.ajax({
        url:  root + "cluster/api_v1/run/script",
        type: "post",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(obj),
        success: function (resonse) {
            document.getElementById("resultShow").value = "" ;
            document.getElementById("resultShow").value = resonse.resultStr;
            document.getElementById("executedTime").innerHTML = resonse.executedTime;
        },
        error: function (resonse) {
            document.getElementById("resultShow").value = "执行失败";
        }
    });
}
