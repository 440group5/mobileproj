<?php
 // on production server convert this back to .html and remove the php. This is only to make cloud 9 happy for testing.
?>


<html>

<head>
    <title>NKU Parking</title>
</head>

<body>
    <center>
    <h1>NKU Parking</h1>
    <h3>Registration Form</h3>
    <form action="inc/register.php" method="post">
        Username:<input type="text" name="username"><br/>
        Password:<input type="password" name="password"><br/>
        Retype:<input type="password" name="password2"><br/>
        Email:<input type="text" name="email"><br/>
        <input type="submit" value="Submit">
    </form>
    </center>
</body>

</html>