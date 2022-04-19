import React from 'react'
import { Link } from 'react-router-dom'

const RegisterForm = () => {
	return (
		<form className='form'>
			<div className='form-div error'>
				<label className='form-label'>Username</label>
				<input name='username' className='form-input' type='text' placeholder='Enter your username' autoComplete='off' />
			</div>

            <div className='form-div'>
				<label className='form-label'>Email Address</label>
				<input name='email' className='form-input' type='email' placeholder='Enter your email address' autoComplete='off' />
			</div>

            <div className='form-div'>
				<label className='form-label'>Password</label>
				<input name='password' className='form-input' type='password' placeholder='Enter your password' autoComplete='off' />
			</div>

            <div className='form-div'>
				<label className='form-label'>Password Confirmation</label>
				<input name='password' className='form-input' type='password2' placeholder='Confirm your password ' autoComplete='off' />
			</div>

			<div className='buttons'>
            	<button className='btn btn-blue'>Sign Up</button>
				<Link className='btn btn-white' to='/sign-in'>Sign In</Link>
			</div>

		</form>
	)
}

export default RegisterForm
