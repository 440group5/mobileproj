<?php 
  require("session.php");
  require("../config.php");
  
?>
      <h1>Lots &amp; Spots</h1>
      <form action="spots.php" method="post">
        LOT:<select name="lot">
            <option value="A" selected>A</option>
            <option value="C">C</option>
            <option value="K">K</option>
            <option value="L">L</option>
            <option value="M">M</option>
        </select>
        <input type="submit" value="submit">
      </form>
      <br/>
      
<?php
      $lot=$_POST["lot"];
      isset($lot) || $lot='A'; //set lot or default to A
      
      $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
      if($db->connect_errno > 0) {
        die("Could not connect to database");
      }
      
      $query = "select * from spots where lot='".$lot."';";
      $spots = $db->query($query);

      echo"<table>";
      echo"<tr><th>ID</th><th>LOT</th><th>Availabiltiy</th><th>User</th></tr>";
      
      while($spot = $spots->fetch_array(MYSQLI_ASSOC) ) {
          if($spot['occupied'] == "1")
            $availability = "Occupied";
          elseif(time() < strtotime($spot['expire']) )
            $availability = "Reserved";
          else
            $availability = "Available";
            
          echo"<tr><td>".$spot['id']."</td><td>".$spot['lot']."</td><td>".$availability."</td><td>".$spot['username']."</td></tr>";
          
      }
      echo"</table><br/>";
      echo $spot['occupied']."<br/>";
      echo"<a href='/admin'>Admin</a>";
   

?>