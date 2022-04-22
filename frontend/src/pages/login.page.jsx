import React, { useContext } from 'react'
import LoginForm from '../components/LoginForm'
import { Link, useNavigate } from 'react-router-dom'

import { sendLogin } from '../utils/api'
import { DEFAULT_ERROR, translateError } from '../utils/generic'
import LoadingCard from '../components/LoadingCard'

import '../styles/forms.css'
import '../styles/LoginPage.css'
import { AuthContext } from '../context/AuthContext'

const LoginPage = () => {
	const { openModel, closeModel, setAuth } = useContext(AuthContext)
    const Navigate = useNavigate()

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
        Navigate('/')
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
