<?php
if ($_SERVER["SERVER_NAME"] == "localhost" ||
	$_SERVER["SERVER_NAME"] == "127.0.0.1") {
	$dbname = 'wordsearch';
	$dbuser = 'root';
	$dbpass = '';
	$dbhost = 'localhost';
} else {
	//mopaas.com
	$dbname = getenv('MOPAAS_MYSQL30155_NAME');
	$dbuser = getenv('MOPAAS_MYSQL30155_USER');
	$dbpass = getenv('MOPAAS_MYSQL30155_PASSWORD');
	$dbhost = getenv('MOPAAS_MYSQL30155_HOST');
}

$con = mysql_connect($dbhost, $dbuser, $dbpass) or die("数据库连接出错，请检查连接字串。"); //提示错误

mysql_query("SET NAMES 'UTF8'", $con); 
mysql_query("SET CHARACTER_SET='UTF8", $con); 
mysql_query("SET CHARACTER_SET_RESULTS='UTF8'", $con); 
mysql_query("SET CHARACTER_SET_CLIENT='UTF8'", $con); 
mysql_query("SET CHARACTER_SET_CONNECTION='UTF8'", $con); 

mysql_select_db($dbname, $con) or die("数据库连接出错，请检查连接字串。"); //提示错误



$starttime = explode(' ', microtime());

$word = isset($_REQUEST["word"]) ? $_REQUEST["word"] : ""; 
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Word Search</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<h2>Word Search</h2>
<form method="GET" action="index.php" enctype="application/x-www-form-urlencoded" style="margin-bottom: 1em; ">
<input type="text" name="word" value="<?php echo($word);?>" size="10" tabindex="1">
<input type="submit" value="search" name="action">
</form>


<?php
if ($word != "") {
	$sql = "SELECT id, word FROM words WHERE word REGEXP '".$word."' order by id";
} else {
	$sql = "SELECT id, word FROM words order by id";
}
$result = mysql_query($sql, $con);
if ($result !== false) {
	$rsRows = mysql_num_rows($result);
    $rs = mysql_fetch_assoc($result);
} else {
	$rsRows = 0;
    $rs = false;
}
if ($rs === false) {
?>
<div>Oh, find nothing!</div>
<?php 
	} else {
 		$proCount = $rsRows;
		$rsPageSize = 15; // 定义显示数目
		$rsPageCount = ceil($rsRows / $rsPageSize);
		$toPage = 1;
		if (isset($_REQUEST["p"])) {
		    $toPage = intval($_REQUEST["p"]);
			if ($toPage > $rsPageCount) {
			   	$rsAbsolutePage = $rsPageCount;
			   	mysql_data_seek($result, ($rsAbsolutePage - 1) * $rsPageSize);
			   	$intCurPage = $rsPageCount;
			} else if ($toPage <= 0) {
			   	$rsAbsolutePage = 1;
				mysql_data_seek($result, ($rsAbsolutePage - 1) * $rsPageSize);
			   	$intCurPage = 1;
			} else {
			   	$rsAbsolutePage = $toPage;
			   	mysql_data_seek($result, ($rsAbsolutePage - 1) * $rsPageSize);
			   	$intCurPage = $toPage;
			}
		 } else {
			$rsAbsolutePage = 1;
			mysql_data_seek($result, ($rsAbsolutePage - 1) * $rsPageSize);
			$intCurPage = 1;
		 }
		 $intCurPage = intval($intCurPage);	
		 
		$wt = $rsRows;
		$pt = $rsPageCount;
		$cp = $toPage;
?>
<div>
<?php 
	if ($cp > 1) {
?>
<a href="index.php?word=<?php echo($word);?>&action=search&p=1"> first </a>
<?php 
	} else {
?>
first
<?php
 	} 
?>
|
<?php 
	if ($cp > 1) {  
?>
<a href="index.php?word=<?php echo($word);?>&action=search&p=<?php echo($cp - 1);?>"> prev </a>
<?php
 	} else {
?>
 prev 
<?php
 	} 
?>
|
<?php
	if ($cp < $pt) {
?>
<a href="index.php?word=<?php echo($word);?>&action=search&p=<?php echo($cp + 1);?>"> next </a>
<?php
 	} else {
?>
next 
<?php 
	} 
?>
|
<?php
 	if ($cp < $pt) {
?>
<a href="index.php?word=<?php echo($word);?>&action=search&p=<?php echo($pt);?>"> last </a>
<?php
 	} else {
?>
last
<?php
 	} 
?>
</div>
<table border="1">
<?php
	for ($i = 0; $i < $rsPageSize; $i++) {
		$rs = mysql_fetch_assoc($result);
	    if ($rs === false) {     
			break; 
		}
?>
<tr>
<td><font size="2"><?php echo($rs["id"]);?></font></td>
<td><?php echo($rs["word"]);?></td>
</tr>
<?php
 	 } 
?>
</table>
<div>
wordTotal:<?php echo($wt);?>, pageTotal:<?php echo($pt);?>, currentPage:<?php echo($cp);?>
</div>
<?php 
} 
?>

<?php
$endtime = explode(' ', microtime());
$qtime = $endtime[0] + $endtime[1] - ($starttime[0] + $starttime[1]);
$qtime = round($qtime, 3);
?>
<div align="center">
<font size="2">Query time <?php echo($qtime);?> second</font><br />
<font size="2">Word search, by <a href="http://weimingtom.iteye.com/">weimingtom</a>, 2011</font><br />
</div>
</body>
</html>
