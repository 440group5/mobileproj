<?php 
  session_start();
  if($_SESSION['type'] != 1)
      echo "You are not an admin!";
  else {
      echo "<a href='users.php'>Edit Users</a><br/>";
      echo "<a href='spots.php'>Edit Spots</a><br/>";
      echo "<a href='../logout.php'>Log Out</a><br/>";
  }

?>