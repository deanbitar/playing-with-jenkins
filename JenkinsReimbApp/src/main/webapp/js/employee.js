/**
 * 
 */

let user = sessionStorage.getItem("user");

if(user){
	console.log(user);
	user = JSON.parse(user);
	console.log(user);
	if(user.role != 1){
		window.location.replace("../index.html");
	}
	else {
		loadEmployee();
	}
}
else{
	window.location.replace("../index.html");
}

let lblBarMain = document.getElementById("lblBarMain");
lblBarMain.innerHTML = "Welcome, " + user.firstName;

let reimbursements;

function loadEmployee(){
	console.log("starting request...");
	let http = new XMLHttpRequest();
	
	http.onreadystatechange = function(){
		if(http.readyState == 4){
			console.log("response received");
			let response = http.responseText;
			console.log(response);
			response = JSON.parse(response);
			console.log(response);
			if(Array.isArray(response)){
				reimbursements = response;
				populateReimbsTable();
			}
		}
	};
	
	http.open("POST","./getAuthorReimbs.do", true);
	http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	http.send(`authorid=${user.id}`);
}

function populateReimbsTable(){
	let table = document.getElementById("reimbsTableBody");
	
	for(let reimb of reimbursements){
		let newRow = document.createElement("tr");
		
		let idCol = document.createElement("td");
		idCol.innerHTML = reimb.id;
		let amtCol = document.createElement("td");
		amtCol.innerHTML = reimb.amount;
		let submitCol = document.createElement("td");
		submitCol.innerHTML = getTime(reimb.submittedTime);
		let typeCol = document.createElement("td");
		typeCol.innerHTML = getType(reimb.type);
		let statusCol = document.createElement("td");
		statusCol.innerHTML = getStatus(reimb.status);
		
		let actCol = document.createElement("td");
		let actBtn = document.createElement("button");
		actBtn.innerText = "Details";
		actBtn.classList.add("btn");
		actBtn.classList.add("btn-primary");
		actBtn.onclick = function() {
			sessionStorage.setItem("reimbid", reimb.id);
			window.location.href = "./reimbdetailemployee.html";
		};
		actCol.appendChild(actBtn);
		
		newRow.appendChild(idCol);
		newRow.appendChild(amtCol);
		newRow.appendChild(submitCol);
		newRow.appendChild(typeCol);
		newRow.appendChild(statusCol);
		newRow.appendChild(actCol);
		
		table.appendChild(newRow);
	}
}

function clickedNewReimb(){
	window.location.href = "./newreimb.html";
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

function clickedLogout(){
	sessionStorage.setItem("user", "");
	window.location.replace("../index.html");
}