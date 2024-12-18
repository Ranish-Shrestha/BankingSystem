<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>

<body>
    <h1>Banking System</h1>
    <div class="container right-panel-active" id="container">
        <div class="form-container sign-up-container">
            <form id="registerForm">
                <h1>Create Account</h1>
                <input type="text" id="name" name="name" placeholder="Name" required />
                <input type="text" id="address" name="address" placeholder="Address" required />
                <input type="text" id="phone_number" name="phone_number" placeholder="Phone Number" required />
                <input type="text" id="email" name="email" placeholder="Email" required />
                <select id="account_type" name="account_type" required>
                    <option value="">Account Type</option>
                    <option value="savings">Savings</option>
                    <option value="checking">Checking</option>
                </select>
                <input type="password" id="password" name="password" placeholder="Password" required />
                <input type="hidden" name="action" value="create">
                <button type="submit">Register</button>
            </form>
        </div>
        <div class="overlay-container">
            <div class="overlay">
                <div class="overlay-panel overlay-left">
                    <h1>Welcome Back!</h1>
                    <p>To keep connected with us please login with your personal info</p>
                    <a type="button" class="button-link ghost" href="login.jsp">Log In</a>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        document.getElementById('registerForm').addEventListener('submit', async function(event) {
            event.preventDefault(); // Prevent form submission

            let formData = new FormData(document.getElementById('registerForm'));

            try {
                const response = await fetch("account", {
                    method: 'POST',
                    body: new URLSearchParams(formData)
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
        });
    </script>
</body>

</html>
