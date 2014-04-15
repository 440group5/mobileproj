<html>
<head>
</head>
<body>

<?php
    require("../config.php");
    require("../secure.php");
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME) or die("can't connect");
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
        else
        {
            $userData= $result->fetch_array(MYSQLI_ASSOC);
            $hashPass = hash('sha256', $userData['salt'].$password);
            if($hashPass == $userData['password'] )
            {
                $output = 1;
                print(json_encode($userData['id']));
            }
            else
            {
                $output = 0;
                print(json_encode($output));
            }
        }

    }

    else if($id == "register")
    {
        $query = "select * from users where username='".$username."'";
        $result = $db->query($query);
        $flag = false;
        while($row = $result->fetch_array(MYSQLI_ASSOC) ) { // this for loop will check at most 2 rows.

            if(strcmp($username, $row['username']) === 0) {
                $flag = true;
            }
            if(strcmp($email, $row['email']) === 0) {
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
            $salt = generateSalt();
            $hashPass = hash('sha256', $salt.$password);
            $query = "insert into users (username, password, salt, type) values ('".$username."', '".$hashPass."', '".$salt."', '3')";
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
            $output[$count] = array($row['lot'], $row['longitude'], $row['latitude'], $row['available'], $row['max']);
            $count++;
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
            array_push($output, $row['id']);
            array_push($output, $row['spot_id']);
            array_push($output, $row['lot']);
            array_push($output, $row['status']);
            array_push($output, $row['reserve']);
            array_push($output, $row['expire']);
            array_push($output, $row['user_id']);
            array_push($output, $row['username']);
            $count++;
        }
        print(json_encode($output));
    }
    
    else if($id = "user")
    {
        $query = "select * from users where username = '".$username."'";
        $result = $db->query($query);
        while($row = $result->fetch_array(MYSQLI_ASSOC))
        {
            $output = array("spot" => $row['spot']);
        }
        print(json_encode($output));
    }
    
    else if($id = "space_reserve")
    {
        $space = $_GET("space_id");
        $now = date('Y-m-d H-i-s');
        $query = "UPDATE spaces SET status = 'reserved' AND reserve = '".$now."' WHERE id = '".$space."'"; 
        $result = $db->query($query);
        print(json_encode(1));
    }

    else
    {
        echo "No id";
        $output = -1;
        print(json_encode($output));
    }

?>
</body>
</html>