<?php  // This script is for filling up spots for the presentation and is not part of the overall functionality, so should prolly not be graded.

    session_start();
    require("session.php");
    require("../config.php");
    include('../template/header.html');    


    
    if (!empty($_POST)) { 
        
        $db = new mysqli(DBHOST, DBUSER, DBPASS, DBNAME);
    
        if($db->connect_errno > 0) {
            die("Could not connect to database");
        }
    
    
        $lot = $_POST['lot'];
        $percent = $_POST['percent'];
        
        $query = "select * from spots where lot='".$lot."';";
        
        $spots = $db->query($query);
        
        while($spot = $spots->fetch_array(MYSQLI_ASSOC) ) {
        
        
    }
    
    else {
        ?>
        <h1>Occupy Spots</h1><br>
        <form action='fill.php' method='post'>
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
        </select><br><br>
        
        Fill %:<input type='text' name='percent'><i>enter 0-100, 0 will clear lot</i><br/><br />
        
        <input type="submit" value="submit">
      </form>
    

<?php
    }
    include('../template/footer.html'); 

?>