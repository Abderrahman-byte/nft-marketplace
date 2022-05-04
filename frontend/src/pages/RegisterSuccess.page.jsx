import React from 'react'
import { Link } from 'react-router-dom'

import '@Styles/RegisterSuccessPage.css'

const RegisterSuccessPage = () => {
	return (
		<div className='RegisterSuccessPage auth-page center-container'>
			<div className='container card'>
				<h2>Verify your email</h2>
				<p>
					Your will need to verify your email to complete
					registration.
				</p>
				<span>
					An email has been sent to your mailbox with the a link to
					verify your account. if you have not received the email
					after a few minutes, please check your spam folder.
				</span>

                <Link to='/login' className='btn btn-blue'>Sign In</Link>
			</div>
		</div>
	)
}

export default RegisterSuccessPage
