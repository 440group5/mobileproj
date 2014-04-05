<?php 
  session_start();
  require("session.php");
  require("../config.php");
  
?>
      <h1>Lots &amp; Spots</h1>
      <form action="spots.php" method="post">
        LOT:<select name="lot">
            <option value="A" selected>A</option>
            <option value="B">B</option>
            <option value="C">C</option>
            <option value="D">D</option>
            <option value="E">E</option>
            <option value="F">F</option>
            <option value="G">G</option>
            <option value="H">H</option>
            <option value="I">I</option>
            <option value="J">J</option>
            <option value="K">K</option>
            <option value="L">L</option>
            <option value="M">M</option>
            <option value="N">N</option>
            <option value="O">O</option>
            <option value="P">P</option>
            <option value="Q">Q</option>
            <option value="R">R</option>
            <option value="S">S</option>
            <option value="T">T</option>
            <option value="U">U</option>
            <option value="V">V</option>
            <option value="W">W</option>
            <option value="X">X</option>
            <option value="Y">Y</option>
            <option value="Z">Z</option>
        </select>
        <input type="submit" value="submit">
      </form>
      <br/>
      
<?php
      if(isset($_POST["lot"]) ) 
          $lot= $_POST["lot"];
      else
          $lot='A';
    
      /*
      $lot=$_POST["lot"];
      isset($lot) || $lot='A'; //set lot or default to A
      */
      $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
      if($db->connect_errno > 0) {
        die("Could not connect to database");
      }
      
      $query = "select * from spots where lot='".$lot."';";
      $spots = $db->query($query);
      $db->close();
      echo"<table cellpadding='10'>";
      echo"<tr><th>ID</th><th>LOT</th><th>Availabiltiy</th><th>User</th><th>Reserved</th><th>Expires</th><th>Edit</th></tr>";
      
      date_default_timezone_set ( "America/New_York");
      
      while($spot = $spots->fetch_array(MYSQLI_ASSOC) ) {
          if($spot['occupied'] == "1")
            $availability = "Occupied";
          elseif(time() < strtotime($spot['expire']) )
            $availability = "Reserved";
          else
            $availability = "Available";
            
          echo"<tr><td>".$spot['id']."</td><td>".$spot['lot']."</td><td>".$availability."</td><td>".$spot['username']."</td><td>".$spot['reserve']."</td><td>".$spot['expire']."</td><td><a href='editspot.php/?id=".$spot['id']."'>Edit</a></td></tr>";
          
      }
      echo"</table><br/>";
      echo $spot['occupied']."<br/>";
      echo"<a href='/admin'>Admin</a>";
   

?>