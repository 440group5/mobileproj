<?php 
    session_start();
    require("session.php");
    include('../template/header.html');
      echo "<h1>Admin Control Panel</h1><br><br>";
      echo "<a href='users.php'><h2>Edit Users</h2></a><br/><br/>";
      echo "<a href='lots.php'><h2>Edit Lots</h2></a><br/><br/>";
      echo "<a href='spots.php'><h2>Edit Spots</h2></a><br/><br/>";
      echo "<a href='../logout.php'><h2>Log Out</h2></a><br/><br/>";
      
    include('../template/footer.html');

?>