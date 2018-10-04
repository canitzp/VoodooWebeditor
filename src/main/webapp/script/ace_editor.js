function findGetParameter(parameterName) {
    var result = "",
        tmp = [];
    var items = location.search.substr(1).split("&");
    for (var index = 0; index < items.length; index++) {
        tmp = items[index].split("=");
        if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
    }
    return result;
}

function htmlDecode(input){
    var e = document.createElement('div');
    e.innerHTML = input;
    return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
}


function initAce(mode, content){
    editor.getSession().setMode("ace/mode/" + mode);
    editor.getSession().setNewLineMode("unix");
    editor.setTheme("ace/theme/chaos");
    editor.setValue(htmlDecode(content), -1);
}
