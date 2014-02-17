<?php  #file needs to be deleted now
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
        
        echo "<br>".DBHOST ."<br>" . DBUSER."<br>".DBPASS."<br>".DBNAME;
        
        global $username, $password, $password2, $email;
        $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME); //connect to database
        
        if(mysqli_connect_errno() ) {
            die("could not connect to database");
        }
        $query = "select * from users where username='".$username."' or email='".$email."'";
        $result = $db->query($query);
        $flag = false;  //if any fields are wrong flag
        
        echo "<br><br>". $query . "<br><br>";
        //check result
        if($result->num_rows > 0) {
            echo "something returned: ". $result->num_rows;
        }
        else {
            echo "nothing returned rows: ". $result->num_rows;
        }
        
        echo "<h1>Data Entered</h1>";
        echo "username = ". $username. "<br>";
        echo "password = ". $password. "<br>";
        echo "password2 = ". $password2. "<br>";
        echo "email = ". $email. "<br>";

        
        echo "<h1>Validation Check</h1><br>";
        echo "Anything wrong should be mentioned below.<br>";
        
        if(strlen($username) < 5 ) {
            echo "Username must be atleast 5 characters long<br>";
            $flag = true;
        }
        
        if(strlen($password) < 5 ) {
            echo "Password must be atleast 5 characters long<br>";
            $flag = true;
        }
        

        if(strcmp($password, $password2) !=0 ) {
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
            echo "<a href='/register.php'>Back</a><br>";
        }
        else {
            echo "data is correct and would be submitted<br>";
        }
        
        echo "done validating<br>";
    }
?>