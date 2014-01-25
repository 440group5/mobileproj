<?php
    require("config.php");

    //get variables from post
    $username = secure($_POST["username"]);
    $password = secure($_POST["password"]);
    $email = secure($_POST["password"]);
    
    
    
    
    
    
    function secure($data)
    {
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
    }
?>