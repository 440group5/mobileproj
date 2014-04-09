<?php

    require("config.php");
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    $id = $_GET['id'];
    $username = $_GET['username'];
    $password = $_GET['password'];
    
    if($id == "login")
    {
        $query = "select * from users where username='".$username."'";
        $result = $db->query($query);
        $output = -1;
        $db->close();
        if(!$result)
        {
            $output = 0;
            print(json_encode($output));
        }
        $userData = $result->fetch_array(MYSQLI_ASSOC);
        $hashPass = hash('sha256', $userData['salt'].$password);
        if($hashPass === $userData['password'] )
        {
            $output = 1;
            print(json_encode($output));
        }
        else
        {
            $output = 0;
            print(json_encode($output));
        }
        
    }
    
    else if($id == "register")
    {
        $query = "select * from users where username='".$username."'";
        $result = $db->query($query);
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
        
        if($flag) 
        {
            $output = 0;
            print(json_encode($output));
        }
        else
        {
            $email = $_GET('email');
            $salt = generateSalt();
            $hashPass = hash('sha256', $salt.$password);
            $query = "insert into users (username, password, salt, email, type) values ('".$username."', '".$hashPass."', '".$salt."', '".$email."', '3')";
            $output = 1;
            print(json_encode($output));
        }
    }
    
    else if($id == "lots")
    {
        $query = "select * from lots";
        $result = $db->query($query);
        $output = array();
        $count = 0;
        while($row = $result->fetch_array(MYSQLI_ASSOC))
        {
            output[$count][0] = $row['lot'];
            output[$count][1] = $row['longitude'];
            output[$count][2] = $row['latitude'];
            output[$count][3] = $row['available'];
            output[$count][4] = $row['max'];
        }
        print(json_encode($output));
    }
    
    else if($id == "spots")
    {
        $query = "select * from spots";
        $result = $db->query($query);
        $output = array();
        $count = 0;
        while($row = $result->fetch_array(MYSQLI_ASSOC))
        {
            output[$count][0] = $row['id'];
            output[$count][1] = $row['spot_id'];
            output[$count][2] = $row['lot'];
            output[$count][3] = $row['status'];
            output[$count][4] = $row['reserve'];
            output[$count][5] = $row['expire'];
            output[$count][6] = $row['user_id'];
            output[$count][7] = $row['username'];
        }
        print(json_encode($output));    
    }
    
    else
    {
        $output = -1;
        print(json_encode($output));
    }
    
?>