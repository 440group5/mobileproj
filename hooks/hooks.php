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
            $out = array('user_id' => '0');
            $output = 0;
            print(json_encode($out));
        }
        else
        {
            $userData= $result->fetch_array(MYSQLI_ASSOC);
            $hashPass = hash('sha256', $userData['salt'].$password);
            if($hashPass == $userData['password'] )
            {
                $out = array('user_id' => $userData['id'], 'status' => $userData['type']);
                print(json_encode($out));
            }
            else
            {
                $out = array('user_id' => '0');
                $output = 0;
                print(json_encode($out));
            }
        }
    }

    else if($id == "register")
    {
        $query = "select * from users where username='".$username."'";
        $selection = $_GET['selection'];
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
            $out = array('success' => '0');
            $output = 0;
            echo "-1";
            //print(json_encode($out));
        }
        else
        {
            $salt = generateSalt();
            $hashPass = hash('sha256', $salt.$password);
            $query = "insert into users (username, password, salt, type) values ('".$username."', '".$hashPass."', '".$salt."', '".$selection."')";
            $output = 1;
            $result = $db->query($query);

            $query = "select id from users where username='".$username."'";
            $result = $db->query($query);
            $row = mysqli_fetch_row($result);
            echo "$row[0]";
            //$out = array('success' => $output);
            //echo "User inserted";
            //print(json_encode($out));
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
            /*array_push($output, $row['lot']);
            array_push($output, $row['longitude']);
            array_push($output, $row['latitude']);
            array_push($output, $row['available']);
            array_push($output, $row['max']);*/
            $output[$count] = array('name' => $row['lot'], 'latitude' => $row['latitude'], 'longitude' => $row['longitude'], 'status' => $row['available'], 'max' => $row['max']);
            $count++;
        }
        print(json_encode($output));
    }

    else if($id == "spots")
    {
        $query = "select * from spots";
        //$result = $db->query($query);
        $output = array();
        $count = 0;
        //$query = "select * from lots";
        $result = $db->query($query);
        /*while($row = $result->fetch_array(MYSQLI_ASSOC))
        {
            //$spotArr = new array();
            //$lotName = $chr($row['lot']);
            //$spotQuery = "select * from spots where lot='".$lotName."'";
            //$spotResult = $eb->query($query);
            //$k = 0;
           /* while($r = $result->fetch_array(MYSQLI_ASSOC))
            {
               $spotArr[$k] = array('id' => $r['id'], 'spot_id' => $r['spot_id'], 'lot' => $r['lot'], 'status' => $r['status', 'reserve' => $r['reserve'], 'expire' => $r['expire'], 'user_id' => $r['user_id'], 'username' => $r['username']);
               $k++;
            }

            $output[$count] = array('name' => $row['lot'], 'lat' => $row['latitude'], 'long' => $row['longitude'], 'status' => $row['available'], 'max' => $row['max'], 'spots' => $spotArray);
            $count++;
        }

        $query -> $free();
        $result -> $free();
        $count = 0;

        for($i = 0; $i < count($output); $i++)
        {
           $query = "select * from spots where name='".$chr($output[$i]['name'])."'";
           $result = $db->query($query);
           $spotArray = array();

           while($row = $result->fetch_array(MYSQLI_ASSOC))
           {
              $output[$count] = array('id' => $row['id'], 'spot_id' => $row['spot_id'], 'lot' => $row['lot'], 'status' => $row['status'], 'reserve' => $row['reserve'], 'expire' => $row['expire'], 'user_id' => $row['user_id'], 'username' => $row['username']);
              $count++;
           }
  //         $output[$i]['spots'] => $spotArray;
        }*/
       while($row = $result->fetch_array(MYSQLI_ASSOC))
        {
            $output[$count] = array('id' => $row['id'], 'spot_id' => $row['spot_id'], 'lot' => ord($row['lot']), 'status' => $row['status'], 'reserve' => $row['reserve'], 'expire' => $row['expire'], 'user_id' => $row['user_id'], 'username' => $row['username']);
            //array_push($output, $row['id']);
            //array_push($output, $row['spot_id']);
            //array_push($output, $row['lot']);
            //array_push($output, $row['status']);
            //array_push($output, $row['reserve']);
            //array_push($output, $row['expire']);
            //array_push($output, $row['user_id']);
            //array_push($output, $row['username']);
            $count++;
        }
        $db->close();

        print(json_encode($output));
    }

    else if($id == "user")
    {
        $query2 = "select * from spots where username = '".$username."'";
        $result2 = $db->query($query2);
        $row2 = $result2->fetch_array(MYSQLI_ASSOC);
        $output = array('spot_id' => $row2['spot_id'], 'lot' => $row2['lot']);
        print(json_encode($output));
    }

    else if($id == "expire")
    {
       $lot = $_GET['lot'];
       $spot = $_GET['spot'];
       $user_id = $_GET['user_id'];
       $query = "update spots set status = 'Open' where spot_id = '".$spot."' and lot = '".$lot."'";
       $result = mysqli_query($db, $query) or die(mysqli_error($db));
       $query2 = "update users set spot = 0 where id = '".$user_id."'";
       $result2 = mysqli_query($db, $query2) or die(mysqli_error($db));
       if($result && $result2)
         echo "0";
       else
         echo "-1";
    }

    else if($id == "checkexpire")
    {
       $spot = $_GET['spot'];
       $lot = $_GET['lot'];
       $query = "select reserve from spots where spot_id = '".$spot."' and lot = '".$lot."'";
       $result = mysqli_query($db, $query) or die(mysqli_error($db));
       $row = mysqli_fetch_row($result);
       date_default_timezone_set('America/New_York');
       $today = date('y-m-d H:i:s');
       $result = strtotime($today) - strtotime($row[0]);
       if($result > 60*60)
         echo "expire";
       else
         echo "-1";
    }

    else if($id == "reserve")
    {
       $userid = $_GET['user_id'];
       $lot = $_GET['lot'];
       $spot = $_GET['spot'];
       $status = $_GET['status'];
       $query = "select * from spots where user_id = '".$userid."'";
       $query2 = "select * from spots where spot_id = '".$spot."' and lot = '".$lot."'";
       $result1 = mysqli_query( $db, $query );
       if( mysqli_num_rows($result1) == 0 )
       {
          $result2 = mysqli_query($db, $query);
          if( mysqli_num_rows($result2) != 0)
          {
              echo "-1";
          }
          else
          {
            $query = "select * from users where id = '".$userid."'";
            $result = mysqli_query($db, $query);
            $row = mysqli_fetch_array($result, MYSQLI_ASSOC);
            date_default_timezone_set('America/New_York');
            $today = date('y-m-d H:i:s');
            $query = "update spots set status = 'Reserved', reserve = '".$today."', user_id = '".$userid."', username = '".$row["username"]."' where spot_id = '".$spot."' and lot = '".$lot."'";
            $result = mysqli_query($db, $query) or die(mysqli_error($db));
            $row1 = mysqli_fetch_array($query2, MYSQLI_ASSOC);
            $query = "update users set spot = '".$spot."' where username = '".$row["username"]."'";
            $result = mysqli_query($db, $query) or die(mysqli_error($db));
            if($result == TRUE)
              echo "1";
            else
              echo "-1";
          }
      }
      else
      {
        echo "-1";
      }
    }
    else
    {
        echo "No id";
        $output = array('spot' => '-1');
        print(json_encode($output));
    }
?>

</body>
</html>