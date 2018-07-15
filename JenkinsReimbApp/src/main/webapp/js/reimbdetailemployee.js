/**
 * 
 */

let user;

function loadCredentials() {
	
	user = sessionStorage.getItem("user");
	
	if (user) {
		console.log(user);
		user = JSON.parse(user);
		console.log(user);
		if (user.role != 1) {
			window.location.replace("./index.html");
		}
		else {
			let lblBarMain = document.getElementById("lblBarMain");
			lblBarMain.innerHTML = "Welcome, " + user.firstName;
			loadReimb();
		}
	} else {
		window.location.replace("./index.html");
	}
}

let reimb;

function loadReimb() {
	
	let reimbId = sessionStorage.getItem("reimbid");

	if (reimbId) {
		loadReimbFromDB(reimbId);
	} else {
		window.location.replace("./pages/employee.html");
	}
}

function loadReimbFromDB(reimbId) {
	let http = new XMLHttpRequest();

	http.onreadystatechange = function() {
		if (http.readyState == 4) {
			let response = http.responseText;
			response = JSON.parse(response);
			console.log(response);
			reimb = response;
			printData();
		}
	};

	http.open("POST", "./getdetailreimbbyid.do", true);
	http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	let params = `reimbid=${reimbId}`;
	http.send(params);
}

let hdReimbId = document.getElementById("reimbId");
let hdReimbAthr = document.getElementById("reimbAuthor");
let hdReimbAmt = document.getElementById("reimbAmount");
let hdReimbTyp = document.getElementById("reimbType");
let pReimbDsc = document.getElementById("reimbDesc");
let hdReimbSbm = document.getElementById("reimbTimeSubmitted");
let hdReimbSts = document.getElementById("reimbStatus");
let hdReimbRsvr = document.getElementById("reimbResolver");
let hdReimbTmRsvd = document.getElementById("reimbTimeResolved");

function printData() {

	hdReimbId.innerText = ` ${reimb.id}`;
	hdReimbAthr.innerText = ` ${reimb.author}`;
	hdReimbAmt.innerText = ` $${reimb.amount}`;
	hdReimbTyp.innerText = ` ${getType(reimb.type)}`;
	pReimbDsc.innerHTML = ` ${reimb.description}`;
	hdReimbSbm.innerText = ` ${getTime(reimb.submittedTime)}`;
	hdReimbSts.innerText = ` ${getStatus(reimb.status)}`;
	hdReimbRsvr.innerText = ` ${reimb.resolver}`;
	hdReimbTmRsvd.innerText = ` ${getTime(reimb.resolvedTime)}`;

	if (!reimb.description) {
		pReimbDsc.innerHTML = "No description";
	}
}

function getStatus(status) {

	switch (status) {
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

function getType(type) {

	switch (type) {
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

function getTime(time) {
	if (!time)
		return "Unresolved";

	let date = new Date(time);
	return `${date.getMonth()+1}-${date.getDate()}-${date.getFullYear()}`;
}

function clickedBack() {
	sessionStorage.setItem("reimbid", "");
	window.location.replace("./pages/employee.html");
}

function clickedLogout() {
	sessionStorage.setItem("user", "");
	window.location.replace("./index.html");
}