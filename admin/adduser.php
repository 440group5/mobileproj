<?php
    session_start();
    require("session.php");
    require("../config.php");
    require("../secure.php");
    include('../template/header.html');
    
    
    if (!empty($_POST)) {
        
        //get variables from post
        $username = secure($_POST["username"]);
        $password = secure($_POST["password"]);
        $password2 = secure($_POST["password2"]);
        $email = secure($_POST["email"]);
        $type = secure($_POST['type']);
        
        $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
        if($db->connect_errno > 0) {
            die("Could not connect to database");
        }
        
        $query = "select * from users where username='".$username."' or email='".$email."'";
        $result = $db->query($query);
        $flag = false;  //if any fields are wrong flag

        
        if(strlen($username) < 5 ) {
            echo "Username must be atleast 5 characters long<br>";
            $flag = true;
        }
        
        if(strlen($password) < 5 ) {
            echo "Password must be atleast 5 characters long<br>";
            $flag = true;
        }
        

        if(strcmp($password, $password2) !== 0 ) {
            echo "Passwords do not match<br>";
            $flag = true;
        }
        
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
             echo "Email is not valid<br>";
             $flag = true;
        }
        //check if username or email is in use. This gets messy but not sure if there is a better way.
        while($row = $result->fetch_array(MYSQLI_ASSOC) ) { // this for loop will check at most 2 rows.
            
            echo "<br><br>username: ". $row['username'];
            echo "<br><br>email: ". $row['email'];
            echo "<br><br>";
            
            if(strcmp($username, $row['username']) === 0) {
                echo "Username is already in use<br>";
                $flag = true;
            }
            if(strcmp($email, $row['email']) === 0) {
                echo "Email is already in use<br>";
                $flag = true;
            }
        }
        
        $result->free();
        
        if($flag) {
            echo "User was not creaed<br>";
            echo "<a href='users.php'>Back</a><br>";
        }
        else {

            $salt = generateSalt();
            $hashPass = hash('sha256', $salt.$password);
            $query = "insert into users (username, password, salt, email, type) values ('".$username."', '".$hashPass."', '".$salt."', '".$email."', '3')";
            $result = $db->query($query);
            if($result) {
                echo "User Succesfully Added<br>";
                echo "<a href='/users.php'>Back</a><br>";
            }
            else {
                echo "An error occured while registrering please contact support";
                echo "<a href='users.php'>Back</a><br>";
            }
        }
        
        $result->free();
        $db->close();
        
        
        
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
    include('../template/footer.html');
?>