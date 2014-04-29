<?php
    include('template/header.html');
?>
<!--
<html>
<head>
    <title>NKU Parking</title>
</head>

<body>
-->
    <center>
    <br><br>
    <h1>NKU Parking</h1>
    <h3>Log In Form</h3>
    <form action="login.php" method="post">
        Username:<input type="text" name="username"><br/>
        Password:<input type="password" name="password"><br/><br/>
        <input type="button" value="Register" onclick="location.href='register.php'">&nbsp;&nbsp;<input type="submit" value="Login">
    </form>
    </center>
    
<?php
    include('template/footer.html');
?>
<!--    
</body>

</html>
-->
