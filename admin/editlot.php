<?php
    session_start();
    require("session.php");
    require("../config.php");
    include('../template/header.html');
    
    $id = $_GET['id'];
    $available = $_GET['available'];
    
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    if($db->connect_errno > 0) {
        die("Could not connect to database");
    }
    
    if($available == "Open") {
        $query = "update lots set available = 'Closed' where lot = '".ord($id)."';";
        $change = "Closed";
    }
    else if($available == "Closed") {
        $query = "update lots set available = 'Faculty/Staff' where lot = '".ord($id)."';";
        $change = "Faculty/Staff";
    }
    else if($available == "Faculty/Staff") {
        $query = "update lots set available = 'Student' where lot = '".ord($id)."';";
        $change = "Student";
    }
    else if($available == "Student") {
        $query = "update lots set available = 'Open' where lot = '".ord($id)."';";
        $change = "Open";
    }
    
    else
        die("What are you trying to do??!?!");
        
    $db->query($query);
    
    if($db->errno > 0) {
        echo $db->error;
        die("Could not update lot");
    }
    else {
        echo "Lot ".chr($id)." has been set to $change .<br>";
        echo "<a href='/admin/lots.php'>Back</a><br/><br/>";
    }
    
    $db->close();
    
    /*
    else {
    
        $query = "select * from lots where id='".$id."';";
        
        $lot = $db->query($query);
        $db->close();
        $lot = $lot->fetch_array(MYSQLI_ASSOC);
        
        echo "<html>";
        echo "<h1>Edit Lot</h1>";
        echo "<a href='lots.php'>Back</a><br/>";
        echo "<form method='post' action='editlot.php'>";
        echo "Lot: ".chr($id)."<br><br>";
        echo "<input type='hidden' name='id' value='$id' />";  // hidden field to pass the id variable via post
        
    }
    */
    include('../template/footer.html');
?>