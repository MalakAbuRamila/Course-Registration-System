'use strict';
(function () {
    document.addEventListener('DOMContentLoaded', function() {

        //hide the logout message after 5 seconds
        setTimeout(function() {
            let element = document.getElementById('logoutMessage');
            if (element) {
                element.style.display = 'none';
            }
        }, 5000);

        const loginForm = document.getElementById('loginForm');
        const usernameInput = document.getElementById('username');

        //event listener for the submit button
        loginForm.addEventListener('submit', () =>{

            //trim  the username before submitting
            usernameInput.value = usernameInput.value.trim();
        });
    });
})();
