<?php
    session_start();
    require("session.php");
    require("../config.php");
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
    echo "<input type='submit' value='Submit'>";
    echo "</html>";
    
?>