import React, { useContext } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'

import { sendLogin } from '@Utils/api'
import { DEFAULT_ERROR, translateError } from '@Utils/generic'
import LoadingCard from '@Components/LoadingCard'
import { AuthContext } from '@Context/AuthContext'
import LoginForm from '@Components/LoginForm'

import '@Styles/forms.css'
import '@Styles/LoginPage.css'

const LoginPage = () => {
	const { openModel, closeModel, setAuth } = useContext(AuthContext)
    const Navigate = useNavigate()
	const location = useLocation()
	const nextPath = location?.state?.next || '/profile'

	const submitCallback = async (data, setError) => {
        openModel(<LoadingCard />)

		const [success, error] = await sendLogin(data.username, data.password)

        closeModel()

		if (error) {
			return setError(translateError(error))
		} else if (!success) {
			return setError(DEFAULT_ERROR)
		}

		setAuth(success)
        Navigate(nextPath)
	}

	
	return (
		<div className='Login center-container page'>
			<div className='container card'>
                <h2>  Login  </h2>
                <LoginForm submitCallback={submitCallback} />
                <div className='Signup'>
                <h6> Don't have an account ? <Link to='/sign-up'>Sign UP</Link></h6> 
                <h6> Forgot  <Link to='#'>Password</Link> ?</h6>
                </div>
            </div>
		</div>
	)
}
export default LoginPage
