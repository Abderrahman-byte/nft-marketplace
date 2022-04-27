import React , {useState} from "react";

const LoginForm = ({ submitCallback }) => {
	const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
	const [errorMessages, setErrorMessages] = useState([]);

	const renderErrors = (errors, className) => {
		return errors.length > 0 ? (
			<div className={className}>
				{errors.map((err, i) => <span key={i}>{err.message}</span>)}
			</div>
		) : null
	}

	const renderFieldErrors = (name) => {
		const errors = errorMessages.filter(err => err.field === name)

		return renderErrors(errors, "errors-div")
	}

	const renderGlobalErrors = () => {
		const errors = errorMessages.filter(err => !Boolean(err.field))

		return renderErrors(errors, "form-div errors-div")
	}

	const handleSubmit = (event) => {
		event.preventDefault();

		const data = { username, password }

		setErrorMessages([])

        if (typeof submitCallback == 'function') submitCallback(data, setErrorMessages)
	};

	return (

		<form className='form' method="post" onSubmit={handleSubmit}>
			<div className='form-div '>
				<label className='form-label'>Username</label>
				<input name='username' className='form-input' onChange={(e)=> setUsername(e.target.value)} type='text' placeholder='Enter your username' autoComplete='off' />
				{renderFieldErrors("username")}
			</div>

			<div className='form-div'>
				<label className='form-label'>Password</label>
				<input name='password' className='form-input' onChange= {(e)=>setPassword(e.target.value)}  type='password' placeholder='Enter your password' autoComplete='off' />
				{renderFieldErrors("password")}
			</div>

			{renderGlobalErrors()}

			<div className='buttons'>
				<button className='btn btn-blue' type="submit"  value="Log In">Sign In</button>
			</div>

		</form>
	)

}

export default LoginForm;