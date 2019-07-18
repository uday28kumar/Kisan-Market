<?php 

require_once 'DbConnect.php';

$response = array();

if(isset($_GET['apicall'])){

	switch($_GET['apicall']){

		case 'signup':
			if(isTheseParametersAvailable(array('name', 'username','email','password','phone', 'address', 'type'))){
				$name=$_POST['name'];
				$username = $_POST['username']; 
				$email = $_POST['email']; 
				$password = $_POST['password'];
				$phone = $_POST['phone'];
				$address=$_POST['address'];
				$type=$_POST['type'];

				$stmt = $conn->prepare("SELECT * FROM users WHERE username = ? OR email = ? OR phone= ?");
				$stmt->bind_param("sss", $username, $email, $phone);
				$stmt->execute();
				$stmt->store_result();

				if($stmt->num_rows > 0){
					$response['error'] = true;
					$response['message'] = 'User already registered!';
					$stmt->close();
				}else{
					$stmt = $conn->prepare("INSERT INTO users (name, username, email, password, phone, address, type) VALUES (?, ?, ?, ?, ?, ?, ?)");
					$stmt->bind_param("sssssss",$name, $username, $email, $password, $phone, $address, $type);

					if($stmt->execute()){
						$stmt = $conn->prepare("SELECT name, username, email, phone, address, type FROM users WHERE username = ?"); 
						$stmt->bind_param("s",$username);
						$stmt->execute();
						$stmt->bind_result($name, $username, $email, $phone, $address, $type);
						$stmt->fetch();

						$user = array(
						'name'=>$name, 
						'username'=>$username, 
						'email'=>$email,
						'phone'=>$phone,
						'address'=>$address,
						'type'=>$type
						);

						$stmt->close();

						$response['error'] = false; 
						$response['message'] = 'Registration successfully!'; 
						$response['user'] = $user; 
					}
				}

			}else{
				$response['error'] = true; 
				$response['message'] = 'Required parameters are not available'; 
			}

			break; 

		case 'login':
			if(isTheseParametersAvailable(array('username', 'password'))){
				$username = $_POST['username'];
				$password = $_POST['password']; 

				$stmt = $conn->prepare("SELECT name, username, email, phone, address, type FROM users WHERE username = ? AND password = ?");
				$stmt->bind_param("ss",$username, $password);
				$stmt->execute();
				$stmt->store_result();

				if($stmt->num_rows > 0){
					$stmt->bind_result($name, $username, $email, $phone, $address, $type);
					$stmt->fetch();

					$user = array(
					'name'=>$name, 
					'username'=>$username, 
					'email'=>$email,
					'phone'=>$phone,
					'address'=>$address,
					'type'=>$type
					);

					$response['error'] = false; 
					$response['message'] = 'Login successfully!'; 
					$response['user'] = $user; 
				}else{
					$response['error'] = false; 
					$response['message'] = 'Invalid username or password!';
				}
			}else{
				$response['error'] = true; 
				$response['message'] = 'Required parameters are not available'; 
			}
			break; 

		case 'upload':
			if(isTheseParametersAvailable(array('p_name', 'p_category','p_sub_category','p_quantity','p_price', 'p_owner', 'p_image'))){
				$p_image = $_POST['p_image'];
				$p_name = $_POST['p_name'];
				$p_category = strtolower($_POST['p_category']);
				$p_sub_category = strtolower($_POST['p_sub_category']);
				$p_quantity = $_POST['p_quantity'];
				$p_price = $_POST['p_price'];
				$p_owner = $_POST['p_owner'];
				$stmt = "SELECT *FROM products order by p_id desc limit 1";
				$data = $conn->query($stmt);
				foreach ($data as $row) {
					$p_id=$row['p_id'];
				}
				$p_id=$p_id+1;
				$path = "product/$p_category/$p_sub_category/$p_id.png";
				$stmt = $conn->prepare("INSERT INTO products (p_image, p_name, p_category, p_sub_category, p_quantity, p_price, p_owner) VALUES (?, ?, ?, ?, ?, ?, ?)");
				$stmt->bind_param("sssssss", $path, $p_name, $p_category, $p_sub_category, $p_quantity, $p_price, $p_owner);
				if($stmt->execute()){
					file_put_contents($path, base64_decode($p_image));
					$response['error'] = false;
					$response['message'] = 'Product uploaded successfully! ';
				}
			}else{
				$response['error'] = true; 
				$response['message'] = 'Required parameters are not available'; 
			}
			break;

		case 'getproduct':
			if(isTheseParametersAvailable(array('p_sub_category'))){
				$p_sub_category = strtoupper($_POST['p_sub_category']);
				$stmt= $conn->prepare("SELECT * FROM products WHERE p_sub_category = ?");
				$stmt->bind_param("s", $p_sub_category);
				$stmt->execute();
				$result = $stmt->get_result();
				if($result->num_rows>0){
					while($row = $result->fetch_assoc()){
						$value[]=$row;
					}
					$response['error'] = false;
					$response['message'] = "Please wait...";
					$response['products'] = $value;
				}else{
					$response['error'] = true;
					$response['message'] = "Sorry no product available!";
				}
			}else{
				$response['error'] = true; 
				$response['message'] = 'Required parameters are not available'; 
			}
			break;

		case 'placeorder':
			if(isTheseParametersAvailable(array('username','p_id', 'p_name', 'p_image', 'address', 'quantity', 'amount'))){
				$username = $_POST['username']; 
				$p_id = $_POST['p_id'];
				$p_name = $_POST['p_name'];
				$p_image = $_POST['p_image']; 
				$address = $_POST['address'];
				$quantity =$_POST['quantity'];
				$amount = $_POST['amount'];
				$order_date = date("Y-m-d");
				$status = "Not delivered";

				//check table exists of username or not
				$querycheck="SELECT 1 FROM '$username'";
				$query_result=$conn->query($querycheck);
				if ($query_result !== FALSE){
				 	echo "table exists";
				}else{
					$stmt = "CREATE TABLE $username (
						order_id VARCHAR(50) PRIMARY KEY, 
						p_id VARCHAR(30) NOT NULL,
						p_name VARCHAR(50) NOT NULL,
						p_image VARCHAR(100) NOT NULL,
						address VARCHAR(100) NOT NULL,
						quantity INT(10) NOT NULL,
						amount INT(10) NOT NULL,
						order_date DATE NOT NULL,
						status VARCHAR(50)
					)";
					$conn->query($stmt);
				}
				//creating orderid
				$stmt = "SELECT *FROM $username";
				$result = $conn->query($stmt);
				$row_cnt = $result->num_rows;
				$order_id = "$username.$row_cnt";
				$stmt = $conn->prepare("INSERT INTO $username (order_id, p_id, p_name, p_image, address, quantity, amount, order_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				$stmt->bind_param("sssssssss", $order_id, $p_id, $p_name, $p_image, $address, $quantity, $amount, $order_date, $status);
				if($stmt->execute()){
					$stmt = "UPDATE products SET p_quantity= (p_quantity-$quantity) WHERE p_id = $p_id";
					$conn->query($stmt);
					$response['error'] = false;
					$response['message'] = "Order placed successfully";
				}
			}else{
				$response['error'] = true; 
				$response['message'] = 'Required parameters are not available'; 
			}

			break; 

		case 'getorder':
			if(isTheseParametersAvailable(array('username'))){
				$username = $_POST['username'];
				$stmt = "SELECT *FROM $username";
				$result = $conn->query($stmt);
				if($result->num_rows>0){
					while($row = $result->fetch_assoc()){
						$value[]=$row;
					}
					$response['error'] = false;
					$response['message'] = "Please wait...";
					$response['orders'] = $value;
				}else{
					$response['error'] = true;
					$response['message'] = "No order available!";
				}
			}else{
				$response['error'] = true; 
				$response['message'] = 'Required parameters are not available'; 
			}
			break;

		default: 
			$response['error'] = true; 
			$response['message'] = 'Invalid Operation Called';
	}
}else{
	$response['error'] = true; 
	$response['message'] = 'Invalid API Call';
}

echo json_encode($response);

function isTheseParametersAvailable($params){

	foreach($params as $param){
		if(!isset($_POST[$param])){
			return false; 
		}
	}
	return true; 
}