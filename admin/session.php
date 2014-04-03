  <?php
  if(!isset($_SESSION) || $_SESSION['type'] != 1) {
      echo "<a href='../index.php'>Home</a>";
      exit("You are not an admin!");
  }
  ?>