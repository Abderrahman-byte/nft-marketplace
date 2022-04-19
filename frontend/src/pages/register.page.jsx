import React from 'react'

import RegisterForm from '../components/RegisterForm'

import '../styles/RegisterPage.css'
import '../styles/forms.css'

const RegisterPage = () => {
    return (
        <div className='RegisterPage center-container page'>
            <div className='container card'>
                <h2>Create New Account</h2>
                <RegisterForm />
            </div>
        </div>
    )
}

export default RegisterPage