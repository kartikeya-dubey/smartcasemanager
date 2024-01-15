console.log("this is script file");

//function to toggle user dashboard sidebar from view

const toggleSidebar = () => {

	if ($('.sidebar').is(":visible")) //check if sidebar is visible? > Hide it and expand content
	{
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "2%");
		$(".content").css("margin-top", "0%");
		$(".open-sidebar-btn").css("display", "block");
	}
	else { // sidebar is hidden > show it and reduce content
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "16%");
		$(".open-sidebar-btn").css("display", "none");
		$(".content").css("margin-top", "3%");
	}
};

//function to ask for deletion confirmation

function deleteCase(cid) {
	swal({
		title: "Are you sure?",
		text: "Once deleted, you will not be able to recover this case details!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {

				window.location = "/user/delete/" + cid;
			}
		});
}

/* Search Case by Custom functions */

const searchCaseId = () => {
	let query = $("#search-caseid").val()

	if (query == "") {
		//if no keyword typed then do nothing and hide suggestion box
		$(".search-caseIdResult").hide();
	}
	else {
		//search
		//console.log(query);

		//sending request to server
		let url = `http://localhost:8282/searchId/${query}`
		fetch(url).then(response => {
			return response.json();
		}).then((data) => {
			//access the json data recieved earlier
			//console.log(data);

			let text = `<div class='list-group'>`;

			data.forEach(caseSuggestions => {
				text += `<a href='/user/case/${caseSuggestions.cid}' class='list-group-item list-group-item-action'>${caseSuggestions.cid} / ${caseSuggestions.clientName} / ${caseSuggestions.caseName}</a>`
			});


			text += `</div>`;

			$(".search-caseIdResult").html(text);
			$(".search-caseIdResult").show()
		});
	}
}

const searchClientName = () => {
	let query = $("#search-client").val()

	if (query == "") {
		//if no keyword typed then do nothing and hide suggestion box
		$(".search-result").hide();
	}
	else {
		//search
		//console.log(query);

		//sending request to server
		let url = `http://localhost:8282/search/${query}`
		fetch(url).then(response => {
			return response.json();
		}).then((data) => {
			//access the json data recieved earlier
			//console.log(data);

			let text = `<div class='list-group'>`;

			data.forEach(caseSuggestions => {
				text += `<a href='/user/case/${caseSuggestions.cid}' class='list-group-item list-group-item-action'>${caseSuggestions.clientName} / ${caseSuggestions.cid} </a>`
			});

			text += `</div>`;

			$(".search-result").html(text);
			$(".search-result").show()
		});
	}
}


const searchCaseName = () => {
	let query = $("#search-casename").val()

	if (query == "") {
		//if no keyword typed then do nothing and hide suggestion box
		$(".search-caseNameResult").hide();
	}
	else {
		//search
		//console.log(query);

		//sending request to server
		let url = `http://localhost:8282/searchCase/${query}`
		fetch(url).then(response => {
			return response.json();
		}).then((data) => {
			//access the json data recieved earlier
			//console.log(data);

			let text = `<div class='list-group'>`;

			data.forEach(caseSuggestions => {
				text += `<a href='/user/case/${caseSuggestions.cid}' class='list-group-item list-group-item-action'>${caseSuggestions.caseName} / ${caseSuggestions.cid}</a>`
			});


			text += `</div>`;

			$(".search-caseNameResult").html(text);
			$(".search-caseNameResult").show()
		});
	}
}


function updateDateTime() {
		var now = new Date();
		var datetimeElement = document.getElementById("datetime");

		// Format the date and time
		var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit', timeZoneName: 'short' };
		var formattedDateTime = now.toLocaleDateString('en-US', options);

		// Update the HTML element
		datetimeElement.textContent = formattedDateTime;
	}

	// Update the date and time every second
	setInterval(updateDateTime, 1000);

	// Initial update
	updateDateTime();
    
	

