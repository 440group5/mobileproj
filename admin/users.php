<?php
    session_start();
    require("session.php");
    require("../config.php");
    
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    if($db->connect_errno > 0) {
        die("Could not connect to database");
    }
    
    $query = "select id,username,email,spot,type from users;";
    $users = $db->query($query);
    $db->close();
    
    echo "<html>";
    echo "<h1>Users</h1>";
    echo "<a href='../users.php'>Back</a><br/><br/>";
    echo "<a href='adduser.php'>Add user</a>";
    echo"<table cellpadding='10'>";
    echo"<tr><th>ID</th><th>Type</th><th>Username</th><th>Email</th><th>Spot</th><th>Edit</th></tr>";
    
    while($user = $users->fetch_array(MYSQLI_ASSOC) ) {
        $tno = $user['type'];
        if($tno == 3)
            $type="Student";
        else if($tno == 2)
            $type="Faculty";
        else if($tno == 1)
            $type="Admin";
        if($user['spot'] == 0)
            echo"<tr><td>".$user['id']."</td><td>$type</td><td>".$user['username']."</td><td>".$user['email']."</td><td>No Spot</td><td><a href='edituser.php/?id=".$user['id']."'>Edit</a></td></tr>";
        else
            echo"<tr><td>".$user['id']."</td><td>$type</td><td>".$user['username']."</td><td>".$user['email']."</td><td><a href='editspot.php/?id=".$user['spot']."'>Edit</a></td><td><a href='edituser.php/?id=".$user['id']."'>Edit</a></td></tr>";
    }
    echo "</table>";
    echo "</html>";
?>