/**
 * 
 */

let user;

function loadCredentials() {
	
	user = sessionStorage.getItem("user")

	if (user) {
		console.log(user);
		user = JSON.parse(user);
		console.log(user);
		if (user.role != 0) {
			window.location.replace("/JenkinsReimbApp/index.html");
		}
		else {
			let lblBarMain = document.getElementById("lblBarMain");
			lblBarMain.innerHTML = "Welcome, " + user.firstName;
			loadReimb();
		}
	} else {
		window.location.replace("/JenkinsReimbApp/index.html");
	}
}

let reimb;

function loadReimb() {
	
	let reimbId = sessionStorage.getItem("reimbid");
	console.log(reimbId);

	if (reimbId) {
		loadReimbFromDB(reimbId);
	} else {
		window.location.replace("/JenkinsReimbApp/pages/manager.html");
	}
}

function loadReimbFromDB(reimbId) {
	let http = new XMLHttpRequest();
	
	http.onreadystatechange = function() {
		if(http.readyState == 4) {
			let response = http.responseText;
			response = JSON.parse(response);
			console.log(response);
			reimb = response;
			printData();
		}
	};
	
	http.open("POST", "/JenkinsReimbApp/getdetailreimbbyid.do", true);
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

let btnApprove = document.getElementById("btnApprove");
let btnDeny = document.getElementById("btnDeny");

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
	
	btnApprove.disabled = reimb.status;
	btnDeny.disabled = reimb.status;
}

function clickedApprove() {
	btnApprove.disabled = true;
	btnDeny.disabled = true;
	sendDecision(1);
}

function clickedDeny() {
	btnApprove.disabled = true;
	btnDeny.disabled = true;
	sendDecision(2)
}

function sendDecision(status) {
	let http = new XMLHttpRequest();
	
	http.onreadystatechange = function() {
		if(http.readyState == 4) {
			let response = http.responseText;
			console.log(response);
			response = JSON.parse(response);
			
			if(response.status == "ok")
				window.location.replace("/JenkinsReimbApp/pages/manager.html");
			else{
				alert("Something went wrong");
				btnApprove.disabled = false;
				btnDent.disabled = false;
			}
		}
	}
	
	http.open("POST", "/JenkinsReimbApp/giveverdict.do", true);
	http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	let params = `resolver=${user.id}&reimbid=${reimb.id}&status=${status}`;
	http.send(params);
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
	if(!time)
		return "Unresolved";
	
	let date = new Date(time);
	return `${date.getMonth()+1}-${date.getDate()}-${date.getFullYear()}`;
}

function clickedBack() {
	sessionStorage.setItem("reimbid", "");
	window.location.replace("/JenkinsReimbApp/pages/manager.html");
}

function clickedLogout() {
	sessionStorage.setItem("user", "");
	window.location.replace("/JenkinsReimbApp/index.html");
}