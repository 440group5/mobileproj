<?php
    session_start();
    require("session.php");
    require("../config.php");
    
    $id = $_GET['id'];    
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    if($db->connect_errno > 0) {
        die("Could not connect to database");
    }
    
    $query = "select * from lots where id='".$id."';";
    
    $lot = $db->query($query);
    $db->close();
    $spot = $spot->fetch_array(MYSQLI_ASSOC);
    
    echo "<html>";
    echo "<h1>Edit Lot</h1>";
    echo "<a href='../lots.php'>Back</a><br/>";
    echo "<form method='post' action='editlot.php'>";
?>