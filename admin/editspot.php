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
    
    echo "<h1>Edit Spot</h1>";
    echo "<form method='post' action='editspot.php'>";
    echo "ID: ".$id;
    echo "Lot: ".$spot['lot'];
    echo "Availability: <select name='availability'>";
    echo "<option value='Available'>";
    echo "<option value='Occupied'>";
    echo "<option value='Reserved'>";
    echo "<option value='Closed'>";
    echo "</select>";
    echo "User: <input type='text' name='user' value='".$spot['user']."'>";
    echo "Reserved: <input type='text' name='reserved' value='".$spot['reserved']."'>";
    echo "Expires: <input type='text' name='expires' value='".$spot['expire']."'>";
    echo "<input type='submit' value='Submit'>";
    
?>