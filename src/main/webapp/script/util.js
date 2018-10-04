function getId(id) {
    return document.getElementById(id);
}

function htmlDecode(input){
    var e = document.createElement('div');
    e.innerHTML = input;
    return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
}

function post(path, params, method, doc) {
    method = method || "post"; // Set method to post by default if not specified.
    doc = doc || document;

    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = doc.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for(var key in params) {
        if(params.hasOwnProperty(key)) {
            var hiddenField = doc.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
        }
    }

    doc.body.appendChild(form);
    form.submit();
}

function encodePost(msg){
    msg = msg.replace('ä', '&auml;');
    msg = msg.replace('ö', '&ouml;');
    msg = msg.replace('ü', '&uuml;');
    msg = msg.replace('Ä', '&Auml;');
    msg = msg.replace('Ö', '&Ouml;');
    msg = msg.replace('Ü', '&Uuml;');
    msg = msg.replace('ß', '&szlig;');
    return msg;
}
