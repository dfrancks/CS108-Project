function loadQuestionTypes(){
	var innerHTML = "<p>Question Type</p>";
	innerHTML += "<select id = \"questionchoice\" onChange=\"detectBoxChange()\">";
	innerHTML += "<option disabled selected> --- Select an option --- </option>";
	innerHTML += "<option value=\"1\">Question-Response</option>";
	innerHTML += "<option value=\"2\">Fill in the Blank</option>";
	innerHTML += "<option value=\"3\">Multiple Choice</option>";
	innerHTML += "<option value=\"4\">Picture-Response</option>";
	innerHTML += "</select>";
	
	var buttonElem = document.getElementById("button");
	buttonElem.disabled = true;
	
	var elem = document.getElementById("questiontype");
	elem.innerHTML = innerHTML;
	
	document.getElementById("points").style = "display:inline block;";
}

function detectBoxChange(){
	elem = document.getElementById("questiontype");
	createQuestion();
	
	var buttonElem = document.getElementById("button");
	buttonElem.parentNode.removeChild(buttonElem);
	
	var saveButton = document.createElement("input");
	saveButton.setAttribute("type", "submit");
	saveButton.setAttribute("value", "Save Question");

	
	var divButton = document.getElementById("divbutton");
	divButton.appendChild(saveButton);
	console.log("index");
}

function createAddAndRemoveButtons(){
	addButton = document.createElement("input");
	addButton.type = "button";
	addButton.value = "Add option";
	addButton.onclick = function() {addTextAreaOptions()};
	elem.appendChild(addButton);
	
	removeButton = document.createElement("input");
	removeButton.disabled = true;
	removeButton.type = "button";
	removeButton.value = "Remove option.";
	removeButton.onclick = function() {removeTextAreaOptions()};
	elem.appendChild(removeButton);
	choice = ["Existing"];
}

function createQuestion(){
	var select = document.getElementById("questionchoice");
	elem.innerHTML = "<input type = \"hidden\" name = \"questionType\" value = " + select.value + " />";
	if(select.value == 1){
		elem.innerHTML += "<p>Question-Response</p>";
		elem.innerHTML += "<textarea name= \"question\" rows = \"3\" cols = \"50\" placeholder = \"Put question here.\"></textarea><p></p>";
		elem.innerHTML += "<textarea name= \"answer\" rows = \"1\" cols = \"50\" placeholder = \"Put answer here.\"></textarea><p></p>";
		createAddAndRemoveButtons();
	} else if (select.value == 2){
		elem.innerHTML += "<p>Fill in the Blank</p>"
		elem.innerHTML += "<textarea name = \"question\" id = \"question\" rows = \"2\" cols = \"30\" onkeyup = \"clearAnswer()\" onselect = \"getHighlightedText()\" placeholder = \"Put question here.\"></textarea><p></p>";
		elem.innerHTML += "<textarea name = \"answer\" id = \"answer\" rows = \"2\" cols = \"30\" type = \"text\" placeholder = \"Highlight part of question for answer.\" readonly= \"readonly\"/></textarea><p></p>";
		elem.innerHTML += "<input type = \"hidden\" name = \"primaryQuestion\" id = \"primaryQuestion\" value = \"\" />";
		elem.innerHTML += "<input type = \"hidden\" name = \"secondaryQuestion\" id = \"secondaryQuestion\" value = \"\" />";
		createAddAndRemoveButtons();
	} else if (select.value == 3){
		elem.innerHTML += "<p>Multiple Choice</p>";
		elem.innerHTML += "<textarea name= \"question\" rows = \"3\" cols = \"50\" placeholder = \"Put question here.\"></textarea><p></p>";
		addButton = document.createElement("input");
		addButton.type = "button";
		addButton.value = "Add option";
		addButton.onclick = function() {addOptions()};
		elem.appendChild(addButton);
		
		removeButton = document.createElement("input");
		removeButton.disabled = true;
		removeButton.type = "button";
		removeButton.value = "Remove option.";
		removeButton.onclick = function() {removeOptions()};
		elem.appendChild(removeButton);
		
		hidden = document.createElement("input");
		hidden.name = "length";
		hidden.value = 0;
		hidden.type = "hidden";
		elem.appendChild(hidden);
		
		choice = [];
		createRadio();
		createField();
	} else{
		elem.innerHTML += "<p>Picture-Response</p>";
		elem.innerHTML += "<textarea name= \"question\" rows = \"3\" cols = \"50\" placeholder = \"Put picture link here.\"></textarea><p></p>";
		elem.innerHTML += "<textarea name= \"answer\" rows = \"1\" cols = \"50\" placeholder = \"Put answer here.\"></textarea><p></p>";
		createAddAndRemoveButtons();
	}
}


function createRadio(){
	radio = document.createElement("input");
	if (choice.length == 0) {
		radio.checked = true;
	}
	radio.name = "radio";
	radio.type = "radio";
	radio.value = choice.length/2;
	elem.insertBefore(radio, addButton);
	choice.push(radio);
}

function createField(){
	field = document.createElement("input");
	field.name = "field" + (choice.length-1)/2;
	field.type = "text";
	field.placeholder = "Option";
	elem.insertBefore(field, addButton);
	
	var paragraph = document.createElement("p");
	elem.insertBefore(paragraph, addButton);
	choice.push(field);
	hidden.value = choice.length/2;
}

function addOptions(){
	createRadio();
	createField();
	removeButton.disabled = false;
}

function createTextArea(){
	field = document.createElement("textarea");
	field.name = "answer";
	field.rows = "1";
	field.cols = "50";
	field.placeholder = "Put answer here.";
	elem.insertBefore(field, addButton);
	
	var paragraph = document.createElement("p");
	elem.insertBefore(paragraph, addButton);
	choice.push(field);
}

function addTextAreaOptions(){
	createTextArea();
	removeButton.disabled = false;
	console.log(choice);
}

function removeTextAreaOptions() {
	field = choice.pop();
	elem.removeChild(field);
	//hidden.value = choice.length / 2;
	if(choice.length <= 1){
		removeButton.disabled = true;
	}
	console.log(choice);
}

function removeOptions(){
	field = choice.pop();
	elem.removeChild(field);
	radio = choice.pop();
	elem.removeChild(radio);
	hidden.value = choice.length / 2;
	if(choice.length <= 2){
		removeButton.disabled = true;
	}
}

function clearAnswer(){
	var textAreaAnswer = document.getElementById("answer")
	textAreaAnswer.value = "";
	document.getElementById("primaryQuestion").value = "";
	document.getElementById("secondaryQuestion").value = "";
}

function getHighlightedText(){
	var textAreaQuestion = document.getElementById("question");
	var textAreaAnswer = document.getElementById("answer")
	var primaryQuestion = textAreaQuestion.value.substring(0, textAreaQuestion.selectionStart);
	var secondaryQuestion = textAreaQuestion.value.substring(textAreaQuestion.selectionEnd, textAreaQuestion.value.length)
	var selectedText = textAreaQuestion.value.substring(textAreaQuestion.selectionStart, textAreaQuestion.selectionEnd);
	textAreaAnswer.value = selectedText;
	document.getElementById("primaryQuestion").value = primaryQuestion;
	document.getElementById("secondaryQuestion").value = secondaryQuestion;
}

function titleTextChanged() {
	var textAreaTitle = document.getElementById("title");
	if (textAreaTitle.value.length === 0 || textAreaTitle.value.trim() === '') {
		document.getElementById("submitquiz").disabled = true;
	} else {
		document.getElementById("submitquiz").disabled = false;
	}
}

function checkDisable() {
	if (document.getElementById("title").value.length === 0 || document.getElementById("title").value.trim() === '') {
			document.getElementById("submitquiz").disabled = true;
		} else {
			document.getElementById("submitquiz").disabled = false;
		}
}