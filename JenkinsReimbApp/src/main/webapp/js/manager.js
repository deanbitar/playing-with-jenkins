/**
 * 
 */

let user = sessionStorage.getItem("user");

if(user){
	console.log(user);
	user = JSON.parse(user);
	console.log(user);
	if(user.role != 0){
		window.location.replace("/JenkinsReimbApp/index.html");
	}
	else {
		loadManager();
	}
}
else{
	window.location.replace("/JenkinsReimbApp/index.html");
}

let lblBarMain = document.getElementById("lblBarMain");
lblBarMain.innerHTML = "Welcome, " + user.firstName;

let reimbursements;

function loadManager(){
	let http = new XMLHttpRequest();
	
	http.onreadystatechange = function(){
		if(http.readyState == 4){
			let response = http.responseText;
			response = JSON.parse(response);
			if(Array.isArray(response)){
				reimbursements = response;
				populateReimbsTable();
			}
		}
	};
	
	http.open("POST", "/JenkinsReimbApp/getAllReimbs.do", true);
	http.send();
}

function populateReimbsTable(){
	
	let table = document.getElementById("reimbsTableBody");
	
	for(let reimb of reimbursements){
		let newRow = document.createElement("tr");
		
		let idCol = document.createElement("td");
		idCol.innerText = reimb.reimbId;
		let amtCol = document.createElement("td");
		amtCol.innerText = reimb.amount;
		let athCol = document.createElement("td");
		athCol.innerText = reimb.author;
		let submitCol = document.createElement("td");
		submitCol.innerText = getTime(reimb.timeSubmitted);
		let typeCol = document.createElement("td");
		typeCol.innerText = getType(reimb.type);
		let statusCol = document.createElement("td");
		statusCol.innerText = getStatus(reimb.status);
		let actCol = document.createElement("td");
		let actBtn = document.createElement("button");
		actBtn.innerText = "Details";
		actBtn.classList.add("btn");
		actBtn.classList.add("btn-primary");
		actBtn.onclick = function() {
			sessionStorage.setItem("reimbid", reimb.reimbId);
			window.location.href = "/JenkinsReimbApp/pages/reimbdetailmanager.html";
		};
		actCol.appendChild(actBtn);
		
		
		newRow.appendChild(idCol);
		newRow.appendChild(amtCol);
		newRow.appendChild(athCol);
		newRow.appendChild(submitCol);
		newRow.appendChild(typeCol);
		newRow.appendChild(statusCol);
		newRow.appendChild(actCol);
		
		table.appendChild(newRow);
	}
}

function getStatus(status){
	
	switch(status){
	case 0:
		return "Pending";
	case 1:
		return "Approved";
	case 2:
		return "Denied";
	default:
		return "Unknown";
	}
}

function getType(type){
	
	switch(type){
	case 0:
		return "Lodging";
	case 1:
		return "Travel";
	case 2:
		return "Food";
	case 3:
		return "Other";
	default:
		return "Unknown";
	}
}

function getTime(time){
	let date = new Date(time);
	return `${date.getMonth()+1}-${date.getDate()}-${date.getFullYear()}`;
}

function filterTable() {
	
	let value = document.getElementById("filter").value.toLowerCase();
	console.log(value);
	let table = document.getElementById("table");
	let tr = table.getElementsByTagName("tr");
	let td;
	
	for (let i = 0; i < tr.length; i++) {
	    td = tr[i].getElementsByTagName("td")[5];
	    if (td) {
	    	if (td.innerHTML.toLowerCase().indexOf(value) > -1) {
	    		tr[i].style.display = "";
	    	} else {
	    		tr[i].style.display = "none";
	    	}
	    } 
	}
}

function clickedLogout(){
	sessionStorage.setItem("user", "");
	window.location.replace("/JenkinsReimbApp/index.html");
}