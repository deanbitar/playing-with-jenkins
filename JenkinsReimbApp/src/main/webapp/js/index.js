/**
 * 
 */

let btnLogin = document.getElementById("btnLogin");

function clickedLogin() {
	btnLogin.disabled = true;

	let username = document.getElementById("txtUsername").value;
	let password = document.getElementById("txtPassword").value;

	console.log(username);
	console.log(password);

	let http = new XMLHttpRequest();

	http.onreadystatechange = function() {
		if (http.readyState == 4) {
			let response = http.responseText;
			console.log(response);
			let user = JSON.parse(response);
			let loggedIn = user.id;

			if (loggedIn) {
				sessionStorage.setItem("user", response);
				if (user.role == 1) {// logged in as employee
					window.location
							.href = "/JenkinsReimbApp/pages/employee.html";
				} else if (user.role == 0) { // logged in as manager
					window.location
					.href = "/JenkinsReimbApp/pages/manager.html";
				} else {
					alert("something went wrong");
					btnLogin.disabled = false;
				}
			}
			else {
				alert("Invalid username or password!");
				btnLogin.disabled = false;
			}
		}
	};

	http.open("POST", "./login.do", true);
	http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	let params = `username=${username}&password=${password}`;
	console.log(params);
	http.send(params);
}