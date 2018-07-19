<html>
<title>Edureka Live</title>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Database Entry Sample</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link rel="shortcut icon" href="favicon.ico" />
</head>
<body background="images/1.jpg">
<?php

  if($_SERVER['REQUEST_METHOD'] == "POST")
{
$cname = $_POST['name'];
$email=$_POST['email'];
$hostname='localhost';
$username='root';
$password='';
$dbname='edureka';
$usertable='hello';
$con=mysqli_connect($hostname,$username, $password) OR DIE ('Unable to connect to database! Please try again later.');
mysqli_select_db($con,$dbname);
$query = "insert into ".$usertable." values('".$cname."','".$email."');";   //<<--- chnage this
mysqli_query($con,$query) or die("Not Updated!");

 echo '<div class="container">  
  <div class="alert alert-success">
    <strong>Success!</strong> Successfully Entered Values!.
  </div>
</div>';
}
  ?>

<form action="" method='post' >
<div class="container" >
    <div class="center"><br><br>
         <div class="jumbotron"  align="center" style="padding: 5.0em -2.0em;">
                        <div class="row">
                        <div class=".col-xs-6 .col-sm-8"  style="margin-left:-30px;">
                        <b>Name:</b> &nbsp;&nbsp;&nbsp;<input type="text" name="name" required>
                        </div>
                          </div>
                        <br>
                        <div class="row">
                        <div class=".col-xs-6 .col-sm-8" style="margin-left:-30px;" >
                        <b>Email:</b> &nbsp;&nbsp;&nbsp;<input type="text" name="email" required>
                        </div>
                        </div>
                        <br>
    <div class="col-sm-14" >
                        <input type="submit" name="submit" class="btn btn-success" >
                        </div>
        </div> 
    </div> 
</div>
</form>

</body>
</html>

