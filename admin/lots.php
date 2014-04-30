<?php
    session_start();
    require("session.php");
    require("../config.php");
    include('../template/header.html');
?>


<h1>Edit Lots</h1><br>
<a href='addlot.php'>Add Lot</a><br/>
<br>
<a href='/admin'>Back</a><br>

<?php
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    if($db->connect_errno > 0) {
        die("Could not connect to database");
    }
    
    $query = "select * from lots;";
    $lots = $db->query($query);
    
    
    echo"<table cellpadding='10'>";
    echo"<tr><th>Lot</th><th>Availability</th><th>Capacity</th><th>Longitude</th><th>Latitude</th><th>Usage Report</th><th>Delete</th><th>Edit</th></tr>";
    
    while($lot = $lots->fetch_array(MYSQLI_ASSOC) ) {
        $query = "select * from spots where lot='".chr($lot['lot'])."' and status='Occupied';";
        $result = $db->query($query);
        $count = $result->num_rows;
        $usage = 100 * ($count / $lot['max']);
        echo "<tr><td>".chr($lot['lot'])."</td><td>".$lot['available']."</td><td>".$lot['max']."</td><td>".$lot['longitude']."</td><td>".$lot['latitude']."</td><td>$usage%<td><a href='deletelot.php/?lot=".$lot['lot']."'>Delete</a></td><td><a href='editlot.php?id=".chr($lot['lot'])."&available=".$lot['available']."'>Edit</a></tr>";
    }
    echo "</table>";
    
    include('../template/footer.html');
?>

