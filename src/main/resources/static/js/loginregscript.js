const container = document.querySelector(".container"),
      pwShowHide = document.querySelectorAll(".showHidePw"),
      pwFields = document.querySelectorAll(".password"),
      signUp = document.querySelector(".signup-link"),
      login = document.querySelector(".login-link");


    // js code to appear signup and login form
    signUp.addEventListener("click", ( )=>{
        container.classList.add("active");
    });
    login.addEventListener("click", ( )=>{
        container.classList.remove("active");
    });

    function togglePasswordVisibility(icon) {
        var passwordInput = icon.previousElementSibling;
        if (passwordInput.type === "password") {
            passwordInput.type = "text";
             icon.classList.replace("fa-eye-slash", "fa-eye");
        } else {
            passwordInput.type = "password";
           icon.classList.replace("fa-eye", "fa-eye-slash");
        }
    }

    // loginregscript.js

    function validateLoginForm() {
        let email = document.getElementById('email').value;
        let password = document.getElementById('password').value;

        // Validate email
        let emailError = validateEmail(email);
        if (emailError) {
            document.getElementById('emailError').innerText = emailError;
            return false; // Prevent form submission
        } else {
            document.getElementById('emailError').innerText = ''; // Clear previous error
        }

        // Validate password
        let passwordError = validatePassword(password);
        if (passwordError) {
            document.getElementById('passwordError').innerText = passwordError;
            return false; // Prevent form submission
        } else {
            document.getElementById('passwordError').innerText = ''; // Clear previous error
        }

        return true; // Allow form submission
    }

    function validateEmail(email) {
        // Regular expression for validating email format
        let emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if (!emailRegex.test(email)) {
            return "Email ID is not valid.";
        }

        return null; // Email is valid
    }

    function validatePassword(password) {
        // Check for at least 8 characters
        if (password.length < 8) {
            return "Password must contain at least 8 characters.";
        }

        // Check for at least one letter, one number, and one special character
        let hasLetter = /[a-zA-Z]/.test(password);
        let hasNumber = /\d/.test(password);
        let hasSpecialChar = /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(password);

        if (!(hasLetter && hasNumber && hasSpecialChar)) {
            return "Password must contain at least one letter, one number, and one special character.";
        }

        return null; // Password is valid
    }


