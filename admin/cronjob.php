<?php

    require("../config.php");
    
    date_default_timezone_set('America/New_York');
    
    $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
    $query = "select * from spots;";
    
    $spots = $db->query($query);
    
    
    while($spot = $spots->fetch_array(MYSQLI_ASSOC)) {
        if($spot['status'] == "Reserved" && time() - $spot['Reserve'] > 3600 ) {
            $query = "update spots set status='Open', reserve='0', username='', user_id='0' where id='".$spot['id']."';";
            $db->query($query);
        }
        
        
    }
    
?>