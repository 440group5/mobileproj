<?php
    session_start();
    require("session.php");
    require("../config.php");
    include('../template/header.html');
    
    $id = $_GET['lot'];
    
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    if($db->connect_errno > 0) {
        die("Could not connect to database");
    }
    
    $query = "delete from spots where lot='".chr($id)."';";
    
    $db->query($query);
    
    if($db->errno > 0) {
        echo $db->error;
        die("Could not delete spots");
    }
    else
        echo "All spots from lot ".chr($id)." have been deleted.<br>";
        
    $query = "delete from lots where lot=$id;";
    
    $db->query($query);
    
    if($db->errno > 0) {
        echo $db->error;
        die("Could not delete lot");
    }
    else {
        echo "Lot ".chr($id)." has been deleted.<br>";
        echo "<a href='/admin/lots.php'>Back</a>";
    }
    
    
    include('../template/footer.html');
?>    