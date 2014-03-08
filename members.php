<?php

    session_start();

    if(isset($_SESSION['username'])) {
        echo "Welcome ".$_SESSION['username']."<br><br>";
        if($_SESSION['type'] == 1)
            echo "<a href='admin/'>Admin</a>";
    }
    else {
        echo "You are not logged in";
    }
    echo "<br><br><a href='logout.php'>Log out</a>";
?>