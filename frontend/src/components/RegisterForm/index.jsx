import React, { useState } from 'react'
import { Link } from 'react-router-dom'

import { registerFormFields, validateForm } from '@Utils/forms';

// TODO : add is artist field to register form

const RegisterForm = ({submitCallback}) => {
	const [errors, setErrors] = useState([])

	const saveForm = (e) => {
		e.preventDefault();

		const elements = e.target.elements

		const data = {
			username: elements.username.value,
			email: elements.email.value,
			password: elements.password.value,
			password2: elements.password2.value,
			isArtist: false
		}

		const localErrors = validateForm(data, registerFormFields)

		if (data.password2 && data.password2 !== '' && data.password2 !== data.password ) {
			localErrors.push({ field: 'password2', message: 'Passwords does not match.'})
		}

		if (localErrors.length > 0) return setErrors(localErrors)

		setErrors(errors.filter(error => Boolean(error.field)))

		if (typeof submitCallback !== 'function') return

		submitCallback(data, setErrors)
	}

	const cleanFieldErrors = (fieldname) => setErrors(errors.filter(error => error.field !== fieldname))

	const hasErrors = (field) => errors.some(error => error.field === field)

	const hasGlobalErrors = () => errors.some(error => !error.field)

	const getGlobalErrors = () => errors.filter(error => !error.field)

	const getFieldErrors = (field) => errors.filter(error => error.field === field)

	return (
		<form onSubmit={saveForm} className='form'>
			<div className={`form-div ${hasErrors('username') ? 'error': ''}`}>
				<label className='form-label'>Username</label>
				<input onChange={() => cleanFieldErrors('username')} name='username' className='form-input' type='text' placeholder='Enter your username' autoComplete='off' />
				{hasErrors('username') ? (
					<div className='errors-div'>
						{getFieldErrors('username').map((error, i) => <span key={i}>{error.message} </span>)}
					</div>
				) : null}
			</div>

            <div className={`form-div ${hasErrors('email') ? 'error': ''}`}>
				<label className='form-label'>Email Address</label>
				<input onChange={() => cleanFieldErrors('email')} name='email' className='form-input' type='text' placeholder='Enter your email address' autoComplete='off' />
				{hasErrors('email') ? (
					<div className='errors-div'>
						{getFieldErrors('email').map((error, i) => <span key={i}>{error.message} </span>)}
					</div>
				) : null}
			</div>

            <div className={`form-div ${hasErrors('password') ? 'error': ''}`}>
				<label className='form-label'>Password</label>
				<input onChange={() => cleanFieldErrors('password')} name='password' className='form-input' type='password' placeholder='Enter your password' autoComplete='off' />
				{hasErrors('password') ? (
					<div className='errors-div'>
						{getFieldErrors('password').map((error, i) => <span key={i}>{error.message} </span>)}
					</div>
				) : null}
			</div>

            <div className={`form-div ${hasErrors('password2') ? 'error': ''}`}>
				<label className='form-label'>Password Confirmation</label>
				<input onChange={() => cleanFieldErrors('password2')} name='password2' className='form-input' type='password' placeholder='Confirm your password ' autoComplete='off' />
				{hasErrors('password2') ? (
					<div className='errors-div'>
						{getFieldErrors('password2').map((error, i) => <span key={i}>{error.message} </span>)}
					</div>
				) : null}
			</div>

			{hasGlobalErrors() ? (
				(<div className='form-div errors-div'>
					{getGlobalErrors().map((error, i) => <span key={i}>{error.message}</span>)}
				</div>)
			): null}

			<div className='buttons'>
				<button className='btn btn-blue'>Sign Up</button>
				<Link className='btn btn-white' to='/sign-in'>Sign In</Link>
			</div>

		</form>
	)
}

export default RegisterForm
