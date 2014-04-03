<?php

    if($_SERVER['REQUEST_METHOD'] == "POST") {

        require("config.php");
        require("secure.php");

        //get variables from post
        $username = secure($_POST["username"]);
        $password = secure($_POST["password"]);
        $password2 = secure($_POST["password2"]);
        $email = secure($_POST["email"]);
        
        validate();
    }
    else {
        ?>
        <html>
        <head>
            <title>NKU Parking</title>
        </head>
        
        <body>
            <center>
            <h1>NKU Parking</h1>
            <h3>Registration Form</h3>
            <form action="register.php" method="post">
                Username:<input type="text" name="username"><br/>
                Password:<input type="password" name="password"><br/>
                Retype:<input type="password" name="password2"><br/>
                Email:<input type="text" name="email"><br/>
                <!-- Type of User:<input type="text" name="type"><br/> -->
                <input type="submit" name="submit" value="Submit">
            </form>
            </center>
        </body>
        </html>
        <?php
    }

    
    function validate() {
        //use globals and prep database connection
        
        
        global $username, $password, $password2, $email;
        $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME); //connect to database
        
        if($db->connect_errno > 0 ) {
            die("could not connect to database");
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
            echo "Registration was not completed<br>";
            echo "<a href='/register.php'>Back</a><br>";
        }
        else {

            $salt = generateSalt();
            $hashPass = hash('sha256', $salt.$password);
            $query = "insert into users (username, password, salt, email, type) values ('".$username."', '".$hashPass."', '".$salt."', '".$email."', '3')";
            $result = $db->query($query);
            if($result) {
                echo "Succesfully Registered";
            }
            else {
                echo "An error occured while registrering please contact support";
            }
        }
        
        $result->free();
        $db->close();
        
    }
    
    
?>