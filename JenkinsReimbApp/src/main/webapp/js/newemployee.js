/**
 * 
 */

let btnSubmit = document.getElementById("btnSubmit");

function clickedSubmit() {
	
	let txtFName = document.getElementById("firstname").value;
	let txtLName = document.getElementById("lastname").value;
	let txtEmail = document.getElementById("email").value;
	let txtUser = document.getElementById("username").value;
	let txtPass = document.getElementById("password").value;
	let txtRPass = document.getElementById("rPassword").value;

	if (txtPass != txtRPass)
		alert("The passwords you entered don't match!");
	else {
		
		btnSubmit.disabled = true;
		let http = new XMLHttpRequest();
		http.onreadystatechange = function() {

			if(http.readyState == 4) {
				let response = http.responseText;
				console.log(response);
				response = JSON.parse(response);

				if (response.status == 'ok') {
					alert('Account successfully created!');
					window.location.href = '../index.html';
				} else if (response.status == 'bad')
					alert("Username or email is taken!");
				else {
					alert('Something went horribly wrong!');
				}
				
				btnSubmit.disabled = false;
			}
		};

		http.open("POST", "/ReimbursementApp/createaccount.do");
		http.setRequestHeader('Content-type',
				'application/x-www-form-urlencoded');
		let params = `fname=${txtFName}&lname=${txtLName}&email=${txtEmail}&username=${txtUser}&password=${txtPass}`;
		http.send(params);
	}
}