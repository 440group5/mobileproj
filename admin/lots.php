<?php
    session_start();
    require("session.php");
    require("../config.php");
?>

<html>
<h1>Edit Lots</h1>
<a href='addlot.php'>Add Lot</a><br/>

<?php
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    if($db->connect_errno > 0) {
        die("Could not connect to database");
    }
    
    $query = "select * from lots;";
    $lots = $db->query($query);
    $db->close();
    
    echo"<table cellpadding='10'>";
    echo"<tr><th>Lot</th><th>Availability</th><th>Capacity</th><th>Longitude</th><th>Latitude</th><th>Edit</th></tr>";
    
    while($lot = $lots->fetch_array(MYSQLI_ASSOC) ) {

        echo "<tr><td>".chr($lot['lot'])."</td><td>".$lot['available']."</td><td>".$lot['max']."</td><td>".$lot['longitude']."</td><td>".$lot['latitude']."</td><td><a href='editlot.php/?lot=".$lot['lot']."'>Edit</a></td></tr>";
    }
    echo "</table>";
    
?>

</html>