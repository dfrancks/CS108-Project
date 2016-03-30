function textChanged(toUserID) {
	console.log(toUserID);
	var textArea = document.getElementById("textArea");
	if (textArea.value.length === 0 || textArea.value.trim() === '' || toUserID == -1) {
		document.getElementById("submit").disabled = true;
	} else {
		document.getElementById("submit").disabled = false;
	}
}

function checkDisable(quizzesTaken, toUserID) {
	console.log(toUserID);
	if (document.getElementById("typeSelect") === "challenge" && quizzesTaken === 0 || toUserID === -1 ||
		document.getElementById("textArea").value.length === 0 || document.getElementById("textArea").value.trim() === '') {
		document.getElementById("submit").disabled = true;
	} else {
		document.getElementById("submit").disabled = false;
	}
}