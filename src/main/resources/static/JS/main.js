'use strict';
(function (){
    document.addEventListener('DOMContentLoaded', function() {

        //hide the error message if the user enters an input
        const errorMessage = document.getElementById('errorMessage');
        if (errorMessage) {
            document.querySelectorAll('input').forEach(input => {
                input.addEventListener('input', () => {
                    errorMessage.style.display = 'none';
                });
            });
        }

        //trim the first name, last name and email inputs of the registration form
        const registrationForm = document.getElementById('registrationForm');
        if (registrationForm) {

            //event listener for the registration form
            registrationForm.addEventListener('submit', function(event) {
                event.preventDefault();

                const firstNameInput = document.getElementById('firstName');
                const lastNameInput = document.getElementById('lastName');
                const emailInput = document.getElementById('email');

                if (firstNameInput) firstNameInput.value = firstNameInput.value.trim();
                if (lastNameInput) lastNameInput.value = lastNameInput.value.trim();
                if (emailInput) emailInput.value = emailInput.value.trim();

                //submit the form after trimming
                registrationForm.submit();
            });
        }

        //trim the email input of the check course form
        const checkCoursesForm = document.getElementById('checkCoursesForm');
        if (checkCoursesForm) {
            //event listener for the check course form
            checkCoursesForm.addEventListener('submit', function(event) {
                event.preventDefault();

                const emailInput = document.getElementById('email');
                if (emailInput) emailInput.value = emailInput.value.trim();

                //submit the form after trimming
                checkCoursesForm.submit();
            });
        }

        //trim the name and max participants input in the new course form and edit course form
        const newCourseForm = document.getElementById('newCourseForm');
        if (newCourseForm) {
            //event listener for the new course form and the edit course form
            newCourseForm.addEventListener('submit', function(event) {
                event.preventDefault();

                const nameInput = document.getElementById('name');
                const maxParticipantsInput = document.getElementById('maxParticipants');

                if (nameInput) nameInput.value = nameInput.value.trim();
                if (maxParticipantsInput) maxParticipantsInput.value = maxParticipantsInput.value.trim();

                //submit the form after trimming
                newCourseForm.submit();
            });
        }

    });
})();