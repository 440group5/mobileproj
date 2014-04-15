<?php
    session_start();
    require("session.php");
    require("../config.php");
    require("../secure.php");
    
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    if (!empty($_POST)) {  // if post is not empty then form was submited so update the database
    
        $id = secure($_POST['id']);
        $avail = secure($_POST['availability']);
        $user = secure($_POST['user']);
        $reserved = secure($_POST['reserved']);
        $expires = secure($_POST['expires']);
        
        $query = "update spots set status='$avail', username='$user', reserve='$reserved', expire='$expires' where id='$id';";
        
        $db->query($query);
        
        
        if($db->errno > 0)
            echo $db->error;
        else {
        
            echo "<html>";
            echo "<h1>Update Successful</h1>";
            echo "<a href='../spots.php'>Back</a>";
            echo "</html>";
        }
        $db->close();
    }
        
    
    
    
    else {  // Edit spot field was not submited so show the form
        
        $id = $_GET['id'];
        
        $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
        
        if($db->connect_errno > 0) {
            die("Could not connect to database");
        }
        
        $query = "select * from spots where id='".$id."';";
        
        $spot = $db->query($query);
        $db->close();
        $spot = $spot->fetch_array(MYSQLI_ASSOC);
        
        echo "<html>";
        echo "<h1>Edit Spot</h1>";
        echo "<a href='../spots.php'>Back</a><br/>";
        echo "<form method='post' action='editspot.php'>";
        echo "ID: ".$id."<br><br>";
        echo "<input type='hidden' name='id' value='$id' />";  // hidden field to pass the id variable via post
        echo "Lot: ".$spot['lot']."<br><br>";
        echo "Availability: <select name='availability'>";
        echo "<option value='Available'>Available</option>";
        echo "<option value='Occupied'>Occupied</option>";
        echo "<option value='Reserved'>Reserved</option>";
        echo "<option value='Closed'>Closed</option>";
        echo "</select><br><br>";
        echo "User: <input type='text' name='user' value='".$spot['username']."'><br><br>";
        echo "Reserved: <input type='text' name='reserved' value='".$spot['reserve']."'><br><br>";
        echo "Expires: <input type='text' name='expires' value='".$spot['expire']."'><br><br>";
        echo "<input type='submit' name='submit' value='Submit'>";
        echo "</form>";
        echo "</html>";
    }
    
?>