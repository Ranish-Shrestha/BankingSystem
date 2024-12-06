<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Log In</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>
<body>
    <h1>Banking System</h1>
    <div class="container" id="container">
        <div class="form-container sign-in-container">
            <form id="loginForm">
                <h1>Log in</h1>
                <input type="text" id="email" name="email" placeholder="Email" required />
                <input type="password" id="password" name="password" placeholder="Password" required />
                <a href="#">Forgot your password?</a>
                <input type="hidden" name="action" value="login">
                <button type="button" onclick="SignIn()">Sign In</button>
            </form>
        </div>
        <div class="overlay-container">
            <div class="overlay">
                <div class="overlay-panel overlay-right">
                    <h1>Hello, Friend!</h1>
                    <p>Enter your personal details and start your journey with us</p>
                    <a type="button" class="button-link ghost" href="register.jsp">Register</a>
                </div>
            </div>
        </div>
    </div>

    <script>
        async function SignIn() {
            let email = document.getElementById("email").value;
            let password = document.getElementById("password").value;

            try {
                const response = await fetch("account", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: "action=login&email=" + encodeURIComponent(email) + "&password=" + encodeURIComponent(password)
                });

                let data = await response.json();

                if (response.ok) {
                    console.log(data);
                    window.location.href = 'index.jsp';
                } else {
                    console.log(data);
                    window.location.href = 'error.jsp';
                }
            } catch (error) {
                console.error('Error:', error);
                window.location.href = 'error.jsp';
            }
        }
    </script>
</body>
</html>
