import React , {useState}from "react";
import { Link } from "react-router-dom";


const LoginForm = () => {

	const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
	const [errorMessages, setErrorMessages] = useState({});

    async function login(e) {
        e.preventDefault();
        console.warn(username, password)
        let item = { username, password }


        let result = await fetch("http://localhost:8080/api/v1/auth/login", {   
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Accept": '*/*'
            },
            body: JSON.stringify(item),
            credentials: "include"

        });
        result = await result.json();
		
        
		if( "error" in result && "invalidFields" in result["error"])
		{
			
			setErrorMessages({ name: result["error"].invalidFields[0].name, message: result["error"].invalidFields[0].reason  });
		}
		else if("error" in result)
		{
			console.log("there is errors")
			setErrorMessages({ name: "username", message: result["error"].detail  });
		}
		else{
			console.log("loggedin")
		}
    }
	const renderErrorMessage = (name) =>
	name === errorMessages.name && (
	  <div className="errors-div"> 
	  <span>{errorMessages.message}  </span></div>
	);
	const handleSubmit = (event) => {
		// Prevent page reload
		event.preventDefault();
        login(event)
	};

	return (

		<form className='form' method="post" onSubmit={handleSubmit}>
			<div className='form-div '>
				<label className='form-label'>Username</label>
				<input name='username' className='form-input' onChange={(e)=> setUsername(e.target.value)} type='text' placeholder='Enter your username' autoComplete='off' />
				{renderErrorMessage("username")}
			</div>

			<div className='form-div'>
				<label className='form-label'>Password</label>
				<input name='password' className='form-input' onChange= {(e)=>setPassword(e.target.value)}  type='password' placeholder='Enter your password' autoComplete='off' />
				{renderErrorMessage("password")}
			</div>

			<div className='buttons'>
				<button className='btn btn-blue' type="submit"  value="Log In">Sign In</button>
			</div>

		</form>
	)

}

export default LoginForm;