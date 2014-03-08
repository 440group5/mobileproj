<?php 
  session_start();
  if($_SESSION['type'] != 1)
      echo "You are not an admin!";
  else {
      echo "<a href='users.php'>Edit Users</a>";
      echo "<a href='spots.php'>Edit Spots</a>";
  }

?>