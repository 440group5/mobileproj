<?php
    session_start();
    require("session.php");
    require("../config.php");
    require("secure.php");
    
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    if (!empty($_POST)) {
        
    }
    
    
    else {
        echo "<html>";
        echo "<h1>Add User</h1>";
        echo "<a href='/admin'>Back</a></br></br>";
        echo "<form action='adduser.php' method='post'>";
        echo "Username:<input type='text' name='username'><br/></br>";
        echo "Password:<input type='password' name='password'><br/></br>";
        echo "Retype:<input type='password' name='password2'><br/></br>";
        echo "Email:<input type='text' name='email'><br/></br>";
        echo "Type of User:<input type='text' name='type'><i>Admin=1, Faculty=2, Student=3</i><br/></br>";
        echo "<input type='submit' name='submit' value='Submit'>";
        echo "</form>";
        echo "</html>";
        
        
    }
    
?>