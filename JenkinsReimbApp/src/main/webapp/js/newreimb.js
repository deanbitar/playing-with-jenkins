/**
 * 
 */

let user = sessionStorage.getItem("user");
console.log(user);
if(user){
	console.log(user);
	user = JSON.parse(user);
	console.log(user);
	if(user.role != 1){
		window.location.replace("../index.html");
	}
}
else{
	window.location.replace("../index.html");
}

let lblBarMain = document.getElementById("lblBarMain");
lblBarMain.innerHTML = "Welcome, " + user.firstName;

let btnSubmit = document.getElementById("btnSubmit");
let btnCancel = document.getElementById("btnCancel");

let txtAmount = document.getElementById("amount");
let slcType = document.getElementById("type");
let txtDesc = document.getElementById("desc");

function clickedSubmit(){
	btnSubmit.disabled = true;
	btnCancel.disabled = true;
	
	let http = new XMLHttpRequest();
	
	http.onreadystatechange = function(){
		if(http.readyState == 4){
			
			let response = http.responseText;
			
			if(response){
				response = JSON.parse(response);
				
				if(response.status == "ok")
					window.location.replace("./employee.html");
				else {
					alert("Something went wrong");
				}
			}
			else{
				alert("Something went wrong");
			}
			
			btnSubmit.disabled = false;
			btnCancel.disabled = false;
		}
	};
	
	let authorId = user.id;
	let amount = txtAmount.value;
	let type = slcType.options[slcType.selectedIndex].value;
	let desc = txtDesc.value;
	
	let params = `authorid=${authorId}&amount=${amount}&type=${type}&desc=${desc}`;
	
	http.open("POST", "./addNewReimb.do", true);
	http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	http.send(params);
}

function clickedCancel(){
	window.location.replace("./employee.html");
}

function clickedLogout(){
	sessionStorage.setItem("user", "");
	window.location.replace("../index.html");
}