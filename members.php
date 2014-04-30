<?php

    session_start();
    include('/template/header.html');

    if(isset($_SESSION['username'])) {
        echo "<h1>Welcome ".$_SESSION['username']."</h1><br><br>";
        if($_SESSION['type'] == 1)
            echo "<a href='admin/'><h2>Admin</h2></a>";
    }
    else {
        echo "You are not logged in";
    }
    echo "<br><br><a href='logout.php'><h2>Log Out</h2></a>";
    
    include('/template/footer.html');
?>