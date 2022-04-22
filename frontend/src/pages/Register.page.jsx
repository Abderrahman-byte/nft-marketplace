import React, { useContext, useState } from 'react'

import RegisterForm from '../components/RegisterForm'
import LoadingCard from '../components/LoadingCard'
import { register } from '../utils/api'
import { DEFAULT_ERROR, translateError } from '../utils/generic'
import RegisterSuccessPage from './RegisterSuccess.page'
import { AuthContext } from '../context/AuthContext'

import '../styles/RegisterPage.css'
import '../styles/forms.css'

const RegisterPage = () => {
    const { openModel, closeModel, setAuth } = useContext(AuthContext)
    const [isDone, setDone] = useState(false)

    const submitCallback = async (data, setErrors) => {
        openModel(<LoadingCard />)

        const [done, error] = await register(data)

        if (error && typeof setErrors === 'function') {
            setErrors(translateError(error))
        } else if (typeof setErrors === 'function') {
            setErrors([ DEFAULT_ERROR ])
        }

        setAuth(done)
        setDone(done)
        closeModel()
    }

    if (isDone) return <RegisterSuccessPage />

    return (
        <div className='RegisterPage center-container page'>
            <div className='container card'>
                <h2>Create New Account</h2>
                <RegisterForm submitCallback={submitCallback} />
            </div>
        </div>
    )
}

export default RegisterPage