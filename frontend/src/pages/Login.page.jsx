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

		const [response, error] = await sendLogin(data.username, data.password)

		console.log('response => ', response)
		console.log('Error => ', error)

        closeModel()

		if (error) {
			return setError(translateError(error))
		} else if (!response || !response.access_token) {
			return setError(DEFAULT_ERROR)
		}

		setAuth(false)
		localStorage.setItem('access_token', response.access_token)
		
		setTimeout(() => {
			setAuth(true)
			Navigate(nextPath)
		}, 100)
	}

	
	return (
		<div className='Login center-container auth-page'>
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
