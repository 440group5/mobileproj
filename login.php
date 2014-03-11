<?php

    ob_start();
    session_start();
    require("config.php");
    require("secure.php");
    
    $username = secure($_POST["username"]);
    $password = secure($_POST["password"]);
    
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    if($db->connect_errno > 0) {
        die("Could not connect to database");
    }
    
    $query = "select * from users where username='".$username."'";
    $result = $db->query($query);
    $db->close();
    if(!$result) {  // if result is empty
        echo "<h2>Username does not exist</h2><br>";
        echo "<a href='/index.php'>Back</a><br>";
    }
    else {
        $userData = $result->fetch_array(MYSQLI_ASSOC);
        $hashPass = hash('sha256', $userData['salt'].$password);
        echo $password;
        echo "<br>";
        echo $userData['salt'];
        if($hashPass === $userData['password'] ) {
            $_SESSION['id'] = $userData['id'];
            $_SESSION['username'] = $userData['username'];
            $_SESSION['spot'] = $userData['spot'];
            $_SESSION['type'] = $userData['type'];
            header('Location: members.php');  // this redirects user to members page
        }
        
        else {
            echo "<h2>invalid password</h2><br>";
            echo "<a href='/index.php'>Back</a><br>";
        }
    }

?>