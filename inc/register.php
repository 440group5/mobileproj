<?php
    require("config.php");

    //get variables from post
    $username = secure($_POST["username"]);
    $password = secure($_POST["password"]);
    $password2 = secure($_POST["password2"]);
    $email = secure($_POST["email"]);
    
    validate();
    /*
    echo "<h1>Data Check</h1>";
    echo "username = ". $username. "<br>";
    echo "password = ". $password. "<br>";
    echo "password2 = ". $password2. "<br>";
    echo "email = ". $email. "<br>";
    */
    
    function secure($data) //secure user input data needs work.
    {
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
    }
    
    function validate() {
        //use globals and prep database connection
        global $username, $password, $password2, $email;
        $mysqli = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME) or die("Unable to connect to database"); //connect to database
        $query = "select * from users where username='".$username."' or email='".$email."'";
        $result = $mysqli->query($query);
        $flag = false;  //if any fields are wrong flag
        
        echo "<br><br>". $query . "<br><br>";
        //check result
        if($result) {
            echo "something returned";
        }
        
        echo "<h1>Data Entered</h1>";
        echo "username = ". $username. "<br>";
        echo "password = ". $password. "<br>";
        echo "password2 = ". $password2. "<br>";
        echo "email = ". $email. "<br>";

        
        echo "<h1>Validation Check</h1><br>";
        echo var_dump(strcmp($password, $password2) )." var_dump on compare passwords<br>";
        echo var_dump(filter_var($email, FILTER_VALIDATE_EMAIL) )." var_dump on validate email<br>";
        
        echo "Anything wrong should be mentioned below. Note no data in db to check against(usernames, emails).<br>";
        if(strcmp($password, $password2) !=0 ) {
            echo "Passwords do not match<br>";
            $flag = true;
        }
        
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
             echo "Email is not valid<br>";
             $flag = true;
        }
        //check if username or email is in use. This gets messy but not sure if there is a better way.
        while($row = $result->fetch_row() ) { // this for loop will check at most 2 rows.
            
            echo "<br><br>username: ". $row['username'];
            echo "<br><br>email: ". $row['email'];
            echo $echoRow;
            if(strcmp($username, $row['username']) == 0) {
                echo "Username is already in use<br>";
                $flag = true;
            }
            if(strcmp($email, $row['email']) == 0) {
                echo "Email is already in use<br>";
                $flag = true;
            }
        }
        

        
        if($flag) {
            echo "Data would not be submited<br>";
            echo "<a href='/register.php'>Back</a>";
        }
        else {
            echo "data is correct and would be submitted<br>";
        }
        
        echo "done validating<br>";
    }
?>