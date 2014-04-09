<?php
    session_start();
    require("session.php");
    require("../config.php");
    require("../secure.php");
    
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    if($db->connect_errno > 0) {
        die("Could not connect to database");
    }
    
    if (!empty($_POST)) {  // if post is not empty then form was submited so update the database
        
        $id = secure($_POST['id']);
        $username = secure($_POST['username']);
        $pass = secure($_POST['password']);
        $email = secure($_POST['email']);
        $spot = secure($_POST['spot']);
        $type = secure($_POST['type']);
        
        // check if the new username is already in use
        $query = "select * from users where username='$username';";
        $user = $db->query($query);
        $user = $user->fetch_array(MYSQLI_ASSOC);
        //var_dump($user['id']);
        if($user['id'] != null && $id != $user['id']) {
            echo "<a href='../users.php'>Back</a><br/>";
            die("Username already in use");
        }
        
        // check if the new email is already in use
        $query = "select * from users where email='$email';";
        $user = $db->query($query);
        $user = $user->fetch_array(MYSQLI_ASSOC);
        if($user['email'] != null && $id != $user['id']){
            echo "<a href='../users.php'>Back</a><br/>";
            die("Email already in use");
        }
        
        
        if($pass != '') {
            if(strlen($pass) < 5 )
                die("Password must be atleast 5 characters long");
            $salt = generateSalt();
            $hashPass = hash('sha256', $salt.$pass);
            $query = "update users set username='$username', password='$hashPass', salt='$salt', email='$email', spot='$spot', type='$type' where id='$id';";
            $db->query($query);
            if($db->errno == 0) {
                echo "<h1>Succesfully Udated</h1>";
                echo "<a href='../users.php'>Back</a><br/>";
            }
            else {
                echo "<h1>An error occured while updating</h1>";
                echo $db->error;
                echo "<a href='../users.php'>Back</a><br/>";
            }
        }
        else {
        
            $query = "update users set username='$username', email='$email', spot='$spot', type='$type' where id='$id';";
            $db->query($query);
            if($db->errno == 0) {
                echo "<h1>Succesfully Udated</h1>";
                echo "<a href='../users.php'>Back</a><br/>";
            }
            else {
                echo "<h1>An error occured while updating</h1>";
                echo $db->error;
                echo "<a href='../users.php'>Back</a><br/>";
            }
        }
    }
    
    else {
        
        $id = $_GET['id'];
        
        $query = "select * from users where id='".$id."';";
        
        $user = $db->query($query);
        $db->close();
        
        $user = $user->fetch_array(MYSQLI_ASSOC);
        
        echo "<html>";
        echo "<h1>Edit User</h1>";
        echo "<a href='../users.php'>Back</a><br/>";
        echo "<i>To not change a field, leave it as is</i>";
        echo "<form method='post' action='edituser.php'>";
        echo "ID: ".$id."<br><br>";
        echo "<input type='hidden' name='id' value='$id' />";  // hidden field to pass the id variable via post
        echo "User: <input type='text' name='username' value='".$user['username']."'><br><br>";
        echo "Password: <input type='password' name='password' value=''><i>Leave blank to not change password</i><br><br>";
        echo "Email: <input type='text' name='email' value='".$user['email']."'><br><br>";
        echo "Spot: <input type='text' name='spot' value='".$user['spot']."'><br><br>";
        echo "Type: <input type='text' name='type' value='".$user['type']."'><i>Admin=1, Faculty=2, Student=3</i><br><br>";
        echo "<input type='submit' name='submit' value='Submit'>";
        echo "</form>";
        echo "</html>";
    }
    
?>