'use strict';
(function () {
    document.addEventListener('DOMContentLoaded', function() {
        function validateForm(event) {
            event.preventDefault();

            let checkboxes = document.getElementsByName("selectedCourses");
            let checked = Array.prototype.slice.call(checkboxes).some(function (el) {
                return el.checked;
            });

            if (!checked) {
                //show the Bootstrap modal
                let modal = new bootstrap.Modal(document.getElementById('errorModal'));
                modal.show();
            } else {
                //submit the form if at least one course is selected
                document.getElementById('homePageForm').submit();
            }
        }

        document.getElementById('homePageForm').onsubmit = validateForm;
    });
})();