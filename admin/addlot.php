<?php
    session_start();
    require("session.php");
    require("../config.php");
    include('../template/header.html');
    
    if (!empty($_POST)) {
        
        $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
        if($db->connect_errno > 0) {
            die("Could not connect to database");
        }
        $query = "insert into lots values('".ord($_POST['lot'])."','".$_POST['longitude']."','".$_POST['latitude']."','".$_POST['available']."','".$_POST['capacity']."');";
        $db->query($query);
        
        $error = $db->error;
        if($db->errno > 0)
            echo $error;
        else 
            echo "<h1>Lot sucessfully added</h1>";
            
        echo "<a href='lots.php'>Back</a><br/>";
        
        if($db->error == 0) {  //if there are no errors create every spot for the lot
            $count = 1;
            while($count <= $_POST['capacity'] ) {
                $db->query("insert into spots (spot_id,lot,status) values('$count','".$_POST['lot']."','".$_POST['available']."');");
                $count++;
            }
        }
        $db->close();
    }
    else {
        
?>


<html>
<h1>Add Lot</h1>
<a href="lots.php">Back</a><br/><br/>
<form action='addlot.php' method='post'>
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
    <br/>
    Availability:<select name="available">
        <option value="Open" selected>Open</option>
        <option value="Closed">Closed</option>
    </select>
    <br/>
    Capacity:<input type="text" name="capacity" ><br/><br/>
    Longitude:<input type="text" name="longitude" ><br/><br/>
    Latitude:<input type="text" name="latitude" ><br/><br/>
    <input type="submit" name="submit" value="Submit" >
</form>
</html>

<?php
include('../template/footer.html');
}
?>